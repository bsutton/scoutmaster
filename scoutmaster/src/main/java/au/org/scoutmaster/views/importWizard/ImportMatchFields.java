package au.org.scoutmaster.views.importWizard;

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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.bytecode.opencsv.CSVReader;
import au.org.scoutmaster.domain.EntityAdaptor;
import au.org.scoutmaster.domain.FormFieldImpl;
import au.org.scoutmaster.domain.ImportColumnFieldMapping;
import au.org.scoutmaster.domain.ImportUserMapping;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.views.ImportView;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

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

		VerticalLayout layout = new VerticalLayout();

		HorizontalLayout row = new HorizontalLayout();
		FormLayout fl = new FormLayout();
		this.selectedUserMapping = this.importView.getFile().getImportMapping();
		fieldMapping = new TextField("Import Mapping", selectedUserMapping);
		fl.addComponent(fieldMapping);
		row.addComponent(fl);
		row.addComponent(new Label(
				"Enter a Import Mapping name to save the mappings if you plan on repeating this import"));

		layout.setMargin(true);
		try
		{
			String[] headers = getHeaders();

			EntityAdaptor<Class <? extends Importable>> adaptor = new EntityAdaptor<Class <? extends Importable>>(importable);

			ArrayList<FormFieldImpl> fields = adaptor.getFields();
			for (String header : headers)
			{
				row = new HorizontalLayout();
				fl = new FormLayout();
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
		JPAContainer<ImportUserMapping> userMappings = JPAContainerFactory.make(ImportUserMapping.class, "scoutmaster");

		// Save the user selected mappings
		if (!fieldMapping.getValue().equals(selectedUserMapping))
		{
			// the name has changed so we need to save a new one

			ImportUserMapping userMapping = new ImportUserMapping(fieldMapping.getValue());

			for (ComboBox mapping : mappings)
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping.getCaption(),
						(String) mapping.getValue());
				userMapping.addColumnFieldMapping(columnMapping);
			}
			userMappings.addEntity(userMapping);
			userMappings.commit();

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

			EntityProvider<ImportUserMapping> ep = userMappings.getEntityProvider();
			EntityManager em = ep.getEntityManager();
			
			TypedQuery<ImportUserMapping> q2 =
				      em.createQuery("SELECT * FROM ImportUserMapping", ImportUserMapping.class);

			ImportUserMapping userMapping = q2.getSingleResult();
			em.getTransaction().begin();

			for (ComboBox mapping : mappings)
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping.getCaption(),
						(String) mapping.getValue());
				userMapping.addColumnFieldMapping(columnMapping);
			}

			em.getTransaction().commit();
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
				fieldMaps.put(field.getFieldName(), field);
			}
		}

		return fieldMaps;
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
