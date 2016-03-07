package au.org.scoutmaster.security.testViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;

// @formatter:off
@iFeature(name = "DuplicateActionView"
		, permissions =
		{
				@iPermission(action = Action.LIST, roles ={ eRole.MEMBER })
				, @iPermission(action = Action.LIST, roles ={ eRole.MEMBER })
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