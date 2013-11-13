package au.org.scoutmaster.domain;

import au.org.scoutmaster.domain.access.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-11-07T22:12:12.325+1100")
@StaticMetamodel(Document.class)
public class Document_ extends BaseEntity_ {
	public static volatile SingularAttribute<Document, User> addedBy;
	public static volatile SingularAttribute<Document, String> description;
	public static volatile SingularAttribute<Document, String> filename;
	public static volatile SingularAttribute<Document, byte[]> content;
	public static volatile SingularAttribute<Document, String> mimeType;
}
