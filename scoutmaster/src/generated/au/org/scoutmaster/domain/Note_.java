package au.org.scoutmaster.domain;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Note.class)
public class Note_ extends BaseEntity_
{

	public static volatile SingularAttribute<Note, Contact> attachedContact;
	public static volatile SingularAttribute<Note, Date> noteDate;
	public static volatile SingularAttribute<Note, String> subject;
	public static volatile SingularAttribute<Note, String> body;

}