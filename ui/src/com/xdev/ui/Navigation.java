/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.ui.Component;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;


public final class Navigation
{
	private Navigation()
	{
	}
	
	
	public static void openInUI(final Component component)
	{
		UI.getCurrent().setContent(component);
	}
	
	
	public static void openInContainer(final SingleComponentContainer container,
			final Component component)
	{
		component.setSizeFull();
		container.setContent(component);
	}
}
