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


import java.awt.Color;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;


/**
 * @author XDEV Software
 *
 */
public interface TableExportStyles
{
	public final static TableExportStyles DEFAULT = new Default();


	public StyleBuilder titleStyle();


	public StyleBuilder footerStyle();


	public StyleBuilder columnTitleStyle();


	public StyleBuilder columnStyle();



	public static class Default implements TableExportStyles
	{
		protected final StyleBuilder	boldStyle		= Styles.style().bold().setPadding(2);
		protected final StyleBuilder	boldCenterStyle	= Styles.style(boldStyle)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		protected final StyleBuilder	columnTitle		= Styles.style(boldCenterStyle)
				.setBorder(Styles.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);
		protected final StyleBuilder	columnStyle		= Styles.style(boldStyle)
				.setBorder(Styles.pen1Point());


		@Override
		public StyleBuilder titleStyle()
		{
			return boldCenterStyle;
		}


		@Override
		public StyleBuilder footerStyle()
		{
			return boldCenterStyle;
		}


		@Override
		public StyleBuilder columnTitleStyle()
		{
			return this.columnTitle;
		}


		@Override
		public StyleBuilder columnStyle()
		{
			return this.columnStyle;
		}
	}
}
