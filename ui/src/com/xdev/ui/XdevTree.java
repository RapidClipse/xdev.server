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


import java.util.Set;

import org.jsoup.nodes.Element;

import com.vaadin.data.Container;
import com.vaadin.server.Resource;
import com.vaadin.ui.Tree;
import com.vaadin.ui.declarative.DesignContext;
import com.xdev.ui.entitycomponent.hierarchical.XdevHierarchicalBeanItemContainer;
import com.xdev.ui.hierarchical.DynamicHierarchicalContainer;
import com.xdev.ui.hierarchical.TreeDataProvider;
import com.xdev.util.Caption;
import com.xdev.util.CaptionResolver;
import com.xdev.util.CaptionUtils;


/**
 * Tree component. A Tree can be used to select an item (or multiple items) from
 * a hierarchical set of items.
 *
 * @author XDEV Software
 *
 */
public class XdevTree extends Tree implements XdevField
{
	private final Extensions			extensions					= new Extensions();
	private boolean						persistValue				= PERSIST_VALUE_DEFAULT;
	private boolean						itemCaptionFromAnnotation	= true;
	private String						itemCaptionValue;
	private ItemCaptionProvider<Object>	itemCaptionProvider;
	private ItemIconProvider<Object>	itemIconProvider;
	
	
	/**
	 * Creates a new empty tree.
	 */
	public XdevTree()
	{
		super();
	}
	
	
	/**
	 * Creates a new tree with caption and connect it to a Container.
	 *
	 * @param caption
	 * @param dataSource
	 */
	public XdevTree(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Creates a new empty tree with caption.
	 *
	 * @param caption
	 */
	public XdevTree(final String caption)
	{
		super(caption);
	}
	
	// init defaults
	{
		addExpandListener(event -> {
			try
			{
				final Container dataSource = getContainerDataSource();
				if(dataSource instanceof DynamicHierarchicalContainer)
				{
					if(((DynamicHierarchicalContainer)dataSource).expand(event.getItemId()))
					{
						markAsDirty();
					}
				}
			}
			catch(final Throwable t)
			{
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistValue()
	{
		return this.persistValue;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPersistValue(final boolean persistValue)
	{
		this.persistValue = persistValue;
	}
	
	
	/**
	 * Sets if the item's caption should be derived from its {@link Caption}
	 * annotation.
	 *
	 * @see CaptionResolver
	 *
	 * @param itemCaptionFromAnnotation
	 *            the itemCaptionFromAnnotation to set
	 *
	 * @since 1.1
	 */
	public void setItemCaptionFromAnnotation(final boolean itemCaptionFromAnnotation)
	{
		this.itemCaptionFromAnnotation = itemCaptionFromAnnotation;
	}
	
	
	/**
	 * @return if the item's caption should be derived from its {@link Caption}
	 *         annotation
	 *
	 * @since 1.1
	 */
	public boolean isItemCaptionFromAnnotation()
	{
		return this.itemCaptionFromAnnotation;
	}
	
	
	/**
	 * Sets a user defined caption value for the items to display.
	 *
	 * @see Caption
	 * @see #setItemCaptionFromAnnotation(boolean)
	 * @param itemCaptionValue
	 *            the itemCaptionValue to set
	 *
	 * @since 1.1
	 */
	public void setItemCaptionValue(final String itemCaptionValue)
	{
		this.itemCaptionValue = itemCaptionValue;
	}
	
	
	/**
	 * Returns the user defined caption value for the items to display
	 *
	 * @return the itemCaptionValue
	 *
	 * @since 1.1
	 */
	public String getItemCaptionValue()
	{
		return this.itemCaptionValue;
	}
	
	
	/**
	 * @param itemCaptionProvider
	 *            the itemCaptionProvider to set
	 * @since 3.0.2
	 */
	public void setItemCaptionProvider(final ItemCaptionProvider<Object> itemCaptionProvider)
	{
		this.itemCaptionProvider = itemCaptionProvider;
	}
	
	
	/**
	 * @return the itemCaptionProvider
	 * @since 3.0.2
	 */
	public ItemCaptionProvider<Object> getItemCaptionProvider()
	{
		return this.itemCaptionProvider;
	}
	
	
	@Override
	public String getItemCaption(final Object itemId)
	{
		if(itemId != null)
		{
			String caption;
			if(this.itemCaptionProvider != null
					&& (caption = this.itemCaptionProvider.getCaption(itemId)) != null)
			{
				return caption;
			}
			
			if(isItemCaptionFromAnnotation())
			{
				if(CaptionUtils.hasCaptionAnnotationValue(itemId.getClass()))
				{
					return CaptionUtils.resolveCaption(itemId,getLocale());
				}
			}
			else if(this.itemCaptionValue != null)
			{
				return CaptionUtils.resolveCaption(itemId,this.itemCaptionValue,getLocale());
			}
		}
		
		return super.getItemCaption(itemId);
	}
	
	
	/**
	 * @param itemIconProvider
	 *            the itemIconProvider to set
	 * @since 3.0.2
	 */
	public void setItemIconProvider(final ItemIconProvider<Object> itemIconProvider)
	{
		this.itemIconProvider = itemIconProvider;
	}
	
	
	/**
	 * @return the itemIconProvider
	 * @since 3.0.2
	 */
	public ItemIconProvider<Object> getItemIconProvider()
	{
		return this.itemIconProvider;
	}
	
	
	@Override
	public Resource getItemIcon(final Object itemId)
	{
		Resource icon;
		if(this.itemIconProvider != null && (icon = this.itemIconProvider.getIcon(itemId)) != null)
		{
			return icon;
		}
		
		return super.getItemIcon(itemId);
	}
	
	
	public void setContainerDataSource(final TreeDataProvider provider, final boolean preloadAll)
	{
		final XdevHierarchicalBeanItemContainer container = new XdevHierarchicalBeanItemContainer(
				provider);
		if(preloadAll)
		{
			container.preloadAll();
		}
		setContainerDataSource(container);
	}
	
	
	@Override
	protected Element writeItem(final Element design, final Object itemId,
			final DesignContext context)
	{
		final Element element = super.writeItem(design,itemId,context);
		
		if(isExpanded(itemId))
		{
			element.attributes().put("expanded",true);
		}
		
		return element;
	}
	
	
	@Override
	protected String readItem(final Element node, final Set<String> selected,
			final DesignContext context)
	{
		final String itemId = super.readItem(node,selected,context);
		
		if(node.hasAttr("expanded"))
		{
			expandItem(itemId);
		}
		
		return itemId;
	}
}
