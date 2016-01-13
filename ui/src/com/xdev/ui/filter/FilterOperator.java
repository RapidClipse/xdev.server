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


import java.util.Date;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.xdev.data.util.filter.Compare;
import com.xdev.res.StringResourceUtils;


/**
 * @author XDEV Software
 *		
 */
public interface FilterOperator
{
	public String getKey();


	public String getName();


	public boolean isPropertyTypeSupported(final Class<?> type);


	public FilterField<?>[] createValueEditors(FilterContext context, Class<?> propertyType);


	public Filter createFilter(FilterContext context, FilterField<?>[] editors);
	
	
	
	public static abstract class Abstract implements FilterOperator
	{
		protected final String	key;
		protected final String	name;


		Abstract(final String key)
		{
			this(key,StringResourceUtils.getResourceString(
					"ContainerFilterComponent.Operator." + key,FilterOperator.class));
		}


		protected Abstract(final String key, final String name)
		{
			this.key = key;
			this.name = name;
		}


		@Override
		public String getKey()
		{
			return this.key;
		}


		@Override
		public String getName()
		{
			return this.name;
		}


		protected boolean isNumberOrDate(final Class<?> type)
		{
			return isNumber(type) || isDate(type);
		}


		protected boolean isNumber(final Class<?> type)
		{
			return Number.class.isAssignableFrom(type) || type == int.class || type == double.class
					|| type == float.class || type == long.class || type == short.class
					|| type == byte.class;
		}


		protected boolean isDate(final Class<?> type)
		{
			return Date.class.isAssignableFrom(type);
		}


		protected boolean isBoolean(final Class<?> type)
		{
			return type == Boolean.class || type == boolean.class;
		}
		
		
		protected FilterField<?> createStringField()
		{
			return new TextFilterField();
		}


		protected FilterField<?> createNumberField(final Class<?> numberType)
		{
			return new TextFilterField(numberType);
		}


		protected FilterField<?> createDateField(final Class<?> dateType)
		{
			return new DateFilterField();
		}


		protected FilterField<?> createBooleanField()
		{
			return new BooleanFilterField();
		}


		@SuppressWarnings({"rawtypes","unchecked"})
		protected FilterField<?> createChoiceField(final FilterContext context,
				final Class<?> propertyType)
		{
			return new ChoiceFilterField(context,propertyType);
		}
	}



	public static class Equals extends Abstract
	{
		public final static String KEY = "EQUALS";


		public Equals()
		{
			super(KEY);
		}


		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return type == String.class;
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			return new FilterField<?>[]{createStringField()};
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			String value = (String)editors[0].getFilterValue();
			if(value == null)
			{
				return null;
			}

			value = value.trim();
			if(value.length() == 0)
			{
				return null;
			}

			return SearchFilterGenerator.Default.createWordFilter(value,context.getPropertyId(),
					context.getSettings());
		}
	}



	public static class StartsWith extends Abstract
	{
		public final static String KEY = "STARTS_WITH";


		public StartsWith()
		{
			super(KEY);
		}
		
		
		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return type == String.class;
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			return new FilterField<?>[]{createStringField()};
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			String value = (String)editors[0].getFilterValue();
			if(value == null)
			{
				return null;
			}

			value = value.trim();
			if(value.length() == 0)
			{
				return null;
			}

			return new SimpleStringFilter(context.getPropertyId(),value,
					!context.getSettings().isCaseSensitive(),true);
		}
	}



	public static class Is extends Abstract
	{
		public final static String KEY = "IS";


		public Is()
		{
			this(KEY);
		}


		protected Is(final String key)
		{
			super(key);
		}


		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return type != String.class;
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			final FilterField<?> editor;
			if(isNumber(propertyType))
			{
				editor = createNumberField(propertyType);
			}
			else if(isDate(propertyType))
			{
				editor = createDateField(propertyType);
			}
			else if(isBoolean(propertyType))
			{
				editor = createBooleanField();
			}
			else
			{
				editor = createChoiceField(context,propertyType);
			}

			return new FilterField<?>[]{editor};
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			final Object value = editors[0].getFilterValue();
			if(value == null)
			{
				return null;
			}

			return new Compare.Equal(context.getPropertyId(),value);
		}
	}



	public static class IsNot extends Is
	{
		public final static String KEY = "IS_NOT";


		public IsNot()
		{
			super(KEY);
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			final Filter filter = super.createFilter(context,editors);
			return filter != null ? new Not(filter) : null;
		}
	}



	public static class Greater extends Abstract
	{
		public final static String KEY = "GREATER";


		public Greater()
		{
			super(KEY);
		}


		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return isNumberOrDate(type);
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			final FilterField<?> editor = isNumber(propertyType) ? createNumberField(propertyType)
					: createDateField(propertyType);
			return new FilterField[]{editor};
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			final Object value = editors[0].getFilterValue();
			if(value == null)
			{
				return null;
			}

			return new Compare.Greater(context.getPropertyId(),value);
		}
	}



	public static class Less extends Abstract
	{
		public final static String KEY = "LESS";


		public Less()
		{
			super(KEY);
		}


		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return isNumberOrDate(type);
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			final FilterField<?> editor = isNumber(propertyType) ? createNumberField(propertyType)
					: createDateField(propertyType);
			return new FilterField<?>[]{editor};
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			final Object value = editors[0].getFilterValue();
			if(value == null)
			{
				return null;
			}

			return new Compare.Less(context.getPropertyId(),value);
		}
	}



	public static class GreaterEqual extends Abstract
	{
		public final static String KEY = "GREATER_EQUAL";


		public GreaterEqual()
		{
			super(KEY);
		}


		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return isNumberOrDate(type);
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			final FilterField<?> editor = isNumber(propertyType) ? createNumberField(propertyType)
					: createDateField(propertyType);
			return new FilterField<?>[]{editor};
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			final Object value = editors[0].getFilterValue();
			if(value == null)
			{
				return null;
			}

			return new Compare.GreaterOrEqual(context.getPropertyId(),value);
		}
	}



	public static class LessEqual extends Abstract
	{
		public final static String KEY = "LESS_EQUAL";


		public LessEqual()
		{
			super(KEY);
		}


		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return isNumberOrDate(type);
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			final FilterField<?> editor = isNumber(propertyType) ? createNumberField(propertyType)
					: createDateField(propertyType);
			return new FilterField<?>[]{editor};
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			final Object value = editors[0].getFilterValue();
			if(value == null)
			{
				return null;
			}

			return new Compare.LessOrEqual(context.getPropertyId(),value);
		}
	}



	public static class Between extends Abstract
	{
		public final static String KEY = "BETWEEN";


		public Between()
		{
			super(KEY);
		}


		@Override
		public boolean isPropertyTypeSupported(final Class<?> type)
		{
			return isNumberOrDate(type);
		}


		@Override
		public FilterField<?>[] createValueEditors(final FilterContext context,
				final Class<?> propertyType)
		{
			final FilterField<?> from = createValueEditor(propertyType);
			final FilterField<?> to = createValueEditor(propertyType);
			return new FilterField<?>[]{from,to};
		}


		protected FilterField<?> createValueEditor(final Class<?> propertyType)
		{
			return isNumber(propertyType) ? createNumberField(propertyType)
					: createDateField(propertyType);
		}


		@Override
		public Filter createFilter(final FilterContext context, final FilterField<?>[] editors)
		{
			final Comparable<?> startValue = (Comparable<?>)editors[0].getFilterValue();
			if(startValue == null)
			{
				return null;
			}

			final Comparable<?> endValue = (Comparable<?>)editors[1].getFilterValue();
			if(endValue == null)
			{
				return null;
			}

			return new com.vaadin.data.util.filter.Between(context.getPropertyId(),startValue,
					endValue);
		}
	}
}
