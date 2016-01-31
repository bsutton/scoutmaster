package au.org.scoutmaster.dao;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.access.LoginAttemptDao;
import au.org.scoutmaster.dao.access.SessionHistoryDao;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.BaseEntity;

public class DaoFactory
{
	public DaoFactory()
	{
	}

	public CommunicationLogDao getCommunicationLogDao()
	{
		return (CommunicationLogDao) instantiateDAO(CommunicationLogDao.class);
	}

	public CommunicationTypeDao getActivityTypeDao()
	{
		return (CommunicationTypeDao) instantiateDAO(CommunicationTypeDao.class);
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

	public GroupDao getGroupDao()

	{
		return (GroupDao) instantiateDAO(GroupDao.class);
	}

	public SchoolDao getSchoolDao()
	{
		return (SchoolDao) instantiateDAO(SchoolDao.class);
	}

	public static <E> JpaBaseDao<E, Long> getGenericDao(final Class<E> class1)
	{
		return new JpaBaseDao<E, Long>(class1);

	}

	@SuppressWarnings("unchecked")
	public <D extends JpaBaseDao<E, Long>, E extends BaseEntity> D getDao(final Class<D> clazz)
	{
		return (D) instantiateDAO(clazz);
	}

	private JpaBaseDao<?, ?> instantiateDAO(final Class<?> daoClass)
	{
		try
		{
			JpaBaseDao<?, ?> dao = (JpaBaseDao<?, ?>) daoClass.newInstance();
			return dao;
		}
		catch (final Exception ex)
		{
			throw new RuntimeException("Cannot instantiate DAO: " + daoClass, ex);
		}
	}
}
