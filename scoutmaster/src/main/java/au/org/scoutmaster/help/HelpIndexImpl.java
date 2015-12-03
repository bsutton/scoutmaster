package au.org.scoutmaster.help;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.help.HelpDisplayedCallback;
import au.com.vaadinutils.help.HelpIndex;

public class HelpIndexImpl implements HelpIndex
{

	static transient Logger logger = LogManager.getLogger(HelpIndexImpl.class);

	private String lookupHelpIndex(Enum<?> helpId) throws ExecutionException
	{
		return HelpPageCache.lookupHelpPage(helpId);
	}

	@Override
	public void setHelpSource(final Enum<?> helpId, final AbstractOrderedLayout layout, HelpDisplayedCallback callback)
	{

		Preconditions.checkNotNull(layout);
		HelpDisplayedCallback safeCallback = callback;
		if (safeCallback == null)
		{
			safeCallback = getNullCallback();
		}

		final HelpDisplayedCallback callbackHandler = safeCallback;

		final UI ui = UI.getCurrent();
		Runnable runner = new Runnable()
		{

			@Override
			public void run()
			{
				if (ui != null)
				{
					ui.accessSynchronously(new Runnable()
					{

						@Override
						public void run()
						{
							if (helpId != null)
							{

								// BrowserFrame helpFrame = new BrowserFrame();
								// helpFrame.setSizeFull();
								// // helpFrame.setHeight("600");
								// // helpFrame.setWidth("800");
								// helpFrame.setStyleName("njadmin-hide-overflow-for-help");
								//
								// layout.addComponent(helpFrame);
								// layout.setExpandRatio(helpFrame, 1.0f);
								//
								// helpFrame.setSource(new
								// ExternalResource(HelpPageCache.getHelpPageURL(helpId)));

								Panel helpFrame = new Panel();
								helpFrame.setSizeFull();
								// helpFrame.setHeight("600");
								// helpFrame.setWidth("800");
								helpFrame.setStyleName("njadmin-hide-overflow-for-help");

								layout.addComponent(helpFrame);
								layout.setExpandRatio(helpFrame, 1.0f);

								Label content;
								try
								{
									content = new Label(HelpPageCache.lookupHelpPage(helpId));
									content.setContentMode(ContentMode.HTML);
									helpFrame.setContent(content);
									callbackHandler.success();
								}
								catch (ExecutionException e)
								{
									callbackHandler.fail();
									logger.error(e);
								}

							}
							else
							{

								VerticalLayout help = new VerticalLayout();
								help.setSizeFull();
								help.addComponent(new Label("A help Id has not been set for this view."));
								help.setSpacing(true);
								help.setMargin(true);
								layout.addComponent(help);
								layout.setExpandRatio(help, 1.0f);
								callbackHandler.fail();
							}
						}
					});
				}

			}
		};

		if (HelpPageCache.cache.getIfPresent(1l) != null)
		{
			runner.run();
		}
		else
		{
			new Thread(runner, "Set help Url").start();
		}

	}

	private HelpDisplayedCallback getNullCallback()
	{
		return new HelpDisplayedCallback()
		{

			@Override
			public void success()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void fail()
			{
				// TODO Auto-generated method stub

			}
		};
	}
}
