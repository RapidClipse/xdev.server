/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.security.authorization;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingTable;

/**
 * Intermediate data structure that represents authorization definition information in a general purpose, yet
 * efficiently processable format.
 *
 * @author XDEV Software (TM)
 */
public interface AuthorizationConfiguration
{
	/**
	 * The child resource names (value) for every valid resource (keys).
	 * The key collection of the returned map also represents the complete set of valid resources.
	 *
	 * @return a table representing all valid resources and their defined child resources.
	 */
	public XGettingTable<String, ? extends XGettingEnum<String>> resourceResources();

	/**
	 * The parent role names (value) for every valid role (keys).
	 * The key collection of the returned map also represents the complete set of valid roles.
	 *
	 * @return a table representing all valid roles and their parent roles.
	 * @see #roleRoles()
	 */
	public XGettingTable<String, ? extends XGettingEnum<String>> roleRoles();

	/**
	 * The explicitely defined permissions (value) for every valid role (keys).
	 * Every permission is represented by its name and its associated factor.
	 * The key collection of the returned map also represents the complete set of valid roles.
	 *
	 * @return a table representing all valid roles and their defined permissions.
	 * @see #rolePermissions()
	 */
	public XGettingTable<String, ? extends XGettingTable<String, Integer>> rolePermissions();

	/**
	 * The role names (value) for every valid subject (keys).
	 * The key collection of the returned map also represents the complete set of valid subjects.
	 *
	 * @return a table representing all valid subjects and their defined roles.
	 */
	public XGettingTable<String, ? extends XGettingEnum<String>> subjectRoles();



	/**
	 * Creates a new {@link AuthorizationConfiguration} instance from the passed parts.
	 *
	 * @param resourceResources see {@link #resourceResources()}
	 * @param roleRoles         see {@link #roleRoles()}
	 * @param rolePermissions   see {@link #rolePermissions()}
	 * @param subjectRoles      see {@link #subjectRoles()}
	 * @return a new {@link AuthorizationConfiguration} instance
	 */
	public static AuthorizationConfiguration New(
		final XGettingTable<String, ? extends XGettingEnum<String>>           resourceResources,
		final XGettingTable<String, ? extends XGettingEnum<String>>           roleRoles        ,
		final XGettingTable<String, ? extends XGettingTable<String, Integer>> rolePermissions  ,
		final XGettingTable<String, ? extends XGettingEnum<String>>           subjectRoles
	)
	{
		return new Implementation(
			notNull(resourceResources),
			notNull(roleRoles)        ,
			notNull(rolePermissions)  ,
			notNull(subjectRoles)
		);
	}



	/**
	 * A simple immutable {@link AuthorizationConfiguration} default implementation.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class Implementation implements AuthorizationConfiguration
	{
		////////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final XGettingTable<String, ? extends XGettingEnum<String>>           resourceResources;
		final XGettingTable<String, ? extends XGettingEnum<String>>           roleRoles        ;
		final XGettingTable<String, ? extends XGettingTable<String, Integer>> rolePermissions  ;
		final XGettingTable<String, ? extends XGettingEnum<String>>           subjectRoles     ;


		////////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////


		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(
			final XGettingTable<String, ? extends XGettingEnum<String>>           resourceResources,
			final XGettingTable<String, ? extends XGettingEnum<String>>           roleRoles        ,
			final XGettingTable<String, ? extends XGettingTable<String, Integer>> rolePermissions  ,
			final XGettingTable<String, ? extends XGettingEnum<String>>           subjectRoles
		)
		{
			super();
			this.resourceResources = resourceResources;
			this.roleRoles         = roleRoles        ;
			this.rolePermissions   = rolePermissions  ;
			this.subjectRoles      = subjectRoles     ;
		}



		////////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XGettingTable<String, ? extends XGettingEnum<String>> resourceResources()
		{
			return this.resourceResources;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XGettingTable<String, ? extends XGettingEnum<String>> roleRoles()
		{
			return this.roleRoles;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XGettingTable<String, ? extends XGettingTable<String, Integer>> rolePermissions()
		{
			return this.rolePermissions;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XGettingTable<String, ? extends XGettingEnum<String>> subjectRoles()
		{
			return this.subjectRoles;
		}

	}

}
