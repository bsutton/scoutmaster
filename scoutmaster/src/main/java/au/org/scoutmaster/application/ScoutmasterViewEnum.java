package au.org.scoutmaster.application;

import java.util.ArrayList;

import com.vaadin.navigator.View;

import au.com.vaadinutils.help.HelpProvider;
import au.com.vaadinutils.menu.ViewMapping;
import au.org.scoutmaster.views.ChangePasswordView;
import au.org.scoutmaster.views.CommunicationLogView;
import au.org.scoutmaster.views.ContactView;
import au.org.scoutmaster.views.EventView;
import au.org.scoutmaster.views.ForgottenPasswordView;
import au.org.scoutmaster.views.HomeView;
import au.org.scoutmaster.views.LoginAttemptView;
import au.org.scoutmaster.views.LoginView;
import au.org.scoutmaster.views.LogoutView;
import au.org.scoutmaster.views.OrganisationTypeView;
import au.org.scoutmaster.views.OrganisationView;
import au.org.scoutmaster.views.QualificationTypeView;
import au.org.scoutmaster.views.RaffleView;
import au.org.scoutmaster.views.ResetPasswordView;
import au.org.scoutmaster.views.SchoolView;
import au.org.scoutmaster.views.SectionTypeView;
import au.org.scoutmaster.views.SessionHistoryView;
import au.org.scoutmaster.views.TagView;
import au.org.scoutmaster.views.TaskStatusView;
import au.org.scoutmaster.views.TaskTypeView;
import au.org.scoutmaster.views.TaskView;
import au.org.scoutmaster.views.UserView;
import au.org.scoutmaster.views.accounting.InvoiceView;
import au.org.scoutmaster.views.calendar.CalendarView;
import au.org.scoutmaster.views.calendar.PublicCalendarView;
import au.org.scoutmaster.views.reports.CalendarReportView;
import au.org.scoutmaster.views.reports.ExternalProspectsReport;
import au.org.scoutmaster.views.reports.MemberAddressReport;
import au.org.scoutmaster.views.reports.MemberReport;
import au.org.scoutmaster.views.reports.RaffleAllocationsReportView;
import au.org.scoutmaster.views.wizards.bulkEmail.BulkEmailWizardView;
import au.org.scoutmaster.views.wizards.bulkSMS.BulkSMSWizardView;
import au.org.scoutmaster.views.wizards.groupmaintenance.GroupMaintenanceWizardView;
import au.org.scoutmaster.views.wizards.groupsetup.GroupSetupWizardView;
import au.org.scoutmaster.views.wizards.importer.ImportWizardView;
import au.org.scoutmaster.views.wizards.raffle.allocateBooks.RaffleBookAllocationWizardView;
import au.org.scoutmaster.views.wizards.raffle.importBooks.RaffleBookImportWizardView;

public enum ScoutmasterViewEnum
{
	// @formatter:off
	// Members
	Contact(ContactView.NAME, ContactView.class, true)
	, Member(MemberReport.NAME, MemberReport.class)
	, MemberAddress(MemberAddressReport.NAME, MemberAddressReport.class)
	, Tag(TagView.NAME, TagView.class)
	, ExternalProspects(ExternalProspectsReport.NAME, ExternalProspectsReport.class)
	, ImportWizard(ImportWizardView.NAME, ImportWizardView.class)
	, School(SchoolView.NAME, SchoolView.class, true)


	// Communications
	, BulkEmailWizard(BulkEmailWizardView.NAME, BulkEmailWizardView.class)
	, BulkSMSWizard(BulkSMSWizardView.NAME, BulkSMSWizardView.class)
	, CommunicationLog(CommunicationLogView.NAME, CommunicationLogView.class)

	// Calendar
	, Calendar(CalendarView.NAME, CalendarView.class)
	, Event(EventView.NAME, EventView.class)
	, CalendarReport(CalendarReportView.NAME, CalendarReportView.class)
	, PublicCalendar(PublicCalendarView.NAME, PublicCalendarView.class)

	// Raffle
	, Raffle(RaffleView.NAME, RaffleView.class)
	, RaffleBookImportWizard(RaffleBookImportWizardView.NAME, RaffleBookImportWizardView.class)
	, RaffleBookAllocationWizard(RaffleBookAllocationWizardView.NAME, RaffleBookAllocationWizardView.class)
	, RaffleAllocationsReport(RaffleAllocationsReportView.NAME, RaffleAllocationsReportView.class)

	// Wizards
	,GroupMaintenance(GroupMaintenanceWizardView.NAME, GroupMaintenanceWizardView.class)

	, GroupSetup(GroupSetupWizardView.NAME, GroupSetupWizardView.class)

	// Admin menu
	, Organisation(OrganisationView.NAME, OrganisationView.class)
	, OrganisationType(OrganisationTypeView.NAME, OrganisationTypeView.class)
	, QualificationType(QualificationTypeView.NAME, QualificationTypeView.class)
	, SectionType(SectionTypeView.NAME, SectionTypeView.class)
	, Task(TaskView.NAME,TaskView.class)
	, TaskType(TaskTypeView.NAME, TaskTypeView.class)
	, TaskStatus(TaskStatusView.NAME, TaskStatusView.class)
	, ChangePassword(ChangePasswordView.NAME, ChangePasswordView.class)

	// Admin Security
	, User(UserView.NAME, UserView.class)
	, SessionHistory(SessionHistoryView.NAME, SessionHistoryView.class)
	, LoginAttempt(LoginAttemptView.NAME, LoginAttemptView.class)


	// viewMap.add(new ViewMap(SectionBulkEmailWizard.NAME,
	// SectionBulkEmailWizard.class));

	, Home(HomeView.NAME, HomeView.class)
	, Login(LoginView.NAME, LoginView.class)
	, Logout(LogoutView.NAME, LogoutView.class)
	, ForgottenPassword(ForgottenPasswordView.NAME, ForgottenPasswordView.class)
	, ResetPassword(ResetPasswordView.NAME, ResetPasswordView.class)

	// Finance
	, Invoice(InvoiceView.NAME, InvoiceView.class);

	// @formatter:on
	private String title;
	private Class<? extends View> clazz;
	private boolean defaultView;

	ScoutmasterViewEnum(final String title, final Class<? extends View> clazz, final boolean defaultView)
	{
		this.title = title;
		this.clazz = clazz;
		this.defaultView = defaultView;

	}

	ScoutmasterViewEnum(final String title, final Class<? extends View> clazz)
	{
		this.title = title;
		this.clazz = clazz;
		this.defaultView = false;
	}

	public boolean isDefaultView()
	{
		return this.defaultView;
	}

	public boolean noHelp()
	{
		return !HelpProvider.class.isAssignableFrom(this.clazz);
	}

	public String getTitle()
	{
		return this.title;
	}

	public Class<? extends View> getViewClass()
	{
		return this.clazz;
	}

	public static ArrayList<ViewMapping> getViewMap()
	{
		final ArrayList<ViewMapping> mappings = new ArrayList<>();

		for (final ScoutmasterViewEnum value : ScoutmasterViewEnum.values())
		{
			mappings.add(new ViewMapping(value.getTitle(), value.getViewClass()));
		}
		return mappings;
	}

	public static Class<? extends View> getDefaultView()
	{
		ScoutmasterViewEnum defaultView = null;
		for (final ScoutmasterViewEnum value : ScoutmasterViewEnum.values())
		{
			if (value.defaultView)
			{
				defaultView = value;
				break;
			}
		}
		return defaultView.clazz;
	}

}
