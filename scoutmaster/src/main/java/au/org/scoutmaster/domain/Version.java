package au.org.scoutmaster.domain;

import javax.persistence.Entity;

import com.google.gwt.thirdparty.guava.common.base.Objects;

/**
 * Used to track the database version.
 * 
 * @author bsutton
 * 
 */
@Entity
public class Version extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	private int majorVersion;

	private int minorVersion;

	private int microVersion;

	public int getMajorVersion()
	{
		return this.majorVersion;
	}

	public int getMinorVersion()
	{
		return this.minorVersion;
	}

	public int getMicroVersion()
	{
		return this.microVersion;
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("majorVersion", this.majorVersion)
				.add("minorVersion", this.minorVersion).add("microVersion", this.microVersion).toString();
	}

}
