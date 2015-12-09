/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
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
	public Filter createSearchFilter(ContainerFilterComponent component);
	
	
	
	public static class Default implements SearchFilterGenerator
	{
		@Override
		public Filter createSearchFilter(final ContainerFilterComponent component)
		{
			final String searchText = component.getSearchText().trim();
			if(searchText.length() == 0)
			{
				return null;
			}
			
			final String[] words = searchText.split(" ");
			
			if(words.length == 1)
			{
				return createSingleWordSearchFilter(component,words[0]);
			}
			
			return createMultiWordSearchFilter(component,words);
		}
		
		
		protected Filter createSingleWordSearchFilter(final ContainerFilterComponent component,
				final String word)
		{
			final List<Filter> propertyFilters = new ArrayList<>();
			
			for(final Object searchableProperty : component.getSearchableProperties())
			{
				propertyFilters.add(createWordFilter(component,word,searchableProperty));
			}

			if(propertyFilters.isEmpty())
			{
				return null;
			}
			
			return new Or(propertyFilters.toArray(new Filter[propertyFilters.size()]));
		}
		
		
		protected Filter createMultiWordSearchFilter(final ContainerFilterComponent component,
				final String[] words)
		{
			final List<Filter> propertyFilters = new ArrayList<>();
			
			for(final Object searchableProperty : component.getSearchableProperties())
			{
				final List<Filter> wordFilters = new ArrayList<>();
				
				for(final String word : words)
				{
					wordFilters.add(createWordFilter(component,word,searchableProperty));
				}
				
				propertyFilters.add(new Or(wordFilters.toArray(new Filter[wordFilters.size()])));
			}

			if(propertyFilters.isEmpty())
			{
				return null;
			}
			
			return new And(propertyFilters.toArray(new Filter[propertyFilters.size()]));
		}
		
		
		public static Filter createWordFilter(final ContainerFilterComponent component,
				final String word, final Object searchableProperty)
		{
			final char wordWildcard = component.getWordWildcard();
			final char letterWildcard = component.getLetterWildcard();
			if(word.indexOf(wordWildcard) != -1 || word.indexOf(letterWildcard) != -1)
			{
				// replace search wildcards with SQL wildcards
				String pattern = word;
				if(wordWildcard != '%')
				{
					pattern = pattern.replace(wordWildcard,'%');
				}
				if(letterWildcard != '_')
				{
					pattern = pattern.replace(letterWildcard,'_');
				}
				
				return new Like(searchableProperty,pattern,component.isCaseSensitive());
			}
			
			return new SimpleStringFilter(searchableProperty,word,!component.isCaseSensitive(),
					true);
		}
	}
}
