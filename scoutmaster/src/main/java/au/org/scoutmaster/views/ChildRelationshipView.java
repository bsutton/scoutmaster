package au.org.scoutmaster.views;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Relationship;
import au.org.scoutmaster.domain.RelationshipType;
import au.org.scoutmaster.domain.RelationshipType_;
import au.org.scoutmaster.domain.Relationship_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

public class ChildRelationshipView extends ChildCrudView<Contact, Relationship>
{
	private static final long serialVersionUID = 1L;
	private ComboBox relatedTo;

	public ChildRelationshipView(BaseCrudView<Contact> parentCrud)
	{
		super(parentCrud, Contact.class, Relationship.class, Contact_.id, Relationship_.lhs.getName());

		JPAContainer<Relationship> container = new DaoFactory().getRelationshipDao().createVaadinContainer();

		// Control which fields are displayed in the list of relationships.
		Builder<Relationship> builder = new HeadingPropertySet.Builder<Relationship>();
		builder.addColumn("Relationship", Relationship_.type.getName())
				.addColumn("Related To", Relationship_.rhs.getName());

		super.init(Relationship.class, container, builder.build());
	}

	@Override
	protected Component buildEditor(ValidatingFieldGroup<Relationship> fieldGroup2)
	{
		SMMultiColumnFormLayout<Relationship> relationshipForm = new SMMultiColumnFormLayout<Relationship>(1,
				this.fieldGroup);
		relationshipForm.setColumnFieldWidth(0, 180);

		FormHelper<Relationship> formHelper = relationshipForm.getFormHelper();

		/**
		 * The type of relationship. e.g. Parent of
		 */
		ComboBox type = formHelper.new EntityFieldBuilder<RelationshipType>()
				.setLabel("Relationship").setField(Relationship_.type).setListFieldName(RelationshipType_.lhs).build();
		type.setFilteringMode(FilteringMode.CONTAINS);
		type.setTextInputAllowed(true);

		/**
		 * The contact we are related to.
		 */
		relatedTo = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Related To").setField(Relationship_.rhs).setListFieldName(Contact_.fullname).build();
		relatedTo.setFilteringMode(FilteringMode.CONTAINS);
		relatedTo.setTextInputAllowed(true);


		@SuppressWarnings("unchecked")
		JPAContainer<Relationship> rhscontainer = (JPAContainer<Relationship>) relatedTo.getContainerDataSource();
		rhscontainer.sort(new String[]
		{ Contact_.lastname.getName(), Contact_.firstname.getName() }, new boolean[]
		{ true, true });


		return relationshipForm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void committed(Contact newParentId) throws Exception
	{
		super.committed(newParentId);
		
		// Whenever the list of contacts change we need to refresh
		// our local list as jpacontainers don't seem to share.
		// TODO: is there some way of optimising this.
		((JPAContainer<Contact>)relatedTo.getContainerDataSource()).refresh();

	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new Or(new Or(new SimpleStringFilter(Relationship_.lhs.getName(), filterString, true, false),
				new SimpleStringFilter(new Path(Relationship_.type, RelationshipType_.lhs).getName(), filterString,
						true, false))));
	}


	@Override
	public void associateChild(Contact newParent, Relationship child)
	{
		ContactDao daoContact = new DaoFactory().getContactDao();
		daoContact.addRelationship(newParent, child);
	}

}
