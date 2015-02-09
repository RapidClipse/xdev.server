
package com.xdev.ui.entitycomponent;


import com.vaadin.data.Container;
import com.vaadin.data.Container.Viewer;
import com.vaadin.data.util.BeanItemContainer;
import com.xdev.server.util.KeyValueType;


/* TODO implementation for custom beanidresolver with <IDTYPE> generic for enhanced use cases 
 * + write javadoc to communicate that per default the bean itself is used as identifier see BeanItemContainer#BeanItemResolver */
public interface UIModelProvider<BEANTYPE>
{
	
	public <K, V> Container getModel(Viewer table, Class<BEANTYPE> entityClass,
			@SuppressWarnings("unchecked") KeyValueType<K, V>... nestedProperties);
	
	
	
	public class Implementation<BEANTYPE> implements UIModelProvider<BEANTYPE>
	{
		
		@Override
		public <K, V> BeanItemContainer<BEANTYPE> getModel(Viewer table,
				Class<BEANTYPE> entityClass,
				@SuppressWarnings("unchecked") KeyValueType<K, V>... nestedProperties)
		{
			BeanItemContainer<BEANTYPE> beanItemContainer = new BeanItemContainer<>(entityClass);
			
			for(KeyValueType<K, V> keyValuePair : nestedProperties)
			{
				beanItemContainer.addNestedContainerProperty(keyValuePair.getKey().toString());
			}
			
			return beanItemContainer;
		}
		
	}
}
