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


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.xdev.reports.ExportType;
import com.xdev.reports.tableexport.Column;
import com.xdev.reports.tableexport.TableExportSettings;
import com.xdev.reports.tableexport.TableReportBuilder;
import com.xdev.res.StringResourceUtils;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevCheckBox;
import com.xdev.ui.XdevLabel;
import com.xdev.ui.XdevPanel;
import com.xdev.ui.XdevTextField;
import com.xdev.ui.XdevVerticalLayout;
import com.xdev.ui.XdevView;
import com.xdev.ui.entitycomponent.combobox.XdevComboBox;
import com.xdev.ui.entitycomponent.table.XdevTable;
import com.xdev.ui.util.UIUtils;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;


public class SettingsView extends XdevView
{
	private static final String			PROPERTY_SELECTED		= "...";
	private static final String			PROPERTY_COLUMNNAME		= "Columnname";
	private static final String			PROPERTY_COLUMNWIDTH	= "Columnwidth";
	private static final String			PROPERTY_STRETCH		= "Stretch";
	private static final String			PROPERTY_COLUMNID		= "ColumnId";
	
	private TableReportBuilder			reportBuilder			= TableReportBuilder.DEFAULT;
	private final TableExportSettings	exportSettings			= new TableExportSettings();
	private final XdevTable<?>			table;
	private final ExportType[]			availableExportTypes;
	
	
	public SettingsView(final XdevTable<?> table, final ExportType... availableExportTypes)
	{
		super();
		
		this.table = table;
		this.availableExportTypes = availableExportTypes;
		
		this.initUI();
		
		this.cbPageType.addItems((Object[])PageType.values());
		this.cbPageType.setValue(this.exportSettings.getPageType());
		
		this.cbPageOrientation.addItems((Object[])PageOrientation.values());
		this.cbPageOrientation.setValue(this.exportSettings.getPageOrientation());
		
		this.tblColumnChooser.addContainerProperty(PROPERTY_SELECTED,CheckBox.class,null);
		this.tblColumnChooser.addContainerProperty(PROPERTY_COLUMNNAME,String.class,null);
		this.tblColumnChooser.addContainerProperty(PROPERTY_COLUMNWIDTH,TextField.class,null);
		this.tblColumnChooser.addContainerProperty(PROPERTY_STRETCH,CheckBox.class,null);
		this.tblColumnChooser.addContainerProperty(PROPERTY_COLUMNID,Object.class,null);
		
		this.tblColumnChooser.setColumnExpandRatio(PROPERTY_COLUMNNAME,1.0F);
		
		this.tblColumnChooser.setColumnAlignment(PROPERTY_SELECTED,Table.Align.CENTER);
		this.tblColumnChooser.setColumnAlignment(PROPERTY_STRETCH,Table.Align.CENTER);
		this.tblColumnChooser.setColumnAlignment(PROPERTY_COLUMNWIDTH,Table.Align.CENTER);
		
		Integer id = 0;
		
		for(final Object columnId : this.table.getVisibleColumns())
		{
			final TextField txtWidth = new TextField();
			txtWidth.addStyleName("small, borderless");
			txtWidth.setConverter(new StringToIntegerConverter());
			txtWidth.setConvertedValue(0);
			txtWidth.addValidator(new IntegerRangeValidator("",0,null));
			txtWidth.setColumns(5);
			
			txtWidth.addValueChangeListener(event -> check());
			
			final CheckBox selectBox = new CheckBox();
			selectBox.addValueChangeListener(event -> check());
			
			selectBox.setValue(true);
			
			final CheckBox stretchBox = new CheckBox();
			
			final int columnWidth = this.table.getColumnWidth(columnId);
			if(columnWidth <= 0)
			{
				txtWidth.setConvertedValue(150);
				stretchBox.setValue(false);
			}
			else
			{
				txtWidth.setConvertedValue(columnWidth);
			}
			
			if(this.table.getColumnExpandRatio(columnId) != -1.0)
			{
				stretchBox.setValue(true);
				txtWidth.setConvertedValue(0);
			}
			
			this.tblColumnChooser.addItem(new Object[]{selectBox,
					this.table.getColumnHeader(columnId),txtWidth,stretchBox,columnId},++id);
		}
	}
	
	
	public void setReportBuilder(final TableReportBuilder reportBuilder)
	{
		this.reportBuilder = reportBuilder;
	}
	
	
	private void cbPageType_valueChange(final Property.ValueChangeEvent event)
	{
		this.exportSettings.setPageType((PageType)this.cbPageType.getValue());
		
		check();
	}
	
	
	private void cbPageOrientation_valueChange(final Property.ValueChangeEvent event)
	{
		this.exportSettings.setPageOrientation((PageOrientation)this.cbPageOrientation.getValue());
		
		check();
	}
	
	
	private void chkShowPageNumber_valueChange(final Property.ValueChangeEvent event)
	{
		this.exportSettings.setShowPageNumber(this.chkShowPageNumber.getValue());
	}
	
	
	private void check()
	{
		if(!checkColumnWidth())
		{
			this.lblStatus.setVisible(true);
			this.cmdExport.setEnabled(false);
		}
		else
		{
			this.lblStatus.setVisible(false);
			this.cmdExport.setEnabled(true);
		}
	}
	
	
	private boolean checkColumnWidth()
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
	
	
	private int calculateFixedWidth()
	{
		int width = 0;
		
		for(final Object object : getSelectedColumns())
		{
			final Item item = this.tblColumnChooser.getItem(object);
			final int convertedValue = (Integer)((TextField)item
					.getItemProperty(PROPERTY_COLUMNWIDTH).getValue()).getConvertedValue();
			
			width = width + convertedValue;
		}
		
		return width;
	}
	
	
	private List<Object> getSelectedColumns()
	{
		final List<Object> columnsToExport = new ArrayList<>();
		
		for(final Object object : this.tblColumnChooser.getItemIds())
		{
			final Item item = this.tblColumnChooser.getItem(object);
			if(((CheckBox)item.getItemProperty(PROPERTY_SELECTED).getValue()).getValue())
			{
				columnsToExport.add(object);
			}
		}
		
		return columnsToExport;
	}
	
	
	/**
	 * Event handler delegate method for the {@link XdevButton}
	 * {@link #cmdExport}.
	 *
	 * @see Button.ClickListener#buttonClick(Button.ClickEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdExport_buttonClick(final Button.ClickEvent event)
	{
		if(this.txtReportName.isValid())
		{
			if(checkColumnWidth())
			{
				this.exportSettings.setTitle(this.txtReportName.getValue());
				
				final JasperReportBuilder builder = this.reportBuilder.buildReport(this.table,
						createColumns(),this.exportSettings);
				
				this.panel.setVisible(false);
				this.verticalLayout3.setVisible(false);
				final FinishView finishView = new FinishView(builder,this.availableExportTypes);
				this.verticalLayout.removeAllComponents();
				this.verticalLayout.addComponent(finishView);
				finishView.setSizeFull();
			}
			else
			{
				Notification.show(StringResourceUtils.localizeString("{$notification.columnwidth}",
						Locale.getDefault(),this),Type.ERROR_MESSAGE);
				
			}
		}
		else
		{
			Notification.show(StringResourceUtils.localizeString("{$notification.reporttitle}",
					Locale.getDefault(),this),Type.ERROR_MESSAGE);
		}
	}
	
	
	private List<Column> createColumns()
	{
		final List<Column> columns = new ArrayList<Column>();
		
		for(final Object id : this.tblColumnChooser.getItemIds())
		{
			final Item item = this.tblColumnChooser.getItem(id);
			
			if(!((CheckBox)item.getItemProperty(PROPERTY_SELECTED).getValue()).getValue())
			{
				continue;
			}
			
			final int columnWidth = Integer.valueOf(
					((TextField)item.getItemProperty(PROPERTY_COLUMNWIDTH).getValue()).getValue());
			final String columnHeader = (String)item.getItemProperty(PROPERTY_COLUMNNAME)
					.getValue();
			float columnExpandRatio = 0F;
			if(((CheckBox)item.getItemProperty(PROPERTY_SELECTED).getValue()).getValue() == true)
			{
				columnExpandRatio = 1F;
			}
			
			final Converter<String, Object> converter = this.table.getConverter(id);
			final Align columnAlignment = this.table.getColumnAlignment(id);
			final Class<?> type = this.table.getBeanContainerDataSource().getType(id);
			
			final Object propertyId = item.getItemProperty(PROPERTY_COLUMNID).getValue();
			final Column col = new Column(columnWidth,columnHeader,columnExpandRatio,converter,
					columnAlignment,type,propertyId);
			
			columns.add(col);
		}
		
		return columns;
	}
	
	
	private void cmdClose_buttonClick(final Button.ClickEvent event)
	{
		UIUtils.getNextParent(this,Window.class).close();
	}
	
	
	private void cmdSelectAll_buttonClick(final Button.ClickEvent event)
	{
		selectAll(true);
	}
	
	
	private void cmdSelectNone_buttonClick(final Button.ClickEvent event)
	{
		selectAll(false);
	}
	
	
	private void selectAll(final boolean status)
	{
		for(final Object object : this.tblColumnChooser.getItemIds())
		{
			final Item item = this.tblColumnChooser.getItem(object);
			((CheckBox)item.getItemProperty(PROPERTY_SELECTED).getValue()).setValue(status);
		}
	}
	
	
	private void initUI()
	{
		this.verticalLayout = new XdevVerticalLayout();
		this.panel = new XdevPanel();
		this.verticalLayout2 = new XdevVerticalLayout();
		this.txtReportName = new XdevTextField();
		this.gridLayout = new GridLayout();
		this.tblColumnChooser = new Table();
		this.verticalLayout4 = new VerticalLayout();
		this.cmdSelectAll = new Button();
		this.cmdSelectNone = new Button();
		this.lblStatus = new XdevLabel();
		this.cbPageType = new XdevComboBox<>();
		this.cbPageOrientation = new XdevComboBox<>();
		this.chkShowPageNumber = new XdevCheckBox();
		this.panel3 = new Panel();
		this.verticalLayout3 = new XdevVerticalLayout();
		this.cmdExport = new XdevButton();
		this.cmdClose = new XdevButton();
		
		this.verticalLayout.setSpacing(false);
		this.verticalLayout.setMargin(new MarginInfo(false));
		this.panel.setTabIndex(0);
		this.panel.setStyleName("borderless");
		this.txtReportName.setCaption(StringResourceUtils.optLocalizeString("{$title}",this));
		this.txtReportName.setInputPrompt(
				StringResourceUtils.optLocalizeString("{$reporttitle.inputprompt}",this));
		this.txtReportName.setStyleName("small");
		this.txtReportName.setRequired(true);
		this.gridLayout.setSpacing(true);
		this.tblColumnChooser.setCaption(
				StringResourceUtils.optLocalizeString("{$tblColumnChooser.caption}",this));
		this.verticalLayout4.setCaption(" ");
		this.cmdSelectAll
				.setCaption(StringResourceUtils.optLocalizeString("{$selectAll.caption}",this));
		this.cmdSelectAll.setStyleName("link small");
		this.cmdSelectNone
				.setCaption(StringResourceUtils.optLocalizeString("{$selectNone.caption}",this));
		this.cmdSelectNone.setStyleName("link small");
		this.lblStatus.setStyleName("small, failure");
		this.lblStatus
				.setValue(StringResourceUtils.optLocalizeString("{$columnsize.warning}",this));
		this.lblStatus.setVisible(false);
		this.lblStatus.setContentMode(ContentMode.HTML);
		this.cbPageType
				.setCaption(StringResourceUtils.optLocalizeString("{$pageformat.title}",this));
		this.cbPageType.setPageLength(20);
		this.cbPageType.setStyleName("tiny");
		this.cbPageOrientation
				.setCaption(StringResourceUtils.optLocalizeString("{$pageorientation.title}",this));
		this.cbPageOrientation.setNullSelectionAllowed(false);
		this.cbPageOrientation.setStyleName("small");
		this.chkShowPageNumber
				.setCaption(StringResourceUtils.optLocalizeString("{$pagenumber.title}",this));
		this.chkShowPageNumber.setStyleName("small");
		this.cmdExport.setCaption(StringResourceUtils.optLocalizeString("{$next}",this));
		this.cmdExport.setStyleName("primary, small");
		this.cmdClose.setCaption(StringResourceUtils.optLocalizeString("{$cancel}",this));
		this.cmdClose.setStyleName("small");
		
		this.cmdSelectAll.setSizeUndefined();
		this.verticalLayout4.addComponent(this.cmdSelectAll);
		this.verticalLayout4.setComponentAlignment(this.cmdSelectAll,Alignment.MIDDLE_LEFT);
		this.cmdSelectNone.setSizeUndefined();
		this.verticalLayout4.addComponent(this.cmdSelectNone);
		this.verticalLayout4.setComponentAlignment(this.cmdSelectNone,Alignment.MIDDLE_LEFT);
		final CustomComponent verticalLayout4_spacer = new CustomComponent();
		verticalLayout4_spacer.setSizeFull();
		this.verticalLayout4.addComponent(verticalLayout4_spacer);
		this.verticalLayout4.setExpandRatio(verticalLayout4_spacer,1.0F);
		this.gridLayout.setColumns(2);
		this.gridLayout.setRows(1);
		this.tblColumnChooser.setSizeFull();
		this.gridLayout.addComponent(this.tblColumnChooser,0,0);
		this.verticalLayout4.setWidth(-1,Unit.PIXELS);
		this.verticalLayout4.setHeight(100,Unit.PERCENTAGE);
		this.gridLayout.addComponent(this.verticalLayout4,1,0);
		this.gridLayout.setColumnExpandRatio(0,10.0F);
		this.gridLayout.setRowExpandRatio(0,10.0F);
		this.txtReportName.setWidth(100,Unit.PERCENTAGE);
		this.txtReportName.setHeight(-1,Unit.PIXELS);
		this.verticalLayout2.addComponent(this.txtReportName);
		this.verticalLayout2.setComponentAlignment(this.txtReportName,Alignment.MIDDLE_CENTER);
		this.gridLayout.setWidth(100,Unit.PERCENTAGE);
		this.gridLayout.setHeight(300,Unit.PIXELS);
		this.verticalLayout2.addComponent(this.gridLayout);
		this.verticalLayout2.setComponentAlignment(this.gridLayout,Alignment.MIDDLE_CENTER);
		this.lblStatus.setWidth(100,Unit.PERCENTAGE);
		this.lblStatus.setHeight(-1,Unit.PIXELS);
		this.verticalLayout2.addComponent(this.lblStatus);
		this.verticalLayout2.setComponentAlignment(this.lblStatus,Alignment.MIDDLE_LEFT);
		this.cbPageType.setWidth(100,Unit.PERCENTAGE);
		this.cbPageType.setHeight(-1,Unit.PIXELS);
		this.verticalLayout2.addComponent(this.cbPageType);
		this.verticalLayout2.setComponentAlignment(this.cbPageType,Alignment.MIDDLE_CENTER);
		this.cbPageOrientation.setWidth(100,Unit.PERCENTAGE);
		this.cbPageOrientation.setHeight(-1,Unit.PIXELS);
		this.verticalLayout2.addComponent(this.cbPageOrientation);
		this.verticalLayout2.setComponentAlignment(this.cbPageOrientation,Alignment.MIDDLE_CENTER);
		this.chkShowPageNumber.setSizeUndefined();
		this.verticalLayout2.addComponent(this.chkShowPageNumber);
		this.verticalLayout2.setComponentAlignment(this.chkShowPageNumber,Alignment.MIDDLE_LEFT);
		this.verticalLayout2.setWidth(100,Unit.PERCENTAGE);
		this.verticalLayout2.setHeight(-1,Unit.PIXELS);
		this.panel.setContent(this.verticalLayout2);
		this.cmdExport.setWidth(100,Unit.PERCENTAGE);
		this.cmdExport.setHeight(-1,Unit.PIXELS);
		this.verticalLayout3.addComponent(this.cmdExport);
		this.verticalLayout3.setComponentAlignment(this.cmdExport,Alignment.MIDDLE_CENTER);
		this.cmdClose.setWidth(100,Unit.PERCENTAGE);
		this.cmdClose.setHeight(-1,Unit.PIXELS);
		this.verticalLayout3.addComponent(this.cmdClose);
		this.verticalLayout3.setComponentAlignment(this.cmdClose,Alignment.MIDDLE_CENTER);
		this.verticalLayout3.setWidth(100,Unit.PERCENTAGE);
		this.verticalLayout3.setHeight(-1,Unit.PIXELS);
		this.panel3.setContent(this.verticalLayout3);
		this.panel.setSizeFull();
		this.verticalLayout.addComponent(this.panel);
		this.verticalLayout.setComponentAlignment(this.panel,Alignment.MIDDLE_CENTER);
		this.verticalLayout.setExpandRatio(this.panel,10.0F);
		this.panel3.setWidth(100,Unit.PERCENTAGE);
		this.panel3.setHeight(-1,Unit.PIXELS);
		this.verticalLayout.addComponent(this.panel3);
		this.verticalLayout.setComponentAlignment(this.panel3,Alignment.MIDDLE_CENTER);
		this.verticalLayout.setSizeFull();
		this.setContent(this.verticalLayout);
		this.setWidth(650,Unit.PIXELS);
		this.setHeight(600,Unit.PIXELS);
		
		this.cmdSelectAll.addClickListener(event -> this.cmdSelectAll_buttonClick(event));
		this.cmdSelectNone.addClickListener(event -> this.cmdSelectNone_buttonClick(event));
		this.cbPageType.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(final Property.ValueChangeEvent event)
			{
				SettingsView.this.cbPageType_valueChange(event);
			}
		});
		this.cbPageOrientation.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(final Property.ValueChangeEvent event)
			{
				SettingsView.this.cbPageOrientation_valueChange(event);
			}
		});
		this.chkShowPageNumber.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(final Property.ValueChangeEvent event)
			{
				SettingsView.this.chkShowPageNumber_valueChange(event);
			}
		});
		this.cmdExport.addClickListener(event -> this.cmdExport_buttonClick(event));
		this.cmdClose.addClickListener(event -> this.cmdClose_buttonClick(event));
	}
	
	private Panel							panel3;
	private XdevLabel						lblStatus;
	private XdevButton						cmdExport, cmdClose;
	private VerticalLayout					verticalLayout4;
	private XdevPanel						panel;
	private XdevComboBox<PageType>			cbPageType;
	private XdevComboBox<PageOrientation>	cbPageOrientation;
	private Table							tblColumnChooser;
	private Button							cmdSelectAll, cmdSelectNone;
	private XdevCheckBox					chkShowPageNumber;
	private XdevTextField					txtReportName;
	private XdevVerticalLayout				verticalLayout, verticalLayout2, verticalLayout3;
	private GridLayout						gridLayout;
}
