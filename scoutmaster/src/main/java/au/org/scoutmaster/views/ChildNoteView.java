package au.org.scoutmaster.views;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.Note_;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class ChildNoteView extends ChildCrudView<Contact, Note>
{
	private static final long serialVersionUID = 1L;

	public ChildNoteView(BaseCrudView<Contact> parentCrud)
	{
		super(parentCrud, Contact.class, Note.class, Contact_.id, Note_.attachedContact.getName());

		JPAContainer<Note> container = new DaoFactory().getNoteDao().createVaadinContainer();
		container.sort(new String[]
		{ Note_.created.getName() }, new boolean[]
		{ false });

		Builder<Note> builder = new HeadingPropertySet.Builder<Note>();
		builder.addColumn("Subject", Note_.subject).addColumn("Date", Note_.noteDate);

		super.init(Note.class, container, builder.build());

	}

	@Override
	protected Component buildEditor(ValidatingFieldGroup<Note> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		MultiColumnFormLayout<Note> overviewForm = new MultiColumnFormLayout<Note>(1, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 180);
		overviewForm.setColumnLabelWidth(0, 70);
		// overviewForm.setColumnExpandRatio(0, 1.0f);
		overviewForm.setSizeFull();

		// overviewForm.colspan(2);
		overviewForm.bindDateField("Note Date", Note_.noteDate, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();
		overviewForm.bindTextField("Subject", Note_.subject);
		overviewForm.newLine();

		TextArea detailsEditor = overviewForm.bindTextAreaField("Body", Note_.body, 6);
		detailsEditor.setSizeFull();
		overviewForm.setExpandRatio(1.0f);

		layout.addComponent(overviewForm);
		return layout;
	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new Or(new Or(new Or(new Or(new SimpleStringFilter(Note_.created.getName(), filterString, true,
				false), new SimpleStringFilter(Note_.subject.getName(), filterString, true, false)),
				new SimpleStringFilter(Note_.body.getName(), filterString, true, false)))));
	}

	@Override
	public void associateChild(Contact newParent, Note child)
	{
		newParent.addNote(child);
	}
}
