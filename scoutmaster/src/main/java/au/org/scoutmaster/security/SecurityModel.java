package au.org.scoutmaster.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Objects;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.security.Feature;
import au.org.scoutmaster.domain.security.Feature_;
import au.org.scoutmaster.domain.security.Permission;
import au.org.scoutmaster.domain.security.Permission_;
import au.org.scoutmaster.domain.security.SecurityRole;
import au.org.scoutmaster.domain.security.SecurityRole_;
import au.org.scoutmaster.domain.security.User;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;

/**
 * Defines the full security model for a given view.
 *
 * When scoutmaster starts up the NavigatorUI causes a full set of
 * SecurityModels to be instantiated and cached in the SecurityModelFactoryImpl.
 *
 * Each time a users logs into Scoutmaster a SecurityManager is instantiated
 * which connects a user to the full set of SecurityModels.
 *
 * As SecurityModel essentially loads the set of current permissions from the
 * database. The SecurityModel will also purge any Features/Permissions from the
 * db which are no longer used (e.g. their is no longer a @Feature annotation
 * found in the code base) as well as adding any new ones that are found will
 * reading annotations (@Feature).
 *
 *
 * @author bsutton
 *
 */
public class SecurityModel
{

	@SuppressWarnings("unused")
	private Logger logger = LogManager.getLogger();
	private String featureName;
	private List<Permission> permissions = new ArrayList<>();

	/**
	 * Loads a security model from an annotating.
	 *
	 * @param feature
	 * @throws SecurityException
	 */
	SecurityModel(Class<?> clazz, iFeature feature) throws SecurityException
	{
		this.featureName = clazz.getSimpleName();

		santityCheckPermissions(feature.permissions());

		// We need to sync the permissions with the databases permissions

		JpaBaseDao<Permission, Long> daoPermission = JpaBaseDao.getGenericDao(Permission.class);
		JpaBaseDao<SecurityRole, Long> daoRole = JpaBaseDao.getGenericDao(SecurityRole.class);

		JpaBaseDao<Feature, Long> daoFeature = DaoFactory.getGenericDao(Feature.class);
		Feature jpaFeature = daoFeature.findOneByAttribute(Feature_.name, featureName);

		List<Permission> dbPermissions = daoPermission.findAllByAttribute(Permission_.feature, jpaFeature,
				Permission_.action);

		// Take a copy of the permissions so we can work out what is missing
		// from the db.
		List<Permission> missingPermissions = createPermissions(feature.permissions());
		for (Permission dbPermission : dbPermissions)
		{
			Permission annoPermission = findAndRemove(missingPermissions, dbPermission);
			if (annoPermission != null)
			{
				sanityCheckDuplicateRoles(annoPermission);

				// DB and annotation match so its a real permission
				this.permissions.add(annoPermission);

				// Now check that the Roles match provided the permission hasn't
				// been locally changed.
				if (!dbPermission.editedByUser())
				{
					List<SecurityRole> missingRoles = new ArrayList<>(annoPermission.getRoles());
					// copy the array as we need to update the original.
					for (SecurityRole dbRole : new CopyOnWriteArrayList<SecurityRole>(dbPermission.getRoles()))
					{
						SecurityRole annoRole = findAndRemove(missingRoles, dbRole);
						// If the db role isn't in the list of annotation roles
						// then we need to delete the db role
						if (annoRole == null)
						{
							dbPermission.removeRole(dbRole);
						}
					}

					// Added any missing roles into the db provided
					// they don't already exist (which normally they should as
					// roles
					// are created when we create the tenant.
					// In fact if the Roles are missing from the DB we should
					// throw an exception.
					for (SecurityRole annoRole : missingRoles)
					{
						// Add the role to the permission as required.
						dbPermission.addRole(annoRole);

						// Check the role is already in the db (as required)
						SecurityRole role = daoRole.findOneByAttribute(SecurityRole_.erole, annoRole.getERole());
						if (role == null)
							throw new MissingRoleException("The role: " + role + " from permission " + annoPermission
									+ " is missing from the database");
					}

				}
			}
			else
			{
				// The db permission wasn't found in the annotation so it needs
				// to be removed.
				daoPermission.remove(dbPermission);
			}
		}

		// Insert any missing permissions into the database
		for (Permission permission : missingPermissions)
		{
			sanityCheckDuplicateRoles(permission);

			for (SecurityRole role : permission.getRoles())
			{
				// Check the role is already in the db (as required)
				SecurityRole dbRole = daoRole.findOneByAttribute(SecurityRole_.erole, role.getERole());
				if (dbRole == null)
					throw new MissingRoleException(
							"The role: " + role + " from permission " + permission + " is missing from the database");
			}

			daoPermission.persist(permission);

		}
	}

	private List<Permission> createPermissions(iPermission[] ipermissions) throws MissingRoleException
	{
		List<Permission> permissions = new ArrayList<>();

		JpaBaseDao<Feature, Long> daoFeature = DaoFactory.getGenericDao(Feature.class);
		Feature jpaFeature = daoFeature.findOneByAttribute(Feature_.name, featureName);

		for (iPermission ipermission : ipermissions)
		{
			permissions.add(new Permission(jpaFeature, ipermission));
		}
		return permissions;
	}

	private void santityCheckPermissions(iPermission[] permissions) throws SecurityException
	{
		// First sanity check : there should be no duplicate actions in any two
		// permissions
		Set<Action> dupTest = new HashSet<>();
		for (iPermission permission : permissions)
		{
			if (!dupTest.add(permission.action()))
			{
				throw new DuplicateActionException("The Feature " + this.featureName
						+ " has two Permissions with the same action: " + permission.toString());
			}
		}
	}

	private void sanityCheckDuplicateRoles(Permission annoPermission) throws SecurityException
	{
		// First sanity check : there should be no duplicate roles.
		Set<SecurityRole> dupTest = new HashSet<>();
		for (SecurityRole role : annoPermission.getRoles())
		{
			if (!dupTest.add(role))
			{
				throw new DuplicateRoleException(
						"The permission " + annoPermission.toString() + " has duplicate Role: " + role.toString());
			}
		}
	}

	private SecurityRole findAndRemove(List<SecurityRole> missingRoles, SecurityRole dbRole)
	{
		SecurityRole found = null;
		for (SecurityRole role : missingRoles)
		{
			if (role.equals(dbRole))
			{
				found = role;
				missingRoles.remove(role);
				break;
			}
		}
		return found;
	}

	private Permission findAndRemove(List<Permission> missingPermissions, Permission dbPermission)
	{
		Permission found = null;
		for (Permission permission : missingPermissions)
		{
			if (permission.equals(dbPermission))
			{
				found = permission;
				missingPermissions.remove(permission);
				break;
			}
		}
		return found;
	}

	/**
	 * Loads a security model from the database.
	 *
	 * @param featureName
	 * @param permissions
	 */
	public SecurityModel(Class<?> clazz, List<Permission> permissions)
	{
		this.featureName = clazz.getSimpleName();
		;
		this.permissions = permissions;

	}

	String getFeatureName()
	{
		return this.featureName;
	}

	public boolean canUser(Enum<?> action, User user)
	{
		boolean permitted = false;

		// Find the action.
		for (Permission permission : this.permissions)
		{
			if (permission.getAction().equals(action))
			{
				// we have found the action of interest.
				// See if the user has a role that matches one of our roles
				for (SecurityRole role : permission.getRoles())
				{
					if (user.hasRole(role.getERole()))
					{
						permitted = true;
						break;
					}
				}
			}
		}
		return permitted;
	}

	/**
	 * Purge the feature from the database.
	 */
	public void purgeFromDB(Feature feature)
	{
		JpaBaseDao<Permission, Long> daoPermission = JpaBaseDao.getGenericDao(Permission.class);
		JpaBaseDao<SecurityRole, Long> daoRole = JpaBaseDao.getGenericDao(SecurityRole.class);

		List<Permission> permissions = daoPermission.findAllByAttribute(Permission_.feature, feature,
				Permission_.action);

		for (Permission permission : permissions)
		{
			for (SecurityRole role : permission.getRoles())
				daoRole.remove(role);
			daoPermission.remove(permission);
		}

		JpaBaseDao<Feature, Long> daoFeature = DaoFactory.getGenericDao(Feature.class);
		daoFeature.remove(feature);

	}

	static int hashRole(List<eSecurityRole> roles)
	{
		return Objects.hashCode(roles.toArray());
	}

}
