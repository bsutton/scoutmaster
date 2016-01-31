package au.org.scoutmaster.views;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.help.HelpProvider;
import au.com.vaadinutils.listener.MouseEventLogged;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Address_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.School;
import au.org.scoutmaster.domain.SchoolGender;
import au.org.scoutmaster.domain.SchoolType;
import au.org.scoutmaster.domain.School_;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.forms.EmailForm;
import au.org.scoutmaster.help.HelpPageIdentifier;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

@Menu(display = "Schools", path = "Members")
public class SchoolView extends BaseCrudView<School> implements View, Selected<School>, HelpProvider
{

	@Override
	protected String getTitleText()
	{
		return "Schools";
	}

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();

	public static final String NAME = "School";

	private Tab youthTab;

	TabSheet tabs = new TabSheet();

	private Image principleEmailImage;

	private Image advertisingContactEmailImage;

	private ChangeListener changeListener;

	public TextField membershipNoField;

	public DateField memberSinceField;

	public DateField dateMemberInvested;

	@Override
	protected VerticalLayout buildEditor(final ValidatingFieldGroup<School> fieldGroup2)
	{
		this.tabs.setSizeFull();

		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		// this needs to be updated when the contact changes
		this.changeListener = new ChangeListener();

		overviewTab();

		layout.addComponent(this.tabs);

		return layout;
	}

	private void overviewTab()
	{
		// Overview tab

		final SMMultiColumnFormLayout<School> overviewForm = new SMMultiColumnFormLayout<School>(2, this.fieldGroup);
		overviewForm.setColumnLabelWidth(0, 200);
		overviewForm.setColumnFieldWidth(0, 300);
		overviewForm.setMargin(true);

		final FormHelper<School> formHelper = overviewForm.getFormHelper();

		this.tabs.addTab(overviewForm, "Overview");
		overviewForm.bindTextField("Name", School_.name);
		overviewForm.newLine();
		overviewForm.bindEnumField("School Type", School_.schoolType, SchoolType.class);
		overviewForm.newLine();
		overviewForm.bindEnumField("School Gender", School_.schoolGender, SchoolGender.class);
		overviewForm.newLine();
		overviewForm.bindTextAreaField("Description", School_.description, 6);
		overviewForm.newLine();
		overviewForm.bindTextField("Phone No.", School_.mainPhoneNo);
		overviewForm.newLine();

		overviewForm.bindTextField("Email Address", School_.generalEmailAddress);
		overviewForm.newLine();

		/** Principle **/

		final ComboBox principle = formHelper.new EntityFieldBuilder<Contact>().setLabel("School Principle")
				.setField(School_.principle).setListFieldName(Contact_.fullname).build();
		principle.setFilteringMode(FilteringMode.CONTAINS);
		principle.setTextInputAllowed(true);
		principle.setDescription("The School's Prinicple.");
		principle.setRequired(true);
		overviewForm.newLine();

		/** Principle Email **/
		final TextField principleEmail = overviewForm.bindTextField("Principle's Email",
				new Path(School_.principle, Contact_.workEmail).getName());
		principleEmail.setReadOnly(true);

		/** Principle Email Icon **/
		this.principleEmailImage = createEmailIcon(new EmailContact()
		{

			@Override
			public Contact getContact()
			{
				return getCurrent().getPrinciple();

			}
		});
		overviewForm.addComponent(this.principleEmailImage);
		overviewForm.newLine();

		/**
		 * Advertising contact
		 */

		final ComboBox advertisingContact = formHelper.new EntityFieldBuilder<Contact>().setLabel("Advertising Contact")
				.setField(School_.advertisingContact).setListFieldName(Contact_.fullname).build();
		advertisingContact.setFilteringMode(FilteringMode.CONTAINS);
		advertisingContact.setTextInputAllowed(true);
		advertisingContact.setDescription("The School's primary Advertising Contact.");
		advertisingContact.setRequired(true);
		overviewForm.newLine();

		/** Advertising email **/
		final TextField advertisingEmail = overviewForm.bindTextField("Advertising Email",
				new Path(School_.advertisingContact, Contact_.workEmail).getName());
		advertisingEmail.setReadOnly(true);

		/** Adveristing email image */

		this.advertisingContactEmailImage = createEmailIcon(new EmailContact()
		{
			@Override
			public Contact getContact()
			{
				return getCurrent().getAdvertisingContact();
			}
		});

		overviewForm.addComponent(this.advertisingContactEmailImage);
		overviewForm.newLine();

		/** Address **/
		TextField street = overviewForm.bindTextField("Street",
				new Path(School_.primaryAddress, Address_.street).getName());
		overviewForm.newLine();
		TextField city = overviewForm.bindTextField("City", new Path(School_.primaryAddress, Address_.city).getName());
		overviewForm.newLine();
		overviewForm.bindTextField("State", new Path(School_.primaryAddress, Address_.state).getName());
		overviewForm.newLine();
		overviewForm.bindTextField("Postcode", new Path(School_.primaryAddress, Address_.postcode).getName());
	}

	private Image createEmailIcon(EmailContact emailContact)
	{
		final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		final FileResource resource = new FileResource(new File(basepath + "/images/email.png"));

		Image image = new Image(null, resource);
		image.setDescription("Click to send an email");
		image.setVisible(false);

		image.addClickListener(new MouseEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final com.vaadin.event.MouseEvents.ClickEvent event)
			{
				showMailForm(emailContact);

			}
		});

		return image;
	}

	interface EmailContact
	{
		Contact getContact();
	}

	private final class ChangeListener implements Property.ValueChangeListener
	{
		private static final long serialVersionUID = 1L;

		private void reset(final School school)
		{
			if (school != null)
			{
			}
			else
			{
			}
		}

		@Override
		public void valueChange(final ValueChangeEvent event)
		{
			// Long groupRoleId = (Long) this.role.getValue();

			@SuppressWarnings("rawtypes")
			final Property source = event.getProperty();

		}

	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<School> container = new DaoFactory().getSchoolDao().createVaadinContainer();

		final Builder<School> builder = new HeadingPropertySet.Builder<School>();
		builder.addColumn("Name", School_.name).addColumn("Type", School_.schoolType)
				.addColumn("Gender", School_.schoolGender)
				.addColumn("Principle", new Path(School_.principle, Contact_.fullname).getName())
				.addColumn("Suburb", new Path(School_.primaryAddress, Address_.city))
				.addColumn("Street", new Path(School_.primaryAddress, Address_.street));

		super.init(School.class, container, builder.build());

	}

	private void showMailForm(EmailContact emailContact)
	{
		final Window mailWindow = new Window("Send Email");
		mailWindow.setWidth("80%");
		mailWindow.setHeight("80%");
		final User sender = (User) getSession().getAttribute("user");
		mailWindow.setContent(
				new EmailForm(mailWindow, sender, emailContact.getContact(), emailContact.getContact().getEmail()));
		mailWindow.setVisible(true);
		mailWindow.center();
		UI.getCurrent().addWindow(mailWindow);

	}

	@Override
	public void rowChanged(final EntityItem<School> item)
	{
		if (item != null)
		{
			final School entity = item.getEntity();
			if (entity != null)
			{
				this.changeListener.reset(entity);
				super.rowChanged(item);
				this.principleEmailImage
						.setVisible(entity.getPrincipleEmail() != null && entity.getPrincipleEmail().length() > 0);
				this.advertisingContactEmailImage
						.setVisible(entity.getAdvertisingEmail() != null && entity.getAdvertisingEmail().length() > 0);

			}
			else
			{
				this.changeListener.reset(null);
				super.rowChanged(item);
			}
		}
		else
		{
			this.changeListener.reset(null);
			super.rowChanged(item);
		}

	}

	@Override
	public HelpPageIdentifier getHelpId()
	{
		return HelpPageIdentifier.SchoolView;
	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new SimpleStringFilter(School_.name.getName(), filterString, true, false);
	}
}
