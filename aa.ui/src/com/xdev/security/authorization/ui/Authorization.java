/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


	/**
	 * Registers an {@link AuthorizationManager} in the current user session.
	 *
	 * @param authorizationManager
	 *            the {@link AuthorizationManager} to register
	 * @see #getAuthorizationManager()
	 */
	public static void setAuthorizationManager(final AuthorizationManager authorizationManager)
	{
		UI.getCurrent().getSession().setAttribute(AuthorizationManager.class,authorizationManager);
	}


	/**
	 * Returns the {@link AuthorizationManager} of the current user session.
	 *
	 * @return the {@link AuthorizationManager}
	 * @see #setAuthorizationManager(AuthorizationManager)
	 */
	public static AuthorizationManager getAuthorizationManager()
	{
		return UI.getCurrent().getSession().getAttribute(AuthorizationManager.class);
	}


	/**
	 * Searches for a specific resource in the current {@link AuthorizationManager}.
	 * If no resource is found an {@link IllegalArgumentException} is thrown.
	 *
	 * @param name
	 *            the resource's name to search for
	 * @return the resource
	 * @throws IllegalStateException
	 *             if no authorization manager has been initialized
	 * @throws IllegalArgumentException
	 *             if the resources couldn't be found
	 * @see #resource(String)
	 */
	public static Resource getResource(final String name)
			throws IllegalStateException, IllegalArgumentException
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


	/**
	 * Searches for a specific resource with {@link #getResource(String)}. If no
	 * resource with the specific name is present a new one will be created and
	 * returned.
	 *
	 * @param name
	 *            the resource's name
	 * @return the found or created resource
	 * @see #getResource(String)
	 */
	public static Resource resource(final String name)
	{
		try
		{
			return getResource(name);
		}
		catch(IllegalStateException | IllegalArgumentException e)
		{
			return Resource.New(name);
		}
	}


	/**
	 * Registers a {@link SubjectEvaluatingComponentExtension} with
	 * <code>component</code>.
	 *
	 * @param component
	 *            the component to register the extension with
	 * @param extension
	 *            the extension to register
	 * @see #evaluateComponents(XdevComponent)
	 */
	public static void setSubjectEvaluatingComponentExtension(final XdevComponent component,
			final SubjectEvaluatingComponentExtension extension)
	{
		component.addExtension(SubjectEvaluatingComponentExtension.class,extension);
	}


	/**
	 * Evaluates all {@link SubjectEvaluatingComponentExtension}s in the component
	 * hierarchy of <code>root</code> against the current user.
	 *
	 * @param root
	 *            the root component
	 * @see Authentication#isUserLoggedIn()
	 * @see Authentication#login(Subject, Object)
	 * @see Authentication#setUser(Subject, Object)
	 * @see Authentication#getUser()
	 * @see #setSubjectEvaluatingComponentExtension(XdevComponent,
	 *      SubjectEvaluatingComponentExtension)
	 */
	public static void evaluateComponents(final XdevComponent root)
	{
		if(Authentication.isUserLoggedIn())
		{
			evaluateComponents(root,Authentication.getUser());
		}
	}


	/**
	 * Evaluates all {@link SubjectEvaluatingComponentExtension}s in the component
	 * hierarchy of <code>root</code> against the given <code>subject</code>.
	 *
	 * @param root
	 *            the root component
	 * @param subject
	 *            the subject to evaluate against
	 * @see #evaluateComponents(XdevComponent)
	 * @see #setSubjectEvaluatingComponentExtension(XdevComponent,
	 *      SubjectEvaluatingComponentExtension)
	 */
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


	/**
	 * Evaluates the {@link SubjectEvaluatingComponentExtension} of the
	 * <code>component</code> against the given <code>subject</code>.
	 * <p>
	 * If no {@link SubjectEvaluatingComponentExtension} has been registered with
	 * the <code>component</code> this method has no effect.
	 *
	 * @param component
	 *            the component to check
	 * @param subject
	 *            the subject to evaluate against
	 * @see #setSubjectEvaluatingComponentExtension(XdevComponent,
	 *      SubjectEvaluatingComponentExtension)
	 */
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
