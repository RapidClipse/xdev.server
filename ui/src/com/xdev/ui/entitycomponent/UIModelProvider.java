
package com.xdev.ui.entitycomponent;


import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.util.KeyValueType;


/* TODO implementation for custom beanidresolver with <IDTYPE> generic for enhanced use cases
 * + write javadoc to communicate that per default the bean itself is used as identifier see BeanItemContainer#BeanItemResolver */
public interface UIModelProvider<BEANTYPE>
{
	
	public Container getModel(AbstractSelect component, Class<BEANTYPE> entityClass,
			KeyValueType<?, ?>... nestedProperties);
	
	
	
	public class Implementation<BEANTYPE> implements UIModelProvider<BEANTYPE>
	{
		
		@Override
		public BeanItemContainer<BEANTYPE> getModel(final AbstractSelect table,
				final Class<BEANTYPE> entityClass, final KeyValueType<?, ?>... nestedProperties)
		{
			final BeanItemContainer<BEANTYPE> beanItemContainer = new BeanItemContainer<>(
					entityClass);
			
			for(final KeyValueType<?, ?> keyValuePair : nestedProperties)
			{
				beanItemContainer.addNestedContainerProperty(keyValuePair.getKey().toString());
			}
			
			return beanItemContainer;
		}
		
	}
}
