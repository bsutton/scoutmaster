package au.org.scoutmaster.security;

import au.com.vaadinutils.crud.CrudSecurityManager;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.access.User;

/**
 * A SecurityManager is created each time a users logs into scoutmaster.
 *
 * The security Manager maps the User to the SecurityModel.
 *
 * @author bsutton
 *
 */
public class SecurityManagerImpl implements CrudSecurityManager
{

	private SecurityModel model;

	// The current logged in user
	private User user;

	public SecurityManagerImpl(SecurityModel model, User user)
	{
		this.model = model;
		this.user = user;
	}

	public SecurityManagerImpl(SecurityModel model)
	{
		this(model, SMSession.INSTANCE.getLoggedInUser());
	}

	@Override
	public boolean canUser(Enum<?> action)
	{
		return model.canUser(action, user);
	}

	@Override
	public boolean canUserView()
	{
		return canUser(Action.LIST);
	}

	@Override
	public boolean canUserDelete()
	{
		return canUser(Action.DELETE);
	}

	@Override
	public boolean canUserEdit()
	{
		return canUser(Action.EDIT);
	}

	@Override
	public boolean canUserCreate()
	{
		return canUser(Action.NEW);
	}

	@Override
	public boolean isUserSuperUser()
	{
		return user.hasRole(eRole.SYS_ADMIN);
	}

	@Override
	public String getFeatureName()
	{
		return model.getFeatureName();
	}

	@Override
	public Long getAccountId()
	{
		return this.user.getId();
	}

}
