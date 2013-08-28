package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.275+1000")
@StaticMetamodel(Note.class)
public class Note_ extends BaseEntity_ {
	public static volatile SingularAttribute<Note, String> subject;
	public static volatile SingularAttribute<Note, String> body;
}
