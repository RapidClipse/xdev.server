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
 
package com.xdev.ui.util.masterdetail;


import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.util.EntityIDResolver;
import com.xdev.util.EntityReferenceResolver;
import com.xdev.util.HibernateEntityIDResolver;
import com.xdev.util.XdevEntityReferenceResolver;


@SuppressWarnings("rawtypes")
public interface JPAMasterDetail extends MasterDetail
{
	public void connectMasterDetail(BeanComponent master, BeanComponent detail,
			Class masterClass, Class detailClass);
	
	
	
	public class Implementation extends MasterDetail.Implementation implements JPAMasterDetail
	{
		private final EntityIDResolver			idResolver;
		private final EntityReferenceResolver	referenceResolver;
		
		
		public Implementation()
		{
			super();
			this.idResolver = new HibernateEntityIDResolver();
			this.referenceResolver = new XdevEntityReferenceResolver();
		}
		
		
		@Override
		public void connectMasterDetail(final BeanComponent master,
				final BeanComponent detailContainer, final Class masterClass,
				final Class detailClass)
		{
			// 1. get primary property from master class
			// 2. get referencing property from detail class
			master.addValueChangeListener(new MasterDetailValueChangeListener(master,
					detailContainer,this.idResolver.getEntityIDProperty(masterClass).getName(),
					this.referenceResolver.getReferenceEntityPropertyName(masterClass,detailClass)));
		}
		
		
		
		private class MasterDetailValueChangeListener implements ValueChangeListener
		{
			private static final long		serialVersionUID	= 3306467309764402175L;
			
			private final BeanComponent	filter;
			private final BeanComponent	detailContainer;
			private final Object			detailProperty, filterProperty;
			
			
			public MasterDetailValueChangeListener(final BeanComponent filter,
					final BeanComponent detailContainer, final Object filterProperty,
					final Object detailProperty)
			{
				this.filter = filter;
				this.detailContainer = detailContainer;
				this.detailProperty = detailProperty;
				this.filterProperty = filterProperty;
			}
			
			
			@Override
			public void valueChange(final ValueChangeEvent event)
			{
				if(this.filter.getSelectedItem() != null)
				{
					prepareFilter(this.detailContainer.getDataContainer(),this.detailProperty,
							this.filter.getSelectedItem().getItemProperty(this.filterProperty)
									.getValue().toString());
				}
				else
				{
					clearFiltering(this.detailContainer.getDataContainer(),this.detailProperty);
				}
			}
		}
	}
}
