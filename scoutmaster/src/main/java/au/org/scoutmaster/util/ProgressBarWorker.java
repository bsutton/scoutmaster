package au.org.scoutmaster.util;


public class ProgressBarWorker extends Thread
{
	private ProgressBarTask task;

	public ProgressBarWorker(ProgressBarTask task)
	{
		super(ProgressBarTask.class.getName());
		this.task = task;
	}

	@Override
	public void run()
	{
		task.run();
	}
}