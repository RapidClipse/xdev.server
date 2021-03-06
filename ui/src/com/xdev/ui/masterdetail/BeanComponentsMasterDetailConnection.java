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


import com.vaadin.data.Property.ValueChangeListener;
import com.xdev.ui.entitycomponent.BeanComponent;


/**
 * Abstract base class for {@link MasterDetailConnection} with two
 * {@link BeanComponent}s.
 *
 * @param <M>
 *            the master type
 * @param <D>
 *            the detail type
 *
 * @see FilterMasterDetailConnection
 * @see LazyMasterDetailConnection
 *
 * @author XDEV Software
 * @since 3.0
 */
public abstract class BeanComponentsMasterDetailConnection<M, D> implements MasterDetailConnection
{
	protected BeanComponent<M>		master;
	protected BeanComponent<D>		detail;
	protected ValueChangeListener	listener;


	public BeanComponentsMasterDetailConnection(final BeanComponent<M> master,
			final BeanComponent<D> detail)
	{
		super();

		this.master = master;
		this.detail = detail;
		this.listener = event -> masterValueChanged();
		this.master.addValueChangeListener(this.listener);
	}


	protected abstract void masterValueChanged();


	@Override
	public void disconnect()
	{
		this.master.removeValueChangeListener(this.listener);

		this.master = null;
		this.detail = null;
		this.listener = null;
	}
}
