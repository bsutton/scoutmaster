package au.org.scoutmaster.domain.schemaManagement;

import au.org.scoutmaster.domain.RelationshipType;
import au.org.scoutmaster.domain.RelationshipType.Type;

public class NewDatabase
{

	static void setupBuiltInRelationshipTypes()
	{
		createRelationshipType("Case Coordinator is", Type.CONTACT, "Case Coordinator", Type.CONTACT);
		createRelationshipType("Child of", Type.CONTACT, "Parent of", Type.CONTACT);
		createRelationshipType("Employee of", Type.CONTACT, "Organisation", Type.CONTACT);
		createRelationshipType("Gardian of", Type.CONTACT, "Ward of", Type.CONTACT);
		createRelationshipType("Grandparent of", Type.CONTACT, "Grandchild of", Type.CONTACT);
		createRelationshipType("Head of Household for", Type.CONTACT, "Head of Household is", Type.CONTACT);
		createRelationshipType("Household Member of", Type.CONTACT, "Household Member is", Type.HOUSEHOLD);
		createRelationshipType("Sibling of", Type.CONTACT, "Sibling of", Type.CONTACT);
		createRelationshipType("Spouse of", Type.CONTACT, "Spouse of", Type.CONTACT);
		createRelationshipType("Step Parent of", Type.CONTACT, "Step Child of", Type.CONTACT);
		createRelationshipType("Supervised by", Type.CONTACT, "Supervisor", Type.CONTACT);
		createRelationshipType("Within our catchment", Type.ORGANISATION, "In catchment of", Type.ORGANISATION);

	}

	private static void createRelationshipType(String lhs, Type lhsType, String rhs, Type rhsType)
	{
		@SuppressWarnings("unused")
		RelationshipType relationshipType = new RelationshipType(lhs, lhsType, rhs, rhsType);
		// TODO: save the relationships to the db.
//		..addItem(relathipshipType)
//		..commit();
	}

}
