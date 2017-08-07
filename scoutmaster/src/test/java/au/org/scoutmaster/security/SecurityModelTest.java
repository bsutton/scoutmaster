package au.org.scoutmaster.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Transaction;
import au.org.scoutmaster.DatabaseProvider;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.security.Feature;
import au.org.scoutmaster.domain.security.Feature_;
import au.org.scoutmaster.domain.security.Permission;
import au.org.scoutmaster.domain.security.Permission_;
import au.org.scoutmaster.domain.security.SecurityRole;
import au.org.scoutmaster.domain.security.SecurityRole_;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;
import au.org.scoutmaster.security.testViews.DuplicateActionView;
import au.org.scoutmaster.security.testViews.DuplicateRoleView;
import au.org.scoutmaster.security.testViews.MultipleActionView;
import au.org.scoutmaster.security.testViews.MultipleActionView2;
import au.org.scoutmaster.security.testViews.MultipleActionView3;
import au.org.scoutmaster.security.testViews.MultipleActionView4;

public class SecurityModelTest
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();
	static private EntityManager em;

	@BeforeClass
	static public void setUpClass() throws Exception
	{
		DatabaseProvider.initDatabaseProvider(false);
		DatabaseProvider.initLiquibase();
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("scoutmastertest");
		EntityManagerProvider.setEntityManagerFactory(emf);

		em = EntityManagerProvider.createEntityManager();
		EntityManagerProvider.setCurrentEntityManager(em);

		// reset the database

		try (Transaction t = new Transaction(em))
		{
			// Now confirm that all of the correct permissions/roles were
			// created.

			deletePermission(DuplicateActionView.class);
			deletePermission(DuplicateRoleView.class);
			deletePermission(MultipleActionView.class);

			// Create the standard set of Roles (if they don't already exist.
			createRole(eSecurityRole.COMMITTEE_MEMBER);
			createRole(eSecurityRole.GROUP_LEADER);
			createRole(eSecurityRole.ADULT_MEMBER);
			createRole(eSecurityRole.LEADER);
			createRole(eSecurityRole.YOUTH_MEMBER);
			createRole(eSecurityRole.NONE);
			createRole(eSecurityRole.PRESIDENT);
			createRole(eSecurityRole.QUARTERMASTER);
			createRole(eSecurityRole.SECRETARY);
			createRole(eSecurityRole.TECH_SUPPORT);

			t.commit();
		}

	}

	private static void createRole(eSecurityRole erole)
	{
		JpaBaseDao<SecurityRole, Long> daoRole = DaoFactory.getGenericDao(SecurityRole.class);

		SecurityRole role = daoRole.findOneByAttribute(SecurityRole_.erole, erole);
		if (role == null)
			daoRole.persist(new SecurityRole(erole));

	}

	private static void deletePermission(Class<?> clazz)
	{
		JpaBaseDao<Permission, Long> daoPermission = DaoFactory.getGenericDao(Permission.class);

		JpaBaseDao<Feature, Long> daoFeature = DaoFactory.getGenericDao(Feature.class);
		Feature feature = daoFeature.findOneByAttribute(Feature_.name, clazz.getSimpleName());

		List<Permission> permissions = daoPermission.findAllByAttribute(Permission_.feature, feature,
				Permission_.action);

		for (Permission permission : permissions)
		{
			// permission.removeAllRoles();
			daoPermission.remove(permission);
		}
	}

	@Test(expected = DuplicateActionException.class)
	public void test() throws SecurityException
	{

		try (Transaction t = new Transaction(em))
		{
			iFeature annotation = DuplicateActionView.class.getAnnotation(iFeature.class);
			new SecurityModel(DuplicateActionView.class, annotation);
			t.commit();
		}

	}

	@Test(expected = DuplicateRoleException.class)
	public void test2() throws SecurityException
	{

		try (Transaction t = new Transaction(em))
		{
			iFeature annotation = DuplicateRoleView.class.getAnnotation(iFeature.class);
			new SecurityModel(DuplicateRoleView.class, annotation);
			t.commit();
		}
	}

	@Test
	public void test3() throws SecurityException
	{
		try (Transaction t = new Transaction(em))
		{
			iFeature annotation = MultipleActionView.class.getAnnotation(iFeature.class);
			new SecurityModel(MultipleActionView.class, annotation);
			t.commit();

			checkPermissionsAndRoles(MultipleActionView.class, annotation);

		}
	}

	/**
	 * Test the annotation adding a new roles and permissions to database.
	 *
	 * @throws SecurityException
	 */
	@Test
	public void test4() throws SecurityException
	{
		try (Transaction t = new Transaction(em))
		{
			// Add the Secretary role to existing permission
			// , @iPermission(action = Action.DELETE, roles ={
			// eRole.GROUP_LEADER, eRole.PRESIDENT, eRole.SECRETARY })
			// add new permission
			// , @iPermission(action = Action.ACCESS_MEMBER_DETAILS, roles ={
			// eRole.GROUP_LEADER, eRole.PRESIDENT })

			iFeature annotation = MultipleActionView2.class.getAnnotation(iFeature.class);

			new SecurityModel(MultipleActionView2.class, annotation);
			t.commit();

			checkPermissionsAndRoles(MultipleActionView2.class, annotation);

		}
	}

	/**
	 * Test the annotation removing a new roles and permissions from database.
	 *
	 * @throws SecurityException
	 */
	@Test
	public void test5() throws SecurityException
	{
		try (Transaction t = new Transaction(em))
		{
			// @formatter:off
//			// Remove permission Action.LIST
//			//@iPermission(action = Action.LIST, roles ={ eRole.MEMBER })
//			@iPermission(action = Action.NEW, roles ={ eRole.COMMITTEE_MEMBER, eRole.LEADER})
//			// Remove secretary
//			, @iPermission(action = Action.DELETE, roles ={ eRole.GROUP_LEADER, eRole.PRESIDENT}) // , eRole.SECRETARY })
//			// Re-add Role President
//			, @iPermission(action = Action.ACCESS_MEMBER_DETAILS, roles ={ eRole.GROUP_LEADER, eRole.PRESIDENT})
			// @formatter:on
			iFeature annotation = MultipleActionView3.class.getAnnotation(iFeature.class);

			new SecurityModel(MultipleActionView3.class, annotation);
			t.commit();

			checkPermissionsAndRoles(MultipleActionView3.class, annotation);

		}
	}

	/**
	 * Test the adding/removing roles to permissions that have been user
	 * editored. In each case the set of roles should not change.
	 *
	 * @throws SecurityException
	 */
	@Test
	public void test6() throws SecurityException
	{
		try (Transaction t = new Transaction(em))
		{
			iFeature annotation = MultipleActionView4.class.getAnnotation(iFeature.class);

			// Start by marking the two permissions as User edited
			JpaBaseDao<Permission, Long> daoPermission = DaoFactory.getGenericDao(Permission.class);
			JpaBaseDao<Feature, Long> daoFeature = DaoFactory.getGenericDao(Feature.class);
			Feature feature = daoFeature.findOneByAttribute(Feature_.name, MultipleActionView4.class.getSimpleName());

			List<Permission> permissions = daoPermission.findAllByAttribute(Permission_.feature, feature,
					Permission_.action);

			for (Permission permission : permissions)
			{
				if (permission.getAction().equals(Action.DELETE)
						|| permission.getAction().equals(Action.SENSITIVE_ACCESS))
				{
					permission.setEditedByUser(true);
					daoPermission.merge(permission);
				}
			}

			new SecurityModel(MultipleActionView4.class, annotation);
			t.commit();

			// Check each permission individually

			// Check we have only the expected permissions

			permissions = daoPermission.findAllByAttribute(Permission_.feature, feature, Permission_.action);
			for (Permission permission : permissions)
			{
				Action action = permission.getAction();
				switch (action)
				{
					case SENSITIVE_ACCESS:
						confirmRoles(permission, new eSecurityRole[]
						{ eSecurityRole.GROUP_LEADER, eSecurityRole.PRESIDENT });
						break;
					case DELETE:
						confirmRoles(permission, new eSecurityRole[]
						{ eSecurityRole.GROUP_LEADER, eSecurityRole.PRESIDENT });
						break;
					case NEW:
						confirmRoles(permission, new eSecurityRole[]
						{ eSecurityRole.LEADER, eSecurityRole.COMMITTEE_MEMBER });
						break;
					default:
						throw new UnexpectedPermissionException("Unexcected Permission: " + action);

				}
			}

		}
	}

	private void confirmRoles(Permission permission, eSecurityRole[] eroles) throws SecurityException
	{
		if (permission.getRoles().size() != eroles.length)
			throw new SecurityException("The expected number of roles doesn't match permission:" + permission.getRoles()
					+ " expected " + eroles);

		for (SecurityRole role : permission.getRoles())
		{
			boolean found = false;
			for (eSecurityRole erole : eroles)
			{
				if (role.getERole() == erole)
				{
					found = true;
					break;
				}
			}
			if (!found)
				throw new UnexpectedRoleException("The role: " + role + " of " + permission
						+ " was not in the expected list of roles: " + eroles);
		}

	}

	private void checkPermissionsAndRoles(Class<?> clazz, iFeature annotation) throws SecurityException
	{
		// Now confirm that all of the correct permissions/roles were
		// created.
		JpaBaseDao<Permission, Long> daoPermission = DaoFactory.getGenericDao(Permission.class);

		JpaBaseDao<Feature, Long> daoFeature = DaoFactory.getGenericDao(Feature.class);
		Feature feature = daoFeature.findOneByAttribute(Feature_.name, clazz.getSimpleName());

		List<Permission> permissions = daoPermission.findAllByAttribute(Permission_.feature, feature,
				Permission_.action);
		Map<Action, iPermission> expected = new HashMap<>();
		// load expected into a map for easy access.
		for (iPermission expect : annotation.permissions())
		{
			expected.put(expect.action(), expect);
		}
		for (Permission permission : permissions)
		{
			if (expected.containsKey(permission.getAction()))
			{
				// check all of the roles have been created
				Map<String, SecurityRole> expectedRoles = new HashMap<>();
				// fill the map
				for (SecurityRole expectedRole : permission.getRoles())
				{
					expectedRoles.put(expectedRole.getName(), expectedRole);
				}
				for (SecurityRole role : permission.getRoles())
				{
					if (expectedRoles.containsKey(role.getName()))
					{
						expectedRoles.remove(role.getName());
					}
					else
						throw new UnexpectedRoleException(
								"Unexpected role in db: " + permission.toString() + ":" + role.getName());

				}
				if (!expectedRoles.isEmpty())
					throw new MissingRoleException("Missing Roles: " + Arrays.toString(expectedRoles.keySet().toArray())
							+ " for permission " + permission.toString());

				expected.remove(permission.getAction());
			}
			else
			{
				// unexpected permission in db.
				throw new UnexpectedPermissionException("Unexpected permission in db: " + permission.toString());
			}
		}

		if (!expected.isEmpty())
		{
			throw new RuntimeException("Missing Permissions: " + Arrays.toString(expected.keySet().toArray()));
		}
	}

}
