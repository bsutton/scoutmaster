package au.org.scoutmaster.views;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.EntityTable;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.RowChangeListener;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.fields.TagChangeListener;
import au.org.scoutmaster.fields.TagField;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SearchableContactTable extends VerticalLayout implements TagChangeListener
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SearchableContactTable.class);
	private static final long serialVersionUID = 1L;
	private EntityTable<Contact> contactTable;
	private TextField searchField = new TextField();
	private VerticalLayout searchLayout = new VerticalLayout();
	private JPAContainer<Contact> contactContainer;
	private HorizontalLayout stringSearchLayout;
	private Button clearButton = new Button("Clear");
	private TagField tagField;

	public SearchableContactTable(JPAContainer<Contact> contactContainer, HeadingPropertySet<Contact> headingPropertySet)
	{
		this.contactContainer = contactContainer;
		this.contactTable = new EntityTable<Contact>(contactContainer, headingPropertySet);
		init();
	}

	void setRowChangeListener(RowChangeListener<Contact> rowChangeListener)
	{
		contactTable.setRowChangeListener(rowChangeListener);
	}

	public void init()
	{
		contactTable.init();
		initSearch();
		this.addComponent(searchLayout);
		this.addComponent(contactTable);
		contactTable.setSizeFull();
		this.setExpandRatio(contactTable, 1);
		this.setSizeFull();
	}

	public Contact getCurrent()
	{
		return contactTable.getCurrent();
	}

	private void initSearch()
	{
		searchLayout = new VerticalLayout();
		HorizontalLayout tagSearchLayout = new HorizontalLayout();
		tagField = new TagField("Search Tags", true);
		tagSearchLayout.addComponent(tagField);

		tagField.addChangeListener(this);
		tagSearchLayout.setSizeFull();
		searchLayout.addComponent(tagSearchLayout);

		stringSearchLayout = new HorizontalLayout();
		stringSearchLayout.addComponent(searchField);
		stringSearchLayout.addComponent(clearButton);
		stringSearchLayout.setWidth("100%");
		searchField.setWidth("100%");
		stringSearchLayout.setExpandRatio(searchField, 1);

		searchLayout.addComponent(stringSearchLayout);
		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
		searchField.setInputPrompt("Search contacts");

		/*
		 * Granularity for sending events over the wire can be controlled. By
		 * default simple changes like writing a text in TextField are sent to
		 * server with the next Ajax call. You can set your component to be
		 * immediate to send the changes to server immediately after focus
		 * leaves the field. Here we choose to send the text over the wire as
		 * soon as user stops writing for a moment.
		 */
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
		searchField.addTextChangeListener(new TextChangeListener()
		{
			private static final long serialVersionUID = 1L;

			public void textChange(final TextChangeEvent event)
			{
				resetFilter(tagField.getTags(), event.getText());
			}
		});

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
		searchField.addTextChangeListener(new TextChangeListener()
		{
			private static final long serialVersionUID = 1L;

			public void textChange(final TextChangeEvent event)
			{
				resetFilter(SearchableContactTable.this.tagField.getTags(), event.getText());
			}

		});
		
		clearButton.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void clicked(ClickEvent event)
			{
				searchField.setValue("");
				tagField.setValue((Object)null);
			}
		});
		
		searchField.focus();

	}

	public void onTagListChanged(ArrayList<Tag> tags)
	{
		resetFilter(tags, this.searchField.getValue());

	}

	void resetFilter(ArrayList<Tag> tags, String fullTextSearch)
	{
		contactContainer.removeAllContainerFilters();
		contactContainer.getEntityProvider().setQueryModifierDelegate(
				new ContactDefaultQueryModifierDelegate(tags, fullTextSearch));
		contactTable.refreshRowCache();

	}

	public int size()
	{
		return contactTable.size();
	}

	public ArrayList<Contact> getFilteredContacts()
	{
		ArrayList<Contact> contacts = new ArrayList<>();

		for (Object itemId : contactContainer.getItemIds())
		{
			Contact contact = contactContainer.getItem(itemId).getEntity();
			contacts.add(contact);
		}
		return contacts;
	}
}
