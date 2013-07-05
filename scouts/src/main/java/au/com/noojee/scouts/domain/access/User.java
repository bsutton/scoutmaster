package au.com.noojee.scouts.domain.access;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	Date created;
	
	String username;
	
	String password;
	
	/**
	 * The set of roles the user undertakes.
	 */
	@ManyToMany
	List<Role> belongsTo = new ArrayList<>();
}
