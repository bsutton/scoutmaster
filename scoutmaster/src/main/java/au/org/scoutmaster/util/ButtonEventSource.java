package au.org.scoutmaster.util;

import rx.Observable;
import rx.Observable.OnSubscribeFunc;
import rx.Observer;
import rx.Subscription;
import au.com.vaadinutils.listener.ClickEventLogged.ClickListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

public enum ButtonEventSource
{
	; // no instances
	
	static public class ClickSubscription implements Subscription
	{
		private Button button;
		private Observer<? super ClickEvent> observer;		

		ClickSubscription(Observer<? super ClickEvent> observer, final Button button)
		{
			this.button = button;
			this.observer = observer;
			button.addClickListener(listener);
		}

		final ClickListener listener = new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(ClickEvent event)
			{
				ClickSubscription.this.observer.onNext(event);
			}
		};

		
		@Override
		public void unsubscribe()
		{
			UI.getCurrent().access(new Runnable()
			{
				@Override
				public void run()
				{
					button.removeClickListener(listener);

				}
			});			
		}
	}


	public static Observable<ClickEvent> fromActionOf(final Button button)
	{
		return Observable.create(new OnSubscribeFunc<ClickEvent>()
		{
			public Subscription onSubscribe(Observer<? super ClickEvent> observer)
			{
				return  new ClickSubscription(observer, button);
			}
			
		});
	}
}