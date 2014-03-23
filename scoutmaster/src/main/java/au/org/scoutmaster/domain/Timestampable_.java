package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-16T17:00:13.942+1100")
@StaticMetamodel(Timestampable.class)
public class Timestampable_ {
	public static volatile SingularAttribute<Timestampable, Long> id;
	public static volatile SingularAttribute<Timestampable, Date> createdAt;
	public static volatile SingularAttribute<Timestampable, Date> updatedAt;
}
