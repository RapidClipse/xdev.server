
package com.xdev.ui.paging;


public interface PageChangedListener<EntityType>
{
	public void pageChanged(PagedTableEvent<EntityType> event);
	
}
