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


import java.util.function.Consumer;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.hierarchical.TreeDataProvider;
import com.xdev.ui.masterdetail.MasterDetail;


/**
 *
 * @deprecated will be removed in a future release, master detail is now done
 *             with {@link MasterDetail}, for tree building see
 *             {@link TreeDataProvider}
 */
@Deprecated
public final class XDEV
{
	// --------------- MASTER DETAIL -----------------------

	/**
	 * Connect a master and a detail {@link BeanComponent} to achieve a Master
	 * Detail relation.
	 *
	 * @param consumer
	 *            {@link JPAComponentFilterBuilder} which requires the master
	 *            and detail {@link BeanComponent} as well as their Entity type.
	 */
	public static void bindComponents(final Consumer<JPAComponentFilterBuilder> consumer)
	{
		final JPAComponentFilterBuilder builder = new XdevJPAComponentFilterBuilder();
		consumer.accept(builder);
		builder.execute();
	}


	/**
	 * Connect a {@link BeanComponent} to display appropriate detail data in a
	 * {@link BeanFieldGroup}.
	 *
	 * @param masterComponent
	 *            the master {@link BeanComponent} to select data from
	 * @param form
	 *            the detail {@link BeanFieldGroup} to display appropriate data
	 *            in
	 */
	public static <T> void bindForm(final BeanComponent<T> masterComponent,
			final BeanFieldGroup<T> form)
	{
		XDEV.bindForm((Consumer<FormBuilder<T>>)formBinder -> {
			formBinder.setForm(form);
			formBinder.setMasterComponent(masterComponent);
		});
	}


	/**
	 * Connect a {@link BeanComponent} to display appropriate detail data in a
	 * {@link BeanFieldGroup}.
	 *
	 * @param consumer
	 *            {@link FormBuilder} which requires the master
	 *            {@link BeanComponent} and a detail {@link BeanFieldGroup} with
	 *            the same entity type.
	 */
	protected static <T> void bindForm(final Consumer<FormBuilder<T>> consumer)
	{
		final FormBuilder<T> builder = new FormBuilder.XdevFormBuilder<T>();
		consumer.accept(builder);
		builder.execute();
	}


	/**
	 * Fills a Tree Component from the given Entitystructure. Structure can be
	 * defined through {@link XdevFillTree#addRootGroup(Class)} and
	 * {@link XdevFillTree#addGroup(Class, Class)} calls.
	 *
	 * @param consumer
	 *            {@link XdevFillTree} utility which gathers its information
	 *            from registered groups
	 *            {@link XdevFillTree#addGroup(Class, Class)}.
	 * @param tree
	 *            the tree component to be filled.
	 *
	 * @deprecated see {@link com.xdev.ui.entitycomponent.hierarchical.FillTree
	 *             FillTree} for more information
	 */
	@Deprecated
	public static void buildTree(final Consumer<XdevFillTree> consumer, final AbstractSelect tree)
	{
		final XdevFillTree builder = new XdevFillTree(tree);
		consumer.accept(builder);
		builder.execute();
	}
}
