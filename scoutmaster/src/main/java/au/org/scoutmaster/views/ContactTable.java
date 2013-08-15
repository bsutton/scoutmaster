package au.org.scoutmaster.views;

import java.util.Map;
import java.util.Set;

import au.org.scoutmaster.domain.Contact;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

public class ContactTable extends Table
{

	private static final long serialVersionUID = 1L;
	private JPAContainer<Contact> contactContainer;
	private RowChangeListener<Contact> rowChangeListener;
	private String[] visibleColumns;

	ContactTable(JPAContainer<Contact> contactContainer, String[] visibleColumns)
	{
		this.contactContainer = contactContainer;
		this.visibleColumns = visibleColumns;
	}
	
	public void setRowChangeListener(RowChangeListener<Contact> rowChangeListener)
	{
		this.rowChangeListener = rowChangeListener;
	}

	public void init()
	{

		this.setContainerDataSource(contactContainer);
		this.setVisibleColumns((Object[]) visibleColumns);

		this.setSelectable(true);
		this.setImmediate(true);

		this.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event)
			{
				Long contactId = (Long) ContactTable.this.getValue();

				if (contactId != null) // it can be null when a row is being
										// deleted.
				{
					EntityItem<Contact> contact = ContactTable.this.contactContainer.getItem(contactId); //.getEntity();
					ContactTable.this.rowChangeListener.rowChanged(contact);
				}
				else
					ContactTable.this.rowChangeListener.rowChanged(null);
			}
		});
	}

	/**
	 * Hooking this allows us to veto the user selecting a new row.
	 */
	@Override
	public void changeVariables(final Object source, final Map<String, Object> variables)
	{
		if (variables.containsKey("selected"))
		{
			
			if (ContactTable.this.rowChangeListener != null
					&& ContactTable.this.rowChangeListener.allowRowChange())
				ContactTable.super.changeVariables(source, variables);
			else
				markAsDirty();
		}
		else
			super.changeVariables(source, variables);
	}

	public Contact getCurrent()
	{
		Long contactId = (Long) this.getValue();
		Contact contact = this.contactContainer.getItem(contactId).getEntity();

		return contact;

	}

	/**
	 * This nasty piece of work exists to stop the following exception being
	 * 
	 * thrown. java.lang.IllegalArgumentException: wrong number of arguments
	 * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	 * sun.reflect.NativeMethodAccessorImpl
	 * .invoke(NativeMethodAccessorImpl.java:57)
	 * sun.reflect.DelegatingMethodAccessorImpl
	 * .invoke(DelegatingMethodAccessorImpl.java:43)
	 * java.lang.reflect.Method.invoke(Method.java:606)
	 * com.vaadin.addon.jpacontainer
	 * .metadata.ClassMetadata.getPropertyValue(ClassMetadata.java:168)
	 * com.vaadin.addon.jpacontainer.metadata.ClassMetadata.getPropertyValue(
	 * ClassMetadata.java:343)
	 * com.vaadin.addon.jpacontainer.PropertyList.getPropertyValue
	 * (PropertyList.java:677)
	 * com.vaadin.addon.jpacontainer.JPAContainerItem$ItemProperty
	 * .getRealValue(JPAContainerItem.java:176)
	 * com.vaadin.addon.jpacontainer.JPAContainerItem$ItemProperty
	 * .getValue(JPAContainerItem.java:163)
	 * com.vaadin.ui.Table.formatPropertyValue(Table.java:4012)
	 * com.vaadin.ui.Table.getPropertyValue(Table.java:3956)
	 * com.vaadin.ui.Table.parseItemIdToCells(Table.java:2308)
	 * com.vaadin.ui.Table.getVisibleCellsNoCache(Table.java:2147)
	 * com.vaadin.ui.Table.refreshRenderedCells(Table.java:1668)
	 * com.vaadin.ui.Table.enableContentRefreshing(Table.java:3143)
	 * com.vaadin.ui.Table.setContainerDataSource(Table.java:2712)
	 * com.vaadin.ui.Table.setContainerDataSource(Table.java:2653)
	 * au.org.scoutmaster.views.ContactTable.init(ContactTable.java:46)
	 * 
	 */
	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property<?> property)
	{
		if (property.getType() == Set.class)
		{
			return null;
		}
		try
		{
			property.getValue();
		}
		catch (Exception e)
		{
			return null;
		}
		return super.formatPropertyValue(rowId, colId, property);
	}

	

}
