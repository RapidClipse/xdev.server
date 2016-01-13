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

package com.xdev.data.util.filter;


import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.QueryBuilder;
import com.xdev.data.util.sqlcontainer.query.generator.filter.CompareTranslator;


/**
 * Simple container filter comparing an item property value against a given
 * constant value. Use the nested classes {@link Equal}, {@link Greater},
 * {@link Less}, {@link GreaterOrEqual} and {@link LessOrEqual} instead of this
 * class directly.
 * <p>
 * This filter also directly supports in-memory filtering.
 * <p>
 * The reference and actual values must implement {@link Comparable} and the
 * class of the actual property value must be assignable from the class of the
 * reference value.
 * <p>
 * Necessary fork of Vaadin's {@link com.vaadin.data.util.filter.Compare} which
 * can't handle java.util.Date vs java.sql.Date/Time/Timestamp.
 *
 * @see CompareTranslator
 */
public abstract class Compare implements Filter
{
	static
	{
		QueryBuilder.addFilterTranslator(new CompareTranslator());
	}



	public enum Operation
	{
		EQUAL, GREATER, LESS, GREATER_OR_EQUAL, LESS_OR_EQUAL
	}

	private final Object	propertyId;
	private final Operation	operation;
	private final Object	value;



	/**
	 * A {@link Compare} filter that accepts items for which the identified
	 * property value is equal to <code>value</code>.
	 *
	 * For in-memory filters, {@link Comparable#compareTo(Object)} or, if not
	 * Comparable, {@link #equals(Object)} is used for the comparison. For other
	 * containers, the comparison implementation is container dependent and may
	 * use e.g. database comparison operations.
	 *
	 *
	 */
	public static final class Equal extends Compare
	{
		/**
		 * Construct a filter that accepts items for which the identified
		 * property value is equal to <code>value</code>.
		 *
		 * For in-memory filters, equals() is used for the comparison. For other
		 * containers, the comparison implementation is container dependent and
		 * may use e.g. database comparison operations.
		 *
		 * @param propertyId
		 *            the identifier of the property whose value to compare
		 *            against value, not null
		 * @param value
		 *            the value to compare against - null values may or may not
		 *            be supported depending on the container
		 */
		public Equal(final Object propertyId, final Object value)
		{
			super(propertyId,value,Operation.EQUAL);
		}
	}



	/**
	 * A {@link Compare} filter that accepts items for which the identified
	 * property value is greater than <code>value</code>.
	 *
	 * For in-memory filters, the values must implement {@link Comparable} and
	 * {@link Comparable#compareTo(Object)} is used for the comparison. For
	 * other containers, the comparison implementation is container dependent
	 * and may use e.g. database comparison operations.
	 *
	 *
	 */
	public static final class Greater extends Compare
	{
		/**
		 * Construct a filter that accepts items for which the identified
		 * property value is greater than <code>value</code>.
		 *
		 * For in-memory filters, the values must implement {@link Comparable}
		 * and {@link Comparable#compareTo(Object)} is used for the comparison.
		 * For other containers, the comparison implementation is container
		 * dependent and may use e.g. database comparison operations.
		 *
		 * @param propertyId
		 *            the identifier of the property whose value to compare
		 *            against value, not null
		 * @param value
		 *            the value to compare against - null values may or may not
		 *            be supported depending on the container
		 */
		public Greater(final Object propertyId, final Object value)
		{
			super(propertyId,value,Operation.GREATER);
		}
	}



	/**
	 * A {@link Compare} filter that accepts items for which the identified
	 * property value is less than <code>value</code>.
	 *
	 * For in-memory filters, the values must implement {@link Comparable} and
	 * {@link Comparable#compareTo(Object)} is used for the comparison. For
	 * other containers, the comparison implementation is container dependent
	 * and may use e.g. database comparison operations.
	 *
	 *
	 */
	public static final class Less extends Compare
	{
		/**
		 * Construct a filter that accepts items for which the identified
		 * property value is less than <code>value</code>.
		 *
		 * For in-memory filters, the values must implement {@link Comparable}
		 * and {@link Comparable#compareTo(Object)} is used for the comparison.
		 * For other containers, the comparison implementation is container
		 * dependent and may use e.g. database comparison operations.
		 *
		 * @param propertyId
		 *            the identifier of the property whose value to compare
		 *            against value, not null
		 * @param value
		 *            the value to compare against - null values may or may not
		 *            be supported depending on the container
		 */
		public Less(final Object propertyId, final Object value)
		{
			super(propertyId,value,Operation.LESS);
		}
	}



	/**
	 * A {@link Compare} filter that accepts items for which the identified
	 * property value is greater than or equal to <code>value</code>.
	 *
	 * For in-memory filters, the values must implement {@link Comparable} and
	 * {@link Comparable#compareTo(Object)} is used for the comparison. For
	 * other containers, the comparison implementation is container dependent
	 * and may use e.g. database comparison operations.
	 *
	 *
	 */
	public static final class GreaterOrEqual extends Compare
	{
		/**
		 * Construct a filter that accepts items for which the identified
		 * property value is greater than or equal to <code>value</code>.
		 *
		 * For in-memory filters, the values must implement {@link Comparable}
		 * and {@link Comparable#compareTo(Object)} is used for the comparison.
		 * For other containers, the comparison implementation is container
		 * dependent and may use e.g. database comparison operations.
		 *
		 * @param propertyId
		 *            the identifier of the property whose value to compare
		 *            against value, not null
		 * @param value
		 *            the value to compare against - null values may or may not
		 *            be supported depending on the container
		 */
		public GreaterOrEqual(final Object propertyId, final Object value)
		{
			super(propertyId,value,Operation.GREATER_OR_EQUAL);
		}
	}



	/**
	 * A {@link Compare} filter that accepts items for which the identified
	 * property value is less than or equal to <code>value</code>.
	 *
	 * For in-memory filters, the values must implement {@link Comparable} and
	 * {@link Comparable#compareTo(Object)} is used for the comparison. For
	 * other containers, the comparison implementation is container dependent
	 * and may use e.g. database comparison operations.
	 *
	 *
	 */
	public static final class LessOrEqual extends Compare
	{
		/**
		 * Construct a filter that accepts items for which the identified
		 * property value is less than or equal to <code>value</code>.
		 *
		 * For in-memory filters, the values must implement {@link Comparable}
		 * and {@link Comparable#compareTo(Object)} is used for the comparison.
		 * For other containers, the comparison implementation is container
		 * dependent and may use e.g. database comparison operations.
		 *
		 * @param propertyId
		 *            the identifier of the property whose value to compare
		 *            against value, not null
		 * @param value
		 *            the value to compare against - null values may or may not
		 *            be supported depending on the container
		 */
		public LessOrEqual(final Object propertyId, final Object value)
		{
			super(propertyId,value,Operation.LESS_OR_EQUAL);
		}
	}


	/**
	 * Constructor for a {@link Compare} filter that compares the value of an
	 * item property with the given constant <code>value</code>.
	 *
	 * This constructor is intended to be used by the nested static classes only
	 * ({@link Equal}, {@link Greater}, {@link Less}, {@link GreaterOrEqual},
	 * {@link LessOrEqual}).
	 *
	 * For in-memory filtering, comparisons except EQUAL require that the values
	 * implement {@link Comparable} and {@link Comparable#compareTo(Object)} is
	 * used for the comparison. The equality comparison is performed using
	 * {@link Object#equals(Object)}.
	 *
	 * For other containers, the comparison implementation is container
	 * dependent and may use e.g. database comparison operations. Therefore, the
	 * behavior of comparisons might differ in some cases between in-memory and
	 * other containers.
	 *
	 * @param propertyId
	 *            the identifier of the property whose value to compare against
	 *            value, not null
	 * @param value
	 *            the value to compare against - null values may or may not be
	 *            supported depending on the container
	 * @param operation
	 *            the comparison {@link Operation} to use
	 */
	Compare(final Object propertyId, final Object value, final Operation operation)
	{
		this.propertyId = propertyId;
		this.value = value;
		this.operation = operation;
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
		switch(getOperation())
		{
			case EQUAL:
				return compareEquals(value);
			case GREATER:
				return compareValue(value) > 0;
			case LESS:
				return compareValue(value) < 0;
			case GREATER_OR_EQUAL:
				return compareValue(value) >= 0;
			case LESS_OR_EQUAL:
				return compareValue(value) <= 0;
		}
		// all cases should have been processed above
		return false;
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
	@SuppressWarnings({"rawtypes","unchecked"})
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
		else if(otherValue instanceof Comparable
				&& getValue().getClass().isAssignableFrom(otherValue.getClass()))
		{
			return ((Comparable)otherValue).compareTo(this.value) == 0;
		}
		else
		{
			return this.value.equals(otherValue);
		}
	}


	@SuppressWarnings({"unchecked","rawtypes"})
	protected int compareValue(final Object value1)
	{
		if(null == this.value)
		{
			return null == value1 ? 0 : -1;
		}
		else if(null == value1)
		{
			return 1;
		}
		else if(getValue() instanceof Comparable
				&& value1.getClass().isAssignableFrom(getValue().getClass()))
		{
			return -((Comparable)getValue()).compareTo(value1);
		}
		else if(value1 instanceof Comparable
				&& getValue().getClass().isAssignableFrom(value1.getClass()))
		{
			return ((Comparable)value1).compareTo(getValue());
		}
		throw new IllegalArgumentException(
				"Could not compare the arguments: " + value1 + ", " + getValue());
	}


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
		final Compare o = (Compare)obj;

		// Checks the properties one by one
		if(getPropertyId() != o.getPropertyId() && null != o.getPropertyId()
				&& !o.getPropertyId().equals(getPropertyId()))
		{
			return false;
		}
		if(getOperation() != o.getOperation())
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
	 * Returns the property id of the property to compare against the fixed
	 * value.
	 *
	 * @return property id (not null)
	 */
	public Object getPropertyId()
	{
		return this.propertyId;
	}


	/**
	 * Returns the comparison operation.
	 *
	 * @return {@link Operation}
	 */
	public Operation getOperation()
	{
		return this.operation;
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
