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

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ChildRelationshipView extends ChildCrudView<Contact, Relationship>
{
	private static final long serialVersionUID = 1L;

	public ChildRelationshipView(BaseCrudView<Contact> parentCrud)
	{
		super(parentCrud, Contact.class, Relationship.class, Contact_.id, Relationship_.lhs.getName());

		JPAContainer<Relationship> container = new DaoFactory().getRelationshipDao().createVaadinContainer();
		// container.sort(new String[] {new Path(Relationship_.lhs,
		// Contact_.lastname).getName()},new boolean[] {true});

		Builder<Relationship> builder = new HeadingPropertySet.Builder<Relationship>();
		builder.addColumn("Relationship", Relationship_.type.getName())
				.addColumn("Related To", Relationship_.rhs.getName());

		super.init(Relationship.class, container, builder.build());
	}

	@Override
	protected Component buildEditor(ValidatingFieldGroup<Relationship> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		SMMultiColumnFormLayout<Relationship> relationshipForm = new SMMultiColumnFormLayout<Relationship>(1,
				this.fieldGroup);
		relationshipForm.setColumnFieldWidth(0, 180);
		relationshipForm.setSizeFull();

		FormHelper<Relationship> formHelper = relationshipForm.getFormHelper();
		// relationshipForm.bindEntityField("LHS", Relationship_.lhs.getName(),
		// Contact.class, "fullname");
		// ComboBox lhs = relationshipForm.bindEntityField("LHS",
		// Relationship_.lhs, Contact.class, Contact_.firstname);

		ComboBox type = formHelper.new EntityFieldBuilder<RelationshipType>()
				.setLabel("Relationship").setField(Relationship_.type).setListFieldName(RelationshipType_.lhs).build();
		type.setFilteringMode(FilteringMode.CONTAINS);
		type.setTextInputAllowed(true);

		ComboBox rhs = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Related To").setField(Relationship_.rhs).setListFieldName(Contact_.fullname).build();
		rhs.setFilteringMode(FilteringMode.CONTAINS);
		rhs.setTextInputAllowed(true);


		@SuppressWarnings("unchecked")
		JPAContainer<Relationship> rhscontainer = (JPAContainer<Relationship>) rhs.getContainerDataSource();
		rhscontainer.sort(new String[]
		{ Contact_.lastname.getName(), Contact_.firstname.getName() }, new boolean[]
		{ true, true });

		layout.addComponent(relationshipForm);

		return layout;
	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new Or(new Or(new SimpleStringFilter(Relationship_.lhs.getName(), filterString, true, false),
				new SimpleStringFilter(new Path(Relationship_.type, RelationshipType_.lhs).getName(), filterString,
						true, false))));
	}

	@Override
	protected void interceptSaveValues(EntityItem<Relationship> entityItem)
	{
		// try
		// {
		// //entityItem.getEntity().setLHS(translateParentId(super.getParentId()));
		// }
		// catch (InstantiationException | IllegalAccessException e)
		// {
		// e.printStackTrace();
		// }
	}

	@Override
	public void associateChild(Contact newParent, Relationship child)
	{
		ContactDao daoContact = new DaoFactory().getContactDao();
		daoContact.addRelationship(newParent, child);
	}

}
