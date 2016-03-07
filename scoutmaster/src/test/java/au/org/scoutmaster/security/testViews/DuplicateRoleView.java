package au.org.scoutmaster.security.testViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;

// @formatter:off
	@iFeature(name = "DuplicateRolesView"
			, permissions =	{
					@iPermission(action = Action.LIST, roles ={ eRole.MEMBER, eRole.MEMBER })
			}

	)
	// @formatter:on
public class DuplicateRoleView implements View
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