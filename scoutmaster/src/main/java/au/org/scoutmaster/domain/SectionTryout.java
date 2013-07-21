package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import au.org.scoutmaster.domain.accounting.MoneyWithTax;


/** 
 * A youth member who are currently trying out to see if they would
 * like to join a Section.
 */
@Entity
public class SectionTryout extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The type of tryout.
	 */
	@ManyToOne
	SectionTryoutType type;
	
	/**
	 * The youth who is doing a trial.
	 */
	@OneToOne
	Contact trialYouthMember;
	
	
	/**
	 * The paperwork necessary for the trial has been completed.
	 */
	boolean trailPaperWorkCompleted;
	/**
	 * The date the youth first attended a trial.
	 */
	Date startDate;
	
	/**
	 * The date the trial is expected to complete.
	 */
	Date expectedCompletionDate;
	
	/**
	 * The actual date the trial completed.
	 */
	Date actualCompletionDate;
	
	/**
	 * The outcome of the trail
	 */
	TryoutOutcome outcome;

	/**
	 * The cost to this individual for the tryout.
	 * Tryouts may be free or the leader may organise
	 * a discount for this youth to do a tryout.
	 */
	@Embedded
	MoneyWithTax cost;

	/*
	 * If the SetionTryoutType has a cost then records if the member has paid.
	 */
	boolean paid;
	
}