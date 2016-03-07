package au.org.scoutmaster.security.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface iFeature
{
	/**
	 * The class that this feature applies to.
	 *
	 * When declaring an Feature annotation if you don't define the view
	 * attribute then the declaring class (the class the annotation is attached
	 * to) is used by default.
	 *
	 * @return
	 */
	String name();

	iPermission[] permissions();
}
