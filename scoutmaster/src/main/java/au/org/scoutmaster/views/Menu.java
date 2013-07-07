package au.org.scoutmaster.views;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME) // Make this annotation accessible at runtime via reflection.
@Target({ElementType.TYPE})       // This annotation can only be applied to classes.

public @interface Menu
{
	public final static String MENUBAR = "MenuBar";
	String display();

	String path() default MENUBAR;
}
