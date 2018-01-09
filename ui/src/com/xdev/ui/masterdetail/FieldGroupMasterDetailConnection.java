/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.xdev.ui.XdevFieldGroup;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.fieldgroup.BeanItemCreator;
import com.xdev.util.DTOUtils;


/**
 * Concrete implementation of a {@link MasterDetailConnection} between a
 * {@link BeanComponent} (master) and a {@link BeanFieldGroup} (detail).
 * <p>
 * If an item is selected in the master component the detail fieldgroup is
 * updated automatically.
 *
 * @param <T>
 *            common data type of the connected components
 *
 * @author XDEV Software
 * @since 3.0
 */
public class FieldGroupMasterDetailConnection<T> implements MasterDetailConnection
{
	protected BeanComponent<T>		master;
	protected BeanFieldGroup<T>		detail;
	protected ValueChangeListener	listener;
	protected BeanItemCreator<T>	beanItemCreator;


	public FieldGroupMasterDetailConnection(final BeanComponent<T> master,
			final BeanFieldGroup<T> detail)
	{
		super();

		this.master = master;
		this.detail = detail;

		this.listener = event -> {

			final BeanItem<T> item = this.master.getSelectedItem();
			T bean;
			if(item != null && (bean = item.getBean()) != null)
			{
				DTOUtils.reattachIfManaged(bean);
				this.detail.setItemDataSource(item);
			}
		};

		this.master.addValueChangeListener(this.listener);

		if(detail instanceof XdevFieldGroup)
		{
			this.beanItemCreator = master.getBeanContainerDataSource()::replaceItem;
			((XdevFieldGroup<T>)detail).setBeanItemCreator(this.beanItemCreator);
		}
	}


	@Override
	public void disconnect()
	{
		this.master.removeValueChangeListener(this.listener);

		if(this.beanItemCreator != null)
		{
			((XdevFieldGroup<T>)this.detail).setBeanItemCreator(null);
		}

		this.master = null;
		this.detail = null;
		this.listener = null;
		this.beanItemCreator = null;
	}
}
