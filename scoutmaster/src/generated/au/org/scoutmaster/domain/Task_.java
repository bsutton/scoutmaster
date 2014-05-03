package au.org.scoutmaster.domain;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.access.User;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Task.class)
public class Task_ extends BaseEntity_
{

	public static volatile SingularAttribute<Task, Contact> withContact;
	public static volatile SingularAttribute<Task, TaskType> taskType;
	public static volatile SingularAttribute<Task, User> addedBy;
	public static volatile SingularAttribute<Task, String> subject;
	public static volatile SingularAttribute<Task, Date> dueDate;
	public static volatile SingularAttribute<Task, Date> completionDate;
	public static volatile SingularAttribute<Task, String> details;
	public static volatile SingularAttribute<Task, Section> section;
	public static volatile SingularAttribute<Task, Boolean> privateTask;
	public static volatile SingularAttribute<Task, TaskStatus> taskStatus;

}