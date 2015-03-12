
package com.xdev.ui.entitycomponent;


import com.vaadin.data.Container.Viewer;


public interface GenericEntityViewer<BEANTYPE> extends Viewer
{

	/**
	 * Sets the Container that serves as the data source of the viewer.
	 *
	 * @param newDataSource
	 *            The new data source Item
	 */
	public void setEntityDataSource(EntityContainer<BEANTYPE> newDataSource);


	/**
	 * Gets the Container serving as the data source of the viewer.
	 *
	 * @return data source Container
	 */
	public EntityContainer<BEANTYPE> getEntityDataSource();

}
