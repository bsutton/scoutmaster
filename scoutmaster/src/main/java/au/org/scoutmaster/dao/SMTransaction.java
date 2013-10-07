package au.org.scoutmaster.dao;

import java.io.Closeable;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.Transaction;

public class SMTransaction extends Transaction implements Closeable
{

	public SMTransaction(EntityManager em)
	{
		super(em);
	}

}
