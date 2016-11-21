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
import java.util.function.Function;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.xdev.ui.entitycomponent.BeanComponent;


/**
 * @author XDEV Software
 * @since 3.0
 */
public final class MasterDetail
{
	public static <M, D> MasterDetailConnection connect(final BeanComponent<M> master,
			final BeanComponent<D> detail,
			final Function<M, Collection<? extends D>> masterToDetail)
	{
		return new LazyMasterDetailConnection<>(master,detail,masterToDetail);
	}
	
	
	public static <M, D> MasterDetailConnection connect(final BeanComponent<M> master,
			final BeanComponent<D> detail, final Object detailPropertyId)
	{
		return new FilterMasterDetailConnection<>(master,detail,value -> value,detailPropertyId);
	}
	
	
	public static <M, D> MasterDetailConnection connect(final BeanComponent<M> master,
			final BeanComponent<D> detail, final Function<M, Object> masterToFilterValue,
			final Object detailPropertyId)
	{
		return new FilterMasterDetailConnection<>(master,detail,masterToFilterValue,
				detailPropertyId);
	}
	
	
	public static <T> MasterDetailConnection connect(final BeanComponent<T> master,
			final BeanFieldGroup<T> detail)
	{
		return new FieldGroupMasterDetailConnection<>(master,detail);
	}
	
	
	private MasterDetail()
	{
	}
}
