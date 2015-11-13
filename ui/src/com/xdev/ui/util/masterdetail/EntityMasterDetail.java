/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.util.masterdetail;


import java.lang.reflect.Field;
import java.util.Collection;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.util.EntityReferenceResolver;
import com.xdev.util.XdevEntityReferenceResolver;


@SuppressWarnings("rawtypes")
public interface EntityMasterDetail extends JPAMasterDetail
{
	@Override
	public void connectMasterDetail(BeanComponent master, BeanComponent detail, Class masterClass,
			Class detailClass);
			
			
			
	public class Implementation extends MasterDetail.Implementation implements EntityMasterDetail
	{
		// private final EntityIDResolver idResolver;
		private final EntityReferenceResolver referenceResolver;
		
		
		public Implementation()
		{
			super();
			// this.idResolver = new HibernateEntityIDResolver();
			this.referenceResolver = new XdevEntityReferenceResolver();
		}
		
		
		@Override
		public void connectMasterDetail(final BeanComponent master,
				final BeanComponent detailContainer, final Class masterClass,
				final Class detailClass)
		{
			// 1. get primary property from master class
			// 2. get referencing property from detail class
			master.addValueChangeListener(
					new MasterDetailValueChangeListener(master,detailContainer,
							this.referenceResolver.getReferenceEntityPropertyName(detailClass,
									masterClass),
					this.referenceResolver.getReferenceEntityPropertyName(masterClass,
							detailClass)));
		}
		
		
		
		private class MasterDetailValueChangeListener implements ValueChangeListener
		{
			private static final long serialVersionUID = 3306467309764402175L;
			
			private final BeanComponent	masterComponent;
			private final BeanComponent	detailComponent;
			private final Object		masterProperty;
			
			
			public MasterDetailValueChangeListener(final BeanComponent filter,
					final BeanComponent detailContainer, final Object masterProperty,
					final Object detailProperty)
			{
				this.masterComponent = filter;
				this.detailComponent = detailContainer;
				// this.detailProperty = detailProperty;
				this.masterProperty = masterProperty;
			}
			
			
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(final ValueChangeEvent event)
			{
				// reset selection
				detailComponent.getContainerDataSource().removeAll();
				final Object selectedBean = masterComponent.getSelectedItem().getBean();
				try
				{
					final Field f = selectedBean.getClass()
							.getDeclaredField(this.masterProperty.toString());
					f.setAccessible(true);
					final Object value = f.get(selectedBean);
					
					if(value instanceof Collection)
					{
						final Collection values = (Collection)value;
						this.detailComponent.getContainerDataSource().addAll(values);
					}
					else
					{
						this.detailComponent.getContainerDataSource().addBean(value);
					}
				}
				catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e)
				{
					e.printStackTrace();
				}
				
				// if(this.filterComponent.getSelectedItem() != null)
				// {
				// clearFiltering(this.detailComponent.getContainerDataSource(),
				// this.filterProperty);
				//
				// prepareFilter(this.detailComponent.getContainerDataSource(),this.detailProperty,
				// this.filterComponent.getSelectedItem().getBean()
				// //
				// .getItemProperty(this.filterProperty).getValue().toString(),
				// );
				// }
				// else
				// {
				// clearFiltering(this.detailComponent.getContainerDataSource(),
				// this.filterProperty);
				// }
			}
		}
	}
}
