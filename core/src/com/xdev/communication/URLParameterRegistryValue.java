/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.communication;


import java.io.Serializable;


/**
 * @author XDEV Software (JW)
 *
 */
public class URLParameterRegistryValue implements Serializable
{
	/**
	 * persistent id stays null if not an persistent entity.
	 */
	private Serializable	persistentID;
							
	private Class<?>		type;
							
	/**
	 * value stays null if persistent entity to avoid performance issues.
	 */
	private Object			value;
							
	private final String	propertyName;
							
							
	/**
	 *
	 */
	public URLParameterRegistryValue(final Class<?> type, final Object value,
			final Serializable persistentID, final String propertyName)
	{
		this.persistentID = persistentID;
		this.type = type;
		this.value = value;
		this.propertyName = propertyName;
	}
	
	
	public String getPropertyName()
	{
		return this.propertyName;
	}
	
	
	public Serializable getPersistentID()
	{
		return this.persistentID;
	}
	
	
	public void setPersistentID(final Serializable persistentID)
	{
		this.persistentID = persistentID;
	}
	
	
	public Class<?> getType()
	{
		return this.type;
	}
	
	
	public void setType(final Class<?> type)
	{
		this.type = type;
	}
	
	
	public Object getValue()
	{
		return this.value;
	}
	
	
	public void setValue(final Object value)
	{
		this.value = value;
	}
}
