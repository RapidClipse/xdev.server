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

package com.xdev.communication;


import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author XDEV Software
 * @since 1.3
 */
public class ContentSecurityPolicy
{
	public final static String	DEFAULT_SRC	= "default-src";
	public final static String	STYLE_SRC	= "style-src";
	public final static String	SCRIPT_SRC	= "script-src";
	public final static String	IMG_SRC		= "img-src";
	
	private final Map<String, Set<String>> directives = new LinkedHashMap<>();
	
	
	public ContentSecurityPolicy()
	{
	}
	
	
	public boolean isEmpty()
	{
		return this.directives.isEmpty()
				|| this.directives.values().stream().mapToInt(Set::size).sum() == 0;
	}
	
	
	public void addDirectives(final String key, final String... values)
	{
		Set<String> set = getDirectives(key);
		if(set == null)
		{
			set = new LinkedHashSet<>();
			this.directives.put(key,set);
		}
		for(final String value : values)
		{
			set.add(value);
		}
	}
	
	
	public Set<String> getDirectives(final String key)
	{
		return this.directives.get(key);
	}
	
	
	public void putDirectives(final String key, final Set<String> values)
	{
		this.directives.put(key,values);
	}


	@Override
	public String toString()
	{
		return this.directives.entrySet().stream().map(entry -> {
			final String key = entry.getKey();
			final Set<String> value = entry.getValue();
			return key.concat(value.stream().collect(Collectors.joining(" "," ","")));
		}).collect(Collectors.joining("; "));
	}
}
