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

/**
 * Static values to use in conjunction with {@link SearchParameters} object. It
 * maps the kind of search you can do in SQL.
 *
 * @author XDEV Software
 * @since 3.0
 */
public enum SearchMode
{
	/**
	 * Match exactly the properties
	 */
	EQUALS("eq"),
	/**
	 * Activates LIKE search and add a '%' prefix and suffix before searching.
	 */
	ANYWHERE("any"),
	/**
	 * Activate LIKE search and add a '%' prefix before searching.
	 */
	STARTING_LIKE("sl"),
	/**
	 * Activate LIKE search. User provides the wildcard.
	 */
	LIKE("li"),
	/**
	 * Activate LIKE search and add a '%' suffix before searching.
	 */
	ENDING_LIKE("el");
	
	private final String code;
	
	
	SearchMode(final String code)
	{
		this.code = code;
	}
	
	
	public String getCode()
	{
		return this.code;
	}
	
	
	public static final SearchMode convert(final String code)
	{
		for(final SearchMode searchMode : SearchMode.values())
		{
			if(searchMode.getCode().equals(code))
			{
				return searchMode;
			}
		}
		
		return EQUALS; // default
	}
}
