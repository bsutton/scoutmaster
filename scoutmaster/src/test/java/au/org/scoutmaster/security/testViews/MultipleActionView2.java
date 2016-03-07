package au.org.scoutmaster.security.testViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;

// @formatter:off
	@iFeature(name = "MultipleActionView"
			, permissions =
			{
					@iPermission(action = Action.LIST, roles ={ eRole.MEMBER })
					, @iPermission(action = Action.NEW, roles ={ eRole.COMMITTEE_MEMBER, eRole.LEADER})
					// Add the Secretary role to existing permission
					, @iPermission(action = Action.DELETE, roles ={ eRole.GROUP_LEADER, eRole.PRESIDENT, eRole.SECRETARY })
					// add new permission
					, @iPermission(action = Action.ACCESS_MEMBER_DETAILS, roles ={ eRole.GROUP_LEADER, eRole.PRESIDENT })
			}
	)
	// @formatter:on
public class MultipleActionView2 implements View
{
	private static final long serialVersionUID = 1L;

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}

}