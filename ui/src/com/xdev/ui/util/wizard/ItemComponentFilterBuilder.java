
package com.xdev.ui.util.wizard;


import com.vaadin.data.Container.Filterable;
import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.util.masterdetail.MasterDetail;


public class ItemComponentFilterBuilder implements ComponentFilterBuilder
{
	private AbstractSelect		masterComponent;
	private Filterable			filterableDetailComponent;
	private Object				masterProperty, detailProperty;
	
	private final MasterDetail	masterDetail;


	public ItemComponentFilterBuilder()
	{
		// TODO customizable?
		this.masterDetail = new MasterDetail.Implementation();
	}


	@Override
	public void execute()
	{
		this.masterDetail.connectMasterDetail(this.masterComponent,this.filterableDetailComponent,
				this.masterProperty,this.detailProperty);
	}


	@Override
	public void setMasterComponent(final AbstractSelect masterComponent)
	{
		this.masterComponent = masterComponent;
	}


	@Override
	public void setFilterableDetail(final Filterable filterable)
	{
		this.filterableDetailComponent = filterable;
	}


	@Override
	public void setMasterProperty(final Object masterProperty)
	{
		this.masterProperty = masterProperty;
	}


	@Override
	public void setDetailProperty(final Object detailProperty)
	{
		this.detailProperty = detailProperty;
	}
}