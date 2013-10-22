package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-21T18:10:33.179+1100")
@StaticMetamodel(Purchase.class)
public class Purchase_ extends BaseEntity_ {
	public static volatile SingularAttribute<Purchase, Contact> purchasedFrom;
	public static volatile SingularAttribute<Purchase, Contact> purchasedBy;
	public static volatile ListAttribute<Purchase, PurchaseLine> purchaseLines;
}
