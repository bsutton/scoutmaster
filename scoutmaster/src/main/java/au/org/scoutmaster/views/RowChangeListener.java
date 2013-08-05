package au.org.scoutmaster.views;

import com.vaadin.data.Item;


public interface RowChangeListener<ENTITY>
{
	/**
	 * Called when a user attempts to change the current row.
	 * Return false to stop the user selecting a new row.
	 * @return
	 */
	boolean allowRowChange();

	/**
	 * Called to inform the listener that a new row has been selected and it the
	 * new row contains the given item.
	 * @param item
	 * @return
	 */
	//void rowChanged(ENTITY entity);
	void rowChanged(Item item);


}
