package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.762+1000")
@StaticMetamodel(MoneyWithTax.class)
public class MoneyWithTax_ {
	public static volatile SingularAttribute<MoneyWithTax, Money> money;
	public static volatile SingularAttribute<MoneyWithTax, FixedDouble> taxPercentage;
}
