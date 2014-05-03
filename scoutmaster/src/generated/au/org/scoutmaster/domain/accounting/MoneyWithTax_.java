package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(MoneyWithTax.class)
public class MoneyWithTax_
{

	public static volatile SingularAttribute<MoneyWithTax, Money> money;
	public static volatile SingularAttribute<MoneyWithTax, FixedDouble> taxPercentage;

}