
package com.xdev.ui.paging;


import java.io.Serializable;

import javax.persistence.EntityManager;

import org.vaadin.addons.lazyquerycontainer.EntityQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import com.xdev.server.communication.EntityManagerHelper;


public class RequisitioningEntityQueryFactory<T> implements QueryFactory, Serializable
{
	
	/**
	 *
	 */
	private static final long	serialVersionUID	= 9119964329587628519L;
	
	
	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager()
	{
		return EntityManagerHelper.getEntityManager();
	}
	
	
	/**
	 * Constructs a new query according to the given QueryDefinition.
	 *
	 * @param queryDefinition
	 *            Properties participating in the sorting.
	 * @return A new query constructed according to the given sort state.
	 */
	@Override
	public Query constructQuery(final QueryDefinition queryDefinition)
	{
		return new RequisitioningEntityQuery<T>((EntityQueryDefinition)queryDefinition);
	}
	
}
