package au.org.scoutmaster.views.wizards.importer;

import java.util.ArrayList;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Importable;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

public class ImportSelectType  implements WizardStep
{
	ArrayList<ImportType> importTypes = new ArrayList<ImportType>();
	private OptionGroup optionGroup;
	
	class ImportType
	{
		String name;
		Class<? extends Importable> importable;
		
		public ImportType(String name, Class<? extends Importable> class1)
		{
			this.name = name;
			this.importable = class1;
		}
		public String toString()
		{
			return name;
		}
	}

	public ImportSelectType(ImportWizardView importView)
	{
		importTypes.add(new ImportType("Contacts", Contact.class));
		
		
		
	}

	@Override
	public String getCaption()
	{
		return "Select entity";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(new Label("Select the type of information you are looking to import. The click 'Next'"));
		FormLayout formLayout = new FormLayout();
		this.optionGroup = new OptionGroup("Type", importTypes);
		formLayout.addComponent(optionGroup);
		layout.addComponent(formLayout);
		layout.setMargin(true);
		
		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = this.optionGroup.getValue() != null;
		
		if (!advance)
			Notification.show("Please select a Type to import and then click Next");
		return advance;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

	public Class<? extends Importable> getEntityClass()
	{
		return ((ImportType)this.optionGroup.getValue()).importable;
	}

}
