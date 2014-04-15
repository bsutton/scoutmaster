package au.org.scoutmaster.domain.accounting;

public enum PurchaseMethod
{
	/**
	 * Used when the group directly purchases goods from a supplier.
	 */
	DIRECT_PURCHASE

	/**
	 * Used when a Group Member purchases goods and then submits an expense
	 * claim.
	 */
	, EXPENSE_CLAIM;

}
