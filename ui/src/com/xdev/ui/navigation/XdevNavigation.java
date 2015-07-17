
package com.xdev.ui.navigation;


import java.util.Collection;

import com.vaadin.ui.UI;
import com.xdev.communication.URLParameterRegistry;
import com.xdev.communication.URLParameterRegistryValue;
import com.xdev.dal.DAOs;


public class XdevNavigation implements NavigationDefinition
{
	private final URLParameterRegistry registry = UI.getCurrent().getSession()
			.getAttribute(URLParameterRegistry.class);
			
	private String viewName;
	
	
	public String getViewName()
	{
		if(this.viewName != null)
		{
			return this.viewName;
		}
		throw new RuntimeException("No View set");
	}
	
	
	@Override
	public NavigationDefinition to(final String viewName)
	{
		this.viewName = viewName;
		
		return this;
	}
	
	
	@Override
	public NavigationDefinition parameter(final String parameterName, final Object parameterValue)
	{
		this.registry.put(parameterValue,this.getViewName(),parameterName);
		return this;
	}
	
	
	@Override
	public void navigate()
	{
		final Collection<URLParameterRegistryValue> values = this.registry
				.getValues(this.getViewName());
				
		String navigationURL = this.getViewName();
		for(final URLParameterRegistryValue urlParameterRegistryValue : values)
		{
			navigationURL += "/" + urlParameterRegistryValue.getPropertyName();
		}
		
		UI.getCurrent().getNavigator().navigateTo(navigationURL);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getParameter(final String URL, final String entityName, final Class<T> type)
	{
		/*
		 * Prüfung: Beinhaltet die URL id (=entityname) um navigation (forward,
		 * backwards) in der selben view zu ermöglichen?
		 */
		if(URL.contains(entityName))
		{
			final URLParameterRegistryValue value = this.registry.get(this.getViewName(),
					entityName);
			if(value != null)
			{
				if(value.getType().isAssignableFrom(type))
				{
					if(value.getPersistent_ID() != null)
					{
						return (T)DAOs.get(type).find(value.getPersistent_ID());
					}
					else
					{
						return (T)value.getEntity();
					}
				}
			}
		}
		throw new RuntimeException("Incompatible navigation process");
	}
	
}
