/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.ui.NativeButton;


/**
 * System button component.
 *
 * @author XDEV Software
 *
 */
public class XdevNativeButton extends NativeButton
{
	/**
	 *
	 */
	public XdevNativeButton()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param listener
	 */
	public XdevNativeButton(final String caption, final ClickListener listener)
	{
		super(caption,listener);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevNativeButton(final String caption)
	{
		super(caption);
	}
}
