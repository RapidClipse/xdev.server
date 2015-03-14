
package com.xdev.ui.paging;


import org.hibernate.mapping.Property;

import com.vaadin.ui.AbstractSelect;
import com.xdev.server.util.HibernateEntityIDResolver;
import com.xdev.ui.entitycomponent.IDToEntityConverter;
import com.xdev.ui.entitycomponent.UIModelProvider;
import com.xdev.ui.util.KeyValueType;


//hibernate/JPA specific implementation
public class LazyLoadingUIModelProvider<BEANTYPE> implements UIModelProvider<BEANTYPE>
{
	private final int			batchSize;
	private boolean				readOnlyProperties				= true, sortableProperties = true;
	/**
	 * for example car.manufacturer.name
	 */
	private static final String	VAADIN_PROPERTY_NESTING_PATTERN	= "\\.";


	public LazyLoadingUIModelProvider(final int batchSize, final Object idProperty)
	{
		this.batchSize = batchSize;
	}


	public LazyLoadingUIModelProvider(final int bachSize, final boolean readOnlyProperties,
			final boolean sortableProperties)
	{
		this.batchSize = bachSize;
		this.readOnlyProperties = readOnlyProperties;
		this.sortableProperties = sortableProperties;
	}


	@Override
	public XdevLazyEntityContainer<BEANTYPE> getModel(final AbstractSelect component,
			final Class<BEANTYPE> entityClass, final KeyValueType<?, ?>... nestedProperties)
	{
		final Property idProperty = new HibernateEntityIDResolver()
				.getEntityIDProperty(entityClass);

		final XdevLazyEntityContainer<BEANTYPE> let = new XdevLazyEntityContainer<>(entityClass,
				this.batchSize,idProperty.getName());

		for(final KeyValueType<?, ?> keyValuePair : nestedProperties)
		{
			let.addContainerProperty(keyValuePair.getKey(),keyValuePair.getType(),
					keyValuePair.getValue(),this.readOnlyProperties,this.sortableProperties);
		}
		let.getQueryView().getQueryDefinition()
				.setMaxNestedPropertyDepth(this.getMaxNestedPropertyDepth(nestedProperties));

		// register non beanitemcontainer id converter
		component.setConverter(new IDToEntityConverter<BEANTYPE>(let));

		return let;
	}


	private int getMaxNestedPropertyDepth(final KeyValueType<?, ?>[] nestedProperties)
	{
		int maxNestedPropertyDepth = 0;

		for(int i = 0; i < nestedProperties.length; i++)
		{
			final int currentDepth = nestedProperties[i].getKey().toString()
					.split(VAADIN_PROPERTY_NESTING_PATTERN).length;
			if(currentDepth > maxNestedPropertyDepth)
			{
				maxNestedPropertyDepth = currentDepth;
			}
		}
		return maxNestedPropertyDepth;
	}
}
