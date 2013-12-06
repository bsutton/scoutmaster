package au.org.scoutmaster.views;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.domain.ActivityType_;
import au.org.scoutmaster.domain.Activity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Activity;
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

	public ChildActivityView(BaseCrudView<Contact> parentCrud)
	{
		super(parentCrud, Contact.class, Activity.class, Contact_.id, Activity_.withContact.getName());

		JPAContainer<Activity> container = new DaoFactory().getActivityDao().createVaadinContainer();
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

		CKEditorEmailField detailsEditor = new CKEditorEmailField(true);
		super.fieldGroup.bind(detailsEditor, Activity_.details.getName());
		layout.addComponent(detailsEditor);

		super.enableActions(false);
		super.showNew(false);

		return layout;
	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
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
	public void associateChild(Contact newParent, Activity child)
	{
		newParent.addActivity(child);
		
	}

}
