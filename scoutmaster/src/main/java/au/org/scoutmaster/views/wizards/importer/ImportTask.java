package au.org.scoutmaster.views.wizards.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Hashtable;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.icu.text.SimpleDateFormat;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import au.com.bytecode.opencsv.CSVReader;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.util.ProgressBarTask;
import au.com.vaadinutils.util.ProgressTaskListener;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Transaction;
import au.org.scoutmaster.domain.EntityAdaptor;
import au.org.scoutmaster.domain.FormFieldImpl;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.util.SMNotification;

public class ImportTask extends ProgressBarTask<ImportItemStatus>
{
	private final Logger logger = LogManager.getLogger(ImportTask.class);

	private final File tempFile;
	private final Class<? extends Importable> clazz;
	private final Hashtable<String, FormFieldImpl> fieldMap;

	private Tag tag;

	// private EntityManager em;

	ImportTask(final ProgressTaskListener<ImportItemStatus> listener, final File tempFile, final File originalFile,
			final Class<? extends Importable> clazz, final Hashtable<String, FormFieldImpl> fieldMap)
	{
		super(listener);
		this.tempFile = tempFile;
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		this.tag = new Tag("Contact Import: " + sdf.format(new Date()), "Imported: " + originalFile.getName());
		this.clazz = clazz;
		this.fieldMap = fieldMap;
	}

	@Override
	public void runUI(UI ui)
	{
		FileReader reader = null;
		try
		{
			reader = new FileReader(this.tempFile);

			final int rows = buildContainerFromCSV(this.clazz, reader);

			reader.close();
			if (this.tempFile.exists())
			{
				this.tempFile.delete();
			}

			super.taskComplete(rows);

		}
		catch (IOException | InstantiationException | IllegalAccessException e)
		{
			this.logger.error(e, e);
			SMNotification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (final IOException e)
				{
					this.logger.error(e, e);
					SMNotification.show(e.getMessage(), Type.ERROR_MESSAGE);
				}
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
	protected <T extends Importable> int buildContainerFromCSV(final Class<T> entityClass, final Reader reader)
			throws IOException, InstantiationException, IllegalAccessException
	{
		EntityManager em = EntityManagerProvider.createEntityManager();
		// final JPAContainer<T> container =
		// JPAContainerFactory.makeBatchable(entityClass, em);

		final JPAContainer<T> container = DaoFactory.getGenericDao(entityClass).createVaadinContainer();

		CSVReader csvReader = null;
		int count = 0;

		try (Transaction t = new Transaction(em))
		{
			csvReader = new CSVReader(reader);

			String[] columnHeaders = null;
			String[] record;
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
						addRow(em, container, entityClass, columnHeaders, record, this.fieldMap);
						++count;
						super.taskProgress(count, -1, null);
					}
					catch (final ConstraintViolationException e)
					{
						this.logger.warn(e, e);
						final ImportItemStatus status = new ImportItemStatus(count, e);
						super.taskItemError(status);
					}
					catch (final Exception e)
					{
						this.logger.warn(e, e);
						final ImportItemStatus status = new ImportItemStatus(count, e);
						super.taskItemError(status);
					}
				}
			}
			t.commit();
		}
		catch (final Throwable e)
		{
			this.logger.error(e, e);
			SMNotification.show("Import failed:", e.getMessage(), Type.ERROR_MESSAGE);
		}
		finally
		{
			if (csvReader != null)
			{
				csvReader.close();
			}
		}
		return count;
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

	private <T extends Importable> void addRow(EntityManager em, final JPAContainer<T> container,
			final Class<T> entityClass, final String[] csvHeaders, final String[] fieldValues,
			final Hashtable<String, FormFieldImpl> fieldMaps) throws InstantiationException, IllegalAccessException
	{
		final EntityItem<T> entityItem = container.createEntityItem(entityClass.newInstance());

		final EntityAdaptor<T> adaptor = EntityAdaptor.create(entityClass);

		final T entity = entityItem.getEntity();
		entity.addTag(this.tag);

		adaptor.save(em, entity, csvHeaders, fieldValues, fieldMaps);

		em.merge(entity);
	}
}
