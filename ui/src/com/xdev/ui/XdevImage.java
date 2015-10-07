/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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
