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

package com.xdev.ui.masterdetail;


/**
 * Master detail connection handle, which can be used to disconnect the
 * established connection.
 *
 * @see #disconnect()
 *
 * @author XDEV Software
 * @since 3.0
 */
public interface MasterDetailConnection
{
	/**
	 * Disconnects the master detail relation between the to components. Removes
	 * all listeners and cleans up all used resources. After this method is
	 * called this connection object cannot be used to reconnect again.
	 *
	 * @see MasterDetail
	 */
	public void disconnect();
}
