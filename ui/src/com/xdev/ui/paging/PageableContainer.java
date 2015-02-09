
package com.xdev.ui.paging;


import org.hibernate.ScrollableResults;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;


public interface PageableContainer<ET> extends Container
{
	// Request next page from ScrollableResultSet
	/**
	 * 
	 * @return the starting index before moving to the next page
	 */
	public int nextPage();
	
	
	// Request previous page from ScrollableResultSet
	public int previousPage();
	
	
	public int lastPage();
	
	
	public int firstPage();
	
	
	// Request last page from ScrollableResultSet
	public int getTotalAmountOfPages();
	
	
	// ...
	public void setCurrentPage(int currentPage);
	
	
	public int getCurrentPage();
	
	
	public void setPageLength(int pageLength);
	
	
	public int getPageLength();
	
	
	public BeanItem<ET> scrollBean(ET bean);
	
	
	public ScrollableResults getResults();
	
}
