package au.org.scoutmaster.views;

import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Relationship;
import au.org.scoutmaster.domain.RelationshipType;
import au.org.scoutmaster.domain.RelationshipType_;
import au.org.scoutmaster.domain.Relationship_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

public class ChildRelationshipView extends ChildCrudView<Contact, Relationship>
{
	private static final long serialVersionUID = 1L;
	private ComboBox relatedTo;

	public ChildRelationshipView(final BaseCrudView<Contact> parentCrud)
	{
		super(parentCrud, Contact.class, Relationship.class, BaseEntity_.id, Relationship_.lhs.getName());

		final JPAContainer<Relationship> container = new DaoFactory().getRelationshipDao().createVaadinContainer();

		// Control which fields are displayed in the list of relationships.
		final Builder<Relationship> builder = new HeadingPropertySet.Builder<Relationship>();
		builder.addColumn("Relationship", Relationship_.type.getName()).addColumn("Related To",
				Relationship_.rhs.getName());

		super.init(Relationship.class, container, builder.build());
	}

	@Override
	protected Component buildEditor(final ValidatingFieldGroup<Relationship> fieldGroup2)
	{
		final SMMultiColumnFormLayout<Relationship> relationshipForm = new SMMultiColumnFormLayout<Relationship>(1,
				this.fieldGroup);
		relationshipForm.setColumnFieldWidth(0, 180);

		final FormHelper<Relationship> formHelper = relationshipForm.getFormHelper();

		/**
		 * The type of relationship. e.g. Parent of
		 */
		final ComboBox type = formHelper.new EntityFieldBuilder<RelationshipType>().setLabel("Relationship")
				.setField(Relationship_.type).setListFieldName(RelationshipType_.lhs).build();
		type.setFilteringMode(FilteringMode.CONTAINS);
		type.setTextInputAllowed(true);

		/**
		 * The contact we are related to.
		 */
		this.relatedTo = formHelper.new EntityFieldBuilder<Contact>().setLabel("Related To").setField(Relationship_.rhs)
				.setListFieldName(Contact_.fullname).build();
		this.relatedTo.setFilteringMode(FilteringMode.CONTAINS);
		this.relatedTo.setTextInputAllowed(true);

		@SuppressWarnings("unchecked")
		final JPAContainer<Relationship> rhscontainer = (JPAContainer<Relationship>) this.relatedTo
				.getContainerDataSource();
		rhscontainer.sort(new String[]
		{ Contact_.lastname.getName(), Contact_.firstname.getName() }, new boolean[]
		{ true, true });

		return relationshipForm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void committed(final Contact newParentId) throws Exception
	{
		super.committed(newParentId);

		// Whenever the list of contacts change we need to refresh
		// our local list as jpacontainers don't seem to share.
		// TODO: is there some way of optimising this.
		((JPAContainer<Contact>) this.relatedTo.getContainerDataSource()).refresh();

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		SimpleStringFilter lhs = new SimpleStringFilter(new Path(Relationship_.lhs, Contact_.fullname).getName(),
				filterString, true, false);
		SimpleStringFilter rhs = new SimpleStringFilter(new Path(Relationship_.type, RelationshipType_.lhs).getName(),
				filterString, true, false);
		return new Or(lhs, rhs);
	}

	@Override
	public void associateChild(final Contact newParent, final Relationship child)
	{
		final ContactDao daoContact = new DaoFactory().getContactDao();
		daoContact.addRelationship(newParent, child);
	}

	@Override
	public String getNewButtonActionLabel()
	{
		return "New Relationship";
	}

	@Override
	public SingularAttribute<Relationship, String> getGuidAttribute()
	{
		return Relationship_.guid;
	}

}
