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

public class ImportSelectType implements WizardStep
{
	ArrayList<ImportType> importTypes = new ArrayList<ImportType>();
	private final OptionGroup optionGroup;
	private final VerticalLayout layout;

	public ImportSelectType(final ImportWizardView importView)
	{
		this.importTypes.add(new ImportType("Contacts", Contact.class));

		this.layout = new VerticalLayout();
		this.layout
				.addComponent(new Label("Select the type of information you are looking to import. The click 'Next'"));
		final FormLayout formLayout = new FormLayout();
		this.optionGroup = new OptionGroup("Type", this.importTypes);
		formLayout.addComponent(this.optionGroup);
		this.layout.addComponent(formLayout);
		this.layout.setMargin(true);
	}

	@Override
	public String getCaption()
	{
		return "Select entity";
	}

	@Override
	public Component getContent()
	{

		return this.layout;
	}

	@Override
	public boolean onAdvance()
	{
		final boolean advance = this.optionGroup.getValue() != null;

		if (!advance)
		{
			Notification.show("Please select a Type to import and then click Next");
		}
		return advance;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

	public Class<? extends Importable> getEntityClass()
	{
		return ((ImportType) this.optionGroup.getValue()).importable;
	}

	class ImportType
	{
		String name;
		Class<? extends Importable> importable;

		public ImportType(final String name, final Class<? extends Importable> class1)
		{
			this.name = name;
			this.importable = class1;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}

}
