package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-19T09:12:29.170+1000")
@StaticMetamodel(Timestampable.class)
public class Timestampable_ {
	public static volatile SingularAttribute<Timestampable, Long> id;
	public static volatile SingularAttribute<Timestampable, Date> createdAt;
	public static volatile SingularAttribute<Timestampable, Date> updatedAt;
}
