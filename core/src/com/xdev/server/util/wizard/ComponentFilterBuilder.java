
package com.xdev.server.util.wizard;


import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.ui.AbstractSelect;
import com.xdev.server.util.MasterDetail;


//master detail for components
public interface ComponentFilterBuilder extends XdevExecutableCommandObject
{
	public void setMasterComponent(AbstractSelect masterComponent);
	
	
	public void setFilterableDetail(Container.Filterable filterable);
	
	
	public void setMasterProperty(Object masterProperty);
	
	
	public void setDetailProperty(Object detailProperty);
	
	
	
	public class XdevComponentFilterBuilder implements ComponentFilterBuilder
	{
		private AbstractSelect	masterComponent;
		private Filterable		filterableDetailComponent;
		private Object			masterProperty, detailProperty;
		
		
		private MasterDetail	masterDetail;
		
		
		public XdevComponentFilterBuilder()
		{
			//TODO customizable?
			this.masterDetail = new MasterDetail.Implementation();
		}
		
		
		@Override
		public void execute()
		{
			this.masterDetail.connectMasterDetail(this.masterComponent,
					this.filterableDetailComponent,this.masterProperty,this.detailProperty);
		}
		
		
		@Override
		public void setMasterComponent(AbstractSelect masterComponent)
		{
			this.masterComponent = masterComponent;
		}
		
		
		@Override
		public void setFilterableDetail(Filterable filterable)
		{
			this.filterableDetailComponent = filterable;
		}
		
		
		@Override
		public void setMasterProperty(Object masterProperty)
		{
			this.masterProperty = masterProperty;
		}
		
		
		@Override
		public void setDetailProperty(Object detailProperty)
		{
			this.detailProperty = detailProperty;
		}
	}
}
