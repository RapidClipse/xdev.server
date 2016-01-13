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

package com.xdev.ui.event;


import java.util.EventListener;

import com.xdev.ui.action.ContextSensitiveAction;
import com.xdev.ui.action.ContextSensitiveHandler;
import com.xdev.ui.action.XdevActionManager;


/**
 * The listener interface for receiving state changes of a
 * {@link ContextSensitiveHandler}.
 *
 * @see ContextSensitiveHandler
 * @see ContextSensitiveAction
 * @see XdevActionManager
 *
 * @author XDEV Software
 *
 */
public interface ContextSensitiveHandlerChangeListener extends EventListener
{
	/**
	 * Invoked when a state change of a {@link ContextSensitiveHandler} occurs.
	 */
	public void contextSensitiveHandlerChanged(ContextSensitiveHandlerChangeEvent event);
}
