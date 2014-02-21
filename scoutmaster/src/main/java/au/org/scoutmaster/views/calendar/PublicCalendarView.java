package au.org.scoutmaster.views.calendar;

import java.util.Date;

import org.vaadin.peter.buttongroup.ButtonGroup;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.OrganisationDao;
import au.org.scoutmaster.domain.Organisation;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.ContainerEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent.EventChangeEvent;
import com.vaadin.ui.themes.Reindeer;

/** A start view for navigating to the main view */
@Menu(display = "Calendar")
public class PublicCalendarView extends CalendarView
{
	public static final String NAME = "PublicCalendar";

	private static final long serialVersionUID = 1L;

	public PublicCalendarView()
	{
		super.setPublic(true);
	}
}