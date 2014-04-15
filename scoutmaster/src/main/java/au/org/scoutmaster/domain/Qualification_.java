package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.886+1000")
@StaticMetamodel(Qualification.class)
public class Qualification_ extends BaseEntity_ {
	public static volatile SingularAttribute<Qualification, QualificationType> type;
	public static volatile SingularAttribute<Qualification, Contact> leader;
	public static volatile SingularAttribute<Qualification, Date> obtained;
	public static volatile SingularAttribute<Qualification, Date> expires;
}
