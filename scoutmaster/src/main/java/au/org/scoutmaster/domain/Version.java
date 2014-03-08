package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.gwt.thirdparty.guava.common.base.Objects;

/**
 * Used to track the database version.
 * 
 * @author bsutton
 * 
 */
@Entity(name = "Version")
@Table(name = "Version")
@Access(AccessType.FIELD)
public class Version extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	private Integer majorVersion;

	private Integer minorVersion;

	private Integer microVersion;

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "JPA injection")
	public Integer getMajorVersion()
	{
		return this.majorVersion;
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "JPA injection")
	public Integer getMinorVersion()
	{
		return this.minorVersion;
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "JPA injection")
	public Integer getMicroVersion()
	{
		return this.microVersion;
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("majorVersion", this.majorVersion)
				.add("minorVersion", this.minorVersion).add("microVersion", this.microVersion).toString();
	}

	@Override
	public String getName()
	{
		return toString();
	}

}
