package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.org.scoutmaster.domain.accounting.MoneyWithTax;

/**
 * A youth member who is currently trying out to see if they would like to join
 * a Section.
 */
@Entity(name = "SectionTryout")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "SectionTryout")
@Access(AccessType.FIELD)
public class SectionTryout extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The type of tryout.
	 */
	@ManyToOne(targetEntity = SectionTryoutType.class)
	SectionTryoutType type;

	/**
	 * The youth who is doing a trial.
	 */
	@OneToOne(targetEntity = Contact.class)
	Contact trialYouthMember;

	/**
	 * The paperwork necessary for the trial has been completed.
	 */
	Boolean trailPaperWorkCompleted;
	/**
	 * The date the youth first attended a trial.
	 */
	Date startDate;

	/**
	 * The date the trial is expected to complete.
	 */
	Date expectedCompletionDate;

	/**
	 * The actual date the trial completed.
	 */
	Date actualCompletionDate;

	/**
	 * The outcome of the trail
	 */
	TryoutOutcome outcome;

	/**
	 * The cost to this individual for the tryout. Tryouts may be free or the
	 * leader may organise a discount for this youth to do a tryout.
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "costMoneyValue") ),
			@AttributeOverride(name = "money.precision", column = @Column(name = "costMoneyPrecision") ),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "costTaxPercentageValue") ),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "costTaxPercentagePrecision") ) })
	MoneyWithTax cost;

	/*
	 * If the SetionTryoutType has a cost then records if the member has paid.
	 */
	Boolean paid;

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return this.trialYouthMember.getFullname() + this.type.getName();
	}

}
