package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.OrganisationTypeDao;
import au.org.scoutmaster.domain.OrganisationType;
import au.org.scoutmaster.domain.OrganisationType_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Organisation Types", path="Admin.Lists")
public class OrganisationTypeView extends BaseCrudView<OrganisationType> implements View, Selected<OrganisationType>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(OrganisationTypeView.class);

	public static final String NAME = "OrganisationType";

	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup<OrganisationType> fieldGroup2)
	{
		SMMultiColumnFormLayout<OrganisationType> overviewForm = new SMMultiColumnFormLayout<OrganisationType>(1, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setColumnLabelWidth(0, 100);

		overviewForm.bindTextField("Name", OrganisationType_.name);
		overviewForm.bindTextAreaField("Description", OrganisationType_.description, 4);

		return overviewForm;
	}

		@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<OrganisationType> container = new DaoFactory().getDao(OrganisationTypeDao.class).createVaadinContainer();
		container.sort(new String[]
		{ OrganisationType_.name.getName() }, new boolean[]
		{ true });

		Builder<OrganisationType> builder = new HeadingPropertySet.Builder<OrganisationType>();
		builder.addColumn("Type Name", OrganisationType_.name);

		super.init(OrganisationType.class, container, builder.build());
	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new SimpleStringFilter(OrganisationType_.name.getName(), filterString, true, false);
	}

	
	@Override
	protected String getTitleText()
	{
		return "Organisation Types";
	};

}
