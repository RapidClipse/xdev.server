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

package com.xdev.ui.util.wizard;


import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.xdev.lang.ExecutableCommandObject;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.util.masterdetail.MasterDetail;


/**
 *
 * @deprecated will be removed in a future release
 */
@Deprecated
public interface FormBuilder<T> extends ExecutableCommandObject
{
	public void setMasterComponent(BeanComponent<T> masterComponent);
	
	
	public void setForm(BeanFieldGroup<T> form);
	
	
	
	public class XdevFormBuilder<T> implements FormBuilder<T>
	{
		private BeanComponent<T>	masterComponent;
		private BeanFieldGroup<T>	form;
		private final MasterDetail	masterDetail;
		
		
		public XdevFormBuilder()
		{
			this.masterDetail = new MasterDetail.Implementation();
		}
		
		
		@Override
		public void execute()
		{
			this.masterDetail.connectForm(this.masterComponent,this.form);
		}
		
		
		@Override
		public void setMasterComponent(final BeanComponent<T> masterComponent)
		{
			this.masterComponent = masterComponent;
		}
		
		
		@Override
		public void setForm(final BeanFieldGroup<T> form)
		{
			this.form = form;
		}
	}
	
}
