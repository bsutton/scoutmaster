package au.org.scoutmaster.views;

import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType_;
import au.org.scoutmaster.domain.Activity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.access.User_;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ChildActivityView extends ChildCrudView<Contact, Activity> 
{
	private static final long serialVersionUID = 1L;

	public ChildActivityView()
	{
		super(Contact.class, Activity.class, Contact_.id, new Path(Activity_.withContact, Contact_.id).getName());

		JPAContainer<Activity> container = new DaoFactory().getActivityDao().makeJPAContainer();
		container.sort(new String[] {Activity_.activityDate.getName()},new boolean[] {false});

		Builder<Activity> builder = new HeadingPropertySet.Builder<Activity>();
		builder.addColumn("Subject", Activity_.subject)
				.addColumn("Type", Activity_.type).addColumn("Activity Date", Activity_.activityDate)
				.addColumn("Added By", Activity_.addedBy);

		super.init(Activity.class, container, builder.build());

	}


	@Override
	protected Component buildEditor(ValidatingFieldGroup<Activity> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

//		SMMultiColumnFormLayout<Activity> overviewForm = new SMMultiColumnFormLayout<Activity>(1, this.fieldGroup);
//		overviewForm.setColumnFieldWidth(0, 180);
////		overviewForm.setColumnLabelWidth(0, 70);
////		overviewForm.setColumnExpandRatio(0, 1.0f);
//		overviewForm.setSizeFull();
//		overviewForm.getFieldGroup().setReadOnly(true);
//
//		//overviewForm.colspan(2);
//		overviewForm.bindDateField("Activity Date", Activity_.activityDate, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
//		overviewForm.newLine();
//		overviewForm.bindEntityField("Type", Activity_.type, ActivityType.class, ActivityType_.name);
//		overviewForm.bindEntityField("Added By", Activity_.addedBy, User.class, User_.username);
//		overviewForm.newLine();
//		overviewForm.bindTextField("Subject", Activity_.subject);
//		overviewForm.newLine();
//		//overviewForm.colspan(2);

//		CKEditorEmailField detailsEditor = overviewForm.bindEditorField(Activity_.details, true);
//		detailsEditor.setSizeFull();
//		overviewForm.setExpandRatio(1.0f);

		CKEditorEmailField detailsEditor = new CKEditorEmailField(true);
		super.fieldGroup.bind(detailsEditor, Activity_.details.getName());
		
//		layout.addComponent(overviewForm);
		layout.addComponent(detailsEditor);
		//layout.setExpandRatio(detailsEditor, 1.0f);

		super.showDelete(false);
		super.showNew(false);
		super.showSaveCancel(false);

		return layout;
	}

	@Override
	protected Filter getContainerFilter(String filterString)
	{
		return new Or(new Or(new Or(new Or(new Or(new SimpleStringFilter(Activity_.activityDate.getName(),
				filterString, true, false), new SimpleStringFilter(
				new Path(Activity_.type, ActivityType_.name).getName(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.withContact, Contact_.lastname).getName(), filterString,
						true, false)), new SimpleStringFilter(
				new Path(Activity_.withContact, Contact_.firstname).getName(), filterString, true, false)),
				new SimpleStringFilter(new Path(Activity_.addedBy, User_.username).getName(), filterString, true,
						false)), new SimpleStringFilter(Activity_.subject.getName(), filterString, true, false));
	}

	@Override
	protected void interceptSaveValues(Activity entity)
	{
		// TODO Auto-generated method stub

	}

}
