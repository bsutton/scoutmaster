package au.org.scoutmaster.views;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.CommunicationLog;
import au.org.scoutmaster.domain.CommunicationLog_;
import au.org.scoutmaster.domain.CommunicationType_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.access.User_;

import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ChildCommunicationView extends ChildCrudView<Contact, CommunicationLog>
{
	private static final long serialVersionUID = 1L;

	public ChildCommunicationView(final BaseCrudView<Contact> parentCrud)
	{
		super(parentCrud, Contact.class, CommunicationLog.class, BaseEntity_.id, CommunicationLog_.withContact
				.getName());

		final JPAContainer<CommunicationLog> container = new DaoFactory().getCommunicationLogDao()
				.createVaadinContainer();
		container.sort(new String[]
		{ CommunicationLog_.activityDate.getName() }, new boolean[]
		{ false });

		final Builder<CommunicationLog> builder = new HeadingPropertySet.Builder<CommunicationLog>();
		builder.addColumn("Subject", CommunicationLog_.subject).addColumn("Type", CommunicationLog_.type)
				.addColumn("Communication Date", CommunicationLog_.activityDate)
		.addColumn("Added By", CommunicationLog_.addedBy);

		super.init(CommunicationLog.class, container, builder.build());
		super.activateEditMode(false);
		showNew(false);

	}

	@Override
	protected Component buildEditor(final ValidatingFieldGroup<CommunicationLog> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final CKEditorEmailField detailsEditor = new CKEditorEmailField(true);
		super.fieldGroup.bind(detailsEditor, CommunicationLog_.details.getName());
		layout.addComponent(detailsEditor);


		return layout;
	}

	/**
	 * Internal method to show hide the new button when editing.
	 *
	 * If the user has called disallowNew then the new button will never be
	 * displayed.
	 */
	private void showNew(boolean show)
	{
		if (isDisallowNew())
		{
			show = false;
		}

		this.newButton.setVisible(show);
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
	public void associateChild(final Contact newParent, final CommunicationLog child)
	{
		final ContactDao daoContact = new DaoFactory().getContactDao();
		daoContact.addCommunication(newParent, child);

	}

	@Override
	public String getNewButtonActionLabel()
	{
		return "New Communication";
	}

	@Override
	public SingularAttribute<CommunicationLog, String> getGuidAttribute()
	{
		return CommunicationLog_.guid;
	}

}
