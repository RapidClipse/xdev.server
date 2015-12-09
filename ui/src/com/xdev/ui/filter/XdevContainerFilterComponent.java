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

package com.xdev.ui.filter;


import javax.persistence.metamodel.Attribute;

import com.vaadin.data.Container.Filterable;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.util.CaptionUtils;
import com.xdev.util.HibernateMetaDataUtils;


/**
 * @author XDEV Software
 * 		
 */
public class XdevContainerFilterComponent extends ContainerFilterComponent
{
	/**
	 *
	 */
	public XdevContainerFilterComponent()
	{
	}


	@Override
	protected String getPropertyCaption(final Object propertyId)
	{
		final Filterable container = getContainer();
		if(container instanceof XdevBeanContainer<?>)
		{
			return CaptionUtils.resolveEntityMemberCaption(
					((XdevBeanContainer<?>)container).getBeanType(),propertyId.toString());
		}
		
		return propertyId.toString();
	}


	@Override
	protected Class<?> getPropertyType(final Object propertyId)
	{
		final Class<?> propertyType = super.getPropertyType(propertyId);
		if(propertyType == null)
		{
			final Filterable container = getContainer();
			if(container instanceof XdevBeanContainer<?>)
			{
				final Class<?> beanType = ((XdevBeanContainer<?>)container).getBeanType();
				final Attribute<?, ?> attribute = HibernateMetaDataUtils.resolveAttribute(beanType,
						propertyId.toString());
				if(attribute != null)
				{
					return attribute.getJavaType();
				}
			}
		}
		
		return propertyType;
	}
}
