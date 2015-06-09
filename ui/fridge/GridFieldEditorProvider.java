///*
// * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
// *
// * This program is free software: you can redistribute it and/or modify it
// * under the terms of the GNU General Public License as published by the
// * Free Software Foundation, either version 3 of the License, or (at your
// * option) any later version.
// *
// * This program is distributed in the hope that it will be useful, but
// * WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// * Public License for more details.
// *
// * You should have received a copy of the GNU General Public License along
// * with this program. If not, see <http://www.gnu.org/licenses/>.
// */
//
//package com.xdev.ui.paging;
//
//
//import java.lang.reflect.Field;
//import java.util.Set;
//
//import com.vaadin.data.util.BeanItemContainer;
//import com.vaadin.ui.AbstractSelect;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.TwinColSelect;
//import com.xdev.dal.DAOs;
//import com.xdev.ui.entitycomponent.BeanContainer;
//
//
///**
// * @author XDEV Software
// *
// */
//public interface GridFieldEditorProvider
//{
//	public AbstractSelect getFieldEditor(Class<?> modelType, Class<?> presentationType);
//
//
//
//	public class Implementation implements GridFieldEditorProvider
//	{
//		private Object				setPropertyID;
//		private BeanContainer<?>	container;
//
//
//		/**
//		 *
//		 */
//		public Implementation()
//		{
//		}
//
//
//		/**
//		 *
//		 */
//		public Implementation(final Object setPropertyID, final BeanContainer<?> container)
//		{
//			this.setPropertyID = setPropertyID;
//			this.container = container;
//		}
//
//
//		/**
//		 * {@inheritDoc}
//		 */
//		@SuppressWarnings({"unchecked","rawtypes"})
//		@Override
//		public AbstractSelect getFieldEditor(final Class<?> modelType,
//				final Class<?> presentationType)
//		{
//			AbstractSelect fieldEditor = null;
//
//			// n-m
//			if(modelType.equals(Set.class))
//			{
//				if(container.getItemIds().size() > 0)
//				{
//					Set<?> set = null;
//					try
//					{
//						final Object bean = container.getBeanItem(0).getBean();
//						final Field setField = bean.getClass().getDeclaredField(
//								this.setPropertyID.toString());
//						setField.setAccessible(true);
//						final Object setFieldValue = setField.get(bean);
//						if(setFieldValue instanceof Set<?>)
//						{
//							set = (Set<?>)setFieldValue;
//						}
//					}
//					catch(NoSuchFieldException | SecurityException | IllegalArgumentException
//							| IllegalAccessException e)
//					{
//						throw new RuntimeException(e.getMessage());
//					}
//					if(set != null)
//					{
//						final Object setContentBean = set.iterator().next();
//						fieldEditor = new TwinColSelect();
//						fieldEditor.setContainerDataSource(new BeanItemContainer(setContentBean
//								.getClass(),DAOs.getByEntityType(setContentBean.getClass())
//								.findAll()));
//						fieldEditor.setSizeFull();
//					}
//				}
//
//			}
//			// n-1
//			else
//			{
//				// TODO create factory for n-1/n-m suitable components
//				fieldEditor = new ComboBox();
//				// TODO caching would be appropriate in this use case.
//				fieldEditor.setContainerDataSource(new BeanItemContainer(modelType,DAOs
//						.getByEntityType(modelType).findAll()));
//			}
//			return fieldEditor;
//		}
//	}
// }
