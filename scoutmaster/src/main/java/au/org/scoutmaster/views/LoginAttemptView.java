package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.access.LoginAttempt;
import au.org.scoutmaster.domain.access.LoginAttempt_;
import au.org.scoutmaster.domain.access.User_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Login Attempts", path="Admin.Security")
public class LoginAttemptView extends BaseCrudView<LoginAttempt> implements View, Selected<LoginAttempt>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();

	public static final String NAME = "LoginAttempt";

	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup<LoginAttempt> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();

		SMMultiColumnFormLayout<LoginAttempt> overviewForm = new SMMultiColumnFormLayout<LoginAttempt>(1, this.fieldGroup);
		overviewForm.setColumnLabelWidth(0, 120);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setSizeFull();
		overviewForm.getFieldGroup().setReadOnly(true);
		
		overviewForm.bindTextField("User", new Path(LoginAttempt_.user, User_.username).getName());
		overviewForm.newLine();
		overviewForm.bindDateField("Attempt Time", LoginAttempt_.dateOfAttempt, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();
		overviewForm.bindBooleanField("Succeeded", LoginAttempt_.succeeded);
		overviewForm.newLine();
		layout.addComponent(overviewForm);

		super.disallowEdit(true, true);
		super.disallowNew(true);
		//super.disallowDelete(true);

		return layout;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<LoginAttempt> container = new DaoFactory().getLoginAttemptDao().createVaadinContainer();
		container.sort(new String[] {LoginAttempt_.dateOfAttempt.getName()},new boolean[] {false});

		Builder<LoginAttempt> builder = new HeadingPropertySet.Builder<LoginAttempt>();
		builder.addColumn("User", LoginAttempt_.user).addColumn("Login Attempt", LoginAttempt_.dateOfAttempt)
				.addColumn("Succeeded", LoginAttempt_.succeeded);

		super.init(LoginAttempt.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(LoginAttempt_.user.getName(), filterString, true, false)
		, new SimpleStringFilter(LoginAttempt_.dateOfAttempt.getName(), filterString, true, false)
		, new SimpleStringFilter(LoginAttempt_.succeeded.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Session History";
	}

}
