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

package com.xdev.security.authorization;

import java.util.Set;



/**
 * Function type that provides {@link Resource} instances.
 * For details, see {@link #provideResource(Resource, String, Set)}.
 *
 * @author XDEV Software (TM)
 */
@FunctionalInterface
public interface ResourceProvider
{
	/**
	 * Provides a suitable {@link Resource} instance based on the passed resource name, a potentially already
	 * existing instance and the collection of names of children (possibly empty).
	 * Providing means either validating an already existing instance or creating a fitting new instance.
	 *
	 * @param  existingInstance the potentially already existing {@link Resource} instance for the passed name.
	 * @param  factor the factor of the access to the passed {@link Resource} instance.
	 *
	 * @return a new {@link Resource} instance that satisfies the specified values.
	 */
	public Resource provideResource(Resource existingInstance, String name, Set<String> children);
}
