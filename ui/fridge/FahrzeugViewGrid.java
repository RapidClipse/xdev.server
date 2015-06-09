//
//package com.xdev.server.test.ui;
//
//
//import com.vaadin.data.Property.ValueChangeEvent;
//import com.vaadin.data.Property.ValueChangeListener;
//import com.vaadin.navigator.View;
//import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
//import com.vaadin.server.FontAwesome;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.Button.ClickListener;
//import com.vaadin.ui.CustomComponent;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.themes.ValoTheme;
//import com.xdev.server.test.domain.Fahrzeug;
//import com.xdev.server.test.domain.Hersteller;
//import com.xdev.ui.entitycomponent.grid.XdevGrid;
//import com.xdev.ui.util.NestedProperty;
//
//
//public class FahrzeugViewGrid extends CustomComponent implements View
//{
//	private static final long		serialVersionUID	= -5475734221218410059L;
//	
//	private Button					btnNew;
//	private Button					btnEdit;
//	private Button					btnDelete;
//	private XdevGrid<Fahrzeug>		detailTable;
//	private XdevGrid<Hersteller>	masterTable;
//	
//	
//	@Override
//	public void enter(ViewChangeEvent event)
//	{
//		// view layout
//		this.setImmediate(true);
//		this.setSizeFull();
//		VerticalLayout layout = new VerticalLayout();
//		layout.setSizeFull();
//		layout.setSpacing(true);
//		setCompositionRoot(layout);
//		
//		// button layout + buttons
//		HorizontalLayout buttonLayout = new HorizontalLayout();
//		buttonLayout.setSpacing(true);
//		
//		btnNew = new Button();
//		btnNew.setIcon(FontAwesome.PLUS);
//		btnNew.setStyleName(ValoTheme.BUTTON_LARGE);
//		btnNew.addClickListener(new NewButtonClickListener());
//		buttonLayout.addComponent(btnNew);
//		
//		btnEdit = new Button();
//		btnEdit.setIcon(FontAwesome.EDIT);
//		btnEdit.setStyleName(ValoTheme.BUTTON_LARGE);
//		btnEdit.setWidth(70,Unit.POINTS);
//		btnEdit.setEnabled(false);
//		btnEdit.addClickListener(new EditButtonClickListener());
//		buttonLayout.addComponent(btnEdit);
//		
//		btnDelete = new Button();
//		btnDelete.setIcon(FontAwesome.MINUS);
//		btnDelete.setStyleName(ValoTheme.BUTTON_LARGE);
//		btnDelete.setWidth(70,Unit.POINTS);
//		btnDelete.setEnabled(false);
//		btnDelete.addClickListener(new DeleteButtonClickListener());
//		buttonLayout.addComponent(btnDelete);
//		
//		TextField txtSearch = new TextField();
//		txtSearch.setImmediate(true);
//		txtSearch.setInputPrompt("Suche...");
//		txtSearch.setMaxLength(20);
//		// txtSearch.addTextChangeListener(new SearchTextChangeListener());
//		buttonLayout.addComponent(txtSearch);
//		
//		layout.addComponent(buttonLayout);
//		
//		HorizontalLayout tableLayout = new HorizontalLayout();
//		tableLayout.setSpacing(true);
//		
//		masterTable = new XdevGrid<>();
//		
//		// factory.getImplementation().setModel(this.masterTable,Hersteller.class,
//		// daoHersteller.findAll());
//		// masterTable.setGenericDataSource(factory.getImplementation().getModel(this.masterTable,
//		// Hersteller.class,20));
//		masterTable.setDataContainer(Hersteller.class);
//		
//		masterTable.setImmediate(true);
//		// masterTable.setSelectable(true);
//		// masterTable.setColumnHeader("marke","Herstellerbezeichnung");
//		// masterTable.setVisibleColumns("marke");
//		
//		masterTable.setHeight(100,Unit.PERCENTAGE);
//		masterTable.removeColumn("id");
//		tableLayout.addComponent(masterTable);
//		
//		detailTable = new XdevGrid<>();
//		// Filterable detailContainer =
//		// factory.getImplementation().setModel(this.table,
//		// Fahrzeug.class,dao.findAll(),"modell.modell","modell.hersteller.marke",
//		// "farbe.farbe");
//		// XdevLazyEntityContainer<Fahrzeug> detailContainer =
//		// factory.getImplementation().getModel(
//		// this.detailTable,Fahrzeug.class,20,NestedProperty.of("modell.modell",String.class),
//		// NestedProperty.of("modell.hersteller.marke",String.class));
//		
//		detailTable.setDataContainer(Fahrzeug.class,
//		// NestedProperty.of("modell.modell",String.class),
//		// NestedProperty.of("farbe.farbe",String.class),
//				NestedProperty.of("modell.hersteller",Hersteller.class));
////		detailTable.setContainerDataSource(new BeanItemContainer<Fahrzeug>(Fahrzeug.class,DAOs.get(
////				FahrzeugDAO.class).findAll()));
////		detailTable.setImmediate(true);
//		// detailTable.removeColumn("erstzulassung");
//		// detailTable.removeColumn("notiz");
//		detailTable.removeColumn("extras");
//		// detailTable.removeColumn("kilometerstand");
//		// detailTable.removeColumn("id");
//		
//		detailTable.setSizeFull();
//		detailTable.setEditorEnabled(true);
//		// detailTable.setHeightMode(HeightMode.ROW);
//		// detailTable.setHeightByRows(10);
//		
//		// detailTable.removeColumn("modell");
//		// detailTable.removeColumn("farbe");
//		// detailTable.addValueChangeListener(new TableValueChangeListener());
//		// detailTable.setColumnHeader("modell.modell","Modellbezeichnung");
//		// detailTable.setColumnHeader("kilometerstand","Kilometerstand");
//		// table.setColumnHeader("farbe.farbe","Farbcode");
//		// table.setColumnHeader("modell.hersteller.marke","Marke");
//		// detailTable.setColumnHeader("preis","Preis");
//		// detailTable.setVisibleColumns(new Object[]{/*
//		// * "modell.hersteller.marke",
//		// */"modell.modell","kilometerstand","preis"});
//		
//		// BeanFieldGroup<Fahrzeug> fg = new BeanFieldGroup<>(Fahrzeug.class);
//		// detailTable.setEditorFieldGroup(fg);
//		// detailTable.getE
//		
//		// MasterDetailFactory mdFactory = new MasterDetailFactory();
//		// mdFactory.getImplementation().connectMasterDetail(this.masterTable,
//		// detailTable.getContainerDataSource(),"id","modell.hersteller.id");
//		
//		// JPAMasterDetail masterDetail = new JPAMasterDetail.Implementation();
//		// masterDetail.connectMasterDetail(this.masterTable,
//		// this.detailTable.getContainerDataSource(),Hersteller.class,Fahrzeug.class);
//		
//		// XDEV.bindComponents(componentBinder -> {
//		// componentBinder.setMasterComponent(this.masterTable);
//		// componentBinder.setDetailComponent(this.detailTable);
//		// componentBinder.setMasterEntity(Hersteller.class);
//		// componentBinder.setDetailEntity(Fahrzeug.class);
//		// });
//		
//		// table.setSizeFull();
//		tableLayout.setSizeFull();
//		tableLayout.addComponent(detailTable);
//		tableLayout.setExpandRatio(detailTable,2);
//		
//		// load first page
//		// table.firstPage();
//		
//		// table.addActionHandler(new TableActionHandler());
//		// table.addItemClickListener(new TableItemClickListener());
//		
//		layout.addComponent(tableLayout);
//		// layout.addComponent(table.createControls());
//		// layout.setComponentAlignment(tableLayout,Alignment.TOP_LEFT);
//		layout.setExpandRatio(tableLayout,2);
//	}
//	
//	
//	
//	public class NewButtonClickListener implements ClickListener
//	{
//		/**
//		 * 
//		 */
//		private static final long	serialVersionUID	= 4847691450699454462L;
//		
//		
//		@Override
//		public void buttonClick(ClickEvent event)
//		{
//			// getUI().addWindow(new
//			// NewDialog(FahrzeugViewGrid.this.detailTable));
//		}
//	}
//	
//	
//	
//	public class EditButtonClickListener implements ClickListener
//	{
//		/**
//		 * 
//		 */
//		private static final long	serialVersionUID	= 3456508816862140862L;
//		
//		
//		@Override
//		public void buttonClick(ClickEvent event)
//		{
//			// Object rowId = detailTable.getValue();
//			// // eigentlich sollte es wohl eine BeanTable geben die
//			// // Typisierbar auf den Beantyp ist um nicht ständig casten zu
//			// // müssen.
//			// @SuppressWarnings("unchecked")
//			// BeanItemContainer<Fahrzeug> container =
//			// (BeanItemContainer<Fahrzeug>)table
//			// .getContainerDataSource();
//			// Fahrzeug toEditBean = detailTable.getSelectedItem().getBean();
//			// int id = (int) FahrzeugView.this.detailTable
//			// .getContainerDataSource().getContainerProperty(rowId, "id")
//			// .getValue();
//			
//			// invoke enter
//			// EditView.open(getUI(),toEditBean,"hansen");
//			
//			// getUI().getNavigator().navigateTo(EditView.createURL(toEditBean,"hansen"));
//			// this.navigateTo(EditView.getURL(toEditBean,"hansen"));
//		}
//	}
//	
//	
//	
//	public class DeleteButtonClickListener implements ClickListener
//	{
//		private static final long	serialVersionUID	= -18837818076144940L;
//		
//		
//		@Override
//		public void buttonClick(ClickEvent event)
//		{
//			Object rowId = detailTable.getSelectedRow();
//			try
//			{
//				int id = (int)FahrzeugViewGrid.this.detailTable.getContainerDataSource()
//						.getContainerProperty(rowId,"ID").getValue();
//				getUI().addWindow(
//						new DeleteConfirmationDialog(FahrzeugViewGrid.this.detailTable
//								.getContainerDataSource(),rowId,id));
//			}
//			catch(NullPointerException e)
//			{
//				throw e;
//			}
//		}
//	}
//	
//	
//	
//	public class TableValueChangeListener implements ValueChangeListener
//	{
//		/**
//		 * 
//		 */
//		private static final long	serialVersionUID	= 2173737514348603786L;
//		
//		
//		@Override
//		public void valueChange(ValueChangeEvent event)
//		{
//			if(event != null)
//			{
//				btnEdit.setEnabled(true);
//				btnDelete.setEnabled(true);
//			}
//			else
//			{
//				btnEdit.setEnabled(false);
//				btnDelete.setEnabled(false);
//			}
//		}
//	}
// }
