/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.communication;


import java.util.Arrays;


/**
 * @author XDEV Software
 *
 */
public final class URLKeyDescriptor
{
	private final String	viewName;
	private final String	propertyName;
	private final int		hash;


	public URLKeyDescriptor(final String viewName, final String propertyName)
	{
		this.viewName = viewName;
		this.propertyName = propertyName;
		this.hash = Arrays.hashCode(new Object[]{viewName,propertyName});
	}


	public String getPropertyName()
	{
		return this.propertyName;
	}


	public String getViewName()
	{
		return this.viewName;
	}


	@Override
	public int hashCode()
	{
		return this.hash;
	}


	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj)
		{
			return true;
		}

		return obj instanceof URLKeyDescriptor && this.hash == ((URLKeyDescriptor)obj).hash;
	}

}
