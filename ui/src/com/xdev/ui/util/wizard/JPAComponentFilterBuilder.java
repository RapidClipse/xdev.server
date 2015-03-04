
package com.xdev.ui.util.wizard;


import com.xdev.ui.entitycomponent.GenericEntityComponent;


//master detail for components
public interface JPAComponentFilterBuilder extends XdevExecutableCommandObject
{
	public void setMasterComponent(GenericEntityComponent<?, ?> masterComponent);
	
	
	public void setDetailComponent(GenericEntityComponent<?, ?> detailComponent);
	
	
	public <T> void setMasterEntity(Class<T> masterEntity);
	
	
	public <T> void setDetailEntity(Class<T> detailEntity);
}
