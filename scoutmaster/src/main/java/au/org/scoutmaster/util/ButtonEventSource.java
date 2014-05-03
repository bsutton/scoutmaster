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
		private final Button button;
		private final Observer<? super ClickEvent> observer;

		ClickSubscription(final Observer<? super ClickEvent> observer, final Button button)
		{
			this.button = button;
			this.observer = observer;
			button.addClickListener(this.listener);
		}

		final ClickListener listener = new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final ClickEvent event)
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
					ClickSubscription.this.button.removeClickListener(ClickSubscription.this.listener);

				}
			});
		}
	}

	public static Observable<ClickEvent> fromActionOf(final Button button)
	{
		return Observable.create(new OnSubscribeFunc<ClickEvent>()
				{
			@Override
			public Subscription onSubscribe(final Observer<? super ClickEvent> observer)
			{
				return new ClickSubscription(observer, button);
			}

		});
	}
}