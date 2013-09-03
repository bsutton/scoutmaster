package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;

import au.com.vaadinutils.dao.EntityManagerProvider;

@Entity(name="Address")
@Table(name="Address")
@NamedQueries(
{
		@NamedQuery(name = Address.FIND_ALL, query = "SELECT address FROM Address address"),
		@NamedQuery(name = Address.FIND_MATCHING, query = "SELECT address FROM Address address WHERE address.street = :street "
				+ "and address.city = :city and address.postcode = :postcode and address.state = :state") })
public class Address extends BaseEntity
{
	public static final String FIND_ALL = "Address.findAll";
	public static final String FIND_MATCHING = "Address.findMatching";
	
	private static final long serialVersionUID = 1L;
	
	private String street= "";
	
	private String city = "";
	
	private String postcode= "";
	
	private String state = "";

	public void setStreet(String street)
	{
		this.street = street;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public Address()
	{

	}

	public Address(String street, String city, String state, String postcode)
	{
		this.street = street;
		this.city = city;
		this.state = state;
		this.postcode = postcode;
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

	public String getPostcode()
	{
		return this.postcode;
	}

	@SuppressWarnings("unchecked")
	static public List<Address> findAddress(String street, String city, String state, String postcode)
	{
		List<Address> addressList = new ArrayList<Address>();
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		Query query = em.createNamedQuery(Address.FIND_MATCHING);
		query.setParameter("street", street);
		query.setParameter("city", city);
		query.setParameter("state", state);
		query.setParameter("postcode", postcode);
		addressList = query.getResultList();

		return addressList;
	}
}
