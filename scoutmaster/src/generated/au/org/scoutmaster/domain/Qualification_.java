package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Qualification.class)
public class Qualification_ extends BaseEntity_
{

	public static volatile SingularAttribute<Qualification, Contact> leader;
	public static volatile SingularAttribute<Qualification, Date> expires;
	public static volatile SingularAttribute<Qualification, Date> obtained;
	public static volatile SingularAttribute<Qualification, QualificationType> type;

}