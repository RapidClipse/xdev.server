
package com.xdev.ui.util.masterdetail;


import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.xdev.server.util.EntityIDResolver;
import com.xdev.server.util.EntityReferenceResolver;
import com.xdev.server.util.HibernateEntityIDResolver;
import com.xdev.server.util.XdevEntityReferenceResolver;
import com.xdev.ui.entitycomponent.EntityComponent;


@SuppressWarnings("rawtypes")
public interface JPAMasterDetail extends MasterDetail
{
	public void connectMasterDetail(EntityComponent master, EntityComponent detail,
			Class masterClass, Class detailClass);
	
	
	
	public class Implementation extends MasterDetail.Implementation implements JPAMasterDetail
	{
		private final EntityIDResolver			idResolver;
		private final EntityReferenceResolver	referenceResolver;
		
		
		public Implementation()
		{
			super();
			this.idResolver = new HibernateEntityIDResolver();
			this.referenceResolver = new XdevEntityReferenceResolver();
		}
		
		
		@Override
		public void connectMasterDetail(final EntityComponent master,
				final EntityComponent detailContainer, final Class masterClass,
				final Class detailClass)
		{
			// 1. get primary property from master class
			// 2. get referencing property from detail class
			master.addValueChangeListener(new MasterDetailValueChangeListener(master,
					detailContainer,this.idResolver.getEntityIDProperty(masterClass).getName(),
					this.referenceResolver.getReferenceEntityPropertyName(masterClass,detailClass)));
		}
		
		
		
		private class MasterDetailValueChangeListener implements ValueChangeListener
		{
			private static final long		serialVersionUID	= 3306467309764402175L;
			
			private final EntityComponent	filter;
			private final EntityComponent	detailContainer;
			private final Object			detailProperty, filterProperty;
			
			
			public MasterDetailValueChangeListener(final EntityComponent filter,
					final EntityComponent detailContainer, final Object filterProperty,
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
				if(this.filter.getSelectedItem() != null)
				{
					prepareFilter(this.detailContainer.getEntityDataSource(),this.detailProperty,
							this.filter.getSelectedItem().getItemProperty(this.filterProperty)
									.getValue().toString());
				}
				else
				{
					clearFiltering(this.detailContainer.getEntityDataSource(),this.detailProperty);
				}
			}
		}
	}
}
