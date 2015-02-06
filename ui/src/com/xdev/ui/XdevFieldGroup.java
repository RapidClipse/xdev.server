/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;


/**
 * FieldGroup provides an easy way of binding fields to data and handling
 * commits of these fields.
 * <p>
 * The typical use case is to create a layout outside the FieldGroup and then
 * use FieldGroup to bind the fields to a data source.
 * </p>
 * <p>
 * {@link FieldGroup} is not a UI component so it cannot be added to a layout.
 * Using the buildAndBind methods {@link FieldGroup} can create fields for you
 * using a FieldGroupFieldFactory but you still have to add them to the correct
 * position in your layout.
 * </p>
 *
 * @author XDEV Software
 *
 */
public class XdevFieldGroup extends FieldGroup
{
	/**
	 * Constructs a field binder. Use {@link #setItemDataSource(Item)} to set a
	 * data source for the field binder.
	 * 
	 */
	public XdevFieldGroup()
	{
		super();
	}
	
	
	/**
	 * Constructs a field binder that uses the given data source.
	 * 
	 * @param itemDataSource
	 *            The data source to bind the fields to
	 */
	public XdevFieldGroup(final Item itemDataSource)
	{
		super(itemDataSource);
	}
}
