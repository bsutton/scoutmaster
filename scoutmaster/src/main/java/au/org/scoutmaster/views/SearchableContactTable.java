package au.org.scoutmaster.views;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.fields.TagChangeListener;
import au.org.scoutmaster.fields.TagField;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SearchableContactTable extends VerticalLayout implements TagChangeListener
{
	private static Logger logger = Logger.getLogger(SearchableContactTable.class);
	private static final long serialVersionUID = 1L;
	private ContactTable contactTable;
	private TextField searchField = new TextField();
	private VerticalLayout searchLayout = new VerticalLayout();
	private JPAContainer<Contact> contactContainer;
	private HorizontalLayout stringSearchLayout;
	private Button clearButton = new Button("Clear");

	public SearchableContactTable(JPAContainer<Contact> contactContainer, String[] visibleColumns)
	{
		this.contactContainer = contactContainer;
		this.contactTable = new ContactTable(contactContainer, visibleColumns);
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
		TagField tagField = new TagField("Search Tags", true);
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

				/* Reset the filter for the contactContainer. */
				contactContainer.removeAllContainerFilters();
				contactContainer.addContainerFilter(new Or(new SimpleStringFilter(Contact.FIRSTNAME, event.getText(),
						true, false), new SimpleStringFilter(Contact.LASTNAME, event.getText(), true, false)));

			}
		});
	}

	public void onTagListChanged(ArrayList<Tag> tags)
	{
		contactContainer.removeAllContainerFilters();
		buildQuery(tags);
		contactTable.refreshRowCache();

		// if (tags.size() > 0)
		// {
		// Filter[] filters = new Filter[tags.size()];
		//
		// int i = 0;
		// for (Tag tag : tags)
		// {
		// filters[i++] = new Equal("tags.id", tag.getId());
		// }
		//
		// contactContainer.addContainerFilter(new Or(filters));
		// }

	}

	void buildQuery(final ArrayList<Tag> tags)
	{
		contactContainer.getEntityProvider().setQueryModifierDelegate(new DefaultQueryModifierDelegate()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void filtersWillBeAdded(CriteriaBuilder cb, CriteriaQuery<?> query, List<Predicate> predicates)
			{
				@SuppressWarnings("unchecked")
				Root<Contact> fromContact = (Root<Contact>) query.getRoots().iterator().next();

				if (tags.size() > 0)
				{
					try
					{
						Tag firstTag = tags.get(0);
						Join<Contact, Tag> tagJoin = fromContact.join("tags");
						predicates.add(cb.equal(tagJoin.get("id"), firstTag.getId()));
					}
					catch (Throwable e)
					{
						logger.error(e, e);
					}
				}

			}
		});

	}

	public int size()
	{
		return contactTable.size();
	}
	
	public ArrayList<Contact> getFilteredContacts()
	{
		ArrayList<Contact> contacts = new ArrayList<>();
		
		for (Object itemId :contactContainer.getItemIds())
		{
			Contact contact = contactContainer.getItem(itemId).getEntity();
			contacts.add(contact);
		}
		return contacts;
	}
}
