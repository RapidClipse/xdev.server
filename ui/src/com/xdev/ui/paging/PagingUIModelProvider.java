
package com.xdev.ui.paging;


import org.hibernate.ScrollableResults;

import com.vaadin.data.Container.Viewer;
import com.xdev.ui.entitycomponent.UIModelProvider;
import com.xdev.ui.util.KeyValueType;


public class PagingUIModelProvider<BEANTYPE> implements UIModelProvider<BEANTYPE>
{
	private final ScrollableResults	results;


	public PagingUIModelProvider(final ScrollableResults results)
	{
		this.results = results;
	}


	@Override
	public <K, V> PagedBeanItemContainer<BEANTYPE> getModel(final Viewer table,
			final Class<BEANTYPE> entityClass,
			@SuppressWarnings("unchecked") final KeyValueType<K, V>... nestedProperties)
	{
		final PagedBeanItemContainer<BEANTYPE> beanItemContainer = new PagedBeanItemContainer<>(
				this.results,entityClass);
		for(final KeyValueType<K, V> keyValuePair : nestedProperties)
		{
			beanItemContainer.addNestedContainerProperty(keyValuePair.getKey().toString());
		}

		return beanItemContainer;
	}
}
