package au.com.vaadinutils.jasper.scheduler.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-13T17:27:00.234+1000")
@StaticMetamodel(ReportEmailRecipient.class)
public class ReportEmailRecipient_ {
	public static volatile SingularAttribute<ReportEmailRecipient, Long> iID;
	public static volatile SingularAttribute<ReportEmailRecipient, String> emailAddress;
	public static volatile SingularAttribute<ReportEmailRecipient, ReportEmailRecipientVisibility> visibility;
}
