package au.org.scoutmaster.views.importWizard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.bytecode.opencsv.CSVReader;
import au.org.scoutmaster.domain.FormFieldImpl;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.views.ImportView;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

public class ImportShowProgress implements WizardStep
{
	static private Logger logger = Logger.getLogger(ImportShowProgress.class);
	JPAContainer<? extends Importable> entities;
	private ImportView importView;
	private boolean importComplete = false;
	private ProgressBar indicator;
	private float count;
	private Label progressDescription;

	public ImportShowProgress(ImportView importView)
	{
		this.importView = importView;
	}

	@Override
	public String getCaption()
	{
		return "Importing data";
	}

	@Override
	public Component getContent()
	{
		this.count = 0.0f;

		VerticalLayout layout = new VerticalLayout();

		progressDescription = new Label("Imported: " + this.count + " records");
		layout.addComponent(progressDescription);
		indicator = new ProgressBar(new Float(0.0));
		indicator.setIndeterminate(true);
		layout.addComponent(indicator);

		// Set polling frequency to 0.5 seconds.

		File tempFile = this.importView.getFile().getTempFile();

		FileReader reader = null;
		try
		{
			reader = new FileReader(tempFile);
			Class<? extends Importable> clazz = importView.getType().getEntityClass();
			entities = buildContainerFromCSV(clazz, reader);

			reader.close();
			if (tempFile.exists())
				tempFile.delete();

		}
		catch (IOException | InstantiationException | IllegalAccessException e)
		{
			logger.error(e, e);
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}
		finally
		{
			if (reader != null)
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					logger.error(e, e);
					Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
				}
		}

		return layout;
	}

	private void updateProgress()
	{
		progressDescription.setValue("Imported: " + this.count + " records");
	}

	@Override
	public boolean onAdvance()
	{
		return importComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	/**
	 * Uses http://opencsv.sourceforge.net/ to read the entire contents of a CSV
	 * file, and creates an IndexedContainer from it
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	protected <T extends Importable> JPAContainer<T> buildContainerFromCSV(Class<T> entityClass,
			Reader reader) throws IOException, InstantiationException, IllegalAccessException
	{
		JPAContainer<T> container = JPAContainerFactory.make(entityClass, "scoutmaster");

		CSVReader csvReader = null;

		try
		{
			csvReader = new CSVReader(reader);

			Hashtable<String, FormFieldImpl> fieldMaps = new Hashtable<String, FormFieldImpl>();

			fieldMaps = this.importView.getMatch().getFieldMap();
			String[] columnHeaders = null;
			String[] record;
			this.count = 0;
			while ((record = csvReader.readNext()) != null)
			{
				if (columnHeaders == null)
				{
					columnHeaders = record;
				}
				else
				{
					addRow(container, entityClass, columnHeaders, record, fieldMaps);
					indicator.setValue(count);
					this.count++;
					this.updateProgress();
				}
			}
		}
		finally
		{
			if (csvReader != null)
				csvReader.close();
		}
		return container;
	}

	/**
	 * Adds an item to the given container, assuming each field maps to it's
	 * corresponding property id. Again, note that I am assuming that the field
	 * is a string.
	 * 
	 * @param container
	 * @param entityClass
	 * @param csvHeaders
	 * @param fields
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	private static <T> void addRow(JPAContainer<T> container,
			Class<T> entityClass, String[] csvHeaders, String[] fields,
			Hashtable<String, FormFieldImpl> fieldMaps) throws InstantiationException, IllegalAccessException
	{
		EntityItem<T> entityItem = container.createEntityItem(entityClass.newInstance());
		for (int i = 0; i < fields.length; i++)
		{
			String csvHeaderName = csvHeaders[i];
			FormFieldImpl fieldMap = fieldMaps.get(csvHeaderName);
			if (fieldMap != null)
			{
				String field = fields[i];
				entityItem.getItemProperty(fieldMap.getFieldName()).setValue(field);
			}
		}
	}

}
