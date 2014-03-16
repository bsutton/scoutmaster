package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.QualificationTypeDao;
import au.org.scoutmaster.domain.QualificationType;
import au.org.scoutmaster.domain.QualificationType_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Qualification Type", path="Admin.Lists")
public class QualificationTypeView extends BaseCrudView<QualificationType> implements View, Selected<QualificationType>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(QualificationTypeView.class);

	public static final String NAME = "QualificationType";

	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup<QualificationType> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();

		SMMultiColumnFormLayout<QualificationType> overviewForm = new SMMultiColumnFormLayout<>(2, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 600);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setColumnExpandRatio(1, 1.0f);
		overviewForm.setSizeFull();
		
		overviewForm.bindTextField("Name", QualificationType_.name);
		overviewForm.newLine();

		overviewForm.bindTextAreaField("Description", QualificationType_.description, 6);
		overviewForm.newLine();
		
		overviewForm.bindBooleanField("Expires", QualificationType_.expires);
		overviewForm.newLine();

		overviewForm.bindTextField("Valid For", QualificationType_.validFor);
		overviewForm.newLine();

		layout.addComponent(overviewForm);

		return layout;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<QualificationType> container = new DaoFactory().getDao(QualificationTypeDao.class).createVaadinContainer();
		container.sort(new String[] {QualificationType_.name.getName()},new boolean[] {false});

		Builder<QualificationType> builder = new HeadingPropertySet.Builder<QualificationType>();
		builder.addColumn("Qualification", QualificationType_.name).addColumn("Description", QualificationType_.description)
				.addColumn("Expires", QualificationType_.expires).addColumn("Valid For", QualificationType_.validFor);

		super.init(QualificationType.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(QualificationType_.name.getName(), filterString, true,
				false), new SimpleStringFilter(QualificationType_.description.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Qualification Types";
	}

}
