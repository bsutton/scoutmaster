package au.org.scoutmaster.security.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eRole;

/**
 * An iFeature creates a linkage between a particular View and the set of
 * Actions that can be performed on the view. The Feature then defines the set
 * of Roles (eRole) that can perform those actions against the view.
 *
 * @author bsutton
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface iPermission
{

	/**
	 * The action we are associating with the above view.
	 *
	 * @return
	 */
	Action action();

	/**
	 * The set of Roles (eRole) that are allowed to perform the action against
	 * the view.
	 *
	 * @return
	 */
	eRole[] roles();

}
