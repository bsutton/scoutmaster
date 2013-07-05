package au.com.noojee.scouts.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gwt.thirdparty.guava.common.base.Objects;

/**
 * Used to track the database version.
 * 
 * @author bsutton
 * 
 */
@Entity
public class Version
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int majorVersion;

	private int minorVersion;

	private int microVersion;

	public int getMajorVersion()
	{
		return majorVersion;
	}

	public int getMinorVersion()
	{
		return minorVersion;
	}

	public int getMicroVersion()
	{
		return microVersion;
	}

	public String toString()
	{
		return Objects.toStringHelper(this).add("majorVersion", majorVersion).add("minorVersion", minorVersion)
				.add("microVersion", microVersion).toString();
	}

}
