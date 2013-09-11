package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-11T21:56:55.457+1000")
@StaticMetamodel(BaseEntity.class)
public class BaseEntity_ {
	public static volatile SingularAttribute<BaseEntity, Long> id;
	public static volatile SingularAttribute<BaseEntity, Date> created;
	public static volatile SingularAttribute<BaseEntity, Date> updated;
	public static volatile SingularAttribute<BaseEntity, Long> consistencyVersion;
}
