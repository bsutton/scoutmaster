package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.com.vaadinutils.dao.EntityManagerProvider;

@Entity(name = "Address")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Address")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = Address.FIND_MATCHING, query = "SELECT address FROM Address address WHERE address.street = :street "
		+ "and address.city = :city and address.postcode = :postcode and address.state = :state") })
public class Address extends BaseEntity
{
	public static final String FIND_MATCHING = "Address.findMatching";

	private static final long serialVersionUID = 1L;

	@Size(max = 255)
	private String street = "";

	@Size(max = 255)
	private String city = "";

	@Size(max = 255)
	private String postcode = "";

	@Size(max = 255)
	private String state = "";

	@Size(max = 255)
	private String country = "";

	// Required by jpa.
	public Address()
	{
	}

	public Address(final String street, final String city, final String state, final String postcode, String country)
	{
		this.street = street;
		this.city = city;
		this.state = state;
		this.postcode = postcode;
		this.country = country;
	}

	public void setStreet(final String street)
	{
		this.street = street;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	public void setPostcode(final String postcode)
	{
		this.postcode = postcode;
	}

	public void setState(final String state)
	{
		this.state = state;
	}

	@Override
	public String toString()
	{
		return this.street + ", " + this.city + ", " + this.state + ", " + this.postcode;
	}

	public String getStreet()
	{
		return this.street;
	}

	public String getState()
	{
		return this.state;
	}

	public String getCity()
	{
		return this.city;
	}

	public String getCountry()
	{
		return this.country;
	}

	public String getPostcode()
	{
		return this.postcode;
	}

	@SuppressWarnings("unchecked")
	static public List<Address> findAddress(final String street, final String city, final String state,
			final String postcode, String country)
	{
		List<Address> addressList = new ArrayList<Address>();
		final EntityManager em = EntityManagerProvider.getEntityManager();

		final Query query = em.createNamedQuery(Address.FIND_MATCHING);
		query.setParameter("street", street);
		query.setParameter("city", city);
		query.setParameter("state", state);
		query.setParameter("postcode", postcode);
		query.setParameter("country", country);
		addressList = query.getResultList();

		return addressList;
	}

	@Override
	public String getName()
	{
		return this.street + ", " + this.city + ", " + this.state + "," + this.country;
	}
}
