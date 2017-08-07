package au.org.scoutmaster.security.testViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eSecurityRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;

// @formatter:off
@iFeature(permissions =
		{
				@iPermission(action = Action.ACCESS, roles ={ eSecurityRole.YOUTH_MEMBER })
				, @iPermission(action = Action.ACCESS, roles ={ eSecurityRole.YOUTH_MEMBER })
		}
	)
	// @formatter:on

public class DuplicateActionView implements View
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}

}