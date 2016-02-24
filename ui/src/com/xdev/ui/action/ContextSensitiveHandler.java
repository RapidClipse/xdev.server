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

package com.xdev.ui.action;

import com.xdev.ui.event.ContextSensitiveHandlerChangeListener;

/**
 * Handler base interface for context providers for
 * {@link ContextSensitiveAction}s.
 * <p>
 * For a detailed explanation see {@link ContextSensitiveAction}.
 *
 * @see ContextSensitiveAction
 * @see ContextSensitiveHandlerChangeListener
 *
 * @author XDEV Software
 *
 */
public interface ContextSensitiveHandler
{
	/**
	 * Returns the base enabled state of the handler. Default is
	 * <code>true</code>.
	 * <p>
	 * The type parameter is used to identify the handler type since one Object
	 * can implement multiple handler interfaces.
	 *
	 * <pre>
	 * public boolean isContextSensitiveHandlerEnabled(
	 * 		final Class&lt;? extends ContextSensitiveHandler&gt; type)
	 * {
	 * 	if(type == SaveHandler.class)
	 * 	{
	 * 		return getCurrentUser().canWrite();
	 * 	}
	 *
	 * 	return true;
	 * }
	 * </pre>
	 *
	 * @param type
	 *            the handler type
	 * @return <code>true</code> if this handler is active and ready to be used,
	 *         <code>false</code> otherwise
	 */
	default public boolean isContextSensitiveHandlerEnabled(
			final Class<? extends ContextSensitiveHandler> type)
	{
		return true;
	}
}
