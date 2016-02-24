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

package com.xdev.util;


import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.xdev.communication.EntityManagerUtils;


/**
 * Returns a Vaadin Item property chain.
 *
 * @author XDEV Software (JW)
 */
// TODO create VaadinItemPathConcatenator to avoid manual "." appending
public class XdevEntityReferenceResolver implements EntityReferenceResolver
{
	private static XdevEntityReferenceResolver instance;


	public static XdevEntityReferenceResolver getInstance()
	{
		if(instance == null)
		{
			instance = new XdevEntityReferenceResolver();
		}

		return instance;
	}

	private final Configuration config;
	// private final EntityIDResolver idResolver;


	private XdevEntityReferenceResolver()
	{
		this.config = HibernateMetaDataUtils
				.getConfiguration(EntityManagerUtils.getEntityManager());
	}


	@Override
	public String getReferenceEntityPropertyName(final Class<?> referenceEntity,
			final Class<?> entity) throws RuntimeException
	{
		final PersistentClass clazz = this.config.getClassMapping(entity.getName());
		Property ref = null;

		for(@SuppressWarnings("unchecked")
		final Iterator<Property> i = clazz.getReferenceablePropertyIterator(); i.hasNext();)
		{
			final Property it = i.next();
			/*
			 * not only referenceable properties are returned, hence a manual
			 * check is required
			 */
			final String propertyName = HibernateMetaDataUtils
					.getReferencablePropertyName(it.getValue());
			if(propertyName != null)
			{
				ref = it;

				if(propertyName.equals(referenceEntity.getName()))
				{
					return ref.getName();
				}
			}
		}
		return getReferenceEntityPropertyname(referenceEntity,entity,ref);
	}


	// look deeper into entity
	protected String getReferenceEntityPropertyname(final Class<?> referenceEntity,
			Class<?> previousClass, Property previousProperty) throws RuntimeException
	{
		if(previousProperty != null)
		{
			final String name = previousProperty.getName() + ".";
			previousClass = previousProperty.getType().getReturnedClass();
			final PersistentClass clazz = this.config.getClassMapping(previousClass.getName());

			for(@SuppressWarnings("unchecked")
			final Iterator<Property> iterator = clazz.getReferenceablePropertyIterator(); iterator
					.hasNext();)
			{
				final Property property = iterator.next();
				/*
				 * not only referenceable properties are returned, hence a
				 * manual check is required
				 */
				if(HibernateMetaDataUtils.getReferencablePropertyName(property.getValue()) != null)
				{
					previousProperty = property;
					final String propertyName = HibernateMetaDataUtils
							.getReferencablePropertyName(previousProperty.getValue());

					if(propertyName != null)
					{
						if(propertyName.equals(referenceEntity.getName()))
						{
							return this.getPropertyName(name,previousProperty);
						}
					}
				}
			}
		}
		return getReferenceEntityPropertyname(referenceEntity,previousClass,previousProperty);
	}


	private String getPropertyName(final String itemPropertyPath, final Property property)
	{
		return itemPropertyPath + property.getName();
	}
	// private String attachId(final String propertyName, final Class<?>
	// javaClass)
	// {
	// return propertyName + "." +
	// this.idResolver.getEntityIDProperty(javaClass).getName();
	// }
}
