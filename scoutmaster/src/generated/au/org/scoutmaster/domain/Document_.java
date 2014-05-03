package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.access.User;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Document.class)
public class Document_ extends BaseEntity_
{

	public static volatile SingularAttribute<Document, String> filename;
	public static volatile SingularAttribute<Document, User> addedBy;
	public static volatile SingularAttribute<Document, String> description;
	public static volatile SingularAttribute<Document, String> mimeType;
	public static volatile SingularAttribute<Document, byte[]> content;

}