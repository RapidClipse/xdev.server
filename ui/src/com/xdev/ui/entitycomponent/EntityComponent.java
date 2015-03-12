
package com.xdev.ui.entitycomponent;


import java.util.Collection;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.xdev.ui.util.KeyValueType;


public interface EntityComponent<BEANTYPE> extends GenericEntityViewer<BEANTYPE>, Component
{
	public void setModel(Class<BEANTYPE> entityClass, KeyValueType<?, ?>... nestedProperties);


	public void setModel(Class<BEANTYPE> entityClass, Collection<BEANTYPE> data,
			KeyValueType<?, ?>... nestedProperties);


	public BeanItem<BEANTYPE> getItem(Object id);


	public BeanItem<BEANTYPE> getSelectedItem();


	public void addValueChangeListener(Property.ValueChangeListener listener);
}
