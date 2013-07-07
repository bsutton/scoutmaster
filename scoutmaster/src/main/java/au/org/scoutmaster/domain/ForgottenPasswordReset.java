package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import org.joda.time.DateTime;

import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.filter.EntityManagerProvider;

/**
 * When a user needs to reset their password we generate a random string which
 * is used to do the reset.
 * 
 * The random string is valid for 24 hours and we store it here.
 * 
 * @author bsutton
 * 
 */

@Entity
@NamedQueries(
// We order the set just incase we get two identical random strings (but almost
// impossible)
{ @NamedQuery(name = "ForgottenPasswordReset.getByResetid", query = "SELECT forgotten FROM ForgottenPasswordReset forgotten order by created desc"), })
public class ForgottenPasswordReset extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The date the reset record expires and the user can no longer reset their
	 * password.
	 */
	Date expires;

	@ManyToOne
	User userWithBadMemory;

	/**
	 * A 32 byte random string used in the reset url.
	 */
	String resetid;

	@SuppressWarnings("unchecked")
	boolean hasExpired(String resetid)
	{
		boolean hasExpired = true;

		List<ForgottenPasswordReset> resultReset = null;
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		Query query = em.createNamedQuery("ForgottenPasswordReset.getByResetid");
		query.setParameter("resetid", resetid);
		resultReset = query.getResultList();
		if (!resultReset.isEmpty())
		{
			ForgottenPasswordReset row = resultReset.get(0);
			DateTime expires = new DateTime(row.expires);
			if (expires.isAfterNow())
				hasExpired = false;
		}
		return hasExpired;

	}

}
