/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui.paging;


import org.hibernate.ScrollableResults;

import com.vaadin.v7.data.util.BeanItem;
import com.xdev.ui.entitycomponent.XdevBeanItemContainer;


@SuppressWarnings("deprecation")
public class PagedBeanItemContainer<ET> extends XdevBeanItemContainer<ET>
		implements PageableContainer<ET>
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


	public PagedBeanItemContainer(final ScrollableResults result, final Class<ET> entitytypeClass)
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

			final int nextPageIndex = currentPageRecordIndex + this.getPageLength();
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

			final int previousPageIndex = currentPageIndex - this.getPageLength();
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
	public void setCurrentPage(final int currentPage)
	{
		this.internalSetCurrentPage(currentPage);
		if(this.getCurrentPage() < this.getTotalAmountOfPages() && this.getCurrentPage() >= 0)
		{
			// this.prepareOtherPage();
			int currentPageRecordIndex = this.getCurrentPage() * this.getPageLength();
			final int nextPageIndex = currentPageRecordIndex + this.getPageLength();
			while(nextPageIndex > currentPageRecordIndex++)
			{
				if(this.result.setRowNumber(currentPageRecordIndex))
				{
					final ET entity = (ET)this.result.get()[0];
					this.scrollBean(entity);
				}
			}
		}
	}


	@Override
	public int getTotalAmountOfPages()
	{
		this.result.last();

		if(this.result.getRowNumber() > 0)
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

		final int rowsLeft = this.getRealSize() - (this.getCurrentPage() * this.getPageLength());
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
			 * include transient items but remove already added / persistent items from
			 * scrollable result.
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
		final int transientItemCount = super.size() - this.scrollableResultAddedItemCount;

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
	public void setPageLength(final int pageLength)
	{
		this.pageLength = pageLength;
	}


	protected void internalSetCurrentPage(final int currentPage) throws RuntimeException
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
	public BeanItem<ET> scrollBean(final ET bean)
	{
		return this.lazyLoadBean(bean);
	}


	protected BeanItem<ET> lazyLoadBean(final ET bean)
	{
		final BeanItem<ET> beanItem = this.getItem(bean);
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
