/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.vibrate;


/**
 * @author XDEV Software
 *
 */
public interface VibrateServiceAccess
{
	/**
	 * Vibrates with the specified pattern.
	 * <p>
	 * Single vibration for one second:
	 *
	 * <pre>
	 * {@code
	 * vibrate(1000);
	 * }
	 * </pre>
	 * <p>
	 * Vibrate for one second,<br>
	 * Pause for half a second,<br>
	 * Vibrate for 200 milliseconds:
	 *
	 * <pre>
	 * {@code
	 * vibrate(1000,500,200);
	 * }
	 * </pre>
	 *
	 *
	 * @param pattern
	 *            time/pause pattern to vibrate in milliseconds
	 */
	public void vibrate(int... pattern);
}
