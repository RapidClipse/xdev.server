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
 */

package com.xdev.ui.util.wizard;


import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.util.masterdetail.EntityMasterDetail;
import com.xdev.ui.util.masterdetail.JPAMasterDetail;


public class XdevJPAComponentFilterBuilder implements JPAComponentFilterBuilder
{
	private BeanComponent<?>	masterComponent, detailComponent;
	private Class<?>			masterEntity, detailEntity;

	private JPAMasterDetail masterDetail;


	public XdevJPAComponentFilterBuilder()
	{
		this.masterDetail = new JPAMasterDetail.Implementation();
	}


	@Override
	public void execute()
	{
		this.masterDetail.connectMasterDetail(this.masterComponent,this.detailComponent,
				this.masterEntity,this.detailEntity);
	}


	@Override
	public void setMasterComponent(final BeanComponent<?> masterComponent)
	{
		this.masterComponent = masterComponent;
	}


	@Override
	public void setDetailComponent(final BeanComponent<?> detailComponent)
	{
		this.detailComponent = detailComponent;
		if(!this.detailComponent.isAutoQueryData())
		{
			// swap master detail implementation
			this.masterDetail = new EntityMasterDetail.Implementation();
		}
	}


	@Override
	public <T> void setMasterEntity(final Class<T> masterEntity)
	{
		this.masterEntity = masterEntity;
	}


	@Override
	public <T> void setDetailEntity(final Class<T> detailEntity)
	{
		this.detailEntity = detailEntity;
	}

}
