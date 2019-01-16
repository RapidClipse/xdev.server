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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.metamodel.Attribute;

import com.google.common.collect.Lists;


/**
 * The SearchParameters is used to pass search parameters to the DAO layer.
 * <p>
 * Its usage keeps 'find' method signatures in the DAO/Service layer simple.
 * <p>
 * A SearchParameters helps you drive your search in the following areas:
 * <ul>
 * <li>Configure the search mode (EQUALS, LIKE, ...)</li>
 * <li>Pagination: it allows you to limit your search results to a specific
 * range.</li>
 * <li>Allow you to specify ORDER BY and ASC/DESC</li>
 * <li>Enable/disable case sensitivity</li>
 * <li>Enable/disable 2d level cache</li>
 * <li>LIKE search against all string values: simply set the searchPattern
 * property</li>
 * <li>Named query: if you set a named query it will be executed. Named queries
 * can be defined in annotation or src/main/resources/META-INF/orm.xml</li>
 * <li>FullTextSearch: simply set the term property (requires Hibernate
 * Search)</li>
 * </ul>
 * <p>
 * Note : All requests are limited to a maximum number of elements to prevent
 * resource exhaustion.
 *
 * @see SearchMode
 * @see OrderBy
 * @see Range
 * @see PropertySelector
 *
 * @author XDEV Software
 * @since 3.0
 */
public class SearchParameters implements Serializable
{
	private static final long					serialVersionUID	= 1L;

	private SearchMode							searchMode			= SearchMode.EQUALS;
	private boolean								andMode				= true;

	private final Set<OrderBy>					orders				= new HashSet<>();

	// technical parameters
	private boolean								caseSensitive		= true;

	private PredicateSupplier					predicateSupplier;

	// pagination
	private int									maxResults			= -1;
	private int									first				= 0;
	private int									pageSize			= 0;

	// fetches
	private final Set<PathHolder>				fetches				= new HashSet<>();

	// ranges
	private final List<Range<?, ?>>				ranges				= new ArrayList<>();

	// property selectors
	private final List<PropertySelector<?, ?>>	properties			= new ArrayList<>();

	// pattern to match against all strings.
	private String								searchPattern;

	// Warn: before enabling cache for queries,
	// check this: https://hibernate.atlassian.net/browse/HHH-1523
	private Boolean								cacheable			= false;
	private String								cacheRegion;
	private CacheStoreMode						cacheStoreMode;
	private CacheRetrieveMode					cacheRetrieveMode;

	private boolean								useAndInXToMany		= true;

	private boolean								useDistinct			= false;

	// -----------------------------------
	// SearchMode
	// -----------------------------------


	/**
	 * Fluently set the @{link SearchMode}. It defaults to EQUALS.
	 *
	 * @param searchMode
	 *            searchmode
	 *
	 * @see SearchMode#EQUALS
	 */
	public void setSearchMode(final SearchMode searchMode)
	{
		this.searchMode = searchMode;
	}


	/**
	 * Return the @{link SearchMode}. It defaults to EQUALS.
	 *
	 * @see SearchMode#EQUALS
	 */
	public SearchMode getSearchMode()
	{
		return this.searchMode;
	}


	public boolean is(final SearchMode searchMode)
	{
		return getSearchMode() == searchMode;
	}


	/**
	 * Fluently set the @{link SearchMode}. It defaults to EQUALS.
	 *
	 * @param searchMode
	 *            searchmode
	 *
	 * @see SearchMode#EQUALS
	 */
	public SearchParameters searchMode(final SearchMode searchMode)
	{
		setSearchMode(searchMode);
		return this;
	}


	/**
	 * Use the EQUALS @{link SearchMode}.
	 *
	 * @param searchMode
	 *            searchmode
	 *
	 * @see SearchMode#EQUALS
	 */
	public SearchParameters equals()
	{
		return searchMode(SearchMode.EQUALS);
	}


	/**
	 * Use the ANYWHERE @{link SearchMode}.
	 *
	 * @param searchMode
	 *            searchmode
	 *
	 * @see SearchMode#ANYWHERE
	 */
	public SearchParameters anywhere()
	{
		return searchMode(SearchMode.ANYWHERE);
	}


	/**
	 * Use the STARTING_LIKE @{link SearchMode}.
	 *
	 * @see SearchMode#STARTING_LIKE
	 */
	public SearchParameters startingLike()
	{
		return searchMode(SearchMode.STARTING_LIKE);
	}


	/**
	 * Use the LIKE @{link SearchMode}.
	 *
	 * @see SearchMode#LIKE
	 */
	public SearchParameters like()
	{
		return searchMode(SearchMode.LIKE);
	}


	/**
	 * Use the ENDING_LIKE @{link SearchMode}.
	 *
	 * @see SearchMode#ENDING_LIKE
	 */
	public SearchParameters endingLike()
	{
		return searchMode(SearchMode.ENDING_LIKE);
	}


	// -----------------------------------
	// Predicate mode
	// -----------------------------------

	public void setAndMode(final boolean andMode)
	{
		this.andMode = andMode;
	}


	public boolean isAndMode()
	{
		return this.andMode;
	}


	/*
	 * use <code>and</code> to build the final predicate
	 */
	public SearchParameters andMode()
	{
		setAndMode(true);
		return this;
	}


	/*
	 * use <code>or</code> to build the final predicate
	 */
	public SearchParameters orMode()
	{
		setAndMode(false);
		return this;
	}

	// -----------------------------------
	// Search pattern support
	// -----------------------------------


	/**
	 * When it returns true, it indicates to the DAO layer to use the given
	 * searchPattern on all string properties.
	 */
	public boolean hasSearchPattern()
	{
		return this.searchPattern != null && this.searchPattern.trim().length() > 0;
	}


	/**
	 * Set the pattern which may contains wildcards (ex: <code>e%r%ka</code> ).
	 * <p>
	 * The given searchPattern is used by the DAO layer on all string properties.
	 * Null by default.
	 */
	public void setSearchPattern(final String searchPattern)
	{
		this.searchPattern = searchPattern;
	}


	public SearchParameters searchPattern(final String searchPattern)
	{
		setSearchPattern(searchPattern);
		return this;
	}


	public String getSearchPattern()
	{
		return this.searchPattern;
	}


	// -----------------------------------
	// Case sensitiveness support
	// -----------------------------------

	/**
	 * Set the case sensitiveness. Defaults to false.
	 *
	 * @param caseSensitive
	 *            caseSensitive
	 */
	public void setCaseSensitive(final boolean caseSensitive)
	{
		this.caseSensitive = caseSensitive;
	}


	public boolean isCaseSensitive()
	{
		return this.caseSensitive;
	}


	public boolean isCaseInsensitive()
	{
		return !this.caseSensitive;
	}


	/**
	 * Fluently set the case sensitiveness. Defaults to false.
	 *
	 * @param caseSensitive
	 *            caseSensitive
	 */
	public SearchParameters caseSensitive(final boolean caseSensitive)
	{
		setCaseSensitive(caseSensitive);
		return this;
	}


	/**
	 * Fluently set the case sensitiveness to true.
	 */
	public SearchParameters caseSensitive()
	{
		return caseSensitive(true);
	}


	/**
	 * Fluently set the case sensitiveness to false.
	 */
	public SearchParameters caseInsensitive()
	{
		return caseSensitive(false);
	}


	// -----------------------------------
	// Predicate support
	// -----------------------------------

	public void setPredicateSupplier(final PredicateSupplier predicateSupplier)
	{
		this.predicateSupplier = predicateSupplier;
	}


	public PredicateSupplier getPredicateSupplier()
	{
		return this.predicateSupplier;
	}


	public SearchParameters predicate(final PredicateSupplier predicateSupplier)
	{
		this.predicateSupplier = predicateSupplier;
		return this;
	}


	// -----------------------------------
	// Order by support
	// -----------------------------------

	public List<OrderBy> getOrders()
	{
		return new ArrayList<>(this.orders);
	}


	public void addOrderBy(final OrderBy orderBy)
	{
		if(!this.orders.add(orderBy))
		{
			throw new IllegalArgumentException("Duplicate orderBy: '" + orderBy + "'.");
		}
	}


	public boolean hasOrders()
	{
		return !this.orders.isEmpty();
	}


	public SearchParameters orderBy(final OrderBy... orderBys)
	{
		for(final OrderBy orderBy : orderBys)
		{
			addOrderBy(orderBy);
		}
		return this;
	}


	public SearchParameters asc(final Attribute<?, ?>... attributes)
	{
		return orderBy(new OrderBy(OrderByDirection.ASC,attributes));
	}


	public SearchParameters desc(final Attribute<?, ?>... attributes)
	{
		return orderBy(new OrderBy(OrderByDirection.DESC,attributes));
	}


	public SearchParameters orderBy(final OrderByDirection orderByDirection,
			final Attribute<?, ?>... attributes)
	{
		return orderBy(new OrderBy(orderByDirection,attributes));
	}


	public SearchParameters asc(final String property, final Class<?> from)
	{
		return orderBy(new OrderBy(OrderByDirection.ASC,property,from));
	}


	public SearchParameters desc(final String property, final Class<?> from)
	{
		return orderBy(new OrderBy(OrderByDirection.DESC,property,from));
	}


	public SearchParameters orderBy(final OrderByDirection orderByDirection, final String property,
			final Class<?> from)
	{
		return orderBy(new OrderBy(orderByDirection,property,from));
	}


	// -----------------------------------
	// Search by range support
	// -----------------------------------

	public List<Range<?, ?>> getRanges()
	{
		return this.ranges;
	}


	public void addRange(final Range<?, ?> range)
	{
		this.ranges.add(range);
	}


	public boolean hasRanges()
	{
		return !this.ranges.isEmpty();
	}


	public SearchParameters range(final Range<?, ?>... ranges)
	{
		for(final Range<?, ?> range : ranges)
		{
			addRange(range);
		}
		return this;
	}


	public <D extends Comparable<? super D>> SearchParameters range(final D from, final D to,
			final Attribute<?, ?>... attributes)
	{
		return range(new Range<D, D>(from,to,attributes));
	}


	// -----------------------------------
	// Search by property selector support
	// -----------------------------------

	public List<PropertySelector<?, ?>> getProperties()
	{
		return this.properties;
	}


	public void addProperty(final PropertySelector<?, ?> propertySelector)
	{
		this.properties.add(propertySelector);
	}


	public boolean hasProperties()
	{
		return !this.properties.isEmpty();
	}


	public SearchParameters property(final PropertySelector<?, ?>... propertySelectors)
	{
		for(final PropertySelector<?, ?> propertySelector : propertySelectors)
		{
			addProperty(propertySelector);
		}
		return this;
	}


	@SuppressWarnings("unchecked")
	public <F> SearchParameters property(final Attribute<?, ?> fields, final F... selected)
	{
		return property(PropertySelector.newPropertySelector(fields).selected(selected));
	}

	// -----------------------------------
	// Pagination support
	// -----------------------------------


	/**
	 * Set the maximum number of results to retrieve. Pass -1 for no limits.
	 */
	public void setMaxResults(final int maxResults)
	{
		this.maxResults = maxResults;
	}


	public int getMaxResults()
	{
		return this.maxResults;
	}


	/**
	 * Set the position of the first result to retrieve.
	 *
	 * @param first
	 *            position of the first result, numbered from 0
	 */
	public void setFirst(final int first)
	{
		this.first = first;
	}


	public int getFirst()
	{
		return this.first;
	}


	/**
	 * Set the page size, that is the maximum number of result to retrieve.
	 */
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}


	public int getPageSize()
	{
		return this.pageSize;
	}


	public SearchParameters maxResults(final int maxResults)
	{
		setMaxResults(maxResults);
		return this;
	}


	public SearchParameters noLimit()
	{
		setMaxResults(-1);
		return this;
	}


	public SearchParameters limitBroadSearch()
	{
		setMaxResults(500);
		return this;
	}


	public SearchParameters first(final int first)
	{
		setFirst(first);
		return this;
	}


	public SearchParameters pageSize(final int pageSize)
	{
		setPageSize(pageSize);
		return this;
	}

	// -----------------------------------------
	// Fetch associated entity using a LEFT Join
	// -----------------------------------------


	/**
	 * Returns the attributes (x-to-one association) which must be fetched with a
	 * left join.
	 */
	public List<List<Attribute<?, ?>>> getFetches()
	{
		return this.fetches.stream().map(PathHolder::getAttributes).collect(Collectors.toList());
	}


	public boolean hasFetches()
	{
		return !this.fetches.isEmpty();
	}


	/**
	 * The given attribute (x-to-one association) will be fetched with a left join.
	 */
	public void addFetch(final Attribute<?, ?>... attributes)
	{
		addFetch(Lists.newArrayList(attributes));
	}


	public void addFetch(final List<Attribute<?, ?>> attributes)
	{
		this.fetches.add(new PathHolder(attributes));
	}


	/**
	 * Fluently set the fetch attribute
	 */
	public SearchParameters fetch(final Attribute<?, ?>... attributes)
	{
		fetch(Lists.newArrayList(attributes));
		return this;
	}


	/**
	 * Fluently set the fetch attribute
	 */
	public SearchParameters fetch(final List<Attribute<?, ?>> attributes)
	{
		addFetch(attributes);
		return this;
	}

	// -----------------------------------
	// Caching support
	// -----------------------------------


	/**
	 * Default to false. Please read https://hibernate.atlassian.net/browse/HHH-1523
	 * before using cache.
	 */
	public void setCacheable(final boolean cacheable)
	{
		this.cacheable = cacheable;
	}


	public boolean isCacheable()
	{
		return this.cacheable;
	}


	public boolean hasCacheRegion()
	{
		return this.cacheRegion != null && this.cacheRegion.trim().length() > 0;
	}


	public void setCacheRegion(final String cacheRegion)
	{
		this.cacheRegion = cacheRegion;
	}


	public String getCacheRegion()
	{
		return this.cacheRegion;
	}


	public boolean hasCacheStoreMode()
	{
		return this.cacheStoreMode != null;
	}


	public void setCacheStoreMode(final CacheStoreMode cacheStoreMode)
	{
		this.cacheStoreMode = cacheStoreMode;
	}


	public CacheStoreMode getCacheStoreMode()
	{
		return this.cacheStoreMode;
	}


	public boolean hasCacheRetrieveMode()
	{
		return this.cacheRetrieveMode != null;
	}


	public void setCacheRetrieveMode(final CacheRetrieveMode cacheRetrieveMode)
	{
		this.cacheRetrieveMode = cacheRetrieveMode;
	}


	public CacheRetrieveMode getCacheRetrieveMode()
	{
		return this.cacheRetrieveMode;
	}


	public SearchParameters cacheable(final boolean cacheable)
	{
		setCacheable(cacheable);
		return this;
	}


	public SearchParameters enableCache()
	{
		setCacheable(true);
		return this;
	}


	public SearchParameters disableCache()
	{
		setCacheable(false);
		return this;
	}


	public SearchParameters cacheRegion(final String cacheRegion)
	{
		setCacheRegion(cacheRegion);
		return this;
	}


	public SearchParameters cacheStoreMode(final CacheStoreMode cacheStoreMode)
	{
		setCacheStoreMode(cacheStoreMode);
		return this;
	}


	public SearchParameters cacheRetrieveMode(final CacheRetrieveMode cacheRetrieveMode)
	{
		setCacheRetrieveMode(cacheRetrieveMode);
		return this;
	}


	// -----------------------------------
	// Use and in XToMany Search
	// -----------------------------------

	public void setUseAndInXToMany(final boolean useAndInXToMany)
	{
		this.useAndInXToMany = useAndInXToMany;
	}


	public boolean getUseAndInXToMany()
	{
		return this.useAndInXToMany;
	}


	public SearchParameters useOrInXToMany()
	{
		return useAndInXToMany(false);
	}


	public SearchParameters useAndInXToMany()
	{
		return useAndInXToMany(true);
	}


	public SearchParameters useAndInXToMany(final boolean xToManyAndMode)
	{
		setUseAndInXToMany(xToManyAndMode);
		return this;
	}


	// -----------------------------------
	// Distinct
	// -----------------------------------

	public void setDistinct(final boolean useDistinct)
	{
		this.useDistinct = useDistinct;
	}


	public boolean getDistinct()
	{
		return this.useDistinct;
	}


	public SearchParameters distinct(final boolean useDistinct)
	{
		setDistinct(useDistinct);
		return this;
	}


	public SearchParameters distinct()
	{
		return distinct(true);
	}
}
