package com.xdev.server.communication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.vaadin.server.VaadinService;
import com.xdev.server.db.connection.HibernateUtil;

//technology specific global helper
public class VaadinSessionEntityManagerHelper {

	private static EntityManagerFactory emf;

	public static EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	private static HibernateUtil hibernateUtil;

	public static EntityManagerFactory setHibernateUtil(
			HibernateUtil hibernateUtil) {
		VaadinSessionEntityManagerHelper.hibernateUtil = hibernateUtil;
		return emf = hibernateUtil.getEntityManagerFactory();
	}

	public static HibernateUtil getHibernateutil() {
		return hibernateUtil;
	}

	public static EntityManager getEntityManager() {
		EntityManager em = (EntityManager) VaadinService.getCurrentRequest()
				.getAttribute("HibernateEntityManager");
		return em;
	}

	public static void closeEntityManager() {
		if (getEntityManager() != null) {
			getEntityManager().close();
		}
	}

	public static void closeEntityManagerFactory() {
		emf.close();
	}

	public static void beginTransaction() {
		getEntityManager().getTransaction().begin();
	}

	public static void rollback() {
		getEntityManager().getTransaction().rollback();
	}

	public static void commit() {
		getEntityManager().getTransaction().commit();
	}

	public static EntityTransaction getTransaction() {
		return getEntityManager().getTransaction();
	}

	public static <T> CriteriaQuery<T> getCriteriaQuery(Class<T> type) {
		CriteriaBuilder cb = VaadinSessionEntityManagerHelper
				.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(type);
		return cq;
	}
}