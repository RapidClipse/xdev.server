
package com.xdev.ui.util.wizard;


import com.vaadin.data.Container;
import com.vaadin.ui.AbstractSelect;


//master detail for components
public interface ComponentFilterBuilder extends XdevExecutableCommandObject
{
	public void setMasterComponent(AbstractSelect masterComponent);
	
	
	public void setFilterableDetail(Container.Filterable filterable);
	
	
	public void setMasterProperty(Object masterProperty);
	
	
	public void setDetailProperty(Object detailProperty);
}
