package au.com.noojee.scouts.views.importWizard;

/**
 * Import code from:
 * https://gist.github.com/canthony/3655917
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Hashtable;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.bytecode.opencsv.CSVReader;
import au.com.noojee.scouts.domain.EntityAdaptor;
import au.com.noojee.scouts.domain.Importable;
import au.com.noojee.scouts.views.ImportView;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

public class ImportMatchFields implements WizardStep
{

	private ImportView importView;
	private ArrayList<ComboBox> mappings;
	private String[] headers;

	public ImportMatchFields(ImportView importView)
	{
		this.importView = importView;
	}

	private String[] getHeaders() throws IOException
	{
		if (this.headers == null)
		{
			File tempFile = importView.getFile().getTempFile();
			/* Let's build a container from the CSV File */
			FileReader reader = new FileReader(tempFile);

			this.headers = readHeaders(reader);
			reader.close();

		}

		return this.headers;
	}

	/**
	 * Uses http://opencsv.sourceforge.net/ to read the entire contents of a CSV
	 * file, and creates an IndexedContainer from it
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	protected String[] readHeaders(Reader reader) throws IOException
	{
		CSVReader csvReader = null;
		String[] columnHeaders = null;

		try
		{
			csvReader = new CSVReader(reader);
			String[] record;
			if ((record = csvReader.readNext()) != null)
				columnHeaders = record;
		}
		finally
		{
			if (csvReader != null)
				csvReader.close();
		}
		return columnHeaders;
	}

	@Override
	public String getCaption()
	{
		return "Match fields";
	}

	@Override
	public Component getContent()
	{
		Class<? extends Importable> importable = this.importView.getType().getEntityClass();

		this.headers = null;
		this.mappings = new ArrayList<ComboBox>();

		VerticalLayout layout = new VerticalLayout();
		try
		{
		String[] headers = getHeaders();

		EntityAdaptor<? extends Importable> adaptor = new EntityAdaptor(importable);

		ArrayList<String> fields = adaptor.getFieldNames();
		fields.add(0, "--Please Select--");
		for (String header : headers)
		{
			HorizontalLayout row = new HorizontalLayout();
//			row.setMargin(true);
//			row.setSpacing(true);
//			row.addComponent(new Label(header));
			FormLayout fl = new FormLayout();
			ComboBox box = new ComboBox(header + "  --Maps to-->  ", fields);
			box.setNullSelectionAllowed(true);
			box.setInputPrompt("--Please Select--");
			box.setNullSelectionItemId("--Please Select--");
			box.setTextInputAllowed(false);
			fl.addComponent(box);
			row.addComponent(fl);
			this.mappings.add(box);
			layout.addComponent(row);
		}
		}
		catch (IOException e)
		{
			Notification.show("An error occured trying to read the CSV file: " + e.getMessage(), Type.ERROR_MESSAGE);
		}
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

	public Hashtable<String, FieldMap> getFieldMap()
	{
		Hashtable<String, FieldMap> fieldMaps = new Hashtable<String, FieldMap>();

		for (int index = 0; index < headers.length; index++)
		{
			ComboBox box = this.mappings.get(index);
			if (box.getValue() != null)
			{
				fieldMaps.put(headers[index], new FieldMap(headers[index], (String) box.getValue()));
			}
		}

		return fieldMaps;
	}

}

class FieldMap
{
	String csvHeader;
	String entityField;

	public FieldMap(String csvHeader, String entityField)
	{
		this.csvHeader = csvHeader;
		this.entityField = entityField;
	}
}
