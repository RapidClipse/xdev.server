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

package com.xdev.ui.masterdetail;


import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.xdev.data.util.filter.CompareBIDirect;
import com.xdev.ui.entitycomponent.BeanComponent;


/**
 * Concrete implementation of a {@link MasterDetailConnection} between two
 * {@link BeanComponent}s.
 * <p>
 * If an item is selected in the master component the filter of the detail
 * component is updated automatically. Works with single selection and multi
 * selection also.
 *
 * @author XDEV Software
 * @since 3.0
 */
public class FilterMasterDetailConnection<M, D> extends BeanComponentsMasterDetailConnection<M, D>
{
	protected Function<M, Object>	masterToFilterValue;
	protected Object				detailPropertyId;
	protected Filter				currentFilter;
	
	
	public FilterMasterDetailConnection(final BeanComponent<M> master,
			final BeanComponent<D> detail, final Function<M, Object> masterToFilterValue,
			final Object detailPropertyId)
	{
		super(master,detail);
		
		this.masterToFilterValue = masterToFilterValue;
		this.detailPropertyId = detailPropertyId;
	}
	
	
	@Override
	protected void masterValueChanged()
	{
		// reset selection
		this.detail.select(null);
		
		clearFilter();
		
		final List<Filter> filters = this.master.getSelectedItems().stream()
				.filter(Objects::nonNull).map(BeanItem::getBean).filter(Objects::nonNull)
				.map(this.masterToFilterValue).filter(Objects::nonNull)
				.map(value -> new CompareBIDirect.Equal(this.detailPropertyId,value))
				.collect(Collectors.toList());
		if(!filters.isEmpty())
		{
			final int size = filters.size();
			this.currentFilter = size == 1 ? filters.get(0)
					: new Or(filters.toArray(new Filter[size]));
			this.detail.getBeanContainerDataSource().addContainerFilter(this.currentFilter);
		}
	}
	
	
	protected void clearFilter()
	{
		if(this.currentFilter != null)
		{
			this.detail.getBeanContainerDataSource().removeContainerFilter(this.currentFilter);
			this.currentFilter = null;
		}
	}
	
	
	@Override
	public void disconnect()
	{
		clearFilter();
		
		this.masterToFilterValue = null;
		this.detailPropertyId = null;
		
		super.disconnect();
	}
}
