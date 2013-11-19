package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.gwt.thirdparty.guava.common.base.Objects;

/**
 * Used to track the database version.
 * 
 * @author bsutton
 * 
 */
@Entity(name="Version")
@Table(name="Version")
TransitionMember
public class Version extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	private Integer majorVersion;

	private Integer minorVersion;

	private Integer microVersion;

	public Integer getMajorVersion()
	{
		return this.majorVersion;
	}

	public Integer getMinorVersion()
	{
		return this.minorVersion;
	}

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
