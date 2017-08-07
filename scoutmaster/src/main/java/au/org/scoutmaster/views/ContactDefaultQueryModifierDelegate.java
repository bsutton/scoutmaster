package au.org.scoutmaster.views;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.QueryModifierAdaptor;
import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Phone_;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.SectionType_;
import au.org.scoutmaster.domain.Tag;

public class ContactDefaultQueryModifierDelegate extends QueryModifierAdaptor
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(ContactDefaultQueryModifierDelegate.class);

	private ArrayList<Tag> includedTags;
	private ArrayList<Tag> excludedTags;
	private final String fullTextSearch;
	private final boolean excludeDoNotSendBulkCommunications;

	public ContactDefaultQueryModifierDelegate(final ArrayList<Tag> includledTags, final ArrayList<Tag> excludedTags,
			final String fullTextSearch, final boolean excludeDoNotSendBulkCommunications)
	{
		if (includledTags == null)
		{
			this.includedTags = new ArrayList<>();
		}
		if (excludedTags == null)
		{
			this.excludedTags = new ArrayList<>();
		}
		this.includedTags = includledTags;
		this.excludedTags = excludedTags;
		this.fullTextSearch = fullTextSearch;
		this.excludeDoNotSendBulkCommunications = excludeDoNotSendBulkCommunications;
	}

	@Override
	public void filtersWillBeAdded(final CriteriaBuilder builder, final CriteriaQuery<?> query,
			final List<Predicate> predicates)
	{
		@SuppressWarnings("unchecked")
		final Root<Contact> fromContact = (Root<Contact>) query.getRoots().iterator().next();

		try
		{
			Predicate fullTextSearchPredicate = null;

			// Build main queries full text search
			if (this.fullTextSearch != null && !this.fullTextSearch.isEmpty())
			{
				final Join<Contact, SectionType> sectionJoin = fromContact.join(Contact_.section, JoinType.LEFT);
				// final Join<Contact, GroupRole> groupRoleJoin =
				// fromContact.join(Contact_.groupRole, JoinType.LEFT);

				fullTextSearchPredicate = builder.like(builder.upper(fromContact.get(Contact_.firstname)),
						"%" + this.fullTextSearch.toUpperCase() + "%");
				fullTextSearchPredicate = builder.or(builder.like(builder.upper(fromContact.get(Contact_.lastname)),
						"%" + this.fullTextSearch.toUpperCase() + "%"), fullTextSearchPredicate);

				fullTextSearchPredicate = builder.or(
						builder.like(builder.function("date_format", String.class, fromContact.get(Contact_.birthDate),
								builder.literal("%Y-%m-%d")), "%" + this.fullTextSearch.toUpperCase() + "%"),
						fullTextSearchPredicate);

				// Section
				fullTextSearchPredicate = builder.or(builder.like(builder.upper(sectionJoin.get(SectionType_.name)),
						"%" + this.fullTextSearch.toUpperCase() + "%"), fullTextSearchPredicate);

				// // Group Role
				// fullTextSearchPredicate = builder.or(
				// builder.like(builder.upper(groupRoleJoin.get(GroupRole_.name)),
				// "%" + this.fullTextSearch.toUpperCase() + "%"),
				// fullTextSearchPredicate);

				fullTextSearchPredicate = builder
						.or(builder.like(builder.upper(fromContact.get(Contact_.phone1).get(Phone_.phoneNo)),
								"%" + this.fullTextSearch.toUpperCase() + "%"), fullTextSearchPredicate);

				fullTextSearchPredicate = builder
						.or(builder.like(builder.upper(fromContact.get(Contact_.phone2).get(Phone_.phoneNo)),
								"%" + this.fullTextSearch.toUpperCase() + "%"), fullTextSearchPredicate);

				fullTextSearchPredicate = builder
						.or(builder.like(builder.upper(fromContact.get(Contact_.phone3).get(Phone_.phoneNo)),
								"%" + this.fullTextSearch.toUpperCase() + "%"), fullTextSearchPredicate);

			}

			// builder.not(root.get({field_name}).in(seqs))

			final ArrayList<Predicate> finalAnds = new ArrayList<>();

			if (this.includedTags != null && this.includedTags.size() != 0)
			{
				finalAnds.add(fromContact.get(BaseEntity_.id).in(buildWhereIn(query, builder, this.includedTags)));

			}
			if (this.excludedTags != null && this.excludedTags.size() != 0)
			{
				finalAnds.add(builder
						.not(fromContact.get(BaseEntity_.id).in(buildWhereIn(query, builder, this.excludedTags))));
			}
			if (fullTextSearchPredicate != null)
			{
				finalAnds.add(fullTextSearchPredicate);
			}

			if (this.excludeDoNotSendBulkCommunications)
			{
				finalAnds.add(builder.equal(fromContact.get(Contact_.doNotSendBulkCommunications), false));
			}

			if (finalAnds.size() == 1)
			{
				query.where(finalAnds.get(0));
			}
			else
			{
				query.where(builder.and(finalAnds.toArray(new Predicate[0])));
			}
		}
		catch (final Throwable e)
		{
			ContactDefaultQueryModifierDelegate.logger.error(e, e);
		}
	}

	private Subquery<Long> buildWhereIn(final CriteriaQuery<?> query, final CriteriaBuilder builder,
			final ArrayList<Tag> tags)
	{
		Subquery<Long> subquery = null;

		if (tags.size() > 0)
		{
			@SuppressWarnings("unchecked")
			final Root<Contact> fromContact = (Root<Contact>) query.getRoots().iterator().next();

			subquery = query.subquery(Long.class);
			final Root<Contact> subqueryContact = subquery.from(Contact.class);
			subquery.select(subqueryContact.get(BaseEntity_.id));

			subqueryContact.alias("C");
			final Join<Contact, Tag> tagJoin = subqueryContact.join(Contact_.tags);

			final List<Predicate> tagPredicates = new ArrayList<>();

			Predicate oredTags = null;
			for (final Tag tag : tags)
			{

				final Predicate tagPredicate = builder.equal(tagJoin.get("id"), tag.getId());
				tagPredicates.add(tagPredicate);
			}
			// Or all of the tag predicates together.
			final Predicate[] orPredicates = new Predicate[tagPredicates.size()];
			oredTags = builder.or(tagPredicates.toArray(orPredicates));

			// link the ored tags to the outer contact
			final Predicate outerLinkingPredicate = builder.equal(subqueryContact.get("id"), fromContact.get("id"));
			subquery.where(builder.and(outerLinkingPredicate, oredTags));

		}
		return subquery;
	}

}
