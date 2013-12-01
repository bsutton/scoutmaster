package au.org.scoutmaster.views;

import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ChildRelationshipView extends ChildCrudView<Contact, Relationship>
{
	private static final long serialVersionUID = 1L;

	public ChildRelationshipView()
	{
		super(Contact.class, Relationship.class, Contact_.id, Relationship_.lhs.getName());

		JPAContainer<Relationship> container = new DaoFactory().getRelationshipDao().createVaadinContainer();
		// container.sort(new String[] {new Path(Relationship_.lhs,
		// Contact_.lastname).getName()},new boolean[] {true});

		Builder<Relationship> builder = new HeadingPropertySet.Builder<Relationship>();
		builder.addColumn("LHS", Relationship_.lhs.getName()).addColumn("Type", Relationship_.type.getName())
				.addColumn("RHS", Relationship_.rhs.getName());

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
		ComboBox lhs = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("LHS").setField(Relationship_.lhs).setListFieldName("fullname").build();

		// relationshipForm.bindEntityField("LHS", Relationship_.lhs.getName(),
		// Contact.class, "fullname");
		// ComboBox lhs = relationshipForm.bindEntityField("LHS",
		// Relationship_.lhs, Contact.class, Contact_.firstname);

		@SuppressWarnings("unchecked")
		JPAContainer<Relationship> lhscontainer = (JPAContainer<Relationship>) lhs.getContainerDataSource();
		lhscontainer.sort(new String[]
		{ Contact_.lastname.getName(), Contact_.firstname.getName() }, new boolean[]
		{ true, true });

		relationshipForm.newLine();

		
		formHelper.new EntityFieldBuilder<RelationshipType>()
				.setLabel("Type").setField(Relationship_.type).setListFieldName(RelationshipType_.lhs).build();

		ComboBox rhs = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Related To").setField(Relationship_.rhs).setListFieldName("fullname").build();


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
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
