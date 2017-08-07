package au.org.scoutmaster.security;

/**
 * @formatter:off
 *
 * eSecurityRoles define the standard set of SecurityRoles available in every system.
 * A Group Leader is able to define additional security roles that suite the operation of the group.
 *
 * Access is broadly described for each security role below.
 *
 * Information is broadly categorised by:
 *
 * Contact Sensitive Access		 	- access to a contacts sensitive information that includes medical records or any legal proceedings such as Custody Orders.
 * Contact Financial Information	- access to a contacts financial information such as payment history, amounts outstanding or donations.
 * Contact Read access				- readonly access to a contacts non-sensitive and non-financial information
 * Contact Write access				- write access to a contacts non-sensitive and non-financial information. Includes the ability to create new contacts.
 * Contact Full access				- read/write access to all contact information including sensitive and financial
 * Communications Access			- able to send communications to contacts via email or sms.
 * Assign Security Roles			- able to assign a security role to a user other than, Child Member or Adult Member
 * Create User						- able to create user accounts with a security role of Child Member, Adult Member or Volunteer
 *
 *These categories are to be used as guiding principles in defining the security model. The feature matrix defines the minutiae of the security model.
 *
 * @formatter:on
 *
 * @author bsutton
 *
 */
public enum eSecurityRole
{

	/**
	 * @formatter:off
	 */
	NONE("None")
	, ADULT_MEMBER("Adult Member")  			// Full Access to their own details as well as any members that they are the guardian of.
	, YOUTH_MEMBER("Youth Member")				// Contact Read/Write Access to their own details. A Child Member cannot change their DOB.
	, COMMITTEE_MEMBER("Committee Member")  	// Contact Read Access
	, COMMUNICATIONS_OFFICER("Communications Officer") // Contact Read/Write Access, Communications Access
	, GROUP_LEADER("Group Leader") 				// Contact Full Access, Communications Access, Create User, Assign Security Roles
	, LEADER("Leader")							// Contact Read/Write/Sensitive of all Contacts. May add Volunteer, Child Member and Adult Member users.
	, PRESIDENT("President")					// Full Contact Access.
	, QUARTERMASTER("Quartermaster")			// Contact Read Access
	, SECRETARY("Secretary")					// Contact Read/Write/Sensitive Access
	, TREASURER("Treasurer") 					// Contact Read/Write/Financial Access
	, USER("User") 								// Implicit. If they have a user account then they have the user securityrole which gives basic operations such as login and own password maintenance.
	, VOLUNTEER("Volunteer")					// Access to broad group information (e.g. forthcoming activities) but no access to the Contact list.
	, TECH_SUPPORT("Tech Support");				// Access everything with the exclusion of Sensitive information. However access needs to be given by the Group Leader and then only for a defined period of time.

	/**
	 * @formatter:on
	 */

	private String label;

	eSecurityRole(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
