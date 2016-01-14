package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
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
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Document")
@Access(AccessType.FIELD)
@NamedQueries(
{})
public class Document extends BaseEntity implements CrudEntity
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = User.class)
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
		return this.addedBy;
	}

	public void setAddedBy(final User addedBy)
	{
		this.addedBy = addedBy;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	@Override
	public String getName()
	{
		return this.filename;
	}

	public String getFilename()
	{
		return this.filename;
	}

	public void setFilename(final String filename)
	{
		this.filename = filename;
	}

	public byte[] getContent()
	{
		return this.content;
	}

	public void setContent(final byte[] content)
	{
		this.content = content;
	}

	public void setMimeType(final String mimeType)
	{
		this.mimeType = mimeType;

	}

	public String getMimeType()
	{
		return this.mimeType;
	}

}
