package au.org.scoutmaster.util;

/**
 * Used as a generic method to track progress of a job/task/thread.
 * 
 * 
 * @author bsutton
 *
 */
public interface ProgressListener
{
	/**
	 * Count and max can be used to indicate the progress towards completion.
	 * If the max number of steps is unknown max should be set to -1.
	 * 
	 * @param count
	 * @param max
	 */
	void progress(int count, int max);
	
	/**
	 * Called when the job is complete.
	 */
	void complete();

}
