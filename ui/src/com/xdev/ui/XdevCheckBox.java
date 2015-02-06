/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;


/**
 * An implementation of a check box -- an item that can be selected or
 * deselected, and which displays its state to the user.
 *
 * @author XDEV Software
 *
 */
public class XdevCheckBox extends CheckBox
{
	
	/**
	 * Creates a new checkbox.
	 */
	public XdevCheckBox()
	{
		super();
	}
	
	
	/**
	 * Creates a new checkbox with a caption and a set initial state.
	 * 
	 * @param caption
	 *            the caption of the checkbox
	 * @param initialState
	 *            the initial state of the checkbox
	 */
	public XdevCheckBox(final String caption, final boolean initialState)
	{
		super(caption,initialState);
	}
	
	
	/**
	 * Creates a new checkbox that is connected to a boolean property.
	 * 
	 * @param state
	 *            the Initial state of the switch-button.
	 * @param dataSource
	 */
	public XdevCheckBox(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Creates a new checkbox with a set caption.
	 * 
	 * @param caption
	 *            the Checkbox caption.
	 */
	public XdevCheckBox(final String caption)
	{
		super(caption);
	}
}
