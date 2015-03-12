
package com.xdev.ui.util.masterdetail;


import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.entitycomponent.EntityComponent;


public interface MasterDetail
{
	public void connectMasterDetail(AbstractSelect master, Filterable detailContainer,
			Object filterProperty, Object detailProperty);
	
	
	public <T> void connectForm(final EntityComponent<T> master, BeanFieldGroup<T> detail);



	public class Implementation implements MasterDetail
	{

		@Override
		public void connectMasterDetail(final AbstractSelect master,
				final Filterable detailContainer, final Object filterProperty,
				final Object detailProperty)
		{
			master.addValueChangeListener(new MasterDetailValueChangeListener(master,
					detailContainer,filterProperty,detailProperty));
		}


		protected Filter prepareFilter(final Filterable detailContainer, final Object propertyId,
				final Object value)
		{
			this.clearFiltering(detailContainer,propertyId);
			final Filter masterDetailFilter = new Compare.Equal(propertyId,value);
			detailContainer.addContainerFilter(masterDetailFilter);

			return masterDetailFilter;
		}


		@Override
		public <T> void connectForm(final EntityComponent<T> master, final BeanFieldGroup<T> detail)
		{
			master.addValueChangeListener(e -> prepareFormData(master.getSelectedItem(),detail));
		}


		protected <T> void prepareFormData(final BeanItem<T> data, final BeanFieldGroup<T> detail)
		{
			detail.setItemDataSource(data);
		}


		protected void clearFiltering(final Filterable filteredContainer, final Object propertyId)
		{
			for(final Filter filter : filteredContainer.getContainerFilters())
			{
				if(filter.appliesToProperty(propertyId))
				{
					filteredContainer.removeContainerFilter(filter);
					return;
				}
			}
		}



		private class MasterDetailValueChangeListener implements ValueChangeListener
		{
			private static final long		serialVersionUID	= 3306467309764402175L;

			private final AbstractSelect	filter;
			private final Filterable		detailContainer;
			private final Object			detailProperty, filterProperty;


			public MasterDetailValueChangeListener(final AbstractSelect filter,
					final Filterable detailContainer, final Object filterProperty,
					final Object detailProperty)
			{
				this.filter = filter;
				this.detailContainer = detailContainer;
				this.detailProperty = detailProperty;
				this.filterProperty = filterProperty;
			}


			@Override
			public void valueChange(final ValueChangeEvent event)
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
