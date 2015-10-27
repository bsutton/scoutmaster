package au.org.scoutmaster.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-10-27T22:28:03.863+1100")
@StaticMetamodel(Note.class)
public class Note_ extends BaseEntity_ {
	public static volatile SingularAttribute<Note, Contact> attachedContact;
	public static volatile SingularAttribute<Note, Date> noteDate;
	public static volatile SingularAttribute<Note, String> subject;
	public static volatile SingularAttribute<Note, String> body;
	public static volatile SingularAttribute<Note, String> guid;
}
