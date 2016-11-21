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

package com.xdev.ui.masterdetail;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.vaadin.data.util.BeanItem;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.util.DTOUtils;


/**
 * @author XDEV Software
 *
 */
public class LazyMasterDetailConnection<M, D> extends BeanComponentsMasterDetailConnection<M, D>
{
	protected Function<M, Collection<? extends D>> masterToDetail;
	
	
	public LazyMasterDetailConnection(final BeanComponent<M> master, final BeanComponent<D> detail,
			final Function<M, Collection<? extends D>> masterToDetail)
	{
		super(master,detail);
		
		this.masterToDetail = masterToDetail;
	}
	
	
	@Override
	protected void masterValueChanged()
	{
		final XdevBeanContainer<D> detailContainer = this.detail.getBeanContainerDataSource();
		detailContainer.removeAll();
		try
		{
			final List<? extends D> detailData = this.master.getSelectedItems().stream()
					.map(BeanItem::getBean).filter(Objects::nonNull)
					.peek(DTOUtils::reattachIfManaged).map(this.masterToDetail)
					.flatMap(Collection::stream).collect(Collectors.toList());
			detailContainer.addAll(detailData);
		}
		catch(final Exception e)
		{
			Logger.getLogger(LazyMasterDetailConnection.class).error(e);
		}
	}
	
	
	@Override
	public void disconnect()
	{
		super.disconnect();
		
		this.masterToDetail = null;
	}
}
