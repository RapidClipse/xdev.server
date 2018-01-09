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

package com.xdev.dal;


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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.Attribute;

import org.hibernate.criterion.Example.PropertySelector;

import com.google.common.collect.Lists;
import com.xdev.util.JPAMetaDataUtils;


/**
 * Holder class for path used by the {@link OrderBy}, {@link PropertySelector},
 * {@link TermSelector} and {@link SearchParameters}.
 *
 * @author XDEV Software
 * @since 3.0
 */
public class PathHolder implements Serializable
{
	private static final long				serialVersionUID	= 1L;
	private final String					path;
	private final Class<?>					from;
	private transient List<Attribute<?, ?>>	attributes;
	
	
	public PathHolder(final Attribute<?, ?>... attributes)
	{
		this(Lists.newArrayList(attributes));
	}
	
	
	public PathHolder(final List<Attribute<?, ?>> attributes)
	{
		JPAMetaDataUtils.verifyPath(attributes);
		this.attributes = new ArrayList<>(attributes);
		this.path = JPAMetaDataUtils.toPath(attributes);
		this.from = attributes.get(0).getDeclaringType().getJavaType();
	}
	
	
	public PathHolder(final String path, final Class<?> from)
	{
		this.path = path;
		this.from = from;
		if(getAttributes() == null)
		{
			throw new IllegalArgumentException();
		}
	}
	
	
	public List<Attribute<?, ?>> getAttributes()
	{
		if(this.attributes == null)
		{
			this.attributes = JPAMetaDataUtils.resolveAttributes(this.from,this.path);
		}
		return this.attributes;
	}
	
	
	public String getPath()
	{
		return this.path;
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
		return result;
	}
	
	
	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		final PathHolder other = (PathHolder)obj;
		if(this.path == null)
		{
			if(other.path != null)
			{
				return false;
			}
		}
		else if(!this.path.equals(other.path))
		{
			return false;
		}
		return true;
	}
	
}
