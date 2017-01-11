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

package com.xdev.util;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;

import com.google.common.collect.Lists;


/**
 * @author XDEV Software
 * @since 3.0
 *
 * @noapi <strong>For internal use only. This class is subject to change in the
 *        future.</strong>
 */
public final class CriteriaUtils
{
	private CriteriaUtils()
	{
	}
	
	
	public static Predicate andPredicate(final CriteriaBuilder builder,
			final Predicate... predicatesNullAllowed)
	{
		return andPredicate(builder,Lists.newArrayList(predicatesNullAllowed));
	}
	
	
	public static Predicate andPredicate(final CriteriaBuilder builder,
			final Collection<Predicate> predicatesNullAllowed)
	{
		final List<Predicate> predicates = predicatesNullAllowed.stream().filter(Objects::nonNull)
				.collect(Collectors.toList());
		if(predicates == null || predicates.isEmpty())
		{
			return null;
		}
		else if(predicates.size() == 1)
		{
			return predicates.get(0);
		}
		else
		{
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		}
	}
	
	
	public static Predicate orPredicate(final CriteriaBuilder builder,
			final Predicate... predicatesNullAllowed)
	{
		return orPredicate(builder,Lists.newArrayList(predicatesNullAllowed));
	}
	
	
	public static Predicate orPredicate(final CriteriaBuilder builder,
			final Collection<Predicate> predicatesNullAllowed)
	{
		final List<Predicate> predicates = predicatesNullAllowed.stream().filter(Objects::nonNull)
				.collect(Collectors.toList());
		if(predicates == null || predicates.isEmpty())
		{
			return null;
		}
		else if(predicates.size() == 1)
		{
			return predicates.get(0);
		}
		else
		{
			return builder.or(predicates.toArray(new Predicate[predicates.size()]));
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static <E, F> Path<F> getPath(final Root<E> root, final List<Attribute<?, ?>> attributes)
	{
		Path<?> path = root;
		for(final Attribute<?, ?> attribute : attributes)
		{
			boolean found = false;
			if(path instanceof FetchParent)
			{
				for(final Fetch<E, ?> fetch : ((FetchParent<?, E>)path).getFetches())
				{
					if(attribute.getName().equals(fetch.getAttribute().getName())
							&& (fetch instanceof Join<?, ?>))
					{
						path = (Join<E, ?>)fetch;
						found = true;
						break;
					}
				}
			}
			if(!found)
			{
				if(attribute instanceof PluralAttribute)
				{
					path = ((From<?, ?>)path).join(attribute.getName(),JoinType.LEFT);
				}
				else
				{
					path = path.get(attribute.getName());
				}
			}
		}
		return (Path<F>)path;
	}
}
