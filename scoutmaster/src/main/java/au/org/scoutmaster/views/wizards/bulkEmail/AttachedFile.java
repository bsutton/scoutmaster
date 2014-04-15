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
	private final File file;
	private final AbstractLayout line;

	public AttachedFile(final VerticalLayout attachedFilesLayout, final File file, final AbstractLayout line)
	{
		this.attachedFilesLayout = attachedFilesLayout;
		this.file = file;
		this.line = line;
	}

	public void remove()
	{
		this.attachedFilesLayout.removeComponent(this.line);
		this.file.delete();
	}

	public File getFile()
	{
		return this.file;
	}
}