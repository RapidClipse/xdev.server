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

package com.xdev.ui.entitycomponent.listselect;


import java.util.Collection;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.xdev.ui.entitycomponent.IDToBeanCollectionConverter;
import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;
import com.xdev.ui.util.KeyValueType;


/**
 * Multiselect component with two lists: left side for available items and right
 * side for selected items.
 *
 * @author XDEV Software
 *
 */
public class XdevTwinColSelect<T> extends AbstractBeanTwinColSelect<T>
{
	/**
	 *
	 */
	public XdevTwinColSelect()
	{
		super();
	}


	/**
	 * @param caption
	 */
	public XdevTwinColSelect(final String caption)
	{
		super(caption);
	}

	// init defaults
	{
		setImmediate(true);
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	@SafeVarargs
	@Override
	public final void setDataContainer(final Class<T> beanClass,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				beanClass,nestedProperties);

		/*
		 * vaadin api compiler warnings, cant define a converter with a set as
		 * wrapper data type
		 */
		this.setConverter(new IDToBeanCollectionConverter(container));
		this.setDataContainer(container);
	}


	@SuppressWarnings({"rawtypes","unchecked"})
	@SafeVarargs
	@Override
	public final void setDataContainer(final Class<T> beanClass, final Collection<T> data,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				beanClass,nestedProperties);
		for(final T entity : data)
		{
			container.addBean(entity);
		}

		/*
		 * vaadin api compiler warnings, cant define a converter with a set as
		 * wrapper data type
		 */
		this.setConverter(new IDToBeanCollectionConverter(container));
		this.setDataContainer(container);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LazyLoadingUIModelProvider<T> getModelProvider()
	{
		return new LazyLoadingUIModelProvider<T>(this.getRows(),false,false);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.AbstractField#setConverter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setConverter(final Class<?> datamodelType)
	{
		// set converter only if utils find a suitable one.
		final Converter<Object, ?> c = (Converter<Object, ?>)ConverterUtil.getConverter(getType(),
				datamodelType,getSession());
		if(c != null)
		{
			setConverter(c);
		}
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see com.vaadin.ui.AbstractSelect#getType()
	// */
	// @Override
	// public Class<?> getType()
	// {
	// if(isMultiSelect())
	// {
	// return Collection.class;
	// }
	// else
	// {
	// return Object.class;
	// }
	// }
}
