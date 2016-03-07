package au.org.scoutmaster.domain.access;

import java.util.ArrayList;
import java.util.List;

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
import org.hibernate.validator.constraints.NotBlank;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.MissingRoleException;
import au.org.scoutmaster.security.eRole;
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
	 * An informal hierarchical descriptor of the feature e.g. Contact.list,
	 * Contact.edit, Contact.delete
	 */
	@NotBlank
	private String featureName;

	@Enumerated(EnumType.STRING)
	private Action action;

	/**
	 * If a user belongs to at least one of these roles then can apply the
	 * action to this feature.
	 */
	@JoinTable(name = "permission_role")
	@ManyToMany
	private List<Role> accessibleBy = new ArrayList<>();

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

	public Permission(String featureName)
	{
		this.featureName = featureName;
	}

	public Permission(String featureName, iPermission ipermission) throws MissingRoleException
	{
		this.featureName = featureName;
		this.action = ipermission.action();

		for (eRole erole : ipermission.roles())
		{
			// Roles must already exist in the db we are just linking to them.
			JpaBaseDao<Role, Long> daoRole = DaoFactory.getGenericDao(Role.class);
			Role role = daoRole.findOneByAttribute(Role_.erole, erole);
			if (role == null)
				throw new MissingRoleException(
						"The Role " + role + " for permission " + this.toString() + " is missing.");

			this.accessibleBy.add(role);
		}
	}

	public String getFeatureName()
	{
		return featureName;
	}

	/**
	 * Required by BaseEntity
	 */
	@Override
	public String getName()
	{
		return getFeatureName();
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
	public void addRole(eRole erole)
	{
		List<Role> roles = DaoFactory.getGenericDao(Role.class).findAllByAttribute(Role_.erole, erole, null);

		if (!roles.isEmpty())
		{
			Role role = roles.get(0);
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

	public List<Role> getRoles()
	{
		return accessibleBy;
	}

	public void removeRole(Role dbRole)
	{
		JpaBaseDao<Role, Long> daoRole = DaoFactory.getGenericDao(Role.class);
		List<Role> roles = daoRole.findAllByAttribute(Role_.erole, dbRole.getERole(), null);

		for (Role role : roles)
		{
			this.accessibleBy.remove(role);
		}

	}

	public void addRole(Role role)
	{
		JpaBaseDao<Role, Long> daoRole = DaoFactory.getGenericDao(Role.class);
		this.accessibleBy.add(role);

	}

	@Override
	public String toString()
	{
		return this.featureName + ":" + this.action;
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
		result = prime * result + ((this.featureName == null) ? 0 : this.featureName.hashCode());
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
		if (this.featureName == null)
		{
			if (other.featureName != null)
				return false;
		}
		else if (!this.featureName.equals(other.featureName))
			return false;
		return true;
	}
}
