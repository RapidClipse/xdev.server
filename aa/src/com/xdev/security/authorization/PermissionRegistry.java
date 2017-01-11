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

import static com.xdev.security.Utils.notNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A registry type that provides a means for centralized lookup and iteration of known {@link Permission} instances.
 * Note that a registry conceptually only provides reading access, not modifying access to its contents. For
 * modifying operations, see {@link PermissionManager}.
 *
 * @author XDEV Software (TM)
 */
public interface PermissionRegistry
{
	/**
	 * Looks up the {@link Permission} instance representing the passed {@link Resource} instance with the passed
	 * factor value. If no suitable {@link Permission} instance can be found, <tt>null</tt> is returned.
	 *
	 * @param  resource the {@link Resource} instance for which the associated {@link Permission} instance shall be
	 *         returned.
	 * @param  factor the factor value of the {@link Resource} instance for which the associated {@link Permission}
	 *         instance shall be returned.
	 * @return the associated {@link Permission} instance specified by the passed values.
	 */
	public Permission permission(Resource resource, Integer factor);

	/**
	 * Returns the lock instance that is internally used by this registry instance.
	 *
	 * @return the lock.
	 */
	public Object lockPermissionRegistry();



	///////////////////////////////////////////////////////////////////////////
	// default methods  //
	/////////////////////

	/**
	 * Looks up the {@link Permission} instance representing the passed {@link Resource} instance with a factor of 0.
	 * If no suitable {@link Permission} instance can be found, <tt>null</tt> is returned.
	 *
	 * @param  resource the {@link Resource} instance for which the associated {@link Permission} instance shall be
	 *         returned.
	 * @return the associated {@link Permission} instance specified by the passed {@link Resource} instance.
	 */
	public default Permission permission(final Resource resource)
	{
		return this.permission(resource, 0);
	}



	/**
	 * Creates a new {@link PermissionRegistry} instance using the passed map instance as its internal datastructure
	 * and the passed registryLock instance as the synchronization lock for accessing the registry.
	 *
	 * @param registry the map instance to be used as the internal datastructure.
	 * @param registryLock the locking instance to be used to synchronize on for accessing the registry.
	 * @return a new {@link PermissionRegistry} instance using the passed instances.
	 */
	public static PermissionRegistry New(
		final Map<Resource, ? extends Map<Integer, Permission>> registry    ,
		final Object                                                            registryLock
	)
	{
		return new Implementation(
			notNull(registry)    ,
			notNull(registryLock)
		);
	}

	/**
	 * Creates a new {@link PermissionRegistry} instance using the passed map instance as its internal datastructure
	 * and an internally created instance as the synchronization lock for accessing the registry.
	 *
	 * @param registry the map instance to be used as the internal datastructure.
	 * @return a new {@link PermissionRegistry} instance using the passed instances.
	 */
	public static PermissionRegistry New(
		final Map<Resource, ? extends Map<Integer, Permission>> registry
	)
	{
		return New(registry, new Object());
	}

	/**
	 * Creates a new {@link PermissionRegistry} instance using the passed {@link Permission} instances to derive its
	 * internal datastructure from and the passed registryLock instance as the synchronization lock for
	 * accessing the registry.
	 *
	 * @param permissions the {@link Permission} instances to derive the internal datastructure from.
	 * @param registryLock the locking instance to be used to synchronize on for accessing the registry.
	 * @return a new {@link PermissionRegistry} instance using the passed instances.
	 */
	public static PermissionRegistry New(
		final Collection<? extends Permission> permissions ,
		final Object                                   registryLock
	)
	{
		return New(
			Implementation.buildRegistry(notNull(permissions)),
			notNull(registryLock)
		);
	}

	/**
	 * Creates a new {@link PermissionRegistry} instance using the passed {@link Permission} instance to derive its
	 * internal datastructure from and a newly instantiated object to be used as the synchronization lock.
	 *
	 * @param permissions the map instance to be used as the internal datastructure.
	 * @return a new {@link PermissionRegistry} instance using the passed instance.
	 */
	public static PermissionRegistry New(final Collection<? extends Permission> permissions)
	{
		return New(permissions, new Object());
	}



	/**
	 * A simple {@link PermissionRegistry} default implementation that synchronizes on a provided lock instance for
	 * accessing the internal registry in order to avoid concurrency issues while the internal datastructure is
	 * rebuilt.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class Implementation implements PermissionRegistry
	{
		///////////////////////////////////////////////////////////////////////////
		// static methods  //
		///////////////////

		static final HashMap<Resource, HashMap<Integer, Permission>> buildRegistry(
			final Collection<? extends Permission> permissions
		)
		{
			final HashMap<Resource, HashMap<Integer, Permission>> registry = new HashMap<>();

			for(final Permission permission : permissions)
			{
				HashMap<Integer, Permission> map = registry.get(permission.resource());
				if(map == null)
				{
					registry.put(permission.resource(), map = new HashMap<>());
				}
				map.put(permission.factor(), permission);
			}

			return registry;
		}



		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private final Map<Resource, ? extends Map<Integer, Permission>> registry    ;
		private final Object                                                            registryLock;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(
			final Map<Resource, ? extends Map<Integer, Permission>> registry    ,
			final Object                                                            registryLock
		)
		{
			super();
			this.registry     = registry    ;
			this.registryLock = registryLock;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final synchronized Permission permission(final Resource resource, final Integer factor)
		{
			synchronized(this.registryLock)
			{
				final Map<Integer, Permission> resourceMap = this.registry.get(resource);
				return resourceMap != null ? resourceMap.get(factor) :null;
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
