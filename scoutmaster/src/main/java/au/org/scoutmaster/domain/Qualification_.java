package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-02-26T22:43:35.156+1100")
@StaticMetamodel(Qualification.class)
public class Qualification_ extends BaseEntity_ {
	public static volatile SingularAttribute<Qualification, QualificationType> type;
	public static volatile SingularAttribute<Qualification, Contact> leader;
	public static volatile SingularAttribute<Qualification, Date> obtained;
	public static volatile SingularAttribute<Qualification, Date> expires;
}
