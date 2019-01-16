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

package com.xdev.ui.event;


import com.vaadin.event.EventRouter;
import com.vaadin.ui.Component;


/**
 * An event which indicates that the current focus owner has changed.
 *
 * @author XDEV Software
 *
 */
public class FocusChangeEvent extends Component.Event
{
	/**
	 * Identifier for event that can be used in {@link EventRouter}
	 */
	public static final String EVENT_ID = "focusChange";


	/**
	 * Creates a new focus change event.
	 *
	 * @param source
	 *            the new focus owner
	 */
	public FocusChangeEvent(final Component source)
	{
		super(source);
	}
}
