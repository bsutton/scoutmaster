package au.org.scoutmaster.domain.accounting;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;

@Entity
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Purchase")
@Access(AccessType.FIELD)
public class Purchase extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * A high level description of what was purchased.
	 */
	String description;

	/**
	 * The reason the goods or services where purchases
	 */
	String reason;

	/**
	 * How the purchase was made. Either direct purchase or an expense claim.
	 *
	 * For a direct purchase
	 */
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('DIRECT_PURCHASE', 'EXPENSE_CLAIM'")
	PurchaseMethod purchaseMethod;

	/**
	 * The organisation/supplier this purchase was made from.
	 *
	 * This will generally
	 */
	@ManyToOne(targetEntity = Organisation.class)
	Organisation purchasedFrom;

	/**
	 * The group member that made the purchase.
	 *
	 * If the method is EXPENSE_CLAIM then this is the person the group needs to
	 * pay.
	 */
	@ManyToOne(targetEntity = Contact.class)
	Contact purchasedBy;

	/**
	 * The group member that approved the purchase.
	 */
	@ManyToOne(targetEntity = Contact.class)
	Contact approvedBy;

	@OneToMany(targetEntity = PurchaseLine.class)
	List<PurchaseLine> purchaseLines = new ArrayList<>();

	@Override
	public String getName()
	{
		return this.purchasedFrom.getName();
	}

}
