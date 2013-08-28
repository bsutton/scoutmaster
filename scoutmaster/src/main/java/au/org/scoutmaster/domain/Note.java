package au.org.scoutmaster.domain;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.filter.EntityManagerProvider;

@Entity
@Table(name="Note")
@NamedQueries(
{ @NamedQuery(name = "Note.findAll", query = "SELECT note FROM Note note"),
		@NamedQuery(name = "Note.findMatching", query = "SELECT note FROM Note note WHERE note.subject = :subject") })
public class Note extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Transient
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@NotBlank
	private String subject;

	private String body;

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
		return sdf.format(this.getCreated()) + " " + subject;
	}

	public String getSubject()
	{
		return this.subject;
	}

	@SuppressWarnings("unchecked")
	static public List<Note> findNote(String subject)
	{
		List<Note> noteList = null;
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		Query query = em.createNamedQuery("Note.findMatching");
		query.setParameter("subject", subject);
		noteList = query.getResultList();

		return noteList;
	}

}
