
package com.xdev.ui.entitycomponent;


import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.xdev.server.util.KeyValueType;


public interface GenericEntityComponent<BEANTYPE, T extends Container> extends
		GenericEntityViewer<T>, Component
{
	public <K, V> void setModel(Class<BEANTYPE> entityClass,
			@SuppressWarnings("unchecked") KeyValueType<K, V>... nestedProperties);
	
	
	public Item getItem(Object id);
	
	
	public Item getSelectedItem();
}
