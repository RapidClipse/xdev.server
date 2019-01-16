/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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

package com.xdev.reports.tableexport;


import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;


public class TableExportSettings
{
	private String			title			= "";
	private PageType		pageType		= PageType.A4;
	private PageOrientation	pageOrientation	= PageOrientation.PORTRAIT;
	private boolean			showPageNumber	= false;
	private boolean			highlightRows	= false;


	public TableExportSettings()
	{
	}
	
	
	public String getTitle()
	{
		return this.title;
	}
	
	
	public void setTitle(final String title)
	{
		this.title = title;
	}
	
	
	public PageType getPageType()
	{
		return this.pageType;
	}
	
	
	public void setPageType(final PageType pageType)
	{
		this.pageType = pageType;
	}
	
	
	public PageOrientation getPageOrientation()
	{
		return this.pageOrientation;
	}
	
	
	public void setPageOrientation(final PageOrientation pageOrientation)
	{
		this.pageOrientation = pageOrientation;
	}
	
	
	public boolean isShowPageNumber()
	{
		return this.showPageNumber;
	}
	
	
	public void setShowPageNumber(final boolean showPageNumber)
	{
		this.showPageNumber = showPageNumber;
	}


	public boolean isHighlightRows()
	{
		return this.highlightRows;
	}


	public void setHighlightRows(final boolean highlightRows)
	{
		this.highlightRows = highlightRows;
	}
}
