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

package com.xdev.security.authorization;

/**
 * Function type that provides {@link Permission} instances.
 * For details, see {@link #providePermission(Resource, Integer)}.
 *
 * @author XDEV Software (TM)
 */
@FunctionalInterface
public interface PermissionProvider
{
	/**
	 * Provides a suitable {@link Permission} instance for the passed {@link Resource} instance and
	 * factor value. Providing means to either return a fitting instance or create a new one.
	 *
	 * @param  resource the {@link Resource} instance to be associated.
	 * @param  factor the factor of the access to the passed {@link Resource} instance.
	 * @return a {@link Permission} instance satisfiying the specified values.
	 */
	public Permission providePermission(Resource resource, Integer factor);

	/**
	 * Provides a permission for the passed {@link Resource} instance and a factor of 0.
	 *
	 * @param  resource the {@link Resource} instance to be associated.
	 * @return a {@link Permission} instance associated with the passed {@link Resource} instance.
	 * @see #providePermission(Resource, Integer)
	 */
	public default Permission providePermission(final Resource resource)
	{
		return this.providePermission(resource, 0);
	}

}
