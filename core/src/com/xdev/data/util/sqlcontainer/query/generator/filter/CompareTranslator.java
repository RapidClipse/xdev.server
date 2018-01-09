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

package com.xdev.data.util.sqlcontainer.query.generator.filter;


import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.sqlcontainer.query.generator.StatementHelper;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.FilterTranslator;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.QueryBuilder;
import com.xdev.data.util.filter.Compare;


public class CompareTranslator implements FilterTranslator
{
	@Override
	public boolean translatesFilter(final Filter filter)
	{
		return filter instanceof Compare;
	}


	@Override
	public String getWhereStringForFilter(final Filter filter, final StatementHelper sh)
	{
		final Compare compare = (Compare)filter;
		sh.addParameterValue(compare.getValue());
		final String prop = QueryBuilder.quote(compare.getPropertyId());
		switch(compare.getOperation())
		{
			case EQUAL:
				return prop + " = ?";
			case GREATER:
				return prop + " > ?";
			case GREATER_OR_EQUAL:
				return prop + " >= ?";
			case LESS:
				return prop + " < ?";
			case LESS_OR_EQUAL:
				return prop + " <= ?";
			default:
				return "";
		}
	}
}
