/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui;


import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;


/**
 * FormLayout is a close relative of {@link VerticalLayout}, but in FormLayout
 * captions are rendered to the left of their respective components. Required
 * and validation indicators are shown between the captions and the fields.
 *
 * FormLayout by default has component spacing on. Also margin top and margin
 * bottom are by default on.
 *
 * @author XDEV Software
 *
 */
public class XdevFormLayout extends FormLayout
{
	/**
	 * Constructs an empty FormLayout.
	 */
	public XdevFormLayout()
	{
		super();
	}
	
	
	/**
	 * Constructs a FormLayout and adds the given components to it.
	 *
	 * @see AbstractOrderedLayout#addComponents(Component...)
	 *
	 * @param children
	 *            Components to add to the FormLayout
	 */
	public XdevFormLayout(final Component... children)
	{
		super(children);
	}

	// init defaults
	{
		setMargin(true);
		setSpacing(true);
	}
}
