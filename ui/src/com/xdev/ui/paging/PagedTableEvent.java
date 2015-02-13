
package com.xdev.ui.paging;


import com.xdev.ui.entitycomponent.table.XdevPagedTable;


public class PagedTableEvent<EntityType>
{
	final XdevPagedTable<EntityType>	table;


	public PagedTableEvent(final XdevPagedTable<EntityType> table)
	{
		this.table = table;
	}


	public XdevPagedTable<EntityType> getTable()
	{
		return this.table;
	}


	public int getCurrentPage()
	{
		return this.table.getCurrentPage();
	}


	public int getTotalAmountOfPages()
	{
		return this.table.getTotalAmountOfPages();
	}
}
