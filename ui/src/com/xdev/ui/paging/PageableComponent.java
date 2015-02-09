
package com.xdev.ui.paging;


public interface PageableComponent<ET>
{
	// Request next page from ScrollableResultSet
	/**
	 * 
	 * @return the starting index before moving to the next page
	 */
	public void nextPage();
	
	
	// Request previous page from ScrollableResultSet
	public void previousPage();
	
	
	public void lastPage();
	
	
	public void firstPage();
	
	
	// Request last page from ScrollableResultSet
	public int getTotalAmountOfPages();
	
	
	// ...
	public void setCurrentPage(int currentPage);
	
	
	public int getCurrentPage();
	
	
	public void setPageLength(int pageLength);
	
	
	public int getPageLength();
}
