package au.org.scoutmaster.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A feature is an annotation that defines the set of actions that are available
 * (typically on a view) and what Security Roles have access to those actions. A
 * permission is a mapping from an Action to one or more Security Roles.
 *
 * Users can then have 'n' Security Roles added to their account affectively
 * given them access to a defined set of Actions.
 *
 * @author bsutton
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface iFeature
{
	iPermission[] permissions();
}
