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

	public ContactDefaultQueryModifierDelegate(ArrayList<Tag> tags)
	{
		this.tags = tags;

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
			    Root<Contact> sqRoot = subquery.from(Contact.class);
			    sqRoot.alias("C");
			    Join<Contact, Tag> tagJoin = sqRoot.join(Contact_.tags);

				List<Predicate> tagPredicates = new ArrayList<>();
				for (Tag tag : tags)
				{

					Predicate tagPredicate = builder.equal(tagJoin.get(Tag_.id), tag.getId());
					tagPredicates.add(tagPredicate);
				}
				Predicate[] orPredicates = new Predicate[tagPredicates.size()];
				Predicate or = builder.or((Predicate[]) tagPredicates.toArray(orPredicates));
				
				ParameterExpression<Long> longParameter = builder.parameter(Long.class);
				Predicate typePredicate = builder.equal(sqRoot.get(Contact_.id), longParameter);

				// i have not tried this before but I assume this will correlate the subquery with the parent root entity
				Predicate correlatePredicate = builder.equal(sqRoot.get(Contact_.id), fromContact);
				Predicate parentCorrelation = builder.and(typePredicate, correlatePredicate);
				subquery.where(builder.and(parentCorrelation, or));
				query.where(builder.exists(subquery));
				query.distinct(true);
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
