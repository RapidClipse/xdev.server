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

package com.xdev.dal;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.Attribute;

import com.google.common.collect.Lists;


/*
 * Copyright 2015 JAXIO http://www.jaxio.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Used to construct OR predicate for a property value. In other words you can
 * search all entities E having a given property set to one of the selected
 * values.
 *
 * @author XDEV Software
 * @since 3.0
 */
public class PropertySelector<E, F> implements Serializable
{
	/*
	 * PropertySelector builder
	 */
	public static <E, F> PropertySelector<E, F> newPropertySelector(final Attribute<?, ?>... fields)
	{
		return new PropertySelector<E, F>(fields);
	}


	/*
	 * PropertySelector builder
	 */
	public static <E, F> PropertySelector<E, F> newPropertySelector(final String path,
			final Class<E> from)
	{
		return new PropertySelector<E, F>(path,from);
	}


	/*
	 * PropertySelector builder
	 */
	public static <E, F> PropertySelector<E, F> newPropertySelector(final boolean orMode,
			final Attribute<?, ?>... fields)
	{
		final PropertySelector<E, F> ps = new PropertySelector<E, F>(fields);
		return ps.orMode(orMode);
	}

	private static final long	serialVersionUID	= 1L;

	private final PathHolder	pathHolder;
	private List<F>				selected			= new ArrayList<>();
	private SearchMode			searchMode;								// for
																		// string
																		// property
																		// only.
	private Boolean				notIncludingNull;
	private boolean				orMode				= true;


	public PropertySelector(final Attribute<?, ?>... attributes)
	{
		this.pathHolder = new PathHolder(attributes);
	}


	public PropertySelector(final String path, final Class<E> from)
	{
		this.pathHolder = new PathHolder(path,from);
	}


	public List<Attribute<?, ?>> getAttributes()
	{
		return this.pathHolder.getAttributes();
	}


	public boolean isNotIncludingNullSet()
	{
		return this.notIncludingNull != null;
	}


	public Boolean isNotIncludingNull()
	{
		return this.notIncludingNull;
	}


	public PropertySelector<E, F> withoutNull()
	{
		this.notIncludingNull = true;
		return this;
	}


	/*
	 * Get the possible candidates for property.
	 */
	public List<F> getSelected()
	{
		return this.selected;
	}


	public PropertySelector<E, F> add(final F object)
	{
		this.selected.add(object);
		return this;
	}


	/*
	 * Set the possible candidates for property.
	 */
	public void setSelected(final List<F> selected)
	{
		this.selected = selected;
	}


	@SuppressWarnings("unchecked")
	public PropertySelector<E, F> selected(final F... selected)
	{
		setSelected(Lists.newArrayList(selected));
		return this;
	}


	public boolean isNotEmpty()
	{
		return this.selected != null && !this.selected.isEmpty();
	}


	public void clearSelected()
	{
		if(this.selected != null)
		{
			this.selected.clear();
		}
	}


	public void setValue(final F value)
	{
		this.selected = Lists.newArrayList(value);
	}


	public F getValue()
	{
		return isNotEmpty() ? this.selected.get(0) : null;
	}


	public boolean isBoolean()
	{
		return isType(Boolean.class);
	}


	public boolean isString()
	{
		return isType(String.class);
	}


	public boolean isNumber()
	{
		return isType(Number.class);
	}


	public boolean isType(final Class<?> type)
	{
		return type.isAssignableFrom(getAttributes().get(getAttributes().size() - 1).getJavaType());
	}


	public SearchMode getSearchMode()
	{
		return this.searchMode;
	}


	/**
	 * In case, the field's type is a String, you can set a searchMode to use. It is
	 * null by default.
	 */
	public void setSearchMode(final SearchMode searchMode)
	{
		this.searchMode = searchMode;
	}


	public PropertySelector<E, F> searchMode(final SearchMode searchMode)
	{
		setSearchMode(searchMode);
		return this;
	}


	public boolean isOrMode()
	{
		return this.orMode;
	}


	public void setOrMode(final boolean orMode)
	{
		this.orMode = orMode;
	}


	public PropertySelector<E, F> orMode(final boolean orMode)
	{
		setOrMode(orMode);
		return this;
	}
}
