/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.LockedGettingMap;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingMap;


/**
 * A registry type that provides a means for centralized lookup and iteration of known {@link Role} instances.
 * Note that a registry conceptually only provides reading access, not modifying access to its contents. For
 * modifying operations, see {@link RoleManager}.
 *
 * @author XDEV Software (TM)
 */
public interface RoleRegistry
{
	/**
	 * Returns the {@link Role} instance identified by the passed role name.
	 *
	 * @param roleName the name identifying the desired {@link Role} instance.
	 * @return the {@link Role} instance identified by the passed name.
	 */
	public Role role(String roleName);

	/**
	 * Returns a read-only map of all known {@link Role} instances (values), identified by their names (keys).
	 *
	 * @return a read-only map containing all known roles.
	 */
	public XGettingMap<String, Role> roles();

	/**
	 * Returns the lock instance that is internally used by this registry instance.
	 *
	 * @return the lock.
	 */
	public Object lockRoleRegistry();



	/**
	 * Creates a new {@link RoleRegistry} instance using the passed map instance as its internal datastructure
	 * and the passed registryLock instance as the synchronization lock for accessing the registry.
	 *
	 * @param registry the map instance to be used as the internal datastructure.
	 * @param registryLock the locking instance to be used to synchronize on for accessing the registry.
	 * @return a new {@link RoleRegistry} instance using the passed instances.
	 */
	public static RoleRegistry New(final XGettingMap<String, Role> registry, final Object registryLock)
	{
		return new Implementation(
			notNull(registry)    ,
			notNull(registryLock)
		);
	}

	/**
	 * Creates a new {@link RoleRegistry} instance using the passed map instance and a newly instantiated object
	 * to be used a synchronization lock.
	 *
	 * @param registry the map instance to be used as the internal datastructure.
	 * @return a new {@link RoleRegistry} instance using the passed instance.
	 */
	public static RoleRegistry New(final XGettingMap<String, Role> registry)
	{
		return New(registry, new Object());
	}

	/**
	 * Creates a new {@link RoleRegistry} instance using the passed {@link Role} instances to derive its
	 * internal datastructure from and the passed registryLock instance as the synchronization lock for
	 * accessing the registry.
	 *
	 * @param subjects the {@link Role} instances to derive the internal datastructure from.
	 * @param registryLock the locking instance to be used to synchronize on for accessing the registry.
	 * @return a new {@link RoleRegistry} instance using the passed instances.
	 */
	public static RoleRegistry New(final XGettingCollection<? extends Role> roles, final Object registryLock)
	{
		return New(
			Implementation.buildRegistry(notNull(roles)),
			notNull(registryLock)
		);
	}

	/**
	 * Creates a new {@link RoleRegistry} instance using the passed {@link Role} instance to derive its
	 * internal datastructure from and a newly instantiated object to be used as the synchronization lock.
	 *
	 * @param subjects the {@link Role} instance to be used as the internal datastructure.
	 * @return a new {@link RoleRegistry} instance using the passed instance.
	 */
	public static RoleRegistry New(final XGettingCollection<? extends Role> roles)
	{
		return New(roles, new Object());
	}


	/**
	 * A simple {@link RoleRegistry} default implementation that synchronizes on a provided lock instance for
	 * accessing the internal registry in order to avoid concurrency issues while the internal datastructure is
	 * rebuilt.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class Implementation implements RoleRegistry
	{
		///////////////////////////////////////////////////////////////////////////
		// static methods //
		///////////////////

		static final XGettingMap<String, Role> buildRegistry(final XGettingCollection<? extends Role> roles)
		{
			final EqHashTable<String, Role> registry = EqHashTable.New();
			roles.iterate(role -> registry.add(role.name(), role));
			return registry;
		}



		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		/**
		 * The read-only role-name-to-role map used as an internal datastructure
		 */
		private final XGettingMap<String, Role>      registry      ;

		/**
		 * The instance used to synchronize on. This may be any instance, even the map or registry instance itself.
		 */
		private final Object                         registryLock  ;

		/**
		 * A map wrapper implementation wrapping the actual registry map and using the registryLock instance to
		 * perform synchronization. Through this technique, the map can be accessed directly without losing the
		 * consistent concurrency protection achieve via the locking instance.
		 */
		private final LockedGettingMap<String, Role> lockedRegistry;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation-detail constructor that might change in the future.
		 *
		 * @param registry the map instance to be used as the internal datastructure.
		 * @param registryLock the locking instance to be used to synchronize on for accessing the registry.
		 */
		Implementation(final XGettingMap<String, Role> registry, final Object registryLock)
		{
			super();
			this.registry       = registry;
			this.registryLock   = registryLock;
			this.lockedRegistry = LockedGettingMap.New(this.registry, registryLock);
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Role role(final String roleName)
		{
			// must lock registry instead of this as a supervising manager instance might modify registry
			synchronized(this.registryLock)
			{
				final Role role = this.registry.get(roleName);
				if(role != null)
				{
					return role;
				}
			}
			throw new RuntimeException("Unknown role: "+roleName);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XGettingMap<String, Role> roles()
		{
			return this.lockedRegistry;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockRoleRegistry()
		{
			return this.registryLock;
		}

	}

}
