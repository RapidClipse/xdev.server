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

package com.xdev.ui.filter;


import com.vaadin.data.util.filter.SimpleStringFilter;


/**
 * @author XDEV Software
 *
 */
public interface FilterSettings
{
	/**
	 *
	 * @return <code>true</code> if the filter only applies to the beginning of the
	 *         value string, <code>false</code> for any location in the value
	 *
	 * @see SimpleStringFilter#isOnlyMatchPrefix()
	 *
	 * @since 3.2
	 */
	public boolean isPrefixMatchOnly();
	
	
	/**
	 *
	 * @return if the search should be case sensitive
	 *
	 * @see SimpleStringFilter#isIgnoreCase()
	 */
	public boolean isCaseSensitive();
	
	
	/**
	 *
	 * @return the character which is used as wildcard in search terms
	 */
	public char getWildcard();
	
	
	public Object[] getSearchableProperties();
	
	
	public Object[] getFilterableProperties();
	
	
	/**
	 *
	 * @return the connector for the searched properties of the container
	 *
	 * @since 3.2.1
	 */
	public Connector getSearchPropertiesConnector();
	
	
	/**
	 *
	 * @return the connector for each word in a multi word search of the search term
	 *
	 * @since 3.2.1
	 */
	public Connector getSearchMultiWordConnector();
	
	
	/**
	 *
	 * @return the connector for the properties of filter condition
	 *
	 * @since 3.2.1
	 */
	public Connector getFilterPropertiesConnector();
	
	
	/**
	 *
	 * @return the connector for the search term and the filter condition
	 *
	 * @since 3.2.1
	 */
	public Connector getSearchAndFilterConnector();
}
