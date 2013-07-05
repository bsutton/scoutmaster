package au.com.noojee.scouts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Make this annotation accessible at runtime via reflection.
@Target({ElementType.FIELD})       // This annotation can only be applied to fields.

public @interface FormField
{

	/**
	 * If true then the Form Field should be displayed in an edit form for the entity.
	 * @return
	 */
	boolean visible() default true;

	/**
	 * When displaying this field use the displayName to identify the field to the user.
	 * @return
	 */
	String displayName();

}
