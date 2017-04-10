/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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


import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;


/**
 * @author XDEV Software
 *
 */
public interface TableReportBuilder
{
	public final static TableReportBuilder DEFAULT = new Default();
	
	
	public JasperReportBuilder buildReport(Table table, List<Column> columns,
			TableExportSettings settings);
	
	
	
	public static class Default implements TableReportBuilder
	{
		protected final TableDataSourceFactory	dataSourceFactory;
		protected final TableExportStyles		styles;
		
		
		public Default()
		{
			this(TableDataSourceFactory.DEFAULT,TableExportStyles.DEFAULT);
		}
		
		
		public Default(final TableDataSourceFactory dataSourceFactory,
				final TableExportStyles styles)
		{
			this.dataSourceFactory = dataSourceFactory;
			this.styles = styles;
		}
		
		
		@Override
		public JasperReportBuilder buildReport(final Table table, final List<Column> columns,
				final TableExportSettings settings)
		{
			final JasperReportBuilder report = DynamicReports.report();
			final List<TextColumnBuilder<?>> jasperCols = createTextColumns(columns);
			
			for(final TextColumnBuilder<?> textColumnBuilder : jasperCols)
			{
				report.columns(textColumnBuilder);
			}
			
			report.setColumnTitleStyle(this.styles.columnTitleStyle());
			report.setColumnStyle(this.styles.columnStyle());
			
			final String title = settings.getTitle();
			if(title != null)
			{
				report.title(Components.text(title).setStyle(this.styles.titleStyle()));
				report.setReportName(title);
			}
			else
			{
				report.setReportName("TableExport_" + System.currentTimeMillis());
			}
			
			if(settings.isShowPageNumber())
			{
				report.pageFooter(
						DynamicReports.cmp.pageXofY().setStyle(this.styles.footerStyle()));
			}
			
			if(settings.isHighlightRows())
			{
				report.highlightDetailOddRows();
			}
			
			report.setShowColumnTitle(true);
			report.setDataSource(this.dataSourceFactory.createDataSource(table,columns));
			report.setPageFormat(settings.getPageType(),settings.getPageOrientation());
			report.setPageMargin(DynamicReports.margin(20));
			
			return report;
		}
		
		
		protected List<TextColumnBuilder<?>> createTextColumns(final List<Column> columns)
		{
			final List<TextColumnBuilder<?>> reportColums = new ArrayList<TextColumnBuilder<?>>();
			
			for(final Column column : columns)
			{
				final TextColumnBuilder<String> reportColumn = Columns
						.column(column.getColumnHeader(),column.getColumnHeader(),String.class);
				
				final Integer width = column.getColumnWidth();
				if(width != null && width > 0)
				{
					reportColumn.setFixedWidth(width);
				}
				
				reportColumn.setHorizontalTextAlignment(
						convertVaadinJasperTextAlignment(column.getColumnAlignment()));
				
				reportColums.add(reportColumn);
			}
			
			return reportColums;
		}
		
		
		protected HorizontalTextAlignment convertVaadinJasperTextAlignment(final Align alignment)
		{
			if(alignment.equals(Align.RIGHT))
			{
				return HorizontalTextAlignment.RIGHT;
			}
			else if(alignment.equals(Align.CENTER))
			{
				return HorizontalTextAlignment.CENTER;
			}
			else
			{
				return HorizontalTextAlignment.LEFT;
			}
		}
	}
}
