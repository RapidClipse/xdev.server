/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.communication;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.VaadinSession.State;
import com.vaadin.util.CurrentInstance;


/**
 * @author XDEV Software
 * @since 1.2
 */
public final class Conversationables implements Serializable
{
	public final static Conversationables getCurrent()
	{
		final VaadinSession session = VaadinSession.getCurrent();
		if(session != null && session.getState() == State.OPEN)
		{
			final Conversationables conversationables = session
					.getAttribute(Conversationables.class);
			if(conversationables != null)
			{
				return conversationables;
			}
		}
		return CurrentInstance.get(Conversationables.class);
	}
	
	private transient Map<String, Conversationable> unitToConversationable;
	
	
	public Conversationables()
	{
	}
	
	
	private Map<String, Conversationable> unitToConversationable()
	{
		if(this.unitToConversationable == null)
		{
			this.unitToConversationable = new LinkedHashMap<>();
		}
		return this.unitToConversationable;
	}
	
	
	public Conversationables put(final String persistenceUnit, final Conversationable conversation)
	{
		unitToConversationable().put(persistenceUnit,conversation);
		return this;
	}
	
	
	public Conversationable remove(final String persistenceUnit)
	{
		return unitToConversationable().remove(persistenceUnit);
	}
	
	
	public Conversationable get(final String persistenceUnit)
	{
		return unitToConversationable().get(persistenceUnit);
	}
	
	
	public void forEach(final Consumer<Conversationable> consumer)
	{
		unitToConversationable().values().forEach(consumer);
	}
	
	
	public void closeAll()
	{
		unitToConversationable().values().forEach(this::close);
	}
	
	
	public void close(final Conversationable conversationable)
	{
		final EntityManager em = conversationable.getEntityManager();
		if(em != null)
		{
			final EntityTransaction transaction = em.getTransaction();
			if(transaction != null && transaction.isActive())
			{
				try
				{
					transaction.commit();
				}
				catch(final RollbackException e)
				{
					if(transaction.isActive())
					{
						transaction.rollback();
					}
				}
			}
			
			try
			{
				em.close();
			}
			catch(final Exception e)
			{
				if(transaction != null && transaction.isActive())
				{
					transaction.rollback();
				}
			}
		}
	}
}
