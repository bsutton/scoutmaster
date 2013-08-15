package au.org.scoutmaster.views.wizards.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;
import au.org.scoutmaster.application.LocalEntityManagerFactory;
import au.org.scoutmaster.domain.EntityAdaptor;
import au.org.scoutmaster.domain.FormFieldImpl;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.filter.Transaction;
import au.org.scoutmaster.util.FormHelper;
import au.org.scoutmaster.util.ProgressBarTask;
import au.org.scoutmaster.util.ProgressTaskListener;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ImportTask extends ProgressBarTask
{
	private Logger logger = Logger.getLogger(ImportTask.class);

	private File tempFile;
	private Class<? extends Importable> clazz;
	private Hashtable<String, FormFieldImpl> fieldMap;

	private EntityManager em;

	ImportTask(ProgressTaskListener listener, File tempFile, Class<? extends Importable> clazz,
			Hashtable<String, FormFieldImpl> fieldMap)
	{
		super(listener);
		this.tempFile = tempFile;
		this.clazz = clazz;
		this.fieldMap = fieldMap;
		this.em = LocalEntityManagerFactory.createEntityManager();
	}

	@Override
	public void run()
	{
		FileReader reader = null;
		try
		{
			reader = new FileReader(tempFile);

			buildContainerFromCSV(clazz, reader);

			reader.close();
			if (tempFile.exists())
				tempFile.delete();

			super.taskComplete();

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
	protected <T extends Importable> void buildContainerFromCSV(Class<T> entityClass, Reader reader)
			throws IOException, InstantiationException, IllegalAccessException
	{
		JPAContainer<T> container = JPAContainerFactory.make(entityClass, em);

		CSVReader csvReader = null;

		try (Transaction t = new Transaction(em))
		{
			csvReader = new CSVReader(reader);

			String[] columnHeaders = null;
			String[] record;
			int count = 0;
			while ((record = csvReader.readNext()) != null)
			{
				if (columnHeaders == null)
				{
					columnHeaders = record;
				}
				else
				{
					try
					{
						++count;
						addRow(container, entityClass, columnHeaders, record, fieldMap);
						super.taskProgress(count, -1);
					}
					catch (Throwable e)
					{
						Notification.show("Import for row " + count + " failed: " + e.getMessage(),
								Type.TRAY_NOTIFICATION);
					}
				}
			}
			t.commit();
			// em.flush();
		}
		catch (ConstraintViolationException e)
		{
			FormHelper.showConstraintViolation(e);
		}
		catch (Throwable e)
		{
			logger.error(e, e);
			Notification.show("Error during import", e.getMessage(), Type.ERROR_MESSAGE);

		}
		finally
		{
			if (csvReader != null)
				csvReader.close();
		}
	}

	/**
	 * Adds an item to the given container, assuming each field maps to it's
	 * corresponding property id. Again, note that I am assuming that the field
	 * is a string.
	 * 
	 * @param container
	 * @param entityClass
	 * @param csvHeaders
	 * @param fieldValues
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private <T> void addRow(JPAContainer<T> container, Class<T> entityClass, String[] csvHeaders, String[] fieldValues,
			Hashtable<String, FormFieldImpl> fieldMaps) throws InstantiationException, IllegalAccessException
	{
		EntityItem<T> entityItem = container.createEntityItem(entityClass.newInstance());

		EntityAdaptor<T> adaptor = EntityAdaptor.create(entityClass);

		T entity = entityItem.getEntity();

		adaptor.save(em, entity, csvHeaders, fieldValues, fieldMaps);

		em.merge(entity);
	}
}
