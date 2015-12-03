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

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import au.com.bytecode.opencsv.CSVReader;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.ImportUserMappingDao;
import au.org.scoutmaster.domain.EntityAdaptor;
import au.org.scoutmaster.domain.FormFieldImpl;
import au.org.scoutmaster.domain.ImportColumnFieldMapping;
import au.org.scoutmaster.domain.ImportUserMapping;
import au.org.scoutmaster.domain.Importable;

public class ImportMatchFields implements WizardStep
{

	private final ImportWizardView importView;
	// private String selectedUserMapping = null;
	private final ArrayList<ComboBox> mappings;
	private ArrayList<ImportUserMapping> userMapping;

	private String[] headers;
	// private TextField fieldMapping;
	private boolean reset = true;
	private GridLayout layout;

	public ImportMatchFields(final ImportWizardView importView)
	{
		this.importView = importView;

		this.headers = null;
		this.mappings = new ArrayList<ComboBox>();

		// MultiColumnFormLayout<Object> layout = new
		// MultiColumnFormLayout<Object>(3, new
		// ValidatingFieldGroup<Object>((EntityItem<?>)null, null));

		// this.selectedUserMapping =
		// this.importView.getFile().getImportMapping();

		// fieldMapping = new TextField("Import Mapping", selectedUserMapping);
		// fl.addComponent(fieldMapping);
		// row.addComponent(fl);
		// row.addComponent(new Label(
		// "Enter a Import Mapping name to save the mappings if you plan on
		// repeating this import"));

		this.layout = null;
		this.layout = new GridLayout(3, 1);
		this.layout.setMargin(true);
		this.layout.setSizeFull();

	}

	private String[] getHeaders() throws IOException
	{
		if (this.headers == null)
		{
			final File tempFile = this.importView.getFile().getTempFile();
			/* Let's build a container from the CSV File */
			final FileReader reader = new FileReader(tempFile);

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
	protected String[] readHeaders(final Reader reader) throws IOException
	{
		CSVReader csvReader = null;
		String[] columnHeaders = null;

		try
		{
			csvReader = new CSVReader(reader);
			String[] record;
			if ((record = csvReader.readNext()) != null)
			{
				columnHeaders = record;
			}
		}
		finally
		{
			if (csvReader != null)
			{
				csvReader.close();
			}
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
		try
		{
			if (this.reset)
			{
				this.reset = false;
				this.layout = new GridLayout(3, 1);
				this.layout.setMargin(true);
				this.layout.setSpacing(true);
				// layout.setSizeFull();

				final String[] headers = getHeaders();

				final Class<? extends Importable> importable = this.importView.getType().getEntityClass();
				final EntityAdaptor<?> adaptor = EntityAdaptor.create(importable);

				final ArrayList<FormFieldImpl> fields = adaptor.getFields();
				for (final String header : headers)
				{
					final Label headerLabel = new Label(header);
					this.layout.addComponent(headerLabel);
					headerLabel.setWidth("" + 160);

					final Label mapToLabel = new Label("--Maps to-->");
					this.layout.addComponent(mapToLabel);
					mapToLabel.setWidth("" + 100);

					final ComboBox box = new ComboBox(null, fields);
					box.setNullSelectionAllowed(true);
					box.setInputPrompt("--Please Select--");
					box.setNullSelectionItemId("--Please Select--");
					box.setTextInputAllowed(false);
					// ComboBox box = layout.addComponent("", fields);
					this.layout.addComponent(box);
					this.mappings.add(box);
					this.layout.newLine();
				}
			}
		}
		catch (final IOException e)
		{
			Notification.show("An error occured trying to read the CSV file: " + e.getMessage(), Type.ERROR_MESSAGE);
		}

		return this.layout;
	}

	@Override
	public boolean onAdvance()
	{
		// EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();
		// JPAContainer<ImportUserMapping> userMappings =
		// JPAContainerFactory.make(ImportUserMapping.class, em);

		// Save the user selected mappings
		// if (selectedUserMapping != null &&
		// !fieldMapping.getValue().equals(selectedUserMapping))
		// {
		// // the name has changed so we need to save a new one
		//
		// ImportUserMappingDao daoImportUserMapping = new
		// DaoFactory().getImportUserMappingDao();
		// ImportUserMapping userMapping = new
		// ImportUserMapping(fieldMapping.getValue());
		//
		// for (ComboBox mapping : mappings)
		// {
		// ImportColumnFieldMapping columnMapping = new
		// ImportColumnFieldMapping(mapping.getCaption(),
		// (String) mapping.getValue());
		// daoImportUserMapping.addColumnFieldMapping(userMapping,
		// columnMapping);
		// }
		// daoImportUserMapping.persist(userMapping);
		// }
		// else
		// {
		// The name hasn't changed so save over the existing one.
		// userMappings.addContainerFilter("mappingName",
		// fieldMapping.getValue(), true, false);
		// userMappings.applyFilters();
		//
		// ImportUserMapping userMapping = new
		// ImportUserMapping(fieldMapping.getValue());

		final ImportUserMappingDao daoImportUserMapping = new DaoFactory().getImportUserMappingDao();

		final List<ImportUserMapping> results = daoImportUserMapping.findAll();

		if (results.size() == 1)
		{
			final ImportUserMapping userMapping = results.get(0);

			for (final ComboBox mapping : this.mappings)
			{
				final ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping.getCaption(),
						(String) mapping.getValue());
				daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
			}
		}
		// }

		this.reset = false;
		return true;
	}

	@Override
	public boolean onBack()
	{
		// this.reset = true;
		return true;
	}

	public Hashtable<String, FormFieldImpl> getFieldMap()
	{
		final Hashtable<String, FormFieldImpl> fieldMaps = new Hashtable<String, FormFieldImpl>();

		for (int index = 0; index < this.headers.length; index++)
		{
			final ComboBox box = this.mappings.get(index);
			if (box.getValue() != null)
			{
				final FormFieldImpl field = (FormFieldImpl) box.getValue();
				fieldMaps.put(this.headers[index], field);
			}
		}

		return fieldMaps;
	}

	public ArrayList<ImportUserMapping> getUserMapping()
	{
		return this.userMapping;
	}

	public void setUserMapping(final ArrayList<ImportUserMapping> userMapping)
	{
		this.userMapping = userMapping;
	}

	// class FieldMap
	// {
	// private String csvHeader;
	// private FormFieldImpl formField;
	//
	// public FieldMap(String csvHeader, FormFieldImpl formField)
	// {
	// this.csvHeader = csvHeader;
	// this.formField = formField;
	// }
	//
	//
	// public String displayName()
	// {
	// return formField.displayName();
	// }
	//
	// public boolean visible()
	// {
	// return formField.visible();
	// }
	// }

}
