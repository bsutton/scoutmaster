package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.access.LoginAttemptDao;
import au.org.scoutmaster.dao.access.SessionHistoryDao;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.BaseEntity;

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

	public CreditNoteDao getCreditNoteDao()
	{
		return (CreditNoteDao) instantiateDAO(CreditNoteDao.class);
	}

	public EventDao getEventDao()
	{
		return (EventDao) instantiateDAO(EventDao.class);
	}

	public GroupRoleDao getGroupRoleDao()
	{
		return (GroupRoleDao) instantiateDAO(GroupRoleDao.class);
	}

	public InvoiceDao getInvoiceDao()
	{
		return (InvoiceDao) instantiateDAO(InvoiceDao.class);
	}

	public LoginAttemptDao getLoginAttemptDao()
	{
		return (LoginAttemptDao) instantiateDAO(LoginAttemptDao.class);
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

	public RaffleDao getRaffleDao()
	{
		return (RaffleDao) instantiateDAO(RaffleDao.class);
	}

	public RaffleAllocationDao getRaffleAllocationDao()
	{
		return (RaffleAllocationDao) instantiateDAO(RaffleAllocationDao.class);

	}

	public RaffleBookDao getRaffleBookDao()
	{
		return (RaffleBookDao) instantiateDAO(RaffleBookDao.class);
	}

	public RelationshipDao getRelationshipDao()
	{
		return (RelationshipDao) instantiateDAO(RelationshipDao.class);
	}

	public SectionTypeDao getSectionTypeDao()
	{
		return (SectionTypeDao) instantiateDAO(SectionTypeDao.class);
	}

	public SessionHistoryDao getSessionHistoryDao()
	{
		return (SessionHistoryDao) instantiateDAO(SessionHistoryDao.class);
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

	public static <E> JpaBaseDao<E, Long> getGenericDao(Class<E> class1)
	{
		return new JpaBaseDao<E, Long>(class1);

	}

	@SuppressWarnings("unchecked")
	public <D extends JpaBaseDao<E, Long>, E extends BaseEntity> D getDao(Class<D> clazz)
	{
		return (D) instantiateDAO(clazz);
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
