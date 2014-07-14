package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.CommunicationLog;
import au.org.scoutmaster.domain.CommunicationLog_;
import au.org.scoutmaster.domain.CommunicationType;
import au.org.scoutmaster.domain.CommunicationType_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.domain.access.User_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Communication Log", path = "Communication")
public class CommunicationLogView extends BaseCrudView<CommunicationLog> implements View, Selected<CommunicationLog>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();

	public static final String NAME = "Communication Log";

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<CommunicationLog> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final SMMultiColumnFormLayout<CommunicationLog> overviewForm = new SMMultiColumnFormLayout<CommunicationLog>(2,
				this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setColumnExpandRatio(1, 1.0f);
		overviewForm.setSizeFull();
		overviewForm.getFieldGroup().setReadOnly(true);

		final FormHelper<CommunicationLog> formHelper = overviewForm.getFormHelper();

		overviewForm.bindDateField("Activity Date", CommunicationLog_.activityDate, "yyyy-MM-dd hh:mm",
				Resolution.MINUTE);
		overviewForm.newLine();

		formHelper.new EntityFieldBuilder<CommunicationType>().setLabel("Type").setField(CommunicationLog_.type)
				.setListFieldName(CommunicationType_.name).build();

		overviewForm.newLine();

		formHelper.new EntityFieldBuilder<User>().setLabel("Added By").setField(CommunicationLog_.addedBy)
				.setListFieldName(User_.username).build();

		overviewForm.newLine();
		formHelper.new EntityFieldBuilder<Contact>().setLabel("With Contact").setField(CommunicationLog_.withContact)
				.setListFieldName("fullname").build();

		overviewForm.newLine();
		overviewForm.bindTextField("Subject", CommunicationLog_.subject);
		overviewForm.newLine();
		overviewForm.colspan(2);

		final CKEditorEmailField detailsEditor = overviewForm.bindEditorField(CommunicationLog_.details, true);
		detailsEditor.setSizeFull();
		overviewForm.setExpandRatio(1.0f);

		layout.addComponent(overviewForm);


		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<CommunicationLog> container = new DaoFactory().getCommunicationLogDao()
				.createVaadinContainer();
		container.sort(new String[]
		{ CommunicationLog_.activityDate.getName() }, new boolean[]
		{ false });

		final Builder<CommunicationLog> builder = new HeadingPropertySet.Builder<CommunicationLog>();
		builder.addColumn("Contact", CommunicationLog_.withContact).addColumn("Subject", CommunicationLog_.subject)
		.addColumn("Type", CommunicationLog_.type).addColumn("Activity Date", CommunicationLog_.activityDate)
		.addColumn("Added By", CommunicationLog_.addedBy);

		super.init(CommunicationLog.class, container, builder.build());
		super.disallowEdit(true, true);
		super.disallowNew(true);


	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(new Or(new Or(new Or(new Or(new SimpleStringFilter(CommunicationLog_.activityDate.getName(),
				filterString, true, false), new SimpleStringFilter(new Path(CommunicationLog_.type,
				CommunicationType_.name).getName(), filterString, true, false)), new SimpleStringFilter(new Path(
				CommunicationLog_.withContact, Contact_.lastname).getName(), filterString, true, false)),
				new SimpleStringFilter(new Path(CommunicationLog_.withContact, Contact_.firstname).getName(),
						filterString, true, false)), new SimpleStringFilter(new Path(CommunicationLog_.addedBy,
				User_.username).getName(), filterString, true, false)), new SimpleStringFilter(
				CommunicationLog_.subject.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Communication Log";
	}

}
