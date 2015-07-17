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


import java.io.Serializable;


/**
 * @author XDEV Software
 *		
 */
public class URLParameterRegistryValue
{
	/**
	 * persistent id stays null if not an persistent entity.
	 */
	private Serializable persistent_ID;

	private Class<?> type;

	/**
	 * entity stays null if persistent entity to avoid performance issues.
	 */
	private Object entity;

	private final String propertyName;


	/**
	 *
	 */
	public URLParameterRegistryValue(final Class<?> type, final Object entity,
			final Serializable persistent_ID, final String propertyName)
	{
		this.persistent_ID = persistent_ID;
		this.type = type;
		this.entity = entity;
		this.propertyName = propertyName;
	}


	public String getPropertyName()
	{
		return this.propertyName;
	}
	
	
	public Serializable getPersistent_ID()
	{
		return this.persistent_ID;
	}


	public void setPersistent_ID(final Serializable persistent_ID)
	{
		this.persistent_ID = persistent_ID;
	}


	public Class<?> getType()
	{
		return this.type;
	}


	public void setType(final Class<?> type)
	{
		this.type = type;
	}


	public Object getEntity()
	{
		return this.entity;
	}


	public void setEntity(final Object entity)
	{
		this.entity = entity;
	}
}
