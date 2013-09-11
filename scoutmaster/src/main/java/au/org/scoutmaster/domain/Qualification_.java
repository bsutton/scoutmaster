package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-11T21:56:55.481+1000")
@StaticMetamodel(Qualification.class)
public class Qualification_ extends BaseEntity_ {
	public static volatile SingularAttribute<Qualification, QualificationType> type;
	public static volatile SingularAttribute<Qualification, Contact> leader;
	public static volatile SingularAttribute<Qualification, Date> obtained;
	public static volatile SingularAttribute<Qualification, Date> expires;
}
