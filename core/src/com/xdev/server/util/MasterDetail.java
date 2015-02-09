
package com.xdev.server.util;


import java.util.Iterator;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.filter.Like;
import com.vaadin.ui.AbstractSelect;


public interface MasterDetail
{
	public <T> void connectMasterDetail(AbstractSelect master, Filterable detailContainer,
			String filterProperty, String detailProperty);
	
	
	public <T> void connectForm(AbstractSelect master, BeanFieldGroup<T> detail);
	
	
	
	public class Implementation implements MasterDetail
	{
		
		@Override
		public <T> void connectMasterDetail(AbstractSelect master, Filterable detailContainer,
				String filterProperty, String detailProperty)
		{
			master.addValueChangeListener(e -> prepareFilter(detailContainer,detailProperty,master
					.getItem(master.getValue()).getItemProperty(filterProperty).getValue()
					.toString()));
		}
		
		
		protected <T> Filter prepareFilter(Filterable detailContainer, Object propertyId,
				String value)
		{
			for(Iterator<Filter> iterator = detailContainer.getContainerFilters().iterator(); iterator
					.hasNext();)
			{
				Filter filter = iterator.next();
				// does not work with multiple filters on the same property
				if(filter.appliesToProperty(propertyId))
				{
					iterator.remove();
				}
			}
			
			Filter masterDetailFilter = new Like(propertyId,value,false);
			detailContainer.addContainerFilter(masterDetailFilter);
			
			return masterDetailFilter;
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> void connectForm(AbstractSelect master, BeanFieldGroup<T> detail)
		{
			master.addValueChangeListener(e -> prepareFormData((T)master.getValue(),detail));
		}
		
		
		protected <T> void prepareFormData(T data, BeanFieldGroup<T> detail)
		{
			detail.setItemDataSource(data);
		}
		
	}
}
