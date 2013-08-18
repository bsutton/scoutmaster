package au.org.scoutmaster.util;

import com.vaadin.ui.UI;

public abstract class ProgressBarTask<T>
{
	private ProgressTaskListener<T> listener;

	public ProgressBarTask(ProgressTaskListener<T> listener)
	{
		this.listener = listener;
	}

	abstract public void run();

	protected void taskComplete()
	{
		synchronized (UI.getCurrent())
		{
			listener.taskComplete();
		}

	}

	public void taskProgress(int count, int max, T status)
	{
		synchronized (UI.getCurrent())
		{
			listener.taskProgress(count++, max, status);
		}
	}

	public void taskItemError(T status)
	{
		synchronized (UI.getCurrent())
		{
			listener.taskItemError(status);
		}
	
	}

	public void taskException(Exception e)
	{
		synchronized (UI.getCurrent())
		{
			listener.taskException(e);
		}
	
	}
}
