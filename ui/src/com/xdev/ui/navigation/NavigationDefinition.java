
package com.xdev.ui.navigation;


public interface NavigationDefinition
{
	
	public NavigationDefinition to(String viewName);
	
	
	public NavigationDefinition parameter(String parameterName, Object parameterValue);
	
	
	public void navigate();
	
	
	public <T> T getParameter(String URL, String entityName, Class<T> type);
	
}