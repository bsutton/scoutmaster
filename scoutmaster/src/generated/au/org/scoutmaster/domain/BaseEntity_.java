package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_
{

	public static volatile SingularAttribute<BaseEntity, Date> created;
	public static volatile SingularAttribute<BaseEntity, Long> id;
	public static volatile SingularAttribute<BaseEntity, Date> updated;
	public static volatile SingularAttribute<BaseEntity, Long> consistencyVersion;

}