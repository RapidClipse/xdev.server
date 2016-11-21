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

package com.xdev.ui.util.masterdetail;


import java.util.Collection;

import org.apache.log4j.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.xdev.dal.DAOs;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.masterdetail.LazyMasterDetailConnection;
import com.xdev.util.DTOUtils;
import com.xdev.util.EntityReferenceResolver;
import com.xdev.util.JPAEntityReferenceResolver;


/**
 *
 * @deprecated replaced by {@link LazyMasterDetailConnection}
 */
@Deprecated
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
			this.referenceResolver = JPAEntityReferenceResolver.getInstance();
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
							this.referenceResolver.getReferenceEntityAttributeName(detailClass,
									masterClass),
							this.referenceResolver.getReferenceEntityAttributeName(masterClass,
									detailClass)));
		}



		private class MasterDetailValueChangeListener implements ValueChangeListener
		{
			private static final long	serialVersionUID	= 3306467309764402175L;

			private final BeanComponent	masterComponent;
			private final BeanComponent	detailComponent;
			private final String		masterProperty;


			public MasterDetailValueChangeListener(final BeanComponent filter,
					final BeanComponent detailContainer, final String masterProperty,
					final Object detailProperty)
			{
				this.masterComponent = filter;
				this.detailComponent = detailContainer;
				this.masterProperty = masterProperty;
			}


			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(final ValueChangeEvent event)
			{
				// reset selection
				detailComponent.getBeanContainerDataSource().removeAll();
				final BeanItem selectedItem = masterComponent.getSelectedItem();
				Object selectedBean;
				if(selectedItem != null && (selectedBean = selectedItem.getBean()) != null)
				{
					// reattach
					DAOs.get(selectedBean).reattach(selectedBean);

					try
					{
						final Object value = DTOUtils.resolveAttributeValue(selectedBean,
								this.masterProperty);
						if(value != null)
						{
							if(value instanceof Collection)
							{
								final Collection values = (Collection)value;
								this.detailComponent.getBeanContainerDataSource().addAll(values);
							}
							else
							{
								this.detailComponent.getBeanContainerDataSource().addBean(value);
							}
						}
					}
					catch(final Exception e)
					{
						Logger.getLogger(EntityMasterDetail.class).error(e);
					}
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
