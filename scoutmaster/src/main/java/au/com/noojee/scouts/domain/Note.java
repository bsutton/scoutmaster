package au.com.noojee.scouts.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Transient;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

@Entity
@NamedQueries(
{ @NamedQuery(name = "Note.findAll", query = "SELECT note FROM Note note"),
		@NamedQuery(name = "Note.findMatching", query = "SELECT note FROM Note note WHERE note.subject = :subject") })

public class Note
{
	@Transient
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date created = new Date();

	private String subject;

	private String body;

	
	public Date getCreated()
	{
		return created;
	}

	public String getBody()
	{
		return body;
	}
	public Note()
	{
		
	}
	public Note(String subject, String body)
	{	
		this.subject = subject;
		this.body = body;
	}


	@Override
	public String toString()
	{
		return sdf.format(this.created) + " " + subject;
	}

	public String getSubject()
	{
		return this.subject;
	}
	
	@SuppressWarnings("unchecked")
	static public List<Note> findNote(String subject)
	{
		List<Note> noteList = null;
		JPAContainer<Note> notes = JPAContainerFactory.make(Note.class, "scouts");
		EntityProvider<Note> ep = notes.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			Query query = em.createNamedQuery("Note.findMatching");
			query.setParameter("subject", subject);
			noteList =  query.getResultList();
		}
		finally
		{
			if (em != null)
				em.close();
		}

		return noteList;
	}



}
