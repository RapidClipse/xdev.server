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

package com.xdev.communication;


/**
 * @author XDEV Software Julian Will
 *		
 */
public class URLParameterDescriptor
{
	private final Class<?>	parameterType;
	private final String	propertyName;
	
	
	/**
	 *
	 */
	public URLParameterDescriptor(final Class<?> parameterType, final String propertyName)
	{
		this.propertyName = propertyName;
		this.parameterType = parameterType;
	}
	
	
	public String getViewPropertyName()
	{
		return this.propertyName;
	}
	
	
	public Class<?> getParameterType()
	{
		return this.parameterType;
	}
}
