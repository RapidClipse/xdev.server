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

package com.xdev.ui.event;


import java.util.EventListener;

import com.xdev.ui.action.Action;


/**
 * The listener interface for receiving action events. The class that is
 * interested in processing an action event implements this interface.
 *
 * @see Action
 * @author XDEV Software
 *		
 */
public interface ActionListener extends EventListener
{
	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent event);
}
