package au.org.scoutmaster.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

import au.com.vaadinutils.crud.ChildCrudEntity;
import au.com.vaadinutils.dao.JpaEntityHelper;

@Entity
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Note")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = "Note.findAll", query = "SELECT note FROM Note note"),
		@NamedQuery(name = "Note.findMatching", query = "SELECT note FROM Note note WHERE note.subject = :subject") })
public class Note extends BaseEntity implements ChildCrudEntity
{
	private static final long serialVersionUID = 1L;

	@Transient
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * Entities used in child cruds must have a guid to help uniquely identify
	 * each child.
	 */
	@NotNull
	@Column(updatable = false)
	String guid = JpaEntityHelper.getGuid(this);

	/**
	 * The contact that this note was made against.
	 */
	@NotNull
	@ManyToOne(targetEntity = Contact.class)
	private Contact attachedContact;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date noteDate = new Date();

	@NotBlank
	@Size(max = 255)
	private String subject;

	@Size(max = 4096)
	private String body;

	public String getBody()
	{
		return this.body;
	}

	public Note()
	{

	}

	public Note(final String subject, final String body)
	{
		this.subject = subject;
		this.body = body;
	}

	@Override
	public String toString()
	{
		return this.sdf.format(getCreated()) + " " + this.subject;
	}

	public String getSubject()
	{
		return this.subject;
	}

	@Override
	public String getName()
	{
		return "Note: " + toString();
	}

	@Override
	public String getGuid()
	{
		return guid;
	}

}
