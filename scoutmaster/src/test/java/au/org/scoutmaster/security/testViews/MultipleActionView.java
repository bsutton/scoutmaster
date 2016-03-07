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
					, @iPermission(action = Action.DELETE, roles ={ eRole.GROUP_LEADER, eRole.PRESIDENT })
			}
	)
	// @formatter:on
public class MultipleActionView implements View
{
	private static final long serialVersionUID = 1L;

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}

}