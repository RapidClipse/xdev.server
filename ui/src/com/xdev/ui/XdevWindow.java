/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;


/**
 * @author XDEV Software
 *
 */
public class XdevWindow extends CustomComponent implements View
{
	/**
	 *
	 */
	public XdevWindow()
	{
		super();
		
		setSizeFull();
	}


	public void setContent(final Component c)
	{
		setCompositionRoot(c);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enter(final ViewChangeEvent event)
	{
	}
}
