package au.org.scoutmaster.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import au.com.vaadinutils.dao.EntityManagerProvider;

/**
 * Use this unit test to rebuild the database from the JPA domain entities.
 * 
 * @author bsutton
 * 
 */
public class RebuildDatabase
{

	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;

	@Before
	public void init() throws ServletException
	{
		entityManagerFactory = Persistence.createEntityManagerFactory("scoutmastertest");
		em = entityManagerFactory.createEntityManager();
		EntityManagerProvider.INSTANCE.setCurrentEntityManager(em);
	}

	@Test
	public void test()
	{
		System.out.println("Before you run this script you should have first dropped and recreated the scoutmastertest database");
		System.out.println("Database has been rebuilt. Run: (password = master$4scout) \n");
		System.out.println("mysqldump --triggers --routines -u scoutmaster -p scoutmastertest > init.sql\n");
		System.out.println("Copy init.sql into /src/main/resources/liqibase \n");
		System.out.println("Edit the init.sql and remove the comments from the SET commands at the start and end of the script.");
		System.out.println("Added the following lines to the top of the init.sql file. \n");
		System.out.println("-- liquibase formatted sql");
		System.out.println("-- changeset bsutton:1");
		System.out.println("Drop and create the scoutmaster database.");
		System.out.println("Start Scoutmaster and your db should be recreated");
	}
}
