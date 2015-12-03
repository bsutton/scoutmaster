package au.org.scoutmaster.views.wizards.bulkEmail;

import java.io.File;

import javax.activation.FileDataSource;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

import au.org.scoutmaster.dao.AttachedFile;

public class AttachedFileLayout implements AttachedFile
{
	/**
	 *
	 */
	private final VerticalLayout attachedFilesLayout;
	private final File file;
	private final AbstractLayout line;

	public AttachedFileLayout(final VerticalLayout attachedFilesLayout, final File file, final AbstractLayout line)
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

	@Override
	public File getFile()
	{
		return this.file;
	}

	public FileDataSource getDataSource()
	{
		return new FileDataSource(this.file);
	}
}