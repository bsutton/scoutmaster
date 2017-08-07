package au.org.scoutmaster.domain;

import au.org.scoutmaster.domain.access.Permission;
import au.org.scoutmaster.domain.GroupRole.BuiltIn;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:55:00.404+1000")
@StaticMetamodel(GroupRole.class)
public class GroupRole_ extends BaseEntity_ {
	public static volatile SingularAttribute<GroupRole, String> name;
	public static volatile SingularAttribute<GroupRole, BuiltIn> enumName;
	public static volatile SingularAttribute<GroupRole, String> description;
	public static volatile SingularAttribute<GroupRole, Boolean> primaryRole;
	public static volatile SetAttribute<GroupRole, Tag> tags;
	public static volatile SetAttribute<GroupRole, Permission> permissions;
}
