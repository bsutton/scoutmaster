package au.org.scoutmaster.dao;

import java.util.List;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.Note_;

public class NoteDao extends JpaBaseDao<Note, Long>
{

	@Override
	public JPAContainer<Note> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	@SuppressWarnings("unchecked")
	public List<Note> findNoteBySubject(final String subject)
	{
		return (List<Note>) super.findOneByAttribute(Note_.subject, subject);
	}

}
