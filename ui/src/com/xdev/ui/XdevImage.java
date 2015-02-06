/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.server.Resource;
import com.vaadin.ui.Image;


/**
 * Component for embedding images.
 *
 * @author XDEV Software
 *
 */
public class XdevImage extends Image
{
	/**
	 * Creates a new empty Image.
	 */
	public XdevImage()
	{
		super();
	}
	
	
	/**
	 * Creates a new Image whose contents is loaded from given resource. The
	 * dimensions are assumed if possible. The type is guessed from resource.
	 * 
	 * @param caption
	 * @param source
	 *            the Source of the embedded object.
	 */
	public XdevImage(final String caption, final Resource source)
	{
		super(caption,source);
	}
	
	
	/**
	 * Creates a new empty Image with caption.
	 * 
	 * @param caption
	 */
	public XdevImage(final String caption)
	{
		super(caption);
	}
}
