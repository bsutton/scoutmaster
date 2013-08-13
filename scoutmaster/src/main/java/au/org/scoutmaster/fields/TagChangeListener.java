package au.org.scoutmaster.fields;

import java.util.ArrayList;

import au.org.scoutmaster.domain.Tag;

public interface TagChangeListener
{
	public void onTagListChanged(ArrayList<Tag> tags);
}
