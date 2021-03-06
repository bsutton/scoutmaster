package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.access.SessionHistory;
import au.org.scoutmaster.domain.access.SessionHistory_;
import au.org.scoutmaster.domain.access.User_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

@Menu(display = "Session History", path = "Admin.Security")
public class SessionHistoryView extends BaseCrudView<SessionHistory> implements View, Selected<SessionHistory>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();

	public static final String NAME = "SessionHistory";

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<SessionHistory> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final SMMultiColumnFormLayout<SessionHistory> overviewForm = new SMMultiColumnFormLayout<SessionHistory>(1,
				this.fieldGroup);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setSizeFull();
		overviewForm.getFieldGroup().setReadOnly(true);

		overviewForm.bindTextField("User", new Path(SessionHistory_.user, User_.username).getName());
		overviewForm.newLine();
		overviewForm.bindDateField("Start Time", SessionHistory_.start, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();
		overviewForm.bindDateField("End Time", SessionHistory_.end, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();
		layout.addComponent(overviewForm);

		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<SessionHistory> container = new DaoFactory().getSessionHistoryDao().createVaadinContainer();
		container.sort(new String[]
		{ SessionHistory_.start.getName() }, new boolean[]
		{ false });

		final Builder<SessionHistory> builder = new HeadingPropertySet.Builder<SessionHistory>();
		builder.addColumn("User", SessionHistory_.user).addColumn("Start Time", SessionHistory_.start)
				.addColumn("End Time", SessionHistory_.end);

		super.disallowNew(true);
		super.disallowEdit(true);

		super.init(SessionHistory.class, container, builder.build());
		super.disallowDelete(true);

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(SessionHistory_.user.getName(), filterString, true, false),
				new SimpleStringFilter(SessionHistory_.start.getName(), filterString, true, false),
				new SimpleStringFilter(SessionHistory_.end.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Session History";
	}

}
