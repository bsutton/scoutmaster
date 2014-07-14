package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-13T17:00:34.969+1000")
@StaticMetamodel(Organisation.class)
public class Organisation_ extends BaseEntity_ {
	public static volatile SingularAttribute<Organisation, Boolean> isOurScoutGroup;
	public static volatile SingularAttribute<Organisation, String> name;
	public static volatile SingularAttribute<Organisation, OrganisationType> organisationType;
	public static volatile SingularAttribute<Organisation, String> description;
	public static volatile ListAttribute<Organisation, Contact> contacts;
	public static volatile SingularAttribute<Organisation, Address> location;
	public static volatile SingularAttribute<Organisation, Phone> phone1;
	public static volatile SingularAttribute<Organisation, Phone> phone2;
	public static volatile SingularAttribute<Organisation, Phone> phone3;
	public static volatile ListAttribute<Organisation, Tag> tags;
	public static volatile ListAttribute<Organisation, Note> notes;
	public static volatile ListAttribute<Organisation, CommunicationLog> activites;
}
