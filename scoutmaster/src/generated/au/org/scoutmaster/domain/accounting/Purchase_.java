package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Purchase.class)
public class Purchase_ extends BaseEntity_
{

	public static volatile SingularAttribute<Purchase, PurchaseMethod> purchaseMethod;
	public static volatile SingularAttribute<Purchase, String> reason;
	public static volatile SingularAttribute<Purchase, Organisation> purchasedFrom;
	public static volatile SingularAttribute<Purchase, Contact> purchasedBy;
	public static volatile SingularAttribute<Purchase, Contact> approvedBy;
	public static volatile SingularAttribute<Purchase, String> description;
	public static volatile ListAttribute<Purchase, PurchaseLine> purchaseLines;

}