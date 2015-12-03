package au.org.scoutmaster.help;

class HelpStringId
{
	String identifier;

	HelpStringId(String id)
	{
		identifier = id.toUpperCase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof HelpStringId))
		{
			return false;
		}
		HelpStringId other = (HelpStringId) obj;
		if (identifier == null)
		{
			if (other.identifier != null)
			{
				return false;
			}
		}
		else if (!identifier.equals(other.identifier))
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "HelpStringId [identifier=" + identifier + "]";
	}

}
