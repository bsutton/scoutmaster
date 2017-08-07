package au.org.scoutmaster.domain.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.annotations.Index;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.MissingRoleException;
import au.org.scoutmaster.security.eSecurityRole;
import au.org.scoutmaster.security.annotations.iPermission;

/**
 * Permissions are used to describe a users access to a feature in Scoutmaster.
 *
 * A Feature is described by a base FeatureName and an Action that can be
 * applied to that feature.
 *
 * A Role is really defined by the set of feature/actions that a User has
 * permission to access.
 *
 * @author bsutton
 *
 */
@Entity
@Table(name = "Permission")
@Index(unique = true, columnNames =
{ "featureName", "action" })
@Access(AccessType.FIELD)
public class Permission extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Transient
	private Logger logger = LogManager.getLogger();

	/**
	 * The core feature we are controlling access to and to which we give a user
	 * the right (via their SecurityRoles) to apply an Action. e.g. Contact,
	 * User,
	 */
	Feature feature;

	@Enumerated(EnumType.STRING)
	private Action action;

	/**
	 * If a user belongs to at least one of these roles then can apply the
	 * action to this feature.
	 */
	@JoinTable(name = "permission_role")
	@ManyToMany
	private Set<SecurityRole> accessibleBy = new HashSet<>();

	/**
	 * If true then the default set of roles for this permission has been
	 * over-ridden by an administrator. During upgrades we shouldn't change the
	 * set of Roles (accessibleBy) that have access to this permission.
	 */
	boolean editedByUser;

	/**
	 * Hash of all of the roles associated with this feature. Can be used to
	 * quickly determine if the set of associated roles has changed.
	 */
	private int roleHash;

	// Requried by JPA
	public Permission()
	{
	}

	public Permission(Feature feature)
	{
		this.feature = feature;
	}

	public Permission(Feature feature, iPermission ipermission) throws MissingRoleException
	{
		this.feature = feature;
		this.action = ipermission.action();

		for (eSecurityRole erole : ipermission.roles())
		{
			// Roles must already exist in the db we are just linking to them.
			JpaBaseDao<SecurityRole, Long> daoRole = DaoFactory.getGenericDao(SecurityRole.class);
			SecurityRole role = daoRole.findOneByAttribute(SecurityRole_.erole, erole);
			if (role == null)
				throw new MissingRoleException(
						"The Role " + erole + " for permission " + this.toString() + " is missing from the database.");

			this.accessibleBy.add(role);
		}
	}

	public Feature getFeature()
	{
		return feature;
	}

	/**
	 * Required by BaseEntity
	 */
	@Override
	public String getName()
	{
		return this.feature.getName() + "." + this.action.toString();
	}

	public boolean editedByUser()
	{
		return editedByUser;
	}

	public Action getAction()
	{
		return action;
	}

	/**
	 * Adds a role to the list of roles that have access to this feature. Is
	 * assume that the Role already exists and is 'attached' to JPA. Also adds
	 * the backlink to Roles.
	 *
	 * @param erole
	 */
	public void addRole(eSecurityRole erole)
	{
		List<SecurityRole> roles = DaoFactory.getGenericDao(SecurityRole.class).findAllByAttribute(SecurityRole_.erole,
				erole, null);

		if (!roles.isEmpty())
		{
			SecurityRole role = roles.get(0);
			this.accessibleBy.add(role);
			role.addPermission(this);
		}
		else
			logger.warn("eRole {} was not found", erole.name());
	}

	public void setRoleHash(int roleHash)
	{
		this.roleHash = roleHash;

	}

	public int getRoleHash()
	{
		return this.roleHash;
	}

	public Set<SecurityRole> getRoles()
	{
		return accessibleBy;
	}

	public void removeRole(SecurityRole dbRole)
	{
		JpaBaseDao<SecurityRole, Long> daoRole = DaoFactory.getGenericDao(SecurityRole.class);
		List<SecurityRole> roles = daoRole.findAllByAttribute(SecurityRole_.erole, dbRole.getERole(), null);

		for (SecurityRole role : roles)
		{
			this.accessibleBy.remove(role);
		}

	}

	public void addRole(SecurityRole role)
	{
		this.accessibleBy.add(role);

	}

	@Override
	public String toString()
	{
		return this.feature.getName() + "." + this.action;
	}

	public void removeAllRoles()
	{
		this.accessibleBy.clear();

	}

	public void setEditedByUser(boolean edited)
	{
		this.editedByUser = edited;

	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.action == null) ? 0 : this.action.hashCode());
		result = prime * result + ((this.feature == null) ? 0 : this.feature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (this.action != other.action)
			return false;
		if (this.feature == null)
		{
			if (other.feature != null)
				return false;
		}
		else if (!this.feature.equals(other.feature))
			return false;
		return true;
	}
}
