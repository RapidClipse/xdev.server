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

package com.xdev.ui;


import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Resource;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.util.Caption;
import com.xdev.util.CaptionResolver;
import com.xdev.util.CaptionUtils;


/**
 * @author XDEV Software
 * @since 3.0.2
 */
public interface XdevSelect<T> extends XdevField, BeanComponent<T>
{
	/**
	 * Sets if the item's caption should be derived from its {@link Caption}
	 * annotation.
	 *
	 * @see CaptionResolver
	 *
	 * @param itemCaptionFromAnnotation
	 *            the itemCaptionFromAnnotation to set
	 */
	public void setItemCaptionFromAnnotation(final boolean itemCaptionFromAnnotation);


	/**
	 * @return if the item's caption should be derived from its {@link Caption}
	 *         annotation
	 */
	public boolean isItemCaptionFromAnnotation();


	/**
	 * Sets a user defined caption value for the items to display.
	 *
	 * @see Caption
	 * @see #setItemCaptionFromAnnotation(boolean)
	 * @param itemCaptionValue
	 *            the itemCaptionValue to set
	 */
	public void setItemCaptionValue(final String itemCaptionValue);


	/**
	 * Returns the user defined caption value for the items to display
	 *
	 * @return the itemCaptionValue
	 */
	public String getItemCaptionValue();


	/**
	 * @param itemCaptionProvider
	 *            the itemCaptionProvider to set
	 * @since 3.0.2
	 */
	public void setItemCaptionProvider(final ItemCaptionProvider<T> itemCaptionProvider);


	/**
	 * @return the itemCaptionProvider
	 * @since 3.0.2
	 */
	public ItemCaptionProvider<T> getItemCaptionProvider();


	/**
	 * @param itemIconProvider
	 *            the itemIconProvider to set
	 * @since 3.0.2
	 */
	public void setItemIconProvider(final ItemIconProvider<T> itemIconProvider);


	/**
	 * @return the itemIconProvider
	 * @since 3.0.2
	 */
	public ItemIconProvider<T> getItemIconProvider();



	public final static class SelectUtils
	{
		private static <T> T getBean(final XdevSelect<T> select, final Object itemId)
		{
			final BeanItem<T> item = select.getBeanItem(itemId);
			return item != null ? item.getBean() : null;
		}


		public static <T> String getItemCaption(final XdevSelect<T> select, final Object itemId)
		{
			if(itemId != null && select.getBeanContainerDataSource() != null)
			{
				T bean;
				ItemCaptionProvider<T> itemCaptionProvider;
				String caption;
				if((itemCaptionProvider = select.getItemCaptionProvider()) != null
						&& (bean = getBean(select,itemId)) != null
						&& (caption = itemCaptionProvider.getCaption(bean)) != null)
				{
					return caption;
				}

				String itemCaptionValue;
				if(select.isItemCaptionFromAnnotation() && (bean = getBean(select,itemId)) != null)
				{
					if(CaptionUtils.hasCaptionAnnotationValue(bean.getClass()))
					{
						return CaptionUtils.resolveCaption(bean,select.getLocale());
					}
				}
				else if((itemCaptionValue = select.getItemCaptionValue()) != null
						&& (bean = getBean(select,itemId)) != null)
				{
					return CaptionUtils.resolveCaption(bean,itemCaptionValue,select.getLocale());
				}
			}

			return null;
		}


		public static <T> Resource getItemIcon(final XdevSelect<T> select, final Object itemId)
		{
			if(itemId != null && select.getBeanContainerDataSource() != null)
			{
				ItemIconProvider<T> itemIconProvider;
				T bean;
				if((itemIconProvider = select.getItemIconProvider()) != null
						&& (bean = getBean(select,itemId)) != null)
				{
					return itemIconProvider.getIcon(bean);
				}
			}

			return null;
		}


		private SelectUtils()
		{
		}
	}
}
