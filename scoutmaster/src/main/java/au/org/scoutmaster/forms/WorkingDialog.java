package au.org.scoutmaster.forms;

import au.com.vaadinutils.ui.UIUpdater;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WorkingDialog extends Window 
{
	private static final long serialVersionUID = 1L;
	private Label messageLabel;
	private VerticalLayout content;

	public WorkingDialog(String caption, String message)
	{
		super(caption);
		this.setModal(true);
		this.setClosable(false);
		this.setResizable(false);
		content = new VerticalLayout();
		content.setWidth("300px");
		content.setHeight("100px");
		content.setMargin(true);
		content.setSpacing(true);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		
		ProgressBar progress = new ProgressBar();
		layout.addComponent(progress);
		progress.setIndeterminate(true);
		messageLabel = new Label(message);
		layout.addComponent(messageLabel);
		
		content.addComponent(layout);
		this.setContent(content);
		this.center();
		
		
	}

	public void progress(final String message)
	{
		new UIUpdater(new Runnable()
		{
			@Override
			public void run()
			{
				messageLabel.setValue(message);
			}
		});
	}

	public void complete()
	{
		WorkingDialog.this.close();		
	}

//	@Override
//	public void onFailure(Throwable t)
//	{
//		messageLabel.setValue(t.getClass().getSimpleName() + ":" + t.getMessage());
//		Button closeButton = new Button("Close");
//		closeButton.addClickListener(new ClickEventLogged.ClickListener()
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void clicked(ClickEvent event)
//			{
//				WorkingDialog.this.close();
//			}
//		});
//		content.addComponent(closeButton);
//		
//	}

}
