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
					, @iPermission(action = Action.NEW, roles ={ eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
					// Add the Secretary role to existing permission
					, @iPermission(action = Action.DELETE, roles ={ eSecurityRole.GROUP_LEADER, eSecurityRole.PRESIDENT, eSecurityRole.SECRETARY })
					// add new permission
					, @iPermission(action = Action.SENSITIVE_ACCESS, roles ={ eSecurityRole.GROUP_LEADER, eSecurityRole.PRESIDENT })
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