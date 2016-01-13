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

package com.xdev.ui.paging;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;


/**
 * Contains helper methods for containers that can be used to ease development
 * of containers in Vaadin.
 *
 * @since 7.0
 */
public class EntityLazyQueryContainerHelpers implements Serializable
{

	/**
	 * Get a range of item ids from the container using
	 * {@link Indexed#getIdByIndex(int)}. This is just a helper method to aid
	 * developers to quickly add the required functionality to a Container
	 * during development. This should not be used in a "finished product"
	 * unless fetching an id for an index is very inexpensive because a separate
	 * request will be performed for each index in the range.
	 *
	 * @param startIndex
	 *            index of the first item id to get
	 * @param numberOfIds
	 *            the number of consecutive items whose ids should be returned
	 * @param container
	 *            the container from which the items should be fetched
	 * @return A list of item ids in the range specified
	 */
	public static List<?> getItemIdsUsingGetIdByIndex(final int startIndex, final int numberOfIds,
			final Container.Indexed container)
	{

		if(container == null)
		{
			throw new IllegalArgumentException("The given container cannot be null!");
		}

		if(startIndex < 0)
		{
			throw new IndexOutOfBoundsException(
					"Start index cannot be negative! startIndex=" + startIndex);
		}

		if(startIndex > container.size())
		{
			throw new IndexOutOfBoundsException("Start index exceeds container size! startIndex="
					+ startIndex + " containerLastItemIndex=" + (container.size() - 1));
		}

		if(numberOfIds < 1)
		{
			if(numberOfIds == 0)
			{
				return Collections.emptyList();
			}

			throw new IllegalArgumentException(
					"Cannot get negative amount of items! numberOfItems=" + numberOfIds);
		}

		// not included in the range
		int endIndex = startIndex + numberOfIds;

		if(endIndex > container.size())
		{
			endIndex = container.size();
		}

		final ArrayList<Object> rangeOfIds = new ArrayList<Object>();
		for(int i = startIndex; i < endIndex; i++)
		{
			final Object idByIndex = container.getIdByIndex(i);
			if(idByIndex != null)
			{
				// throw new RuntimeException("Unable to get item id for index:
				// " + i
				// + " from container using Container.Indexed#getIdByIndex() "
				// + "even though container.size() > endIndex. "
				// + "Returned item id was null. " + "Check your container
				// implementation!");
				rangeOfIds.add(idByIndex);
			}

		}

		return Collections.unmodifiableList(rangeOfIds);
	}
}
