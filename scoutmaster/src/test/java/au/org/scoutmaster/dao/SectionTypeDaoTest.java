package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.domain.Age;

public class SectionTypeDaoTest
{

	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;

	@Before
	public void init() throws ServletException
	{

		this.entityManagerFactory = Persistence.createEntityManagerFactory("scoutmastertest");
		this.em = this.entityManagerFactory.createEntityManager();
		EntityManagerProvider.setCurrentEntityManager(this.em);
	}

	@After
	public void finalise()
	{
		this.em.close();
	}

	@Test
	public void testSectionEligibility()
	{

		final SectionTypeDao daoSectionType = new DaoFactory().getSectionTypeDao();
		// Test the middle and cusp of each section
		final Age tooYoungStart = new Age(0, 0, 0);
		testSectionEligibility(daoSectionType, tooYoungStart, "Too Young");
		final Age tooYoungMiddle = new Age(2, 1, 1);
		testSectionEligibility(daoSectionType, tooYoungMiddle, "Too Young");
		final Age tooYoungEnd = new Age(5, 11, 30);
		testSectionEligibility(daoSectionType, tooYoungEnd, "Too Young");
		failSectionEligibility(daoSectionType, tooYoungEnd, "Joeys");

		final Age joeyStart = new Age(6, 0, 0);
		failSectionEligibility(daoSectionType, joeyStart, "Too Young");

		testSectionEligibility(daoSectionType, joeyStart, "Joeys");
		final Age joeyMiddle = new Age(7, 1, 1);
		testSectionEligibility(daoSectionType, joeyMiddle, "Joeys");
		final Age joeyEnd = new Age(7, 11, 30);
		testSectionEligibility(daoSectionType, joeyEnd, "Joeys");
		failSectionEligibility(daoSectionType, joeyEnd, "Cubs");

		final Age cubStart = new Age(8, 0, 0);
		failSectionEligibility(daoSectionType, cubStart, "Joeys");
		testSectionEligibility(daoSectionType, cubStart, "Cubs");
		final Age cubMiddle = new Age(9, 1, 1);
		testSectionEligibility(daoSectionType, cubMiddle, "Cubs");
		final Age cubEnd = new Age(10, 11, 30);
		testSectionEligibility(daoSectionType, cubEnd, "Cubs");
		failSectionEligibility(daoSectionType, cubEnd, "Scouts");

		final Age scoutStart = new Age(11, 0, 0);
		failSectionEligibility(daoSectionType, scoutStart, "Cubs");
		testSectionEligibility(daoSectionType, scoutStart, "Scouts");
		final Age scoutMiddle = new Age(12, 1, 1);
		testSectionEligibility(daoSectionType, scoutMiddle, "Scouts");
		final Age scoutEnd = new Age(14, 5, 29);
		testSectionEligibility(daoSectionType, scoutEnd, "Scouts");
		failSectionEligibility(daoSectionType, scoutEnd, "Venturers");

		final Age venturerStart = new Age(14, 7, 1);
		failSectionEligibility(daoSectionType, venturerStart, "Scouts");
		testSectionEligibility(daoSectionType, venturerStart, "Venturers");
		final Age venturerMiddle = new Age(15, 1, 1);
		testSectionEligibility(daoSectionType, venturerMiddle, "Venturers");
		final Age venturerEnd = new Age(20, 11, 30);
		testSectionEligibility(daoSectionType, venturerEnd, "Venturers");
		failSectionEligibility(daoSectionType, venturerEnd, "Rovers");

		// Rovers actually starts at 18 but has an overlap with Venturers
		final Age roverStart = new Age(21, 0, 0);
		failSectionEligibility(daoSectionType, roverStart, "Venturers");
		testSectionEligibility(daoSectionType, roverStart, "Rovers");
		final Age roverMiddle = new Age(21, 1, 1);
		testSectionEligibility(daoSectionType, roverMiddle, "Rovers");
		final Age roverEnd = new Age(25, 11, 30);
		testSectionEligibility(daoSectionType, roverEnd, "Rovers");
		failSectionEligibility(daoSectionType, roverEnd, "Leader");

		final Age tooOldStart = new Age(26, 0, 0);
		testSectionEligibility(daoSectionType, tooOldStart, "Leaders");
		failSectionEligibility(daoSectionType, tooOldStart, "Rovers");
		final Age tooOldMiddle = new Age(50, 1, 1);
		testSectionEligibility(daoSectionType, tooOldMiddle, "Leaders");
	}

	private void testSectionEligibility(final SectionTypeDao daoSectionType, final Age age, final String section)
	{
		final DateTime birthDate = age.getBirthDate();
		Assert.assertTrue(daoSectionType.getEligibleSection(birthDate).getName().equals(section));
	}

	private void failSectionEligibility(final SectionTypeDao daoSectionType, final Age age, final String section)
	{
		final DateTime birthDate = age.getBirthDate();
		Assert.assertFalse(daoSectionType.getEligibleSection(birthDate).getName().equals(section));
	}

}
