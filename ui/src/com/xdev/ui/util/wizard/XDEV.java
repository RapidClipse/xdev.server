/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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


import java.util.function.Consumer;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.entitycomponent.BeanComponent;


public final class XDEV
{
	// --------------- MASTER DETAIL -----------------------
	
	public static void bindComponents(final Consumer<JPAComponentFilterBuilder> consumer)
	{
		final JPAComponentFilterBuilder builder = new XdevJPAComponentFilterBuilder();
		consumer.accept(builder);
		builder.execute();
	}
	
	
	public static <T> void bindForm(final BeanComponent<T> masterComponent,
			final BeanFieldGroup<T> form)
	{
		XDEV.bindForm((Consumer<FormBuilder<T>>)formBinder -> {
			formBinder.setForm(form);
			formBinder.setMasterComponent(masterComponent);
		});
	}
	
	
	protected static <T> void bindForm(final Consumer<FormBuilder<T>> consumer)
	{
		final FormBuilder<T> builder = new FormBuilder.XdevFormBuilder<T>();
		consumer.accept(builder);
		builder.execute();
	}
	
	
	public static void buildTree(final Consumer<XdevFillTree> consumer, final AbstractSelect tree)
	{
		final XdevFillTree builder = new XdevFillTree(tree);
		consumer.accept(builder);
		builder.execute();
	}
}
