package au.org.scoutmaster.views.wizards.mailing;

import java.io.File;

import com.vaadin.ui.AbstractLayout;

public class AttachedFile
{
	/**
	 * 
	 */
	private final MailingDetailsStep mailingDetailsStep;
	private File file;
	private AbstractLayout line;

	AttachedFile(MailingDetailsStep mailingDetailsStep, File file, AbstractLayout line)
	{
		this.mailingDetailsStep = mailingDetailsStep;
		this.file = file;
		this.line = line;
	}

	public void remove()
	{
		this.mailingDetailsStep.attachedFiles.removeComponent(line);
		file.delete();
	}

	public File getFile()
	{
		return file;
	}
}