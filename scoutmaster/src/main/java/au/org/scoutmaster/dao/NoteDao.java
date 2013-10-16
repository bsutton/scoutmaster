package au.org.scoutmaster.dao;

import java.util.List;

import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.Note_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class NoteDao extends JpaBaseDao<Note, Long>
{

	@Override
	public JPAContainer<Note> makeJPAContainer()
	{
		return super.makeJPAContainer(Note.class);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Note> findNoteBySubject(String subject)
	{
		return (List<Note>) super.findOneByAttribute(Note_.subject, subject);
	}

	
	

}
