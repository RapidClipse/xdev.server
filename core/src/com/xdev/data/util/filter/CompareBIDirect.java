/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


import java.util.Collection;

import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.filter.Compare;
import com.vaadin.v7.data.util.filter.Compare.Greater;
import com.vaadin.v7.data.util.filter.Compare.GreaterOrEqual;
import com.vaadin.v7.data.util.filter.Compare.Less;
import com.vaadin.v7.data.util.filter.Compare.LessOrEqual;


/**
 * Simple container filter comparing an item property value against a given
 * constant value. Use the nested classes {@link Equal}, {@link Greater},
 * {@link Less}, {@link GreaterOrEqual} and {@link LessOrEqual} instead of this
 * class directly.
 *
 * This filter also directly supports in-memory filtering.
 *
 * The reference and actual values must implement {@link Comparable} and the
 * class of the actual property value must be assignable from the class of the
 * reference value.
 * <p>
 *
 * @noapi <strong>For internal use only. This class is subject to change in the
 *        future.</strong>
 */
@SuppressWarnings("deprecation")
public abstract class CompareBIDirect implements Filter
{
	
	private final Object	propertyId;
	private final Object	value;
	
	
	
	/**
	 * A {@link Compare} filter that accepts items for which the identified property
	 * value is equal to <code>value</code>.
	 *
	 * For in-memory filters, {@link Comparable#compareTo(Object)} or, if not
	 * Comparable, {@link #equals(Object)} is used for the comparison. For other
	 * containers, the comparison implementation is container dependent and may use
	 * e.g. database comparison operations.
	 *
	 * @since 6.6
	 */
	public static final class Equal extends CompareBIDirect
	{
		/**
		 * Construct a filter that accepts items for which the identified property value
		 * is equal to <code>value</code>.
		 *
		 * For in-memory filters, equals() is used for the comparison. For other
		 * containers, the comparison implementation is container dependent and may use
		 * e.g. database comparison operations.
		 *
		 * @param propertyId
		 *            the identifier of the property whose value to compare against
		 *            value, not null
		 * @param value
		 *            the value to compare against - null values may or may not be
		 *            supported depending on the container
		 */
		public Equal(final Object propertyId, final Object value)
		{
			super(propertyId,value);
		}
	}
	
	
	/**
	 * Constructor for a {@link Compare} filter that compares the value of an item
	 * property with the given constant <code>value</code>.
	 *
	 * This constructor is intended to be used by the nested static classes only
	 * ({@link Equal}, {@link Greater}, {@link Less}, {@link GreaterOrEqual},
	 * {@link LessOrEqual}).
	 *
	 * For in-memory filtering, comparisons except EQUAL require that the values
	 * implement {@link Comparable} and {@link Comparable#compareTo(Object)} is used
	 * for the comparison. The equality comparison is performed using
	 * {@link Object#equals(Object)}.
	 *
	 * For other containers, the comparison implementation is container dependent
	 * and may use e.g. database comparison operations. Therefore, the behavior of
	 * comparisons might differ in some cases between in-memory and other
	 * containers.
	 *
	 * @param propertyId
	 *            the identifier of the property whose value to compare against
	 *            value, not null
	 * @param value
	 *            the value to compare against - null values may or may not be
	 *            supported depending on the container
	 */
	CompareBIDirect(final Object propertyId, final Object value)
	{
		this.propertyId = propertyId;
		this.value = value;
	}
	
	
	@Override
	public boolean passesFilter(final Object itemId, final Item item)
	{
		final Property<?> p = item.getItemProperty(getPropertyId());
		if(null == p)
		{
			return false;
		}
		final Object value = p.getValue();
		return compareEquals(value);
	}
	
	
	/**
	 * Checks if the this value equals the given value. Favors Comparable over
	 * equals to better support e.g. BigDecimal where equals is stricter than
	 * compareTo.
	 *
	 * @param otherValue
	 *            The value to compare to
	 * @return true if the values are equal, false otherwise
	 */
	@SuppressWarnings({"unchecked","rawtypes"})
	private boolean compareEquals(final Object otherValue)
	{
		if(this.value == null || otherValue == null)
		{
			return(otherValue == this.value);
		}
		else if(this.value == otherValue)
		{
			return true;
		}
		else if(this.value instanceof Comparable
				&& otherValue.getClass().isAssignableFrom(getValue().getClass()))
		{
			return ((Comparable)this.value).compareTo(otherValue) == 0;
		}
		else if(otherValue instanceof Collection)
		{
			final Collection otherValueCollection = (Collection)otherValue;
			final boolean ret = otherValueCollection.contains(this.value);
			return ret;
		}
		else
		{
			return this.value.equals(otherValue);
		}
	}
	
	
	// @SuppressWarnings({"unchecked","rawtypes"})
	// protected int compareValue(final Object value1)
	// {
	// if(null == this.value)
	// {
	// return null == value1 ? 0 : -1;
	// }
	// else if(null == value1)
	// {
	// return 1;
	// }
	// else if(getValue() instanceof Comparable
	// && value1.getClass().isAssignableFrom(getValue().getClass()))
	// {
	// return -((Comparable)getValue()).compareTo(value1);
	// }
	// throw new IllegalArgumentException(
	// "Could not compare the arguments: " + value1 + ", " + getValue());
	// }
	
	@Override
	public boolean appliesToProperty(final Object propertyId)
	{
		return getPropertyId().equals(propertyId);
	}
	
	
	@Override
	public boolean equals(final Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		
		// Only objects of the same class can be equal
		if(!getClass().equals(obj.getClass()))
		{
			return false;
		}
		final CompareBIDirect o = (CompareBIDirect)obj;
		
		// Checks the properties one by one
		if(getPropertyId() != o.getPropertyId() && null != o.getPropertyId()
				&& !o.getPropertyId().equals(getPropertyId()))
		{
			return false;
		}
		return (null == getValue()) ? null == o.getValue() : getValue().equals(o.getValue());
	}
	
	
	@Override
	public int hashCode()
	{
		return (null != getPropertyId() ? getPropertyId().hashCode() : 0)
				^ (null != getValue() ? getValue().hashCode() : 0);
	}
	
	
	/**
	 * Returns the property id of the property to compare against the fixed value.
	 *
	 * @return property id (not null)
	 */
	public Object getPropertyId()
	{
		return this.propertyId;
	}
	
	
	/**
	 * Returns the value to compare the property against.
	 *
	 * @return comparison reference value
	 */
	public Object getValue()
	{
		return this.value;
	}
}
