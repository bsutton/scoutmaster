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
					// None of these changes should be applied ad the feature has been user edited.
					@iPermission(action = Action.NEW, roles ={ eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
					// re-add secretary
					, @iPermission(action = Action.DELETE, roles ={ eSecurityRole.GROUP_LEADER, eSecurityRole.PRESIDENT, eSecurityRole.SECRETARY })
					// Remove Role President
					, @iPermission(action = Action.SENSITIVE_ACCESS, roles ={ eSecurityRole.GROUP_LEADER}) //, eRole.PRESIDENT
			}
	)
	// @formatter:on
public class MultipleActionView4 implements View
{
	private static final long serialVersionUID = 1L;

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}

}