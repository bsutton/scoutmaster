package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

public class AllocationMethodStep implements WizardStep, ValueChangeListener
{

	private static final long serialVersionUID = 1L;
	private OptionGroup options;
	private RaffleBookAllocationWizardView wizard;
	private VerticalLayout layout;

	public AllocationMethodStep(RaffleBookAllocationWizardView setupWizardView)
	{
		this.wizard = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Method";
	}

	@Override
	public Component getContent()
	{
		if (layout == null)
		{
			layout = new VerticalLayout();
			layout.setMargin(true);

			Label label = new Label("<h1>Select the Allocation method.</h1>");
			label.setContentMode(ContentMode.HTML);
			layout.addComponent(label);

			options = new OptionGroup();
			options.addItem(AllocationMethod.BulkAllocation);
			options.addItem(AllocationMethod.SingleAllocation);
			options.addItem(AllocationMethod.ManualAllocation);
			options.setValue(AllocationMethod.BulkAllocation);
			options.setImmediate(true);
			options.addValueChangeListener(this);

			layout.addComponent(options);

			StringBuilder sb = new StringBuilder();
			sb.append("<ul><li>Bulk Allocation allows you to automatically allocate a number of books to a list of selected Contacts.</li>");
			sb.append("<li>Single Allocation allows you to automatically allocate a number of books to a single Contact.</li>");
			sb.append("<li>Manual Allocation allows you select individual books to allocate to a single Contact.</li></ul>");

			layout.addComponent(new Label(sb.toString(), ContentMode.HTML));

		}

		return layout;
	}

	enum AllocationMethod
	{
		BulkAllocation("Bulk Allocation"), SingleAllocation("Single Allocation"), ManualAllocation("Manual Allocation");

		private String description;

		AllocationMethod(String description)
		{
			this.description = description;
		}

		public String toString()
		{
			return description;
		}

	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;

		if (options.getValue() != null)
		{
			advance = true;
		}
		return advance;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

	@Override
	public void valueChange(ValueChangeEvent event)
	{
		AllocationMethod method = (AllocationMethod) options.getValue();
		switch (method)
		{
			case BulkAllocation:
				this.wizard.addAllocationStep(new BulkSelectionStep(wizard), new BulkAllocationStep(wizard));
				break;
			case ManualAllocation:
				this.wizard.addAllocationStep(new ManualSelectionStep(wizard), new ManualAllocationStep(wizard));
				break;
			case SingleAllocation:
				this.wizard.addAllocationStep(new SingleSelectionStep(wizard), new SingleAllocationStep(wizard));
				break;
		}

	}

}
