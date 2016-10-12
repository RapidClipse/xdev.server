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

import static com.xdev.security.Utils.notNull;

import java.util.HashMap;


/**
 * Manager type that extends {@link PermissionRegistry} with functionality for mutating (managing) the registered entries.
 * A {@link PermissionManager} is also a relaying implementation of a {@link PermissionProvider}.
 *
 * @author XDEV Software (TM)
 */
public interface PermissionManager extends PermissionRegistry, PermissionProvider
{
	/**
	 * Provides a permission for the passed {@link Resource} instance and the passed factor.
	 * Providing means to either return a suitable existing {@link Permission} instance of create, register and then
	 * return a new one.
	 *
	 * @param  resource the {@link Resource} instance to be associated.
	 * @param  factor the factor of the access to the passed {@link Resource} instance.
	 * @return a {@link Permission} instance satisfiying the specified values.
	 * @see #providePermission(Resource)
	 */
	@Override
	public Permission providePermission(Resource resource, Integer factor);



	///////////////////////////////////////////////////////////////////////////
	// default methods  //
	/////////////////////

	/**
	 * Provides a permission for the passed {@link Resource} instance and a factor of 0.
	 *
	 * @param  resource the {@link Resource} instance to be associated.
	 * @return a {@link Permission} instance associated with the passed {@link Resource} instance.
	 * @see #providePermission(Resource, Integer)
	 */
	@Override
	public default Permission providePermission(final Resource resource)
	{
		return this.providePermission(resource, 0);
	}



	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Creates a new {@link PermissionManager} instance with a {@link PermissionProvider} default implementation.
	 * The {@link PermissionProvider} is used whenever {@link #providePermission(Resource, Integer)} is unable to find
	 * an existing suitable {@link Permission} instance.
	 *
	 * @return a new {@link PermissionManager} instance.
	 * @see Permission#New(Resource, int)
	 * @see PermissionManager#New(PermissionProvider)
	 * @see PermissionManager#New(Object, PermissionProvider, HashMap)
	 */
	public static PermissionManager New()
	{
		return New(Permission::New);
	}

	/**
	 * Creates a new {@link PermissionManager} instance with the passed {@link PermissionProvider} instance.
	 * The {@link PermissionProvider} is used whenever {@link #providePermission(Resource, Integer)} is unable to find
	 * an existing suitable {@link Permission} instance.
	 *
	 * @param  permissionProvider the {@link PermissionProvider} instance to be used to provide {@link Permission}
	 *         instances.
	 * @return a new {@link PermissionManager} instance.
	 * @see PermissionManager#New()
	 * @see PermissionManager#New(Object, PermissionProvider, HashMap)
	 */
	public static PermissionManager New(final PermissionProvider permissionProvider)
	{
		return New(new Object(), permissionProvider, new HashMap<>());
	}

	/**
	 * Creates a new {@link PermissionManager} instance with the passed {@link PermissionProvider} instance the passed
	 * registry lock instance to be used as the internal synchronization lock and the passed table as its internal
	 * datastructure.
	 *
	 * @param registryLock the synchronization lock instance to be used for concurrency handling.
	 * @param permissionProvider permissionProvider the {@link PermissionProvider} instance to be used to provide
	 *        {@link Permission} instances.
	 * @param table the table to be used as the internal datastructure.
	 * @return a new {@link PermissionManager} instance.
	 * @see PermissionManager#New()
	 * @see PermissionManager#New(PermissionProvider)
	 */
	public static PermissionManager New(
		final Object                                                  registryLock      ,
		final PermissionProvider                                      permissionProvider,
		final HashMap<Resource, HashMap<Integer, Permission>> table
	)
	{
		return new PermissionManager.Implementation(
			notNull(permissionProvider) ,
			notNull(registryLock),
			notNull(table)
		);
	}



	/**
	 * A simple {@link PermissionManager} default implementation that uses a shared synchronization lock.
	 *
	 * @author XDEV Software (TM)
	 */
	public class Implementation implements PermissionManager
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final PermissionProvider                                      permissionProvider;
		final HashMap<Resource, HashMap<Integer, Permission>> table             ;
		final Object                                                  registryLock      ;
		final PermissionRegistry                                      registry          ;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(
			final PermissionProvider                                      permissionProvider ,
			final Object                                                  registryLock,
			final HashMap<Resource, HashMap<Integer, Permission>> table
		)
		{
			super();
			this.permissionProvider = permissionProvider;
			this.registryLock       = registryLock      ;
			this.table              = table             ;
			this.registry           = new PermissionRegistry.Implementation(this.table, registryLock);
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Permission providePermission(final Resource resource, final Integer factor)
		{
			synchronized(this.registryLock)
			{
				Permission permission = this.registry.permission(resource, factor);
				if(permission == null)
				{
					permission = this.permission(resource, factor);
					HashMap<Integer, Permission> resourceMap = this.table.get(resource);
					if(resourceMap == null)
					{
						this.table.put(resource, resourceMap = new HashMap<>());
					}
					resourceMap.put(factor, permission);
				}
				return permission;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized Permission permission(final Resource resource, final Integer factor)
		{
			synchronized(this.registryLock)
			{
				return this.registry.permission(resource, factor);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockPermissionRegistry()
		{
			return this.registryLock;
		}

	}

}
