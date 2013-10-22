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

@Entity
@Table(name="Purchase")
public class Purchase  extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The contact this purchase was made from.
	 */
	@ManyToOne
	Contact purchasedFrom;
	
	/**
	 * The group member that made the purchase.
	 */
	@ManyToOne
	Contact purchasedBy;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<PurchaseLine> purchaseLines = new ArrayList<>();

	@Override
	public String getName()
	{
		return purchasedFrom.getFullname() ;
	}
	
}
