
package com.xdev.server.communication;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.xdev.server.db.connection.HibernateUtil;


//has full control over each type of vaadin communication, including websockets
public class InterceptorServlet extends VaadinServlet
{
	private static final long	serialVersionUID	= 2973107786947933744L;
	
	
	@Override
	protected VaadinServletService createServletService(
			DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		VaadinServletService servletService = new VaadinServletService(this,deploymentConfiguration)
		{
			private static final long	serialVersionUID				= -7847744792732696004L;
			private static final String	HIBERNATEUTIL_FILTER_INIT_PARAM	= "hibernateUtil";
			
			
			@Override
			public void requestStart(VaadinRequest request, VaadinResponse response)
			{
				if(request.getMethod().equals("POST"))
				{
					String hibernatePersistenceUnit = deploymentConfiguration
							.getApplicationOrSystemProperty(HIBERNATEUTIL_FILTER_INIT_PARAM,"");
					EntityManagerHelper
							.initializeHibernateFactory(new HibernateUtil.Implementation(
									hibernatePersistenceUnit));
					EntityManager em = EntityManagerHelper.getEntityManagerFactory()
							.createEntityManager();
					
					// Add the EntityManager to the request
					request.setAttribute("HibernateEntityManager",em);
				}
				super.requestStart(request,response);
			}
			
			
			@Override
			public void requestEnd(VaadinRequest request, VaadinResponse response,
					VaadinSession session)
			{
				try
				{
					EntityManagerHelper.closeEntityManager();
				}
				catch(Exception e)
				{
					if(EntityManagerHelper.getEntityManager() != null)
					{
						EntityTransaction tx = EntityManagerHelper.getTransaction();
						if(tx != null && tx.isActive())
							EntityManagerHelper.rollback();
					}
				}
				
				super.requestEnd(request,response,session);
			}
		};
		servletService.init();
		return servletService;
	}
}
