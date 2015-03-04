
package com.xdev.ui.entitycomponent;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.xdev.ui.util.KeyValueType;


public interface GenericEntityComponent<BEANTYPE, T extends Container.Filterable> extends
		GenericEntityViewer<T>, Component
{
	public <K, V> void setModel(Class<BEANTYPE> entityClass,
			@SuppressWarnings("unchecked") KeyValueType<K, V>... nestedProperties);


	public <K, V> void setModel(Class<BEANTYPE> entityClass, Collection<BEANTYPE> data,
			@SuppressWarnings("unchecked") KeyValueType<K, V>... nestedProperties);


	public Item getItem(Object id);


	public Item getSelectedItem();
	
	
	public void addValueChangeListener(Property.ValueChangeListener listener);
}
