
package com.xdev.ui.paging;


import org.hibernate.ScrollableResults;

import com.vaadin.ui.AbstractSelect;
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
	public PagedBeanItemContainer<BEANTYPE> getModel(final AbstractSelect component,
			final Class<BEANTYPE> entityClass, final KeyValueType<?, ?>... nestedProperties)
	{
		final PagedBeanItemContainer<BEANTYPE> beanItemContainer = new PagedBeanItemContainer<>(
				this.results,entityClass);
		for(final KeyValueType<?, ?> keyValuePair : nestedProperties)
		{
			beanItemContainer.addNestedContainerProperty(keyValuePair.getKey().toString());
		}

		return beanItemContainer;
	}
}
