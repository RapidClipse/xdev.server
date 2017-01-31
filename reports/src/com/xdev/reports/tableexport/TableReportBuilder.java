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


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;
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
		protected final StyleBuilder	BOLDSTYLE			= Styles.style().bold().setPadding(2);

		protected final StyleBuilder	BOLDCENTERSTYLE		= Styles.style(BOLDSTYLE)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

		protected final StyleBuilder	COLUMNTITLESTYLE	= Styles.style(BOLDCENTERSTYLE)
				.setBorder(Styles.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);

		protected final StyleBuilder	COLUMNSTYLE			= Styles.style(BOLDSTYLE)
				.setBorder(Styles.pen1Point());


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

			report.setColumnTitleStyle(COLUMNTITLESTYLE);
			report.setColumnStyle(COLUMNSTYLE);

			final String title = settings.getTitle();
			if(title != null)
			{
				report.title(Components.text(title).setStyle(BOLDCENTERSTYLE));
				report.setReportName(title);
			}
			else
			{
				report.setReportName("TableExport_" + System.currentTimeMillis());
			}

			if(settings.isShowPageNumber())
			{
				report.pageFooter(DynamicReports.cmp.pageXofY().setStyle(BOLDCENTERSTYLE));
			}

			report.setShowColumnTitle(true);
			report.setDataSource(DataSourceFactory.createDataSource(table,columns));
			report.setPageFormat(settings.getPageType(),settings.getPageOrientation());
			report.setPageMargin(DynamicReports.margin(20));

			return report;
		}


		protected List<TextColumnBuilder<?>> createTextColumns(final List<Column> columns)
		{
			final List<TextColumnBuilder<?>> reportColums = new ArrayList<TextColumnBuilder<?>>();

			for(final Column col : columns)
			{
				final TextColumnBuilder<String> column = Columns.column(col.getColumnHeader(),
						col.getColumnHeader(),String.class);

				if(col.getColumnWidth() > 0)
				{
					column.setFixedWidth(col.getColumnWidth());
				}

				column.setHorizontalTextAlignment(
						convertVaadinJasperTextAlignment(col.getColumnAlignment()));

				reportColums.add(column);
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
