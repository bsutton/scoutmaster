package au.org.scoutmaster.views.wizards.importer;

import java.io.File;
import java.io.FileNotFoundException;
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

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ImportShowSample implements WizardStep
{
	Logger logger = Logger.getLogger(ImportShowSample.class.getName());

	private ImportView importView;

	public ImportShowSample(ImportView importView)
	{
		this.importView = importView;
	}

	@Override
	public String getCaption()
	{
		return "View Sample";
	}

	@Override
	public Component getContent()
	{
		File tempFile = this.importView.getFile().getTempFile();
		Table table = new Table();
		table.setSizeFull();

		FileReader reader;
		try
		{
			if (tempFile.exists())
			{
				reader = new FileReader(tempFile);
				IndexedContainer indexedContainer = buildContainerFromCSV(reader);
				reader.close();

				/* Finally, let's update the table with the container */
				table.setCaption("Sample from: " + this.importView.getFile().getSelectedFilename());
				table.setContainerDataSource(indexedContainer);
				table.setVisible(true);
			}
			else
			{
				/* Finally, let's update the table with the container */
				table.setCaption("No file selected");
				table.setVisible(true);
	
			}

		}
		catch (FileNotFoundException e)
		{
			logger.error(e, e);
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			logger.error(e, e);
			Notification.show(e.getMessage());
		}

		/* Main layout */
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		layout.addComponent(table);
		layout.setExpandRatio(table, 1);
		layout.addComponent(new Label(
				"If you are happy with the mappings click Next to import the data or click 'Back' to adjust the mappings."));

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	protected IndexedContainer buildContainerFromCSV(Reader reader) throws IOException
	{
		IndexedContainer container = new IndexedContainer();
		CSVReader csvReader = null;

		try
		{
			csvReader = new CSVReader(reader);
			String[] columnHeaders = null;
			String[] record;

			Hashtable<String, FormFieldImpl> fieldMaps = new Hashtable<String, FormFieldImpl>();

			fieldMaps = this.importView.getMatch().getFieldMap();
			addItemProperties(container, fieldMaps);

			// Import no more than 100 records as this is only a sample
			int count = 0;
			while (((record = csvReader.readNext()) != null) && count < 100)
			{
				if (columnHeaders == null)
				{
					columnHeaders = record;
				}
				else
				{
					addRow(container, columnHeaders, record, fieldMaps);
					count++;
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

	private static void addItemProperties(IndexedContainer container, Hashtable<String, FormFieldImpl> fieldMaps)
	{
		for (String key : fieldMaps.keySet())
		{
			container.addContainerProperty(fieldMaps.get(key).getFieldName(), String.class, null);
		}
	}

	private static <R extends Importable> void addRow(IndexedContainer container, String[] csvHeaders, String[] fields,
			Hashtable<String, FormFieldImpl> fieldMaps)
	{
		Object itemId = container.addItem();
		Item item = container.getItem(itemId);
		for (int i = 0; i < fields.length; i++)
		{
			String csvHeaderName = csvHeaders[i];
			FormFieldImpl fieldMap = fieldMaps.get(csvHeaderName);
			if (fieldMap != null)
			{
				String field = fields[i];
				String fieldName = fieldMap.getFieldName();
				@SuppressWarnings("unchecked")
				Property<String> itemProperty = item.getItemProperty(fieldName);
				itemProperty.setValue(field);
			}
		}
	}

}
