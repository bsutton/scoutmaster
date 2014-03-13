package au.org.scoutmaster.help;

import java.util.ArrayList;

import au.com.vaadinutils.menu.ViewMapping;
import au.org.scoutmaster.views.ActivityView;
import au.org.scoutmaster.views.ChangePasswordView;
import au.org.scoutmaster.views.ContactView;
import au.org.scoutmaster.views.EventView;
import au.org.scoutmaster.views.ForgottenPasswordView;
import au.org.scoutmaster.views.LoginView;
import au.org.scoutmaster.views.LogoutView;
import au.org.scoutmaster.views.OrganisationTypeView;
import au.org.scoutmaster.views.OrganisationView;
import au.org.scoutmaster.views.QualificationTypeView;
import au.org.scoutmaster.views.RaffleView;
import au.org.scoutmaster.views.ResetPasswordView;
import au.org.scoutmaster.views.SectionTypeView;
import au.org.scoutmaster.views.TagView;
import au.org.scoutmaster.views.UserView;
import au.org.scoutmaster.views.calendar.CalendarView;
import au.org.scoutmaster.views.reports.CalendarReportView;
import au.org.scoutmaster.views.reports.ExternalProspectsReport;
import au.org.scoutmaster.views.reports.MemberAddressReport;
import au.org.scoutmaster.views.reports.MemberReport;
import au.org.scoutmaster.views.wizards.bulkEmail.BulkEmailWizardView;
import au.org.scoutmaster.views.wizards.bulkSMS.BulkSMSWizardView;
import au.org.scoutmaster.views.wizards.importer.ImportWizardView;
import au.org.scoutmaster.views.wizards.raffle.allocateBooks.RaffleBookAllocationWizardView;
import au.org.scoutmaster.views.wizards.raffle.importBooks.RaffleBookImportWizardView;
import au.org.scoutmaster.views.wizards.setup.GroupSetupWizardView;

import com.vaadin.navigator.View;

public enum ScoutmasterViewEnum
{
	Contact(ContactView.NAME, ContactView.class,  true),
	BulkEmailWizard(BulkEmailWizardView.NAME, BulkEmailWizardView.class, false),
	BulkSMSWizard(BulkSMSWizardView.NAME, BulkSMSWizardView.class, false),
	Calendar(CalendarView.NAME, CalendarView.class, false),
	Event(EventView.NAME, EventView.class, false),
	CalendarReport(CalendarReportView.NAME, CalendarReportView.class, false),
	ExternalProspects(ExternalProspectsReport.NAME, ExternalProspectsReport.class, false),
	Member(MemberReport.NAME, MemberReport.class, false),
	MemberAddress(MemberAddressReport.NAME, MemberAddressReport.class, false),
	
	// Wizards
	GroupSetup(GroupSetupWizardView.NAME, GroupSetupWizardView.class, false),
	RaffleBookImportWizard(RaffleBookImportWizardView.NAME, RaffleBookImportWizardView.class, false),
	RaffleBookAllocationWizard(RaffleBookAllocationWizardView.NAME, RaffleBookAllocationWizardView.class, false),
	ImportWizard(ImportWizardView.NAME, ImportWizardView.class, false),

	
	// Admin menu
	Activity(ActivityView.NAME, ActivityView.class, false),
	Organisation(OrganisationView.NAME, OrganisationView.class, false),
	OrganisationType(OrganisationTypeView.NAME, OrganisationTypeView.class, false),
	QualificationType(QualificationTypeView.NAME, QualificationTypeView.class, false),
	Raffle(RaffleView.NAME, RaffleView.class, false),
	SectionType(SectionTypeView.NAME, SectionTypeView.class, false),
	ChangePassword(ChangePasswordView.NAME, ChangePasswordView.class, false),
	Tag(TagView.NAME, TagView.class, false),
	User(UserView.NAME, UserView.class, false),
	

	
	// viewMap.add(new ViewMap(SectionBulkEmailWizard.NAME,
	// SectionBulkEmailWizard.class));

	Login(LoginView.NAME, LoginView.class, false),
	Logout(LogoutView.NAME, LogoutView.class, false),
	ForgottenPassword(ForgottenPasswordView.NAME, ForgottenPasswordView.class, false),
	ResetPassword(ResetPasswordView.NAME, ResetPasswordView.class, false);

	
	
	
	private String title;
	private Class<? extends View> clazz;
	private boolean defaultView;

	ScoutmasterViewEnum(String title, Class<? extends View> clazz, boolean defaultView)
	{
		this.title = title;
		this.clazz = clazz;
		this.defaultView = defaultView;
				
		
	}

	public boolean noHelp()
	{
		return !HelpProvider.class.isAssignableFrom(clazz);
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
		ArrayList<ViewMapping> mappings = new ArrayList<>();
		
		for (ScoutmasterViewEnum value : values())
		{
			mappings.add(new ViewMapping(value.getTitle(), value.getViewClass()));
		}
		return mappings;
	}

}