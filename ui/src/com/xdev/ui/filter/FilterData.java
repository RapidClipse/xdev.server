/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.filter;


/**
 * @author XDEV Software
 *
 */
public class FilterData
{
	private Object			propertyId;
	private FilterOperator	operator;
	private Object[]		values;


	public FilterData()
	{
	}


	public FilterData(final Object propertyId, final FilterOperator operator, final Object[] values)
	{
		this.propertyId = propertyId;
		this.operator = operator;
		this.values = values;
	}


	public void setPropertyId(final Object propertyId)
	{
		this.propertyId = propertyId;
	}


	public Object getPropertyId()
	{
		return this.propertyId;
	}


	public void setOperator(final FilterOperator operator)
	{
		this.operator = operator;
	}


	public FilterOperator getOperator()
	{
		return this.operator;
	}


	public void setValues(final Object[] values)
	{
		this.values = values;
	}


	public Object[] getValues()
	{
		return this.values;
	}
}
