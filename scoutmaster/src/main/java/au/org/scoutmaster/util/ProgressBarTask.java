package au.org.scoutmaster.util;

import com.vaadin.ui.UI;

public abstract class ProgressBarTask
{
	private ProgressTaskListener listener;

	public ProgressBarTask(ProgressTaskListener listener)
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

	public void taskProgress(int count, int max)
	{
		synchronized (UI.getCurrent())
		{
			listener.taskProgress(count++, max);
		}
	}
}
