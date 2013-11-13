package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-28T23:52:41.903+1100")
@StaticMetamodel(MoneyWithTax.class)
public class MoneyWithTax_ {
	public static volatile SingularAttribute<MoneyWithTax, Money> money;
	public static volatile SingularAttribute<MoneyWithTax, FixedDouble> taxPercentage;
}
