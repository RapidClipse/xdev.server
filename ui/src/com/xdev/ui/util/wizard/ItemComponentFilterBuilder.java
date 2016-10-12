/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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
