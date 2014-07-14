package au.com.vaadinutils.jasper.scheduler.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-13T17:27:00.234+1000")
@StaticMetamodel(ReportEmailScheduledDateParameter.class)
public class ReportEmailScheduledDateParameter_ {
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, Long> iID;
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, DateParameterType> type;
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, DateParameterOffsetType> offsetType;
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, String> label;
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, String> startName;
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, Date> startDate;
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, String> endName;
	public static volatile SingularAttribute<ReportEmailScheduledDateParameter, Date> endDate;
}
