package au.org.scoutmaster.fields;

import com.vaadin.ui.Label;

import au.com.vaadinutils.crud.splitFields.SplitField;
import au.org.scoutmaster.domain.BaseEntity;

@SuppressWarnings("unchecked")
public class SplitTagField<T extends BaseEntity> extends TagField implements SplitField
{
	private static final long serialVersionUID = 7753660388792217050L;
	private final Label label;

	public SplitTagField(final String fieldLabel, final boolean readonly)
	{
		super(null, readonly);
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
