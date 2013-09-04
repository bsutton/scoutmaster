package au.org.scoutmaster.views;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.Tag_;

import com.vaadin.addon.jpacontainer.QueryModifierDelegate;

public class ContactDefaultQueryModifierDelegate implements QueryModifierDelegate
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ContactDefaultQueryModifierDelegate.class);

	private ArrayList<Tag> tags;
	private String fullTextSearch;

	public ContactDefaultQueryModifierDelegate(ArrayList<Tag> tags, String fullTextSearch)
	{
		this.tags = tags;
		this.fullTextSearch = fullTextSearch;

	}

	@Override
	public void queryWillBeBuilt(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void queryHasBeenBuilt(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query)
	{

	}

	@Override
	public void filtersWillBeAdded(CriteriaBuilder builder, CriteriaQuery<?> query, List<Predicate> predicates)
	{
		@SuppressWarnings("unchecked")
		Root<Contact> fromContact = (Root<Contact>) query.getRoots().iterator().next();

		try
		{
			if (tags.size() > 0)
			{
				Subquery<Contact> subquery = query.subquery(Contact.class);
				Root<Contact> subqueryContact = subquery.from(Contact.class);
				subqueryContact.alias("C");
				Join<Contact, Tag> tagJoin = subqueryContact.join(Contact_.tags);

				List<Predicate> tagPredicates = new ArrayList<>();
				for (Tag tag : tags)
				{

					Predicate tagPredicate = builder.equal(tagJoin.get("id"), tag.getId());
					tagPredicates.add(tagPredicate);
				}
				// Or all of the tag predicates together.
				Predicate[] orPredicates = new Predicate[tagPredicates.size()];
				Predicate or = builder.or((Predicate[]) tagPredicates.toArray(orPredicates));

				// link the subquery to the outer contact
				Predicate subqueryPredicate = builder.equal(subqueryContact.get("id"), fromContact.get("id"));
				subquery.where(builder.and(subqueryPredicate, or));

				// Build main queries full text search
				if (fullTextSearch != null && !fullTextSearch.isEmpty())
				{
					Predicate fullTextSearchPredicate = builder.like(
							builder.upper(fromContact.get(Contact_.firstname)), "%" + this.fullTextSearch.toUpperCase()
									+ "%");
					fullTextSearchPredicate = builder.or(builder.like(
							builder.upper(fromContact.get(Contact_.lastname)), "%" + this.fullTextSearch.toUpperCase()
									+ "%"), fullTextSearchPredicate);

					query.where(builder.and(fullTextSearchPredicate, builder.exists(subquery)));
				}
				else
					query.where(builder.exists(subquery));
				// query.distinct(true);
			}

		}
		catch (Throwable e)
		{
			logger.error(e, e);
		}

	}

	public void originalfiltersWillBeAdded(CriteriaBuilder builder, CriteriaQuery<?> query, List<Predicate> predicates)
	{
		@SuppressWarnings("unchecked")
		Root<Contact> fromContact = (Root<Contact>) query.getRoots().iterator().next();

		try
		{
			if (tags.size() > 0)
			{
				Join<Contact, Tag> tagJoin = fromContact.join("tags");

				List<Predicate> tagPredicates = new ArrayList<>();
				for (Tag tag : tags)
				{

					Predicate tagPredicate = builder.equal(tagJoin.get("id"), tag.getId());
					tagPredicates.add(tagPredicate);
				}
				Predicate[] orPredicates = new Predicate[tagPredicates.size()];
				Predicate or = builder.or((Predicate[]) tagPredicates.toArray(orPredicates));
				predicates.add(or);
				query.distinct(true);

			}
		}
		catch (Throwable e)
		{
			logger.error(e, e);
		}

	}

	@Override
	public void filtersWereAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void orderByWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Order> orderBy)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void orderByWasAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query)
	{
		// TODO Auto-generated method stub

	}

}
