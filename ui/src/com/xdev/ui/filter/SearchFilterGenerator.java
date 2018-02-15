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

package com.xdev.ui.filter;


import java.util.Arrays;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.SimpleStringFilter;


/**
 * @author XDEV Software
 *
 */
public interface SearchFilterGenerator
{
	public final static char SQL_WILDCARD = '%';


	public Filter createSearchFilter(String searchText, FilterSettings settings);


	public static String toPattern(final String value, final FilterSettings settings)
	{
		final char wildcard = settings.getWildcard();
		final StringBuilder sb = new StringBuilder(value.length());
		for(final char ch : value.toCharArray())
		{
			if(ch == wildcard)
			{
				sb.append(SQL_WILDCARD);
			}
			else if(ch != SQL_WILDCARD)
			{
				sb.append(ch);
			}
		}
		return sb.toString();
	}



	public static class Default implements SearchFilterGenerator
	{
		@Override
		public Filter createSearchFilter(final String searchText, final FilterSettings settings)
		{
			if(searchText == null || searchText.length() == 0)
			{
				return null;
			}

			final String[] words = searchText.split(" ");

			if(words.length == 1)
			{
				return createSingleWordSearchFilter(words[0],settings);
			}

			return createMultiWordSearchFilter(words,settings);
		}


		protected Filter createSingleWordSearchFilter(final String word,
				final FilterSettings settings)
		{
			final Filter[] propertyFilters = Arrays.stream(settings.getSearchableProperties())
					.map(searchableProperty -> createWordFilter(word,searchableProperty,settings))
					.toArray(Filter[]::new);

			return combinePropertyFilters(propertyFilters,settings);
		}


		protected Filter createMultiWordSearchFilter(final String[] words,
				final FilterSettings settings)
		{
			final Filter[] propertyFilters = Arrays.stream(settings.getSearchableProperties())
					.map(searchableProperty -> settings.getSearchMultiWordConnector()
							.connect(Arrays.stream(words)
									.map(word -> createWordFilter(word,searchableProperty,settings))
									.toArray(Filter[]::new)))
					.toArray(Filter[]::new);

			return combinePropertyFilters(propertyFilters,settings);
		}


		protected Filter createWordFilter(final String word, final Object searchableProperty,
				final FilterSettings settings)
		{
			final String pattern = SearchFilterGenerator.toPattern(word,settings);
			if(pattern.indexOf(SQL_WILDCARD) != -1)
			{
				return new Like(searchableProperty,pattern,settings.isCaseSensitive());
			}

			return new SimpleStringFilter(searchableProperty,pattern,!settings.isCaseSensitive(),
					settings.isPrefixMatchOnly());
		}


		protected Filter combinePropertyFilters(final Filter[] propertyFilters,
				final FilterSettings settings)
		{
			if(propertyFilters.length == 0)
			{
				return null;
			}

			if(propertyFilters.length == 1)
			{
				return propertyFilters[0];
			}

			return settings.getSearchPropertiesConnector().connect(propertyFilters);
		}
	}
}
