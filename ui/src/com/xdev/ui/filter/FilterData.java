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

package com.xdev.ui.filter;


import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.ClassUtils;

import com.xdev.dal.DAOs;
import com.xdev.util.EntityIDResolver;
import com.xdev.util.JPAEntityIDResolver;
import com.xdev.util.JPAMetaDataUtils;


/**
 * @author XDEV Software
 *
 */
public class FilterData
{
	protected static class EntityID
	{
		private final Class<?>	entityType;
		private final Object	id;
		
		
		public EntityID(final Class<?> entityType, final Object id)
		{
			this.entityType = entityType;
			this.id = id;
		}


		public Class<?> getEntityType()
		{
			return this.entityType;
		}


		public Object getId()
		{
			return this.id;
		}
	}

	private Object		propertyId;
	private String		operatorKey;
	private Object[]	values;
	
	
	public FilterData()
	{
	}


	public FilterData(final Object propertyId, final FilterOperator operator, final Object[] values)
	{
		setPropertyId(propertyId);
		setOperator(operator);
		setValues(values);
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
		this.operatorKey = operator.getKey();
	}


	public FilterOperator getOperator()
	{
		return FilterOperatorRegistry.getFilterOperators().stream()
				.filter(op -> op.getKey().equals(this.operatorKey)).findAny().orElse(null);
	}


	public void setValues(final Object[] values)
	{
		if(values == null)
		{
			this.values = null;
		}
		else
		{
			final EntityIDResolver idResolver = JPAEntityIDResolver.getInstance();
			this.values = Arrays.stream(values).map(value -> storeFilterValue(value,idResolver))
					.toArray();
		}
	}


	private Object storeFilterValue(final Object value, final EntityIDResolver idResolver)
	{
		if(value == null)
		{
			return null;
		}

		final Class<? extends Object> entityType = value.getClass();
		if(JPAMetaDataUtils.isManaged(entityType))
		{
			return new EntityID(entityType,idResolver.getEntityIDAttributeValue(value));
		}

		return value;
	}


	public Object[] getValues()
	{
		if(this.values == null)
		{
			return null;
		}

		return Arrays.stream(this.values).map(this::resolveFilterValue).toArray();
	}


	protected Object resolveFilterValue(final Object filterValue)
	{
		if(filterValue instanceof EntityID)
		{
			final EntityID entityID = (EntityID)filterValue;
			Serializable id = (Serializable)entityID.getId();
			Class<?> idType = JPAMetaDataUtils.getIdAttribute(entityID.getEntityType())
					.getJavaType();
			// XXX see XWS-1289
			if(idType != null && idType.isPrimitive())
			{
				idType = ClassUtils.primitiveToWrapper(idType);
			}
			if(idType != null && Number.class.isAssignableFrom(idType) && id instanceof Number
					&& !idType.isInstance(id))
			{
				if(Integer.class.isAssignableFrom(idType))
				{
					id = ((Number)id).intValue();
				}
				else if(Long.class.isAssignableFrom(idType))
				{
					id = ((Number)id).longValue();
				}
				else if(Short.class.isAssignableFrom(idType))
				{
					id = ((Number)id).shortValue();
				}
				else if(Byte.class.isAssignableFrom(idType))
				{
					id = ((Number)id).byteValue();
				}
				else if(Double.class.isAssignableFrom(idType))
				{
					id = ((Number)id).doubleValue();
				}
				else if(Float.class.isAssignableFrom(idType))
				{
					id = ((Number)id).floatValue();
				}
			}
			return DAOs.getByEntityType(entityID.getEntityType()).find(id);
		}

		return filterValue;
	}
}
