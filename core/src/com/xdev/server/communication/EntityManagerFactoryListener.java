
package com.xdev.server.communication;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * 
 * @author jwill
 *
 */
public class EntityManagerFactoryListener implements ServletContextListener
{
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		// nothing to do here
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		EntityManagerHelper.closeEntityManagerFactory();
	}
	
}
