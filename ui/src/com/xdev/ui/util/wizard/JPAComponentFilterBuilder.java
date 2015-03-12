
package com.xdev.ui.util.wizard;


import com.xdev.ui.entitycomponent.EntityComponent;


//master detail for components
public interface JPAComponentFilterBuilder extends XdevExecutableCommandObject
{
	public void setMasterComponent(EntityComponent<?> masterComponent);


	public void setDetailComponent(EntityComponent<?> detailComponent);


	public <T> void setMasterEntity(Class<T> masterEntity);


	public <T> void setDetailEntity(Class<T> detailEntity);
}
