package au.com.vaadinutils.jasper.scheduler.entities;

import au.com.vaadinutils.jasper.JasperManager.OutputFormat;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-13T17:27:00.235+1000")
@StaticMetamodel(ReportEmailScheduleEntity.class)
public class ReportEmailScheduleEntity_ {
	public static volatile SingularAttribute<ReportEmailScheduleEntity, Long> iID;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> JasperReportPropertiesClassName;
	public static volatile ListAttribute<ReportEmailScheduleEntity, ReportEmailScheduledDateParameter> dateParameters;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, Boolean> enabled;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, ReportEmailSender> sender;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, Date> lastRuntime;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, ScheduleMode> scheduleMode;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, Date> oneTimeRunDateTime;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, Date> timeOfDayToRun;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, Integer> scheduledDayOfMonth;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> scheduledDaysOfWeek;
	public static volatile ListAttribute<ReportEmailScheduleEntity, ReportEmailParameterEntity> reportParameters;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, OutputFormat> outputFormat;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> message;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> subject;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> reportLog;
	public static volatile ListAttribute<ReportEmailScheduleEntity, ReportEmailRecipient> recipients;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> reportFileName;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> reportTitle;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, String> reportIdentifier;
	public static volatile SingularAttribute<ReportEmailScheduleEntity, Date> nextScheduledTime;
}
