package com.xdev.server.db.connection;

import javax.persistence.EntityManagerFactory;

public interface HibernateUtil
{
	public EntityManagerFactory getEntityManagerFactory();
}
