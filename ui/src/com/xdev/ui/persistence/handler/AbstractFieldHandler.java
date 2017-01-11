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

package com.xdev.ui.persistence.handler;


import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Map;

import javax.persistence.metamodel.Attribute;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.xdev.dal.DAOs;
import com.xdev.ui.XdevField;
import com.xdev.ui.persistence.GuiPersistenceEntry;
import com.xdev.util.JPAMetaDataUtils;
import com.xdev.util.ReflectionUtils;


@SuppressWarnings("rawtypes")
public abstract class AbstractFieldHandler<C extends AbstractField>
		extends AbstractComponentHandler<C>
{
	protected static final String KEY_VALUE = "value";
	
	
	protected static boolean persistFieldValue(final Component component)
	{
		if(component instanceof XdevField)
		{
			return ((XdevField)component).isPersistValue();
		}
		
		return true;
	}
	
	
	
	public final static class IdEntry
	{
		private Class<?>	entityType;
		private Class<?>	idType;
		private Object		id;
		
		
		public IdEntry()
		{
		}
		
		
		public IdEntry(final Class<?> entityType, final Class<?> idType, final Object id)
		{
			this.entityType = entityType;
			this.idType = idType;
			this.id = id;
		}
		
		
		public Class<?> getEntityType()
		{
			return this.entityType;
		}
		
		
		public void setEntityType(final Class<?> entityType)
		{
			this.entityType = entityType;
		}
		
		
		public Class<?> getIdType()
		{
			return this.idType;
		}
		
		
		public void setIdType(final Class<?> idType)
		{
			this.idType = idType;
		}
		
		
		public Object getId()
		{
			return this.id;
		}
		
		
		public void setId(final Object id)
		{
			this.id = id;
		}
	}
	
	
	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final C component)
	{
		super.addEntryValues(entryValues,component);
		
		if(persistFieldValue(component))
		{
			entryValues.put(KEY_VALUE,getFieldValueToStore(component.getValue()));
		}
	}
	
	
	protected Object getFieldValueToStore(final Object value)
	{
		if(value == null)
		{
			return null;
		}

		final Class<? extends Object> clazz = value.getClass();
		if(clazz.isArray())
		{
			final int length = Array.getLength(value);
			final Object[] array = new Object[length];
			for(int i = 0; i < array.length; i++)
			{
				array[i] = getFieldValueToStore(Array.get(value,i));
			}
			return array;
		}
		
		if(JPAMetaDataUtils.isManaged(clazz))
		{
			final Attribute<?, ?> idAttribute = JPAMetaDataUtils.getIdAttribute(clazz);
			if(idAttribute != null)
			{
				final Object id = ReflectionUtils.getMemberValue(value,idAttribute.getJavaMember());
				return new IdEntry(clazz,id.getClass(),id);
			}
		}
		
		return value;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void restore(final C component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);
		
		if(persistFieldValue(component))
		{
			component.setValue(getFieldValueToRestore(component,entry.value(KEY_VALUE)));
		}
	}
	
	
	protected Object getFieldValueToRestore(final C component, final Object value)
	{
		if(value == null)
		{
			return null;
		}

		final Class<? extends Object> clazz = value.getClass();
		if(clazz.isArray())
		{
			final int length = Array.getLength(value);
			final Object[] array = new Object[length];
			for(int i = 0; i < array.length; i++)
			{
				array[i] = getFieldValueToRestore(component,Array.get(value,i));
			}
			return array;
		}

		if(value instanceof IdEntry)
		{
			final IdEntry idEntry = (IdEntry)value;
			Object id = idEntry.id;
			if(!idEntry.idType.isAssignableFrom(id.getClass()) && id instanceof Number)
			{
				// Correct json number type mismatch
				if(idEntry.idType == Integer.class)
				{
					id = ((Number)id).intValue();
				}
				else if(idEntry.idType == Short.class)
				{
					id = ((Number)id).shortValue();
				}
				else if(idEntry.idType == Byte.class)
				{
					id = ((Number)id).byteValue();
				}
				else if(idEntry.idType == Long.class)
				{
					id = ((Number)id).longValue();
				}
				else if(idEntry.idType == Float.class)
				{
					id = ((Number)id).floatValue();
				}
			}

			if(component instanceof AbstractSelect)
			{
				for(final Object existingItemId : ((AbstractSelect)component).getItemIds())
				{
					final Class<?> existingClass = existingItemId.getClass();
					if(JPAMetaDataUtils.isManaged(existingClass))
					{
						final Attribute<?, ?> idAttribute = JPAMetaDataUtils
								.getIdAttribute(existingClass);
						if(idAttribute != null)
						{
							final Object existingId = ReflectionUtils.getMemberValue(existingItemId,
									idAttribute.getJavaMember());
							if(id.equals(existingId))
							{
								return existingItemId;
							}
						}
					}
				}
			}

			return DAOs.getByEntityType(idEntry.entityType).find((Serializable)id);
		}
		
		return value;
	}
}
