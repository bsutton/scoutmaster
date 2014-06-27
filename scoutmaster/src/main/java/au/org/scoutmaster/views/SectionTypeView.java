package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.domain.iColorFactory;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.ColorDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.QualificationTypeDao;
import au.org.scoutmaster.domain.QualificationType;
import au.org.scoutmaster.domain.QualificationType_;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.SectionType_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Section Type", path = "Admin.Lists")
public class SectionTypeView extends BaseCrudView<SectionType> implements View, Selected<SectionType>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SectionTypeView.class);

	public static final String NAME = "SectionType";

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<SectionType> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final SMMultiColumnFormLayout<SectionType> overviewForm = new SMMultiColumnFormLayout<>(2, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 600);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setColumnExpandRatio(1, 1.0f);
		overviewForm.setSizeFull();

		overviewForm.bindTextField("Name", SectionType_.name);
		overviewForm.newLine();

		overviewForm.bindTextField("Description", SectionType_.description);
		overviewForm.newLine();

		overviewForm.bindTextField("Starting Age", SectionType_.startingAge);
		overviewForm.newLine();

		overviewForm.bindTextField("Starting Age", SectionType_.endingAge);
		overviewForm.newLine();

		final iColorFactory factory = new ColorDao.ColorFactory();
		overviewForm.bindColorPicker(factory, "Section Colour", SectionType_.colour.getName());
		overviewForm.newLine();

		final QualificationTypeDao daoQualificationType = new DaoFactory().getDao(QualificationTypeDao.class);

		overviewForm.getFormHelper().new TwinColSelectBuilder<QualificationType>()
				.setField(SectionType_.leaderRequirements).setContainer(daoQualificationType.createVaadinContainer())
				.setForm(overviewForm).setListFieldName(QualificationType_.name).setLabel("Leader Qualifications")
				.build();
		overviewForm.newLine();

		overviewForm.getFormHelper().new TwinColSelectBuilder<QualificationType>()
				.setField(SectionType_.assistentLeaderRequirements)
				.setContainer(daoQualificationType.createVaadinContainer()).setForm(overviewForm)
				.setListFieldName(QualificationType_.name).setLabel("Assistant Leader Qualifications").build();
		overviewForm.newLine();

		overviewForm.getFormHelper().new TwinColSelectBuilder<QualificationType>()
				.setField(SectionType_.parentHelperRequirements)
				.setContainer(daoQualificationType.createVaadinContainer()).setForm(overviewForm)
				.setListFieldName(QualificationType_.name).setLabel("Parent Helper Qualifications").build();

		layout.addComponent(overviewForm);

		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<SectionType> container = new DaoFactory().getSectionTypeDao().createVaadinContainer();
		container.sort(new String[]
		{ SectionType_.name.getName() }, new boolean[]
		{ false });

		final Builder<SectionType> builder = new HeadingPropertySet.Builder<SectionType>();
		builder.addColumn("Section", SectionType_.name).addColumn("Description", SectionType_.description)
		.addColumn("Starting Age", SectionType_.startingAge).addColumn("End Age", SectionType_.endingAge);

		super.init(SectionType.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(SectionType_.name.getName(), filterString, true, false),
				new SimpleStringFilter(SectionType_.description.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Section Types";
	}

}
