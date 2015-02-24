
package com.xdev.server.util;


import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.AbstractSelect;


public interface MasterDetail
{
	public void connectMasterDetail(AbstractSelect master, Filterable detailContainer,
			Object filterProperty, Object detailProperty);
	
	
	public <T> void connectForm(AbstractSelect master, BeanFieldGroup<T> detail);
	
	
	
	public class Implementation implements MasterDetail
	{
		
		@Override
		public void connectMasterDetail(AbstractSelect master, Filterable detailContainer,
				Object filterProperty, Object detailProperty)
		{
			master.addValueChangeListener(new MasterDetailValueChangeListener(master,
					detailContainer,filterProperty,detailProperty));
		}
		
		
		protected Filter prepareFilter(Filterable detailContainer, Object propertyId, Object value)
		{
			this.clearFiltering(detailContainer,propertyId);
			Filter masterDetailFilter = new Compare.Equal(propertyId,value);
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
		
		
		private void clearFiltering(Filterable filteredContainer, Object propertyId)
		{
			for(Filter filter : filteredContainer.getContainerFilters())
			{
				if(filter.appliesToProperty(propertyId))
				{
					filteredContainer.removeContainerFilter(filter);
					return;
				}
			}
		}
		
		
		
		class MasterDetailValueChangeListener implements ValueChangeListener
		{
			private static final long	serialVersionUID	= 3306467309764402175L;
			
			private AbstractSelect		filter;
			private Filterable			detailContainer;
			private Object				detailProperty, filterProperty;
			
			
			public MasterDetailValueChangeListener(AbstractSelect filter,
					Filterable detailContainer, Object filterProperty, Object detailProperty)
			{
				this.filter = filter;
				this.detailContainer = detailContainer;
				this.detailProperty = detailProperty;
				this.filterProperty = filterProperty;
			}
			
			
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				if(this.filter.getValue() != null)
				{
					prepareFilter(
							this.detailContainer,
							this.detailProperty,
							this.filter.getItem(this.filter.getValue())
									.getItemProperty(this.filterProperty).getValue().toString());
				}
				else
				{
					clearFiltering(this.detailContainer,this.detailProperty);
				}
			}
		}
	}
}
