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

package com.xdev.security.authorization;


/**
 * Contract for central resource collection enums.
 *
 * @author XDEV Software
 */
public interface ResourceEnum<E extends Enum<E> & ResourceEnum<E>>
{
	/**
	 * The resource's name
	 *
	 * @return the name of the resource
	 */
	public String resourceName();


	/**
	 * Gets the actual resource held by this enum entry.
	 *
	 * @return the actual resource
	 */
	public Resource resource();
}
