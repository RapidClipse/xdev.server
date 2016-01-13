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


import com.xdev.lang.ExecutableCommandObject;
import com.xdev.ui.entitycomponent.BeanComponent;


//master detail for components
public interface JPAComponentFilterBuilder extends ExecutableCommandObject
{
	public void setMasterComponent(BeanComponent<?> masterComponent);


	public void setDetailComponent(BeanComponent<?> detailComponent);


	public <T> void setMasterEntity(Class<T> masterEntity);


	public <T> void setDetailEntity(Class<T> detailEntity);
}
