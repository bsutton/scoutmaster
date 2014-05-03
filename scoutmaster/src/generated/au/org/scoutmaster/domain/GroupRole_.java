package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.GroupRole.BuiltIn;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(GroupRole.class)
public class GroupRole_ extends BaseEntity_
{

	public static volatile SingularAttribute<GroupRole, String> name;
	public static volatile SingularAttribute<GroupRole, String> description;
	public static volatile SingularAttribute<GroupRole, BuiltIn> enumName;
	public static volatile SetAttribute<GroupRole, Tag> tags;

}