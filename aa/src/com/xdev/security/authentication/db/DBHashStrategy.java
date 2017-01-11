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

package com.xdev.security.authentication.db;


import com.xdev.security.authentication.jpa.HashStrategy;


/**
 * @author XDEV Software (JW)
 * @deprecated replaced by {@link HashStrategy}, will be removed in a future
 *             release
 */
@Deprecated
public interface DBHashStrategy extends HashStrategy
{
	/**
	 * @deprecated see {@link DBHashStrategy}
	 */
	@Deprecated
	public static class MD5 extends HashStrategy.MD5 implements DBHashStrategy
	{
	}



	/**
	 * @deprecated see {@link DBHashStrategy}
	 */
	@Deprecated
	public static class SHA2 extends HashStrategy.SHA2 implements DBHashStrategy
	{
	}



	/**
	 * @deprecated see {@link DBHashStrategy}
	 */
	@Deprecated
	public static class SHA1 extends HashStrategy.SHA1 implements DBHashStrategy
	{
	}



	/**
	 * @deprecated see {@link DBHashStrategy}
	 */
	@Deprecated
	public static class PBKDF2WithHmacSHA1 extends HashStrategy.PBKDF2WithHmacSHA1
			implements DBHashStrategy
	{
	}
}
