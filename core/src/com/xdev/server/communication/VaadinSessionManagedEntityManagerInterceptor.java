/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
package com.xdev.server.communication;


import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.xdev.server.db.connection.HibernateUtil;


//clean http only alternative to vaadin servlet does not support websockets
public class VaadinSessionManagedEntityManagerInterceptor implements Filter
{
	private static final String	HIBERNATEUTIL_FILTER_INIT_PARAM	= "hibernateUtil";
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException
	{
		try
		{
			HttpServletRequest httpRequest = (HttpServletRequest)req;
			if(!httpRequest.getMethod().equals("POST"))
			{
				// pass it down the chain
				chain.doFilter(req,res);
				return;
			}
			
			EntityManagerFactory factory = EntityManagerHelper.getEntityManagerFactory();
			EntityManager manager = null;
			if(factory != null)
			{
				manager = factory.createEntityManager();
				// System.out.println("opened em");
				
				// Add the EntityManager to the request
				req.setAttribute("HibernateEntityManager",manager);
			}
			
			// Call the next filter until the actual request (continue request
			// processing)
			chain.doFilter(req,res);
			
			// Flush and close the EntityManager after request is resolved
			// System.out.println("closing em");
			// System.out.println("---------------------------------------------------");
			if(manager != null)
			{
				manager.close();
			}
		}
		catch(Exception ex)
		{
			if(EntityManagerHelper.getEntityManager() != null)
			{
				EntityTransaction tx = EntityManagerHelper.getTransaction();
				if(tx != null && tx.isActive())
				{
					EntityManagerHelper.rollback();
				}
			}
			throw ex;
		}
	}
	
	
	@Override
	public void destroy()
	{
		// nothing to do here
	}
	
	
	@Override
	public void init(FilterConfig fc) throws ServletException
	{
		String hibernatePersistenceUnit = fc.getServletContext().getInitParameter(
				HIBERNATEUTIL_FILTER_INIT_PARAM);
		EntityManagerHelper.initializeHibernateFactory(new HibernateUtil.Implementation(
				hibernatePersistenceUnit));
		
		// try
		// {
		// Class<?> hibernateUtilClazz = Class.forName(hibernateUtilClassName);
		// HibernateUtil hibernateUtilObject =
		// (HibernateUtil)hibernateUtilClazz.newInstance();
		// VaadinSessionEntityManagerHelper.setHibernateUtil(hibernateUtilObject);
		// }
		// catch(Exception e)
		// {
		// throw new RuntimeException(e.getMessage());
		// }
	}
}
