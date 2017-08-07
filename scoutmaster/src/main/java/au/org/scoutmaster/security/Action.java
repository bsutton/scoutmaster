package au.org.scoutmaster.security;

public enum Action
{
	/**
	 * @formatter:off
	 */
	ACCESS					// Access a view, generally you can see the list of entities and view their details.
	, SENSITIVE_ACCESS		// If an entity has potentially sensitive details (e.g. medical records) then this Action is required to see them).
	, NEW					// Create new entities
	, DELETE				// Delete entities
	, EDIT					// Edit an existing entity
	, CHANGE_USER_ROLES		// Change the security roles of a user.
	, RESET_PASSWORD		//
	, MANAGE_SELF 			// Allows a user to manage their own contact details
	, EDIT_TAGS  			// Allows a user to edit the set of tags a contact has.
	, EDIT_WARD  			// Allows a Guardian (parent etc) to edit contact details of their wards (children, grandchildren).
	;
	/**
	 * @formatter:on
	 */
}