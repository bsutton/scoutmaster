package au.org.scoutmaster.domain;

import au.org.scoutmaster.domain.access.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T17:08:00.073+1000")
@StaticMetamodel(Task.class)
public class Task_ extends BaseEntity_ {
	public static volatile SingularAttribute<Task, TaskType> taskType;
	public static volatile SingularAttribute<Task, User> addedBy;
	public static volatile SingularAttribute<Task, TaskStatus> taskStatus;
	public static volatile SingularAttribute<Task, Boolean> privateTask;
	public static volatile SingularAttribute<Task, Contact> withContact;
	public static volatile SingularAttribute<Task, Date> dueDate;
	public static volatile SingularAttribute<Task, Date> completionDate;
	public static volatile SingularAttribute<Task, String> subject;
	public static volatile SingularAttribute<Task, String> details;
	public static volatile SingularAttribute<Task, Section> section;
}
