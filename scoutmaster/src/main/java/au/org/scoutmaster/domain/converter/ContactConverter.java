package au.org.scoutmaster.domain.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;

public class ContactConverter extends BaseConverter<Contact>
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(ContactConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Class<Contact> getModelType()
	{
		return Contact.class;
	}

	@Override
	protected Contact newInstance(final Object value)
	{
		return new Contact();
	}

	@Override
	protected JpaBaseDao<Contact, Long> getDao()
	{
		return new DaoFactory().getContactDao();
	}
}
