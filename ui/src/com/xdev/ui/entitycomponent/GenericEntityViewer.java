
package com.xdev.ui.entitycomponent;


import com.vaadin.data.Container;
import com.vaadin.data.Container.Viewer;


public interface GenericEntityViewer<T extends Container.Filterable> extends Viewer
{

	/**
	 * Sets the Container that serves as the data source of the viewer.
	 *
	 * @param newDataSource
	 *            The new data source Item
	 */
	public void setGenericDataSource(T newDataSource);


	/**
	 * Gets the Container serving as the data source of the viewer.
	 *
	 * @return data source Container
	 */
	@Override
	public T getContainerDataSource();

}
