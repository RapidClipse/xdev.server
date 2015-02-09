
package com.xdev.ui.paging;


import org.hibernate.ScrollableResults;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;


public class PagedBeanItemContainer<ET> extends BeanItemContainer<ET> implements
		PageableContainer<ET>
{
	/**
	 * 
	 */
	private static final long		serialVersionUID				= 5612912594095794018L;
	private final ScrollableResults	result;
	private static final int		LANDING_PAGE					= 0;
	private int						currentPage						= LANDING_PAGE;
	private int						pageLength						= 5;
	private int						scrollableResultAddedItemCount	= 0;
	
	
	public PagedBeanItemContainer(ScrollableResults result, Class<ET> entitytypeClass)
	{
		// this.result = new TransactionalScrollableResults(result);
		super(entitytypeClass);
		this.result = result;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int nextPage()
	{
		if(this.getCurrentPage() < this.getTotalAmountOfPages())
		{
			this.internalSetCurrentPage(++this.currentPage);
			// this.prepareOtherPage();
			int currentPageRecordIndex = this.getCurrentPage() * this.getPageLength();
			
			int nextPageIndex = currentPageRecordIndex + this.getPageLength();
			while(nextPageIndex > currentPageRecordIndex++)
			{
				if(this.result.setRowNumber(currentPageRecordIndex))
				{
					this.scrollBean((ET)this.result.get()[0]);
				}
			}
			return currentPageRecordIndex;
		}
		throw new RuntimeException("Not able to scroll to next page");
	}
	
	
	// untyped hibernate API
	@SuppressWarnings("unchecked")
	@Override
	public int previousPage()
	{
		if(this.getCurrentPage() > 0)
		{
			this.internalSetCurrentPage(--this.currentPage);
			// this.prepareOtherPage();
			// FIXME Scrollen von zweite auf erste Seite führt zu 0 Ergebnis
			int currentPageIndex = this.getCurrentPage() * this.getPageLength();
			
			int previousPageIndex = currentPageIndex - this.getPageLength();
			while(previousPageIndex < currentPageIndex--)
			{
				if(this.result.setRowNumber(currentPageIndex))
				{
					this.scrollBean((ET)this.result.get()[0]);
				}
			}
			
			return previousPageIndex;
		}
		throw new RuntimeException("Not able to scroll to previous page");
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int lastPage()
	{
		this.internalSetCurrentPage(this.getTotalAmountOfPages());
		// this.prepareOtherPage();
		while(this.result.next())
		{
			this.scrollBean((ET)this.result.get()[0]);
		}
		
		return this.result.getRowNumber() - this.getPageLength();
	}
	
	
	@Override
	public int firstPage()
	{
		this.setCurrentPage(0);
		return 0;
	}
	
	
	// untyped hibernate API
	@SuppressWarnings("unchecked")
	@Override
	public void setCurrentPage(int currentPage)
	{
		this.internalSetCurrentPage(currentPage);
		if(this.getCurrentPage() < this.getTotalAmountOfPages() && this.getCurrentPage() >= 0)
		{
			// this.prepareOtherPage();
			int currentPageRecordIndex = this.getCurrentPage() * this.getPageLength();
			int nextPageIndex = currentPageRecordIndex + this.getPageLength();
			while(nextPageIndex > currentPageRecordIndex++)
			{
				if(this.result.setRowNumber(currentPageRecordIndex))
				{
					ET entity = (ET)this.result.get()[0];
					this.scrollBean(entity);
				}
			}
		}
	}
	
	
	@Override
	public int getTotalAmountOfPages()
	{
		this.result.last();
		
		if(result.getRowNumber() > 0)
		{
			if(this.result.getRowNumber() <= this.getPageLength())
			{
				return 1;
			}
			else
			{
				// double result = Math.ceil(this.result.getRowNumber() /
				// this.getPageLength());
				return new Double(this.result.getRowNumber() / this.getPageLength()).intValue() + 1;
			}
		}
		return 0;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	// determines the visible item count
	@Override
	public int size()
	{
		// System.out.println("current page: " + currentPage);
		// System.out.println("current index: " + this.getCurrentPage() *
		// this.getPageLength());
		
		int rowsLeft = this.getRealSize() - (this.getCurrentPage() * this.getPageLength());
		if(rowsLeft > this.getPageLength())
		{
			// System.out.println("return full page");
			return this.getPageLength();
		}
		else
		{
			// System.out.println("return partial page");
			return rowsLeft;
		}
	}
	
	
	// FIXME consider transient items outside the scrollable resultset
	public int getRealSize()
	{
		if(this.result.last())
		{
			// System.out.println("current row: " + this.result.getRowNumber());
			// System.out.println("super size: " + super.size());
			
			/*
			 * include transient items but remove already added / persistent
			 * items from scrollable result.
			 */
			// int itemCount = this.result.getRowNumber()
			// + this.compareTransientWithScrollableResultItemSize();
			// System.out.println("Itemcount: " + itemCount);
			return this.result.getRowNumber() + this.compareTransientWithScrollableResultItemSize();
		}
		throw new RuntimeException("No last row found");
	}
	
	
	private int compareTransientWithScrollableResultItemSize()
	{
		int transientItemCount = super.size() - this.scrollableResultAddedItemCount;
		
		if(transientItemCount > 0)
		{
			return transientItemCount;
		}
		
		return 0;
	}
	
	
	@Override
	public int getCurrentPage()
	{
		return this.currentPage;
	}
	
	
	@Override
	public int getPageLength()
	{
		return this.pageLength;
	}
	
	
	@Override
	public void setPageLength(int pageLength)
	{
		this.pageLength = pageLength;
	}
	
	
	protected void internalSetCurrentPage(int currentPage) throws RuntimeException
	{
		if(this.getCurrentPage() <= this.getTotalAmountOfPages() && this.getCurrentPage() >= 0)
		{
			this.currentPage = currentPage;
		}
		else
		{
			throw new RuntimeException("Current page not within a valid page range");
		}
	}
	
	
	@Override
	public BeanItem<ET> scrollBean(ET bean)
	{
		return this.lazyLoadBean(bean);
	}
	
	
	protected BeanItem<ET> lazyLoadBean(ET bean)
	{
		BeanItem<ET> beanItem = this.getItem(bean);
		if(beanItem == null)
		{
			this.scrollableResultAddedItemCount++;
			return this.addBean(bean);
		}
		else
		{
			return beanItem;
		}
	}
	
	
	@Override
	public ScrollableResults getResults()
	{
		return this.result;
	}
}
