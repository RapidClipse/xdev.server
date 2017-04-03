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


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;
import com.xdev.reports.ExportType;
import com.xdev.res.StringResourceUtils;
import com.xdev.ui.PopupWindow;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevView;
import com.xdev.ui.entitycomponent.listselect.XdevOptionGroup;
import com.xdev.ui.util.UIUtils;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;


public class FinishView extends XdevView
{
	private Resource					resource;
	private final JasperReportBuilder	builder;
	
	private final FileDownloader		fileDownloader;


	public FinishView(final JasperReportBuilder builder, final ExportType... exportTypes)
	{
		super();
		this.initUI();

		this.builder = builder;

		this.resource = createEmptyResource();

		this.fileDownloader = new FileDownloader(this.resource);
		this.fileDownloader.setOverrideContentType(false);
		this.fileDownloader.extend(this.btnSave);
		this.fileDownloader.extend(this.btnViewAndSave);

		for(final ExportType exportType : exportTypes)
		{
			this.optionGroup.addItem(exportType);
		}
	}


	private Resource createEmptyResource()
	{
		final StreamResource resource = new StreamResource(new StreamSource()
		{
			@Override
			public InputStream getStream()
			{
				return new ByteArrayInputStream(new byte[0]);
			}
		},"empty");
		resource.setMIMEType("text/plain");
		return resource;
	}


	/**
	 * Event handler delegate method for the {@link XdevButton} {@link #btnView}
	 * .
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void btnView_buttonClick(final Button.ClickEvent event)
	{
		view();
	}
	
	
	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #btnViewAndSave}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void btnViewAndSave_buttonClick(final Button.ClickEvent event)
	{
		view();
	}


	private void view()
	{
		try
		{
			this.resource = ((ExportType)this.optionGroup.getValue())
					.exportToResource(this.builder);
			this.fileDownloader.setFileDownloadResource(this.resource);

			PopupWindow.For(new ReportViewer(this.resource)).closable(true).draggable(true)
					.resizable(true).modal(true).show();
		}
		catch(final DRException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdClose} .
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdClose_buttonClick(final Button.ClickEvent event)
	{
		UIUtils.getNextParent(this,Window.class).close();
	}


	/*
	 * WARNING: Do NOT edit!<br>The content of this method is always regenerated
	 * by the UI designer.
	 */
	// <generated-code name="initUI">
	private void initUI()
	{
		this.verticalLayout = new XdevVerticalLayout();
		this.optionGroup = new XdevOptionGroup<>();
		this.btnView = new XdevButton();
		this.btnSave = new XdevButton();
		this.btnViewAndSave = new XdevButton();
		this.cmdClose = new XdevButton();

		this.optionGroup
				.setCaption(StringResourceUtils.optLocalizeString("{$outputtype.title}",this));
		this.btnView
				.setCaption(StringResourceUtils.optLocalizeString("{$showexport.caption}",this));
		this.btnSave
				.setCaption(StringResourceUtils.optLocalizeString("{$saveexport.caption}",this));
		this.btnViewAndSave
				.setCaption(StringResourceUtils.optLocalizeString("{$saveandshow.caption}",this));
		this.cmdClose.setCaption(StringResourceUtils.optLocalizeString("{$close}",this));

		this.optionGroup.setWidth(100,Unit.PERCENTAGE);
		this.optionGroup.setHeight(-1,Unit.PIXELS);
		this.verticalLayout.addComponent(this.optionGroup);
		this.verticalLayout.setComponentAlignment(this.optionGroup,Alignment.MIDDLE_LEFT);
		this.btnView.setWidth(100,Unit.PERCENTAGE);
		this.btnView.setHeight(-1,Unit.PIXELS);
		this.verticalLayout.addComponent(this.btnView);
		this.verticalLayout.setComponentAlignment(this.btnView,Alignment.MIDDLE_CENTER);
		this.btnSave.setWidth(100,Unit.PERCENTAGE);
		this.btnSave.setHeight(-1,Unit.PIXELS);
		this.verticalLayout.addComponent(this.btnSave);
		this.verticalLayout.setComponentAlignment(this.btnSave,Alignment.MIDDLE_CENTER);
		this.btnViewAndSave.setWidth(100,Unit.PERCENTAGE);
		this.btnViewAndSave.setHeight(-1,Unit.PIXELS);
		this.verticalLayout.addComponent(this.btnViewAndSave);
		this.verticalLayout.setComponentAlignment(this.btnViewAndSave,Alignment.MIDDLE_CENTER);
		this.cmdClose.setWidth(100,Unit.PERCENTAGE);
		this.cmdClose.setHeight(-1,Unit.PIXELS);
		this.verticalLayout.addComponent(this.cmdClose);
		this.verticalLayout.setComponentAlignment(this.cmdClose,Alignment.BOTTOM_CENTER);
		this.verticalLayout.setExpandRatio(this.cmdClose,10.0F);
		this.verticalLayout.setSizeFull();
		this.setContent(this.verticalLayout);
		this.setWidth(350,Unit.PIXELS);
		this.setHeight(500,Unit.PIXELS);

		this.btnView.addClickListener(event -> this.btnView_buttonClick(event));
		this.btnViewAndSave.addClickListener(event -> this.btnViewAndSave_buttonClick(event));
		this.cmdClose.addClickListener(event -> this.cmdClose_buttonClick(event));
	} // </generated-code>
	
	// <generated-code name="variables">
	private XdevButton							btnView, btnSave, btnViewAndSave, cmdClose;
	private XdevOptionGroup<CustomComponent>	optionGroup;
	private XdevVerticalLayout					verticalLayout;								// </generated-code>

}
