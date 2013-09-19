package au.org.scoutmaster.views;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
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

@Menu(display = "Activity")
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

		SMMultiColumnFormLayout<Activity> overviewForm = new SMMultiColumnFormLayout<Activity>(2, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setSizeFull();
		overviewForm.getFieldGroup().setReadOnly(true);

		overviewForm.bindDateField("Activity Date", Activity_.activityDate, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();
		overviewForm.bindEntityField("Type", Activity_.type, ActivityType.class, ActivityType_.name);
		overviewForm.newLine();
		overviewForm.bindEntityField("Added By", Activity_.addedBy, User.class, User_.username);
		overviewForm.newLine();
		overviewForm.bindEntityField("With Contact", Activity_.withContact.getName(), Contact.class, "fullname");
		overviewForm.newLine();
		overviewForm.bindTextField("Subject", Activity_.subject);
		overviewForm.newLine();
		overviewForm.colspan(2);
		overviewForm.bindEditorField(Activity_.details, true);
		layout.addComponent(overviewForm);

		super.showDelete(false);
		super.showNew(false);
		super.showSaveCancel(false);

		return layout;
	}

	@Override
	protected void interceptSaveValues(Activity entity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<Activity> container = new DaoFactory().getActivityDao().makeJPAContainer();
		container.sort(new String[] {Activity_.activityDate.getName()},new boolean[] {false});

		Builder<Activity> builder = new HeadingPropertySet.Builder<Activity>();
		builder.addColumn("Contact", Activity_.withContact).addColumn("Subject", Activity_.subject)
				.addColumn("Type", Activity_.type).addColumn("Activity Date", Activity_.activityDate)
				.addColumn("Added By", Activity_.addedBy);

		super.init(Activity.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(String filterString)
	{
//		return new Or(new Or(new Or(new Or(new SimpleStringFilter(Activity_.activityDate.getName(), filterString, true,
//				false), new SimpleStringFilter(Activity_.type.getName(), filterString, true, false)),
//				new SimpleStringFilter(Activity_.withContact.getName(), filterString, true, false)),
//				new SimpleStringFilter(Activity_.addedBy.getName(), filterString, true, false)),
//				new SimpleStringFilter(Activity_.subject.getName(), filterString, true, false));

		return new Or(new Or(new Or(new Or(new Or(new SimpleStringFilter(Activity_.activityDate.getName(), filterString, true,
				false), new SimpleStringFilter(new Path(Activity_.type, ActivityType_.name).toString(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.withContact, Contact_.lastname).toString(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.withContact, Contact_.firstname).toString(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.addedBy, User_.username).toString(), filterString, true, false)),
				new SimpleStringFilter(Activity_.subject.getName(), filterString, true, false));
	}

}
