package au.org.scoutmaster.security;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;

/** @formatter:off **/
@iFeature(permissions =
	{ @iPermission(action = Action.ACCESS, roles = { eSecurityRole.YOUTH_MEMBER }) })
/** @formatter:on **/

public class AccessDeniedView implements View
{
	private static final long serialVersionUID = 1L;

	protected static final String NAME = "AccessDeniedView";

	public AccessDeniedView(String featureName)
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}

}
