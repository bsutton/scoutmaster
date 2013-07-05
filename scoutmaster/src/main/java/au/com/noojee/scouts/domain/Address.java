package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;


@Entity
@NamedQueries(
{
		@NamedQuery(name = "Address.findAll", query = "SELECT address FROM Address address"),
		@NamedQuery(name = "Address.findMatching", query = "SELECT address FROM Address address WHERE address.street = :street " +
				"and address.city = :city and address.postcode = :postcode and address.state = :state") 
})

public class Address
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date created = new Date();
	
	public Date getCreated()
	{
		return created;
	}

	String street;
	String city;
	String postcode;
	String state;
	
	@ManyToOne(optional=false)
	@JoinColumn
	private Contact occupant;
	
	
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

	public void setOccupant(Contact occupant)
	{
		this.occupant = occupant;
		
	}
	
	public String toString()
	{
		return street + ", " + city + ", " + state + ", " + postcode;
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
		JPAContainer<Address> addresss = JPAContainerFactory.make(Address.class, "scouts");
		EntityProvider<Address> ep = addresss.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			Query query = em.createNamedQuery("Address.findMatching");
			query.setParameter("street", street);
			query.setParameter("city", city);
			query.setParameter("state", state);
			query.setParameter("postcode", postcode);
			addressList = (List<Address>)query.getResultList();
		}
		finally
		{
			if (em != null)
				em.close();
		}

		return addressList;
	}

}
