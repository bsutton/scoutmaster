package au.org.scoutmaster.views.wizards.importer;

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
import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.bytecode.opencsv.CSVReader;
import au.org.scoutmaster.dao.ImportUserMappingDao;
import au.org.scoutmaster.domain.EntityAdaptor;
import au.org.scoutmaster.domain.FormFieldImpl;
import au.org.scoutmaster.domain.ImportColumnFieldMapping;
import au.org.scoutmaster.domain.ImportUserMapping;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.util.MultiColumnFormLayout;
import au.org.scoutmaster.views.ImportView;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

public class ImportMatchFields implements WizardStep
{

	private ImportView importView;
	private String selectedUserMapping = null;
	private ArrayList<ComboBox> mappings;
	private ArrayList<ImportUserMapping> userMapping;

	private String[] headers;
	private TextField fieldMapping;

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

		MultiColumnFormLayout<Object> layout = new MultiColumnFormLayout<>(3, new FieldGroup());
		this.selectedUserMapping = this.importView.getFile().getImportMapping();
		
		
//		fieldMapping = new TextField("Import Mapping", selectedUserMapping);
//		fl.addComponent(fieldMapping);
//		row.addComponent(fl);
//		row.addComponent(new Label(
//				"Enter a Import Mapping name to save the mappings if you plan on repeating this import"));

		layout.setMargin(true);
		layout.setSizeFull();
		try
		{
			String[] headers = getHeaders();
			

			EntityAdaptor<?> adaptor = EntityAdaptor.create(importable);
			
			ArrayList<FormFieldImpl> fields = adaptor.getFields();
			for (String header : headers)
			{
				layout.addComponent(new Label(header));
				layout.addComponent(new Label("--Maps to-->"));
				ComboBox box = new ComboBox(null, fields);
				box.setNullSelectionAllowed(true);
				box.setInputPrompt("--Please Select--");
				box.setNullSelectionItemId("--Please Select--");
				box.setTextInputAllowed(false);
				layout.addComponent(box);
				this.mappings.add(box);
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
//		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();
//		JPAContainer<ImportUserMapping> userMappings = JPAContainerFactory.make(ImportUserMapping.class, em);

		
		// Save the user selected mappings
		if (selectedUserMapping != null && !fieldMapping.getValue().equals(selectedUserMapping))
		{
			// the name has changed so we need to save a new one

			ImportUserMappingDao daoImportUserMapping = new ImportUserMappingDao();
			ImportUserMapping userMapping = new ImportUserMapping(fieldMapping.getValue());

			for (ComboBox mapping : mappings)
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping.getCaption(),
						(String) mapping.getValue());
				daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
			}
			daoImportUserMapping.persist(userMapping);
		}
		else
		{
			// The name hasn't changed so save over the existing one.
			// userMappings.addContainerFilter("mappingName",
			// fieldMapping.getValue(), true, false);
			// userMappings.applyFilters();
			//
			// ImportUserMapping userMapping = new
			// ImportUserMapping(fieldMapping.getValue());

			ImportUserMappingDao daoImportUserMapping = new ImportUserMappingDao();

			List<ImportUserMapping> results = daoImportUserMapping.findAll();
			
			if (results.size() == 1)
			{
				ImportUserMapping userMapping = results.get(0);

				for (ComboBox mapping : mappings)
				{
					ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping.getCaption(),
							(String) mapping.getValue());
					daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
				}
			}
		}

		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public Hashtable<String, FormFieldImpl> getFieldMap()
	{
		Hashtable<String, FormFieldImpl> fieldMaps = new Hashtable<String, FormFieldImpl>();

		for (int index = 0; index < headers.length; index++)
		{
			ComboBox box = this.mappings.get(index);
			if (box.getValue() != null)
			{
				FormFieldImpl field = (FormFieldImpl) box.getValue();
				fieldMaps.put(headers[index], field);
			}
		}

		return fieldMaps;
	}

	public ArrayList<ImportUserMapping> getUserMapping()
	{
		return userMapping;
	}

	public void setUserMapping(ArrayList<ImportUserMapping> userMapping)
	{
		this.userMapping = userMapping;
	}

	class FieldMap
	{
		String csvHeader;
		FormFieldImpl formField;

		public String displayName()
		{
			return formField.displayName();
		}

		public boolean visible()
		{
			return formField.visible();
		}
	}

}
