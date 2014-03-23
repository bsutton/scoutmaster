package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-20T22:06:36.898+1100")
@StaticMetamodel(Purchase.class)
public class Purchase_ extends BaseEntity_ {
	public static volatile SingularAttribute<Purchase, String> description;
	public static volatile SingularAttribute<Purchase, String> reason;
	public static volatile SingularAttribute<Purchase, PurchaseMethod> purchaseMethod;
	public static volatile SingularAttribute<Purchase, Organisation> purchasedFrom;
	public static volatile SingularAttribute<Purchase, Contact> purchasedBy;
	public static volatile SingularAttribute<Purchase, Contact> approvedBy;
	public static volatile ListAttribute<Purchase, PurchaseLine> purchaseLines;
}
