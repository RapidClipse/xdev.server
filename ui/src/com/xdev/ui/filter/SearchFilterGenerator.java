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
 */

package com.xdev.ui.filter;


import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;


/**
 * @author XDEV Software
 *
 */
public interface SearchFilterGenerator
{
	public Filter createSearchFilter(String searchText, FilterSettings settings);



	public static class Default implements SearchFilterGenerator
	{
		@Override
		public Filter createSearchFilter(final String searchText, final FilterSettings settings)
		{
			if(searchText.length() == 0)
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
			final List<Filter> propertyFilters = new ArrayList<>();

			for(final Object searchableProperty : settings.getSearchableProperties())
			{
				propertyFilters.add(createWordFilter(word,searchableProperty,settings));
			}

			if(propertyFilters.isEmpty())
			{
				return null;
			}
			if(propertyFilters.size() == 1)
			{
				return propertyFilters.get(0);
			}

			return new Or(propertyFilters.toArray(new Filter[propertyFilters.size()]));
		}


		protected Filter createMultiWordSearchFilter(final String[] words,
				final FilterSettings settings)
		{
			final List<Filter> propertyFilters = new ArrayList<>();

			for(final Object searchableProperty : settings.getSearchableProperties())
			{
				final List<Filter> wordFilters = new ArrayList<>();

				for(final String word : words)
				{
					wordFilters.add(createWordFilter(word,searchableProperty,settings));
				}

				propertyFilters.add(new Or(wordFilters.toArray(new Filter[wordFilters.size()])));
			}

			if(propertyFilters.isEmpty())
			{
				return null;
			}
			if(propertyFilters.size() == 1)
			{
				return propertyFilters.get(0);
			}

			return new And(propertyFilters.toArray(new Filter[propertyFilters.size()]));
		}


		public static Filter createWordFilter(final String word, final Object searchableProperty,
				final FilterSettings settings)
		{
			final char wildcard = settings.getWildcard();
			if(word.indexOf(wildcard) != -1)
			{
				// replace search wildcard with SQL wildcard
				String pattern = word;
				if(wildcard != '%')
				{
					pattern = pattern.replace(wildcard,'%');
				}

				return new Like(searchableProperty,pattern,settings.isCaseSensitive());
			}

			return new SimpleStringFilter(searchableProperty,word,!settings.isCaseSensitive(),true);
		}
	}
}
