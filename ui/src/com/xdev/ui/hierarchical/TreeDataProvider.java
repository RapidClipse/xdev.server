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

package com.xdev.ui.hierarchical;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Hierarchical;


/**
 * @author XDEV Software
 * @since 1.1
 */
public interface TreeDataProvider
{
	public Iterable<? extends Object> roots();


	public Iterable<? extends Object> children(Object parent, Container.Hierarchical requestor);


	public boolean isLeaf(Object item, Container.Hierarchical requestor);


	public static <T> Implementation<T> New(final Iterable<T> rootData)
	{
		return New(() -> rootData);
	}


	public static <T> Implementation<T> New(final Supplier<Iterable<T>> rootDataSupplier)
	{
		return new Implementation<T>(rootDataSupplier);
	}



	public static class Implementation<T> implements TreeDataProvider
	{
		private final Supplier<? extends Iterable<?>>			rootsSupplier;
		private final List<Function<?, ? extends Iterable<?>>>	childrenSupplier;


		public Implementation(final Supplier<? extends Iterable<?>> rootsSupplier)
		{
			this(rootsSupplier,new ArrayList<>());
		}


		public Implementation(final Supplier<? extends Iterable<?>> rootsSupplier,
				final List<Function<?, ? extends Iterable<?>>> childrenSupplier)
		{
			this.rootsSupplier = rootsSupplier;
			this.childrenSupplier = childrenSupplier;
		}


		public <C> Implementation<C> addLevel(final Function<T, Iterable<C>> supplier)
		{
			this.childrenSupplier.add(supplier);
			return new Implementation<C>(this.rootsSupplier,this.childrenSupplier);
		}


		@Override
		public Iterable<? extends Object> roots()
		{
			return this.rootsSupplier.get();
		}


		@Override
		public Iterable<? extends Object> children(final Object parent,
				final Container.Hierarchical requestor)
		{
			final int level = getLevel(parent,requestor);
			if(level < 0 || level >= this.childrenSupplier.size())
			{
				return null;
			}
			
			// type safety secured by builder logic of this implementation
			@SuppressWarnings("unchecked")
			final Function<Object, ? extends Iterable<?>> function = (Function<Object, ? extends Iterable<?>>)this.childrenSupplier
					.get(level);
			return function.apply(parent);
		}


		@Override
		public boolean isLeaf(final Object item, final Hierarchical requestor)
		{
			return getLevel(item,requestor) == this.childrenSupplier.size();
		}


		protected int getLevel(Object item, final Container.Hierarchical container)
		{
			int level = 0;
			while((item = container.getParent(item)) != null)
			{
				level++;
			}
			return level;
		}
	}
}
