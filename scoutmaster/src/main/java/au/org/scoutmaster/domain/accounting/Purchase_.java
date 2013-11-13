package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-28T21:43:48.775+1100")
@StaticMetamodel(Purchase.class)
public class Purchase_ extends BaseEntity_ {
	public static volatile SingularAttribute<Purchase, String> description;
	public static volatile SingularAttribute<Purchase, String> reason;
	public static volatile SingularAttribute<Purchase, PurchaseMethod> method;
	public static volatile SingularAttribute<Purchase, Organisation> purchasedFrom;
	public static volatile SingularAttribute<Purchase, Contact> purchasedBy;
	public static volatile SingularAttribute<Purchase, Contact> approvedBy;
	public static volatile ListAttribute<Purchase, PurchaseLine> purchaseLines;
}
