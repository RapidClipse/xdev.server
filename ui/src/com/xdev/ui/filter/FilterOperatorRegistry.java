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

package com.xdev.ui.filter;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author XDEV Software
 *
 */
public final class FilterOperatorRegistry
{
	private FilterOperatorRegistry()
	{
	}
	
	private static Map<String, FilterOperator> registry = new LinkedHashMap<>();


	static
	{
		addFilterOperator(new FilterOperator.Equals());
		addFilterOperator(new FilterOperator.StartsWith());
		addFilterOperator(new FilterOperator.Is());
		addFilterOperator(new FilterOperator.IsNot());
		addFilterOperator(new FilterOperator.Greater());
		addFilterOperator(new FilterOperator.Less());
		addFilterOperator(new FilterOperator.GreaterEqual());
		addFilterOperator(new FilterOperator.LessEqual());
		addFilterOperator(new FilterOperator.Between());
	}


	public static void addFilterOperator(final FilterOperator operator)
	{
		registry.put(operator.getKey(),operator);
	}
	
	
	public static FilterOperator removeFilterOperator(final String key)
	{
		return registry.remove(key);
	}


	public static Collection<FilterOperator> getFilterOperators()
	{
		return Collections.unmodifiableCollection(registry.values());
	}
}
