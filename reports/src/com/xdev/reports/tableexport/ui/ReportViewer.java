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

package com.xdev.reports.tableexport.ui;


import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.xdev.ui.XdevBrowserFrame;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevView;


public class ReportViewer extends XdevView
{
	public ReportViewer(final Resource resource)
	{
		super();
		this.initUI();

		this.browserFrame.setSource(resource);
	}


	private void initUI()
	{
		this.verticalLayout = new XdevVerticalLayout();
		this.browserFrame = new XdevBrowserFrame();

		this.verticalLayout.setMargin(new MarginInfo(false));

		this.browserFrame.setSizeFull();
		this.verticalLayout.addComponent(this.browserFrame);
		this.verticalLayout.setComponentAlignment(this.browserFrame,Alignment.MIDDLE_CENTER);
		this.verticalLayout.setExpandRatio(this.browserFrame,0.1F);
		this.verticalLayout.setSizeFull();
		this.setContent(this.verticalLayout);
		this.setWidth(1200,Unit.PIXELS);
		this.setHeight(800,Unit.PIXELS);
	}

	private XdevBrowserFrame	browserFrame;
	private XdevVerticalLayout	verticalLayout;
}
