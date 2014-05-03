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
	private final RaffleBookAllocationWizardView wizard;
	private VerticalLayout layout;

	public AllocationMethodStep(final RaffleBookAllocationWizardView setupWizardView)
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
		if (this.layout == null)
		{
			this.layout = new VerticalLayout();
			this.layout.setMargin(true);

			final Label label = new Label("<h1>Select the Allocation method.</h1>");
			label.setContentMode(ContentMode.HTML);
			this.layout.addComponent(label);

			this.options = new OptionGroup();
			this.options.addItem(AllocationMethod.BulkAllocation);
			this.options.addItem(AllocationMethod.SingleAllocation);
			this.options.addItem(AllocationMethod.ManualAllocation);
			this.options.setValue(AllocationMethod.BulkAllocation);
			this.options.setImmediate(true);
			this.options.addValueChangeListener(this);

			this.layout.addComponent(this.options);

			final StringBuilder sb = new StringBuilder();
			sb.append("<ul><li>Bulk Allocation allows you to automatically allocate a number of books to a list of selected Contacts.</li>");
			sb.append("<li>Single Allocation allows you to automatically allocate a number of books to a single Contact.</li>");
			sb.append("<li>Manual Allocation allows you select individual books to allocate to a single Contact.</li></ul>");

			this.layout.addComponent(new Label(sb.toString(), ContentMode.HTML));

		}

		return this.layout;
	}

	enum AllocationMethod
	{
		BulkAllocation("Bulk Allocation"), SingleAllocation("Single Allocation"), ManualAllocation("Manual Allocation");

		private String description;

		AllocationMethod(final String description)
		{
			this.description = description;
		}

		@Override
		public String toString()
		{
			return this.description;
		}

	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;

		if (this.options.getValue() != null)
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
	public void valueChange(final ValueChangeEvent event)
	{
		final AllocationMethod method = (AllocationMethod) this.options.getValue();
		switch (method)
		{
			case BulkAllocation:
				this.wizard.addAllocationStep(new BulkSelectionStep(this.wizard), new BulkAllocationStep(this.wizard));
				break;
			case ManualAllocation:
				this.wizard.addAllocationStep(new ManualSelectionStep(this.wizard), new ManualAllocationStep(
						this.wizard));
				break;
			case SingleAllocation:
				this.wizard.addAllocationStep(new SingleSelectionStep(this.wizard), new SingleAllocationStep(
						this.wizard));
				break;
		}

	}

}
