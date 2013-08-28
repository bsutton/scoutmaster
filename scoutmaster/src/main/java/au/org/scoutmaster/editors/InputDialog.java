package au.org.scoutmaster.editors;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class InputDialog extends Window
{
	Recipient r;
	TextField tf = new TextField();

	public InputDialog(final UI parent, String title, String question, Recipient recipient)
	{
		r = recipient;
		setCaption(title);
		setModal(true);
		this.setClosable(false);
		this.setResizable(false);

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeUndefined();
		FormLayout form = new FormLayout();

		tf.setCaption(question);
		form.addComponent(tf);
		layout.addComponent(form);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);

		buttons.addComponent(new Button("Cancel", new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				r.cancelled();
				InputDialog.this.close();
			}
		}));
		buttons.addComponent(new Button("Ok", new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				try
				{
					tf.validate();
					r.gotInput(tf.getValue());
					InputDialog.this.close();
				}
				catch (InvalidValueException e)
				{
					Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
				}
			}
		}));

		layout.addComponent(buttons);
		layout.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);

		this.setContent(layout);
		parent.addWindow(this);
	}

	public interface Recipient
	{
		public void gotInput(String input);

		public void cancelled();
	}

	/**
	 * Add validators to the field which will be run when the user clicks OK.
	 * 
	 * The OK button will not succeed whilst there are field validation errors.
	 * 
	 * @param validator
	 */
	public void addValidator(Validator validator)
	{
		tf.addValidator(validator);
	}
}