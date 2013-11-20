package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import au.com.vaadinutils.crud.CrudEntity;
import au.org.scoutmaster.domain.access.User;

/**
 * Used to log a variety of activities
 * 
 * @author bsutton
 *
 */
@Entity
@Table(name="Document")
@Access(AccessType.FIELD)
@NamedQueries(
{
})

public class Document extends BaseEntity implements CrudEntity
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity=User.class)
	private User addedBy;
	
	/**
	 * A short description of the document
	 */
	@NotBlank
	private String description;

	/**
	 * The original filename (sans the path)
	 */
	@NotBlank
	private String filename;

	/**
	 * The actual contents of the document.
	 */
	@Lob
	private byte[] content;


	private String mimeType;


	public User getAddedBy()
	{
		return addedBy;
	}

	public void setAddedBy(User addedBy)
	{
		this.addedBy = addedBy;
	}


	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	public String getName()
	{
		return filename;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public byte[] getContent()
	{
		return content;
	}

	public void setContent(byte[] content)
	{
		this.content = content;
	}


	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
		
	}

	public String getMimeType()
	{
		return mimeType;
	}

	
	
}
