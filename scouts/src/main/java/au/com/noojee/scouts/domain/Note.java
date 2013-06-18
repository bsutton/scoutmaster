package au.com.noojee.scouts.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Note
{
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date created;

	@ManyToOne 
		@JoinColumn(name="contact_id")
	private Contact contact;

	private String subject;

	private String content;

	
	@Override
	public String toString()
	{
		return sdf.format(this.created) + " " + subject;
	}
}
