package au.org.scoutmaster.views;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.EntityTable;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.RowChangeListener;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.fields.TagField;

public class SearchableContactTable extends VerticalLayout
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SearchableContactTable.class);
	private static final long serialVersionUID = 1L;
	private final EntityTable<Contact> contactTable;
	private final TextField searchField = new TextField();
	private VerticalLayout searchLayout = new VerticalLayout();
	private final JPAContainer<Contact> contactContainer;
	private HorizontalLayout stringSearchLayout;
	private final Button clearButton = new Button("Clear");
	private TagField includeTagField;
	private TagField excludeTagField;
	private boolean excludeDoNotSendBulkCommunications;

	public SearchableContactTable(final JPAContainer<Contact> contactContainer,
			final HeadingPropertySet<Contact> headingPropertySet)
	{
		this.contactContainer = contactContainer;
		this.contactTable = new EntityTable<Contact>(contactContainer, headingPropertySet);
		init();
	}

	void setRowChangeListener(final RowChangeListener<Contact> rowChangeListener)
	{
		this.contactTable.setRowChangeListener(rowChangeListener);
	}

	public void init()
	{
		this.contactTable.init("SearchableContentTable");
		initSearch();
		this.addComponent(this.searchLayout);
		this.addComponent(this.contactTable);
		this.contactTable.setSizeFull();
		setExpandRatio(this.contactTable, 1);
		setSizeFull();
	}

	public EntityItem<Contact> getCurrent()
	{
		return this.contactTable.getCurrent();
	}

	private void initSearch()
	{
		this.searchLayout = new VerticalLayout();
		final HorizontalLayout tagSearchLayout = new HorizontalLayout();
		this.includeTagField = new TagField("Include Tags", true);
		this.excludeTagField = new TagField("Exclude Tags", true);
		tagSearchLayout.addComponent(this.includeTagField);
		tagSearchLayout.addComponent(this.excludeTagField);

		this.includeTagField
				.addChangeListener(tags -> resetFilter(tags, SearchableContactTable.this.excludeTagField.getTags(),
						SearchableContactTable.this.searchField.getValue()));
		this.excludeTagField
				.addChangeListener(tags -> resetFilter(SearchableContactTable.this.includeTagField.getTags(), tags,
						SearchableContactTable.this.searchField.getValue()));

		tagSearchLayout.setSizeFull();
		this.searchLayout.addComponent(tagSearchLayout);

		this.stringSearchLayout = new HorizontalLayout();
		this.stringSearchLayout.addComponent(this.searchField);
		this.stringSearchLayout.addComponent(this.clearButton);
		this.stringSearchLayout.setWidth("100%");
		this.searchField.setWidth("100%");
		this.stringSearchLayout.setExpandRatio(this.searchField, 1);

		this.searchLayout.addComponent(this.stringSearchLayout);
		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
		this.searchField.setInputPrompt("Search contacts");

		/*
		 * Granularity for sending events over the wire can be controlled. By
		 * default simple changes like writing a text in TextField are sent to
		 * server with the next Ajax call. You can set your component to be
		 * immediate to send the changes to server immediately after focus
		 * leaves the field. Here we choose to send the text over the wire as
		 * soon as user stops writing for a moment.
		 */
		this.searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
		this.searchField.addTextChangeListener(new TextChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(final TextChangeEvent event)
			{
				resetFilter(SearchableContactTable.this.includeTagField.getTags(),
						SearchableContactTable.this.excludeTagField.getTags(), event.getText());
			}
		});

		this.clearButton.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void clicked(final ClickEvent event)
			{
				SearchableContactTable.this.searchField.setValue("");
				SearchableContactTable.this.includeTagField.setValue((Object) null);
				SearchableContactTable.this.excludeTagField.setValue((Object) null);
			}
		});

		this.searchField.focus();

	}

	void resetFilter(final ArrayList<Tag> includeTags, final ArrayList<Tag> excludeTags, final String fullTextSearch)
	{
		this.contactContainer.removeAllContainerFilters();
		this.contactContainer.getEntityProvider().setQueryModifierDelegate(new ContactDefaultQueryModifierDelegate(
				includeTags, excludeTags, fullTextSearch, this.excludeDoNotSendBulkCommunications));
		this.contactTable.refreshRowCache();

	}

	public int size()
	{
		return this.contactTable.size();
	}

	public ArrayList<Contact> getFilteredContacts()
	{
		final ArrayList<Contact> contacts = new ArrayList<>();

		for (final Object itemId : this.contactContainer.getItemIds())
		{
			final Contact contact = this.contactContainer.getItem(itemId).getEntity();
			contacts.add(contact);
		}
		return contacts;
	}

	/**
	 * if set to true then any contact that does not want bulk communications
	 * will be excluded from the list.
	 *
	 * @param b
	 */
	public void excludeDoNotSendBulkCommunications(final boolean exclude)
	{
		this.excludeDoNotSendBulkCommunications = exclude;

	}
}
