package au.org.scoutmaster.util;


public interface ProgressTaskListener<T>
{

	void taskProgress(final int count, final int max, T status);

	void taskComplete();

	/**
	 * Used to indicate an error on a single item being processed in the task.
	 * @param status
	 */
	void taskItemError(T status);
	
	void taskException(Exception e);

}
