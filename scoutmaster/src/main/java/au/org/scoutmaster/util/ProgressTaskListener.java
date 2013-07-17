package au.org.scoutmaster.util;

public interface ProgressTaskListener
{

	void taskProgress(final int count, final int max);

	void taskComplete();

}
