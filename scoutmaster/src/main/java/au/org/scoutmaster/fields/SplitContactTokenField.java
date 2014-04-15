package au.org.scoutmaster.fields;

import au.com.vaadinutils.crud.splitFields.SplitField;
import au.org.scoutmaster.domain.BaseEntity;

import com.vaadin.ui.Label;

public class SplitContactTokenField<T extends BaseEntity> extends ContactTokenField<T> implements SplitField
{
	private static final long serialVersionUID = 7753660388792217050L;
	private final Label label;

	public SplitContactTokenField(final String fieldLabel)
	{
		super(null);
		this.label = new Label(fieldLabel);
		setCaption(fieldLabel);

	}

	@Override
	public void setVisible(final boolean visible)
	{
		this.label.setVisible(visible);
		super.setVisible(visible);
	}

	@Override
	public Label getLabel()
	{
		return this.label;
	}

	@Override
	public String getCaption()
	{
		return this.label.getValue();
	}

	@Override
	public void hideLabel()
	{
		setCaption(null);

	}

}
