package au.org.scoutmaster.views.wizards.bulkEmail;

import java.io.File;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

public class AttachedFile
{
	/**
	 * 
	 */
	private final VerticalLayout attachedFilesLayout;
	private File file;
	private AbstractLayout line;

	public AttachedFile(VerticalLayout attachedFilesLayout, File file, AbstractLayout line)
	{
		this.attachedFilesLayout = attachedFilesLayout;
		this.file = file;
		this.line = line;
	}

	public void remove()
	{
		this.attachedFilesLayout.removeComponent(line);
		file.delete();
	}

	public File getFile()
	{
		return file;
	}
}