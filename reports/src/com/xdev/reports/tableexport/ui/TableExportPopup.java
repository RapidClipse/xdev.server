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

package com.xdev.reports.tableexport.ui;


import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.xdev.reports.ExportType;
import com.xdev.reports.ReportException;
import com.xdev.reports.tableexport.Column;
import com.xdev.reports.tableexport.TableExportSettings;
import com.xdev.reports.tableexport.TableReportBuilder;
import com.xdev.res.StringResourceUtils;
import com.xdev.ui.DynamicFileDownloader;
import com.xdev.ui.PopupWindow;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevCheckBox;
import com.xdev.ui.XdevGridLayout;
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevTextField;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.entitycomponent.combobox.XdevComboBox;
import com.xdev.ui.entitycomponent.table.XdevTable;
import com.xdev.ui.util.UIUtils;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;


public class TableExportPopup
{
	public static void show(final XdevTable<?> table)
	{
		show(table,ExportType.values());
	}
	
	
	public static void show(final XdevTable<?> table, final ExportType... availableExportTypes)
	{
		PopupWindow.For(new TableExportPopup(table,availableExportTypes).getRoot()).closable(true)
				.draggable(true).resizable(false).modal(true).show();
	}
	
	protected final String				PROPERTY_SELECTED		= "...";
	protected final String				PROPERTY_COLUMNNAME		= StringResourceUtils
			.localizeString("{$columnName}",this);
	protected final String				PROPERTY_COLUMNWIDTH	= StringResourceUtils
			.localizeString("{$columnWidth}",this);
	protected final String				PROPERTY_COLUMNID		= StringResourceUtils
			.localizeString("{$columnId}",this);
	
	protected TableReportBuilder		tableReportBuilder		= TableReportBuilder.DEFAULT;
	protected final TableExportSettings	exportSettings			= new TableExportSettings();
	protected final XdevTable<?>		table;
	
	
	protected TableExportPopup(final XdevTable<?> table, final ExportType... availableExportTypes)
	{
		this.table = table;
		
		this.initUI();
		
		this.cbPageType.addItems((Object[])PageType.values());
		this.cbPageType.setValue(this.exportSettings.getPageType());
		
		this.cbPageOrientation.addItems((Object[])PageOrientation.values());
		this.cbPageOrientation.setValue(this.exportSettings.getPageOrientation());
		
		this.chkShowPageNumber.setValue(this.exportSettings.isShowPageNumber());
		this.chkHighlightRows.setValue(this.exportSettings.isHighlightRows());
		
		this.cmbFormat.addItems((Object[])availableExportTypes);
		this.cmbFormat.setValue(availableExportTypes[0]);
		
		this.tblColumnChooser.addContainerProperty(this.PROPERTY_SELECTED,CheckBox.class,null,"",
				FontAwesome.EYE,Align.CENTER);
		this.tblColumnChooser.addContainerProperty(this.PROPERTY_COLUMNNAME,String.class,null);
		this.tblColumnChooser.addContainerProperty(this.PROPERTY_COLUMNWIDTH,TextField.class,null);
		this.tblColumnChooser.addContainerProperty(this.PROPERTY_COLUMNID,Object.class,null);
		
		this.tblColumnChooser.setColumnExpandRatio(this.PROPERTY_COLUMNNAME,1.0F);
		
		this.tblColumnChooser.setColumnAlignment(this.PROPERTY_SELECTED,Table.Align.CENTER);
		this.tblColumnChooser.setColumnAlignment(this.PROPERTY_COLUMNWIDTH,Table.Align.CENTER);
		
		int id = 0;
		
		for(final Object columnId : this.table.getVisibleColumns())
		{
			// Don't show generated columns
			if(this.table.getColumnGenerator(columnId) != null)
			{
				continue;
			}
			
			final TextField txtWidth = new TextField();
			txtWidth.addStyleName("small, borderless");
			txtWidth.setColumns(4);
			txtWidth.setImmediate(true);
			
			final CheckBox selectBox = new CheckBox();
			selectBox.setValue(true);
			
			final int columnWidth = this.table.getColumnWidth(columnId);
			if(columnWidth > 0)
			{
				txtWidth.setValue(String.valueOf(columnWidth));
			}
			
			this.tblColumnChooser.addItem(
					new Object[]{selectBox,this.table.getColumnHeader(columnId),txtWidth,columnId},
					++id);
			
			selectBox.addValueChangeListener(event -> check());
			txtWidth.addValueChangeListener(event -> check());
		}

		this.tblColumnChooser.setVisibleColumns(this.PROPERTY_SELECTED,this.PROPERTY_COLUMNNAME,
				this.PROPERTY_COLUMNWIDTH);
		
		DynamicFileDownloader.install(this.cmdSave,this::createReportResource);
		
		check();
	}
	
	
	public void setReportBuilder(final TableReportBuilder reportBuilder)
	{
		this.tableReportBuilder = reportBuilder;
	}
	
	
	protected void cmdSelectAll_buttonClick(final Button.ClickEvent event)
	{
		selectAll(true);
	}
	
	
	protected void cmdSelectNone_buttonClick(final Button.ClickEvent event)
	{
		selectAll(false);
	}
	
	
	protected void selectAll(final boolean status)
	{
		for(final Object object : this.tblColumnChooser.getItemIds())
		{
			final Item item = this.tblColumnChooser.getItem(object);
			((CheckBox)item.getItemProperty(this.PROPERTY_SELECTED).getValue()).setValue(status);
		}
	}
	
	
	protected void cbPageType_valueChange(final Property.ValueChangeEvent event)
	{
		this.exportSettings.setPageType((PageType)this.cbPageType.getValue());
		
		check();
	}
	
	
	protected void cbPageOrientation_valueChange(final Property.ValueChangeEvent event)
	{
		this.exportSettings.setPageOrientation((PageOrientation)this.cbPageOrientation.getValue());
		
		check();
	}
	
	
	protected void check()
	{
		if(checkColumnWidth())
		{
			this.lblStatus.setVisible(false);
			this.cmdSave.setEnabled(true);
			this.cmdShow.setEnabled(true);
		}
		else
		{
			this.lblStatus.setVisible(true);
			this.cmdSave.setEnabled(false);
			this.cmdShow.setEnabled(false);
		}
	}
	
	
	protected boolean checkColumnWidth()
	{
		final Integer calculateFixedWidth = calculateFixedWidth();
		
		if(this.cbPageType.getValue() != null && this.cbPageOrientation.getValue() != null)
		{
			if(this.cbPageOrientation.getValue().equals(PageOrientation.PORTRAIT))
			{
				if(calculateFixedWidth > ((PageType)this.cbPageType.getValue()).getWidth() - 40)
				{
					return false;
				}
			}
			else
			{
				if(calculateFixedWidth > ((PageType)this.cbPageType.getValue()).getHeight() - 40)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	protected int calculateFixedWidth()
	{
		int width = 0;
		
		for(final Object object : getSelectedColumns())
		{
			final Item item = this.tblColumnChooser.getItem(object);
			final TextField txt = (TextField)item.getItemProperty(this.PROPERTY_COLUMNWIDTH)
					.getValue();
			try
			{
				width += Integer.parseInt(txt.getValue());
			}
			catch(final Exception e)
			{
			}
		}
		
		return width;
	}
	
	
	protected List<Object> getSelectedColumns()
	{
		final List<Object> columnsToExport = new ArrayList<>();
		
		for(final Object object : this.tblColumnChooser.getItemIds())
		{
			final Item item = this.tblColumnChooser.getItem(object);
			if(((CheckBox)item.getItemProperty(this.PROPERTY_SELECTED).getValue()).getValue())
			{
				columnsToExport.add(object);
			}
		}
		
		return columnsToExport;
	}
	
	
	private void cmdClose_buttonClick(final Button.ClickEvent event)
	{
		UIUtils.getNextParent(this.root,Window.class).close();
	}
	
	
	private void cmdShow_buttonClick(final Button.ClickEvent event)
	{
		final BrowserFrame browserFrame = new BrowserFrame();
		browserFrame.setSource(createReportResource());
		PopupWindow.For(browserFrame).size(400,300).caption(this.exportSettings.getTitle())
				.maximized(true).closable(true).draggable(true).resizable(true).modal(true).show();
	}
	
	
	private Resource createReportResource() throws ReportException
	{
		this.exportSettings.setTitle(this.txtReportName.getValue());
		this.exportSettings.setShowPageNumber(this.chkShowPageNumber.getValue());
		this.exportSettings.setHighlightRows(this.chkHighlightRows.getValue());
		
		final JasperReportBuilder builder = this.tableReportBuilder.buildReport(this.table,
				createColumns(),this.exportSettings);
		return ((ExportType)this.cmbFormat.getValue()).exportToResource(builder,
				this.exportSettings.getTitle());
	}
	
	
	private List<Column> createColumns()
	{
		final List<Column> columns = new ArrayList<Column>();
		
		for(final Object id : this.tblColumnChooser.getItemIds())
		{
			final Item item = this.tblColumnChooser.getItem(id);
			
			if(!((CheckBox)item.getItemProperty(this.PROPERTY_SELECTED).getValue()).getValue())
			{
				continue;
			}
			
			final String columnHeader = (String)item.getItemProperty(this.PROPERTY_COLUMNNAME)
					.getValue();
			Integer columnWidth = null;
			try
			{
				columnWidth = Integer.parseInt(
						((TextField)item.getItemProperty(this.PROPERTY_COLUMNWIDTH).getValue())
								.getValue());
			}
			catch(final Exception e)
			{
			}
			
			final Object propertyId = item.getItemProperty(this.PROPERTY_COLUMNID).getValue();
			final Converter<String, Object> converter = this.table.getConverter(propertyId);
			final Align columnAlignment = this.table.getColumnAlignment(propertyId);
			final Class<?> type = this.table.getBeanContainerDataSource().getType(propertyId);
			
			final Column col = new Column(columnHeader,columnWidth,converter,columnAlignment,type,
					propertyId);
			
			columns.add(col);
		}
		
		return columns;
	}
	
	
	protected XdevGridLayout getRoot()
	{
		return this.root;
	}
	
	
	private void initUI()
	{
		this.root = new XdevGridLayout();
		this.txtReportName = new XdevTextField();
		this.tblColumnChooser = new XdevTable<>();
		this.layoutTableButtons = new XdevVerticalLayout();
		this.cmdSelectAll = new XdevButton();
		this.cmdSelectNone = new XdevButton();
		this.layoutSettings = new XdevGridLayout();
		this.cbPageType = new XdevComboBox<>();
		this.cbPageOrientation = new XdevComboBox<>();
		this.chkShowPageNumber = new XdevCheckBox();
		this.cmbFormat = new XdevComboBox<>();
		this.chkHighlightRows = new XdevCheckBox();
		this.footer = new XdevGridLayout();
		this.lblStatus = new XdevLabel();
		this.cmdClose = new XdevButton();
		this.cmdSave = new XdevButton();
		this.cmdShow = new XdevButton();
		
		this.root.setIcon(FontAwesome.TABLE);
		this.root.setCaption(StringResourceUtils.localizeString("{$caption}",this));
		this.txtReportName.setCaption(StringResourceUtils.localizeString("{$title}",this));
		this.txtReportName.setStyleName("small");
		this.txtReportName.setColumns(25);
		this.txtReportName.setValue("Report");
		this.txtReportName.setImmediate(true);
		this.txtReportName.setRequired(true);
		this.tblColumnChooser.setCaption(StringResourceUtils.localizeString("{$columns}",this));
		this.tblColumnChooser.setStyleName("compact");
		this.layoutTableButtons.setMargin(new MarginInfo(true,false,false,false));
		this.cmdSelectAll.setCaption(StringResourceUtils.localizeString("{$selectAll}",this));
		this.cmdSelectAll.setStyleName("borderless small link");
		this.cmdSelectNone.setCaption(StringResourceUtils.localizeString("{$selectNone}",this));
		this.cmdSelectNone.setStyleName("borderless small link");
		this.layoutSettings.setMargin(new MarginInfo(false));
		this.cbPageType.setCaption(StringResourceUtils.localizeString("{$format}",this));
		this.cbPageType.setStyleName("small");
		this.cbPageType.setNullSelectionAllowed(false);
		this.cbPageOrientation
				.setCaption(StringResourceUtils.localizeString("{$orientation}",this));
		this.cbPageOrientation.setStyleName("small");
		this.cbPageOrientation.setNullSelectionAllowed(false);
		this.chkShowPageNumber
				.setCaption(StringResourceUtils.localizeString("{$showPageNumbers}",this));
		this.cmbFormat.setCaption(StringResourceUtils.localizeString("{$format}",this));
		this.cmbFormat.setNullSelectionAllowed(false);
		this.chkHighlightRows
				.setCaption(StringResourceUtils.localizeString("{$highlightRows}",this));
		this.footer.setStyleName("dark");
		this.footer.setMargin(new MarginInfo(false));
		this.lblStatus.setValue(StringResourceUtils.localizeString("{$widthTooBigError}",this));
		this.lblStatus.setStyleName("small failure");
		this.cmdClose.setCaption(StringResourceUtils.localizeString("{$close}",this));
		this.cmdSave.setIcon(FontAwesome.SAVE);
		this.cmdSave.setCaption(StringResourceUtils.localizeString("{$save}",this));
		this.cmdSave.setStyleName("friendly");
		this.cmdShow.setIcon(FontAwesome.EYE);
		this.cmdShow.setCaption(StringResourceUtils.localizeString("{$show}",this));
		this.cmdShow.setStyleName("primary");
		
		this.cmdSelectAll.setWidth(100,Unit.PERCENTAGE);
		this.cmdSelectAll.setHeight(-1,Unit.PIXELS);
		this.layoutTableButtons.addComponent(this.cmdSelectAll);
		this.layoutTableButtons.setComponentAlignment(this.cmdSelectAll,Alignment.MIDDLE_LEFT);
		this.cmdSelectNone.setWidth(100,Unit.PERCENTAGE);
		this.cmdSelectNone.setHeight(-1,Unit.PIXELS);
		this.layoutTableButtons.addComponent(this.cmdSelectNone);
		this.layoutTableButtons.setComponentAlignment(this.cmdSelectNone,Alignment.MIDDLE_CENTER);
		this.layoutSettings.setColumns(4);
		this.layoutSettings.setRows(2);
		this.cbPageType.setWidth(100,Unit.PERCENTAGE);
		this.cbPageType.setHeight(-1,Unit.PIXELS);
		this.layoutSettings.addComponent(this.cbPageType,0,0,0,1);
		this.cbPageOrientation.setWidth(100,Unit.PERCENTAGE);
		this.cbPageOrientation.setHeight(-1,Unit.PIXELS);
		this.layoutSettings.addComponent(this.cbPageOrientation,1,0,1,1);
		this.chkShowPageNumber.setSizeUndefined();
		this.layoutSettings.addComponent(this.chkShowPageNumber,2,0);
		this.cmbFormat.setWidth(100,Unit.PERCENTAGE);
		this.cmbFormat.setHeight(-1,Unit.PIXELS);
		this.layoutSettings.addComponent(this.cmbFormat,3,0,3,1);
		this.chkHighlightRows.setSizeUndefined();
		this.layoutSettings.addComponent(this.chkHighlightRows,2,1);
		this.layoutSettings.setColumnExpandRatio(0,10.0F);
		this.layoutSettings.setColumnExpandRatio(1,10.0F);
		this.layoutSettings.setColumnExpandRatio(3,10.0F);
		this.footer.setColumns(4);
		this.footer.setRows(1);
		this.lblStatus.setSizeUndefined();
		this.footer.addComponent(this.lblStatus,0,0);
		this.cmdClose.setSizeUndefined();
		this.footer.addComponent(this.cmdClose,1,0);
		this.footer.setComponentAlignment(this.cmdClose,Alignment.MIDDLE_RIGHT);
		this.cmdSave.setSizeUndefined();
		this.footer.addComponent(this.cmdSave,2,0);
		this.cmdShow.setSizeUndefined();
		this.footer.addComponent(this.cmdShow,3,0);
		this.footer.setColumnExpandRatio(0,10.0F);
		this.root.setColumns(2);
		this.root.setRows(4);
		this.txtReportName.setSizeUndefined();
		this.root.addComponent(this.txtReportName,0,0);
		this.tblColumnChooser.setWidth(100,Unit.PERCENTAGE);
		this.tblColumnChooser.setHeight(300,Unit.PIXELS);
		this.root.addComponent(this.tblColumnChooser,0,1);
		this.layoutTableButtons.setSizeUndefined();
		this.root.addComponent(this.layoutTableButtons,1,1);
		this.layoutSettings.setSizeUndefined();
		this.root.addComponent(this.layoutSettings,0,2);
		this.footer.setWidth(100,Unit.PERCENTAGE);
		this.footer.setHeight(-1,Unit.PIXELS);
		this.root.addComponent(this.footer,0,3,1,3);
		this.root.setComponentAlignment(this.footer,Alignment.MIDDLE_RIGHT);
		this.root.setColumnExpandRatio(0,10.0F);
		this.root.setSizeUndefined();
		
		this.cmdSelectAll.addClickListener(event -> this.cmdSelectAll_buttonClick(event));
		this.cmdSelectNone.addClickListener(event -> this.cmdSelectNone_buttonClick(event));
		this.cbPageType.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(final Property.ValueChangeEvent event)
			{
				TableExportPopup.this.cbPageType_valueChange(event);
			}
		});
		this.cbPageOrientation.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(final Property.ValueChangeEvent event)
			{
				TableExportPopup.this.cbPageOrientation_valueChange(event);
			}
		});
		this.cmdClose.addClickListener(event -> this.cmdClose_buttonClick(event));
		this.cmdShow.addClickListener(event -> this.cmdShow_buttonClick(event));
	}
	
	private XdevGridLayout					root;
	private XdevButton						cmdSelectAll, cmdSelectNone, cmdClose, cmdSave, cmdShow;
	private XdevLabel						lblStatus;
	private XdevComboBox<PageType>			cbPageType;
	private XdevComboBox<ExportType>		cmbFormat;
	private XdevTable<?>					tblColumnChooser;
	private XdevComboBox<PageOrientation>	cbPageOrientation;
	private XdevCheckBox					chkShowPageNumber, chkHighlightRows;
	private XdevTextField					txtReportName;
	private XdevGridLayout					layoutSettings, footer;
	private XdevVerticalLayout				layoutTableButtons;
}
