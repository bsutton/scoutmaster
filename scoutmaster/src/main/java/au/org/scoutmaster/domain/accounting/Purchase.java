package au.org.scoutmaster.domain.accounting;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;

@Entity
@Table(name="Purchase")
public class Purchase  extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * How the purchase was made. Either direct purchase or an expense claim.
	 * 
	 * For a direct purchase
	 */
	PurchaseMethod method;

	/**
	 * The organisation/supplier this purchase was made from.
	 * 
	 * This will generally 
	 */
	@ManyToOne
	Organisation purchasedFrom;
	
	/**
	 * The group member that made the purchase.
	 * 
	 * If the method is EXPENSE_CLAIM then this is the person the group needs to pay.
	 */
	@ManyToOne
	Contact purchasedBy;
	
	
	/**
	 * The group member that approved the purchase.
	 */
	Contact approvedBy;
	
	/**
	 * The reason the goods or services where purchases
	 */
	String reason;
	
	/**
	 * A high level description of what was purchased.
	 */
	String description;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<PurchaseLine> purchaseLines = new ArrayList<>();

	@Override
	public String getName()
	{
		return purchasedFrom.getName();
	}
	
}
