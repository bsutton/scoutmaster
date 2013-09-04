package au.org.scoutmaster.views;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.ColumnList;
import au.com.vaadinutils.crud.EntityTable;
import au.com.vaadinutils.crud.HeadingToPropertyId;
import au.com.vaadinutils.crud.RowChangeListener;
import au.org.scoutmaster.dao.filter.TagFilter;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.fields.TagChangeListener;
import au.org.scoutmaster.fields.TagField;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
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
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SearchableContactTable.class);
	private static final long serialVersionUID = 1L;
	private EntityTable<Contact> contactTable;
	private TextField searchField = new TextField();
	private VerticalLayout searchLayout = new VerticalLayout();
	private JPAContainer<Contact> contactContainer;
	private HorizontalLayout stringSearchLayout;
	private Button clearButton = new Button("Clear");

	public SearchableContactTable(JPAContainer<Contact> contactContainer, List<HeadingToPropertyId> visibleColumns)
	{
		this.contactContainer = contactContainer;
		this.contactTable = new EntityTable<Contact>(contactContainer, visibleColumns);
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
		final TagField tagField = new TagField("Search Tags", true);
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
				resetQueryBuilder(tagField.getTags(), event.getText());
//				contactContainer.removeAllContainerFilters();
//				contactContainer.addContainerFilter(new Or(new SimpleStringFilter(Contact.FIRSTNAME, event.getText(),
//						true, false), new SimpleStringFilter(Contact.LASTNAME, event.getText(), true, false)));

			}
		});
	}

	
	public void onTagListChanged(ArrayList<Tag> tags)
	{
		resetQueryBuilder(tags, this.searchField.getValue());

	}

	void resetQueryBuilder(ArrayList<Tag> tags, String fullTextSearch)
	{
		contactContainer.removeAllContainerFilters();
		contactContainer.getEntityProvider().setQueryModifierDelegate(new ContactDefaultQueryModifierDelegate(tags, fullTextSearch));
		contactTable.refreshRowCache();

	}
//
//	public void onTagListChanged(ArrayList<Tag> tags)
//	{
//		contactContainer.removeAllContainerFilters();
//		buildQuery(tags);
//		contactTable.refreshRowCache();
//
//		// if (tags.size() > 0)
//		// {
//		// Filter[] filters = new Filter[tags.size()];
//		//
//		// int i = 0;
//		// for (Tag tag : tags)
//		// {
//		// filters[i++] = new Equal("tags.id", tag.getId());
//		// }
//		//
//		// contactContainer.addContainerFilter(new Or(filters));
//		// }
//
//	}
//	public void onTagListChanged(ArrayList<Tag> tags)
//	{
//		contactContainer.removeAllContainerFilters();
//		contactContainer.addContainerFilter(getContainerFilter(tags));
//		contactTable.refreshRowCache();
//	}

	protected Filter getContainerFilter(ArrayList<Tag> tags)
	{
		Filter filter = null;

		if (tags.size() > 0)
		{
			for (Tag tag : tags)
			{
				if (filter == null)
				{
					filter = new TagFilter(tag);
				}
				else
					filter = new Or(new TagFilter(tag), filter);
			}

			ColumnList list = new ColumnList(Contact_.firstname, Contact_.lastname, Contact_.birthDate);

			String searchString = this.searchField.getValue();
			for (SingularAttribute<? extends Object, ? extends Object> column : list.getList())
			{
				if (filter == null)
				{
					filter = new SimpleStringFilter(column.getName(), searchString, true, false);
				}
				filter = new Or(new SimpleStringFilter(column.getName(), searchString, true, false), filter);

			}
		}

		return filter;
	}

	// try
	// {
	// if (tags.size() > 0)
	// {
	// Subquery<Contact> subquery = query.subquery(Contact.class);
	// Root<Contact> sqRoot = subquery.from(Contact.class);
	// sqRoot.alias("C");
	// Join<Contact, Tag> tagJoin = sqRoot.join(Contact_.tags);
	//
	// List<Predicate> tagPredicates = new ArrayList<>();
	// for (Tag tag : tags)
	// {
	//
	// Predicate tagPredicate = builder.equal(tagJoin.get(Tag_.id),
	// tag.getId());
	// tagPredicates.add(tagPredicate);
	// }
	// Predicate[] orPredicates = new Predicate[tagPredicates.size()];
	// Predicate or = builder.or((Predicate[])
	// tagPredicates.toArray(orPredicates));
	//
	// ParameterExpression<Long> longParameter = builder.parameter(Long.class);
	// Predicate typePredicate = builder.equal(sqRoot.get(Contact_.id),
	// longParameter);
	//
	// // i have not tried this before but I assume this will correlate the
	// subquery with the parent root entity
	// Predicate correlatePredicate = builder.equal(sqRoot.get(Contact_.id),
	// fromContact);
	// Predicate parentCorrelation = builder.and(typePredicate,
	// correlatePredicate);
	// subquery.where(builder.and(parentCorrelation, or));
	// query.where(builder.exists(subquery));
	// query.distinct(true);
	// }
	// }
	// catch (Throwable e)
	// {
	// logger.error(e, e);
	// }

	//
	// void resetQueryBuilder(ArrayList<Tag> tags)
	// {
	// contactContainer.getEntityProvider().setQueryModifierDelegate(new
	// ContactDefaultQueryModifierDelegate(tags));
	// contactContainer.removeAllContainerFilters();
	//
	// }
	//
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
