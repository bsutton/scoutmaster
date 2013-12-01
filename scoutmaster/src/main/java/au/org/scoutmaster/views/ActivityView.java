package au.org.scoutmaster.views;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.ActivityType_;
import au.org.scoutmaster.domain.Activity_;
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

@Menu(display = "Activities")
public class ActivityView extends BaseCrudView<Activity> implements View, Selected<Activity>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ActivityView.class);

	public static final String NAME = "Activity";

	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup<Activity> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		SMMultiColumnFormLayout<Activity> overviewForm = new SMMultiColumnFormLayout<Activity>(2, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setColumnExpandRatio(1, 1.0f);
		overviewForm.setSizeFull();
		overviewForm.getFieldGroup().setReadOnly(true);
		
		FormHelper<Activity> formHelper = overviewForm.getFormHelper();


		overviewForm.bindDateField("Activity Date", Activity_.activityDate, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();
		
		formHelper.new EntityFieldBuilder<ActivityType>()
				.setLabel("Type").setField(Activity_.type).setListFieldName(ActivityType_.name).build();

		overviewForm.newLine();
		
		formHelper.new EntityFieldBuilder<User>()
			.setLabel("Added By").setField(Activity_.addedBy).setListFieldName(User_.username).build();
		
		overviewForm.newLine();
		formHelper.new EntityFieldBuilder<Contact>()
			.setLabel("With Contact").setField(Activity_.withContact).setListFieldName("fullname").build();
	
		overviewForm.newLine();
		overviewForm.bindTextField("Subject", Activity_.subject);
		overviewForm.newLine();
		overviewForm.colspan(2);

		CKEditorEmailField detailsEditor = overviewForm.bindEditorField(Activity_.details, true);
		detailsEditor.setSizeFull();
		overviewForm.setExpandRatio(1.0f);
		
		layout.addComponent(overviewForm);

		super.enableActions(false);
		super.showNew(false);
		super.showSaveCancel(false);

		return layout;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<Activity> container = new DaoFactory().getActivityDao().createVaadinContainer();
		container.sort(new String[] {Activity_.activityDate.getName()},new boolean[] {false});

		Builder<Activity> builder = new HeadingPropertySet.Builder<Activity>();
		builder.addColumn("Contact", Activity_.withContact).addColumn("Subject", Activity_.subject)
				.addColumn("Type", Activity_.type).addColumn("Activity Date", Activity_.activityDate)
				.addColumn("Added By", Activity_.addedBy);

		super.init(Activity.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new Or(new Or(new Or(new Or(new SimpleStringFilter(Activity_.activityDate.getName(), filterString, true,
				false), new SimpleStringFilter(new Path(Activity_.type, ActivityType_.name).getName(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.withContact, Contact_.lastname).getName(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.withContact, Contact_.firstname).getName(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.addedBy, User_.username).getName(), filterString, true, false)),
				new SimpleStringFilter(Activity_.subject.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Activities";
	}

}
