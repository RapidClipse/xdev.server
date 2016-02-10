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
 */

package com.xdev.security.authorization.ui;


import com.vaadin.ui.HasComponents;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.xdev.security.authentication.ui.Authentication;
import com.xdev.security.authorization.AuthorizationManager;
import com.xdev.security.authorization.Resource;
import com.xdev.security.authorization.Subject;
import com.xdev.ui.XdevComponent;


/**
 * Utility class for authorization purposes.
 *
 * @author XDEV Software
 *		
 */
public final class Authorization
{
	private Authorization()
	{
	}


	public static void setAuthorizationManager(final AuthorizationManager authorizationManager)
	{
		UI.getCurrent().getSession().setAttribute(AuthorizationManager.class,authorizationManager);
	}


	public static AuthorizationManager getAuthorizationManager()
	{
		return UI.getCurrent().getSession().getAttribute(AuthorizationManager.class);
	}


	public static Resource getResource(final String name)
	{
		final AuthorizationManager authorizationManager = getAuthorizationManager();
		if(authorizationManager == null)
		{
			throw new IllegalStateException("No authorization manager has been initialized");
		}
		final Resource resource = authorizationManager.resource(name);
		if(resource == null)
		{
			throw new IllegalArgumentException("Resource not found: " + name);
		}
		return resource;
	}


	public static void setSubjectEvaluatingComponentExtension(final XdevComponent component,
			final SubjectEvaluatingComponentExtension extension)
	{
		component.addExtension(SubjectEvaluatingComponentExtension.class,extension);
	}


	public static void evaluateComponents(final XdevComponent root)
	{
		if(Authentication.isUserLoggedIn())
		{
			evaluateComponents(root,Authentication.getUser());
		}
	}


	public static void evaluateComponents(final XdevComponent root, final Subject subject)
	{
		evaluateComponent(root,subject);

		Iterable<?> children = null;
		if(root instanceof HasComponents)
		{
			children = (HasComponents)root;
		}
		else if(root instanceof MenuBar)
		{
			children = ((MenuBar)root).getItems();
		}
		else if(root instanceof MenuItem)
		{
			children = ((MenuItem)root).getChildren();
		}
		if(children != null)
		{
			for(final Object child : children)
			{
				if(child instanceof XdevComponent)
				{
					evaluateComponents((XdevComponent)child,subject);
				}
			}
		}
	}


	public static void evaluateComponent(final XdevComponent component, final Subject subject)
	{
		final SubjectEvaluatingComponentExtension extension = component
				.getExtension(SubjectEvaluatingComponentExtension.class);
		if(extension != null)
		{
			extension.evaluateSubject(component,subject);
		}
	}
}
