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

package com.xdev.ui.action;


import com.xdev.ui.event.ActionEvent;


/**
 * Source object used for {@link ActionEvent}s if the event's origin is a
 * shortcut action.
 *
 * @see ActionEvent#getSource()
 * @author XDEV Software
 *
 */
public class ShortcutSource
{
	private final Object	sender;
	private final Object	target;


	/**
	 * Creates a new shortcut source.
	 *
	 * @param sender
	 *            the sender of the action. This is most often the action
	 *            container.
	 * @param target
	 *            the target of the action. For item containers this is the item
	 *            id.
	 */
	public ShortcutSource(final Object sender, final Object target)
	{
		this.sender = sender;
		this.target = target;
	}


	/**
	 * Returns the party that was sending the action.
	 *
	 * @return the sender
	 */
	public Object getSender()
	{
		return this.sender;
	}


	/**
	 * Returns the target of the action.
	 *
	 * @return the target
	 */
	public Object getTarget()
	{
		return this.target;
	}
}
