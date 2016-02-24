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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.data.util.filter;


import java.util.Objects;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.xdev.util.CaptionUtils;


/**
 * @author XDEV Software
 *		
 */
public class CaptionStringFilter implements Filter
{
	private final Object	propertyId;
	private final String	filterString;
	private final boolean	ignoreCase;
	private final boolean	onlyMatchPrefix;
	private final String	itemCaptionValue;
	private int				hash	= 0;


	public CaptionStringFilter(final Object propertyId, final String filterString,
			final boolean ignoreCase, final boolean onlyMatchPrefix, final String itemCaptionValue)
	{
		this.propertyId = propertyId;
		this.filterString = ignoreCase ? filterString.toLowerCase() : filterString;
		this.ignoreCase = ignoreCase;
		this.onlyMatchPrefix = onlyMatchPrefix;
		this.itemCaptionValue = itemCaptionValue;
	}


	@Override
	public boolean passesFilter(final Object itemId, final Item item)
	{
		final String value = getValue(item);
		if(value == null)
		{
			return false;
		}

		if(this.onlyMatchPrefix)
		{
			if(!value.startsWith(this.filterString))
			{
				return false;
			}
		}
		else
		{
			if(!value.contains(this.filterString))
			{
				return false;
			}
		}
		return true;
	}


	private String getValue(final Item item)
	{
		String value = null;
		if(item instanceof BeanItem<?>)
		{
			final Object bean = ((BeanItem<?>)item).getBean();
			if(this.itemCaptionValue != null)
			{
				value = CaptionUtils.resolveCaption(bean,this.itemCaptionValue);
			}
			else if(CaptionUtils.hasCaptionAnnotationValue(bean.getClass()))
			{
				value = CaptionUtils.resolveCaption(bean);
			}
		}

		if(value == null)
		{
			final Property<?> p = item.getItemProperty(this.propertyId);
			if(p != null)
			{
				final Object propertyValue = p.getValue();
				if(propertyValue != null)
				{
					value = propertyValue.toString();
				}
			}
		}

		if(value == null)
		{
			return null;
		}

		return this.ignoreCase ? value.toLowerCase() : value;
	}


	@Override
	public boolean appliesToProperty(final Object propertyId)
	{
		return this.propertyId.equals(propertyId);
	}


	@Override
	public boolean equals(final Object obj)
	{
		if(obj == null)
		{
			return false;
		}

		// Only ones of the objects of the same class can be equal
		if(!(obj instanceof CaptionStringFilter))
		{
			return false;
		}
		final CaptionStringFilter other = (CaptionStringFilter)obj;

		// Checks the properties one by one
		if(this.propertyId != other.propertyId && other.propertyId != null
				&& !other.propertyId.equals(this.propertyId))
		{
			return false;
		}
		if(this.filterString != other.filterString && other.filterString != null
				&& !other.filterString.equals(this.filterString))
		{
			return false;
		}
		if(this.ignoreCase != other.ignoreCase)
		{
			return false;
		}
		if(this.onlyMatchPrefix != other.onlyMatchPrefix)
		{
			return false;
		}
		if(this.itemCaptionValue != other.itemCaptionValue && other.itemCaptionValue != null
				&& !other.itemCaptionValue.equals(this.itemCaptionValue))
		{
			return false;
		}

		return true;
	}


	@Override
	public int hashCode()
	{
		if(this.hash == 0)
		{
			this.hash = Objects.hash(this.propertyId,this.filterString,this.ignoreCase,
					this.onlyMatchPrefix,this.itemCaptionValue);
		}
		return this.hash;
	}


	/**
	 * Returns the property identifier to which this filter applies.
	 *
	 * @return property id
	 */
	public Object getPropertyId()
	{
		return this.propertyId;
	}


	/**
	 * Returns the filter string.
	 *
	 * Note: this method is intended only for implementations of lazy string
	 * filters and may change in the future.
	 *
	 * @return filter string given to the constructor
	 */
	public String getFilterString()
	{
		return this.filterString;
	}


	/**
	 * Returns whether the filter is case-insensitive or case-sensitive.
	 *
	 * Note: this method is intended only for implementations of lazy string
	 * filters and may change in the future.
	 *
	 * @return true if performing case-insensitive filtering, false for
	 *         case-sensitive
	 */
	public boolean isIgnoreCase()
	{
		return this.ignoreCase;
	}


	/**
	 * Returns true if the filter only applies to the beginning of the value
	 * string, false for any location in the value.
	 *
	 * Note: this method is intended only for implementations of lazy string
	 * filters and may change in the future.
	 *
	 * @return true if checking for matches at the beginning of the value only,
	 *         false if matching any part of value
	 */
	public boolean isOnlyMatchPrefix()
	{
		return this.onlyMatchPrefix;
	}
}
