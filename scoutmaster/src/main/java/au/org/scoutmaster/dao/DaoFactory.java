package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.org.scoutmaster.dao.access.UserDao;

public class DaoFactory
{
	private final EntityManager em;

	/**
	 * This version uses the injected em.
	 */
	public DaoFactory()
	{
		this.em = null;
	}

	/**
	 * Allows us to control the used em. This is normally used for unit testing.
	 * 
	 * @param em
	 */
	public DaoFactory(EntityManager em)
	{
		this.em = em;
	}

	public ActivityDao getActivityDao()
	{
		return (ActivityDao) instantiateDAO(ActivityDao.class);
	}

	public ActivityTypeDao getActivityTypeDao()
	{
		return (ActivityTypeDao) instantiateDAO(ActivityTypeDao.class);
	}

	public ContactDao getContactDao()
	{
		return (ContactDao) instantiateDAO(ContactDao.class);
	}

	public EventDao getEventDao()
	{
		return (EventDao) instantiateDAO(EventDao.class);
	}

	public GroupRoleDao getGroupRoleDao()
	{
		return (GroupRoleDao) instantiateDAO(GroupRoleDao.class);
	}

	public NoteDao getNoteDao()
	{
		return (NoteDao) instantiateDAO(NoteDao.class);
	}

	public SMTPSettingsDao getSMTPSettingsDao()
	{
		return (SMTPSettingsDao) instantiateDAO(SMTPSettingsDao.class);
	}

	public ForgottenPasswordResetDao getForgottenPasswordResetDao()
	{
		return (ForgottenPasswordResetDao) instantiateDAO(ForgottenPasswordResetDao.class);
	}

	public ImportUserMappingDao getImportUserMappingDao()
	{
		return (ImportUserMappingDao) instantiateDAO(ImportUserMappingDao.class);
	}

	public OrganisationDao getOrganisationDao()
	{
		return (OrganisationDao) instantiateDAO(OrganisationDao.class);
	}

	public PhoneDao getPhoneDao()
	{
		return (PhoneDao) instantiateDAO(PhoneDao.class);
	}

	public SectionTypeDao getSectionTypeDao()
	{
		return (SectionTypeDao) instantiateDAO(SectionTypeDao.class);
	}

	public SMSProviderDao getSMSProviderDao()
	{
		return (SMSProviderDao) instantiateDAO(SMSProviderDao.class);
	}

	public TagDao getTagDao()
	{
		return (TagDao) instantiateDAO(TagDao.class);
	}

	public UserDao getUserDao()
	{
		return (UserDao) instantiateDAO(UserDao.class);
	}

	private JpaBaseDao<?, ?> instantiateDAO(Class<?> daoClass)
	{
		try
		{
			JpaBaseDao<?, ?> dao = null;
			if (this.em != null)
			{
				dao = (JpaBaseDao<?, ?>) daoClass.getDeclaredConstructor(new Class[]
				{ EntityManager.class }).newInstance(this.em);
			}
			else
				dao = (JpaBaseDao<?, ?>) daoClass.newInstance();
			return dao;
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Cannot instantiate DAO: " + daoClass, ex);
		}
	}


}
