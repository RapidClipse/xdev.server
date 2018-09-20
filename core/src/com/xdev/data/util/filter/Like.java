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

package com.xdev.data.util.filter;


import java.util.regex.Pattern;

import com.vaadin.data.Item;


/**
 * Extended Vaddin {@link com.vaadin.data.util.filter.Like} filter to match
 * values with line breaks as well.
 *
 * @author XDEV Software
 * @since 4.0
 */
public class Like extends com.vaadin.data.util.filter.Like
{
	private Pattern pattern;


	public Like(final Object propertyId, final String value, final boolean caseSensitive)
	{
		super(propertyId,value,caseSensitive);
	}


	public Like(final Object propertyId, final String value)
	{
		super(propertyId,value);
	}


	@Override
	public void setCaseSensitive(final boolean caseSensitive)
	{
		super.setCaseSensitive(caseSensitive);

		// reset pattern
		this.pattern = null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean passesFilter(final Object itemId, final Item item)
			throws UnsupportedOperationException
	{
		if(!item.getItemProperty(getPropertyId()).getType().isAssignableFrom(String.class))
		{
			// We can only handle strings
			return false;
		}

		final String colValue = (String)item.getItemProperty(getPropertyId()).getValue();

		// Fix issue #10167 - avoid NPE and drop null property values
		if(colValue == null)
		{
			return false;
		}

		// ====================================================================
		// ^^ taken from super method, below fix for XWS-1561
		// ====================================================================

		if(this.pattern == null)
		{
			int flags = Pattern.DOTALL;
			if(!isCaseSensitive())
			{
				flags |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
			}
			this.pattern = Pattern.compile(getValue().replace("%",".*"),flags);
		}
		
		return this.pattern.matcher(colValue).matches();
	}
}
