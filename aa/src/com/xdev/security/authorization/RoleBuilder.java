/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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
import java.util.ConcurrentModificationException;
import java.util.HashSet;


/**
 * A builder pattern utility type that allows more a flexible and ergonomical assembly of {@link Role} instances
 * than using a constructor method directly.
 *
 * @author XDEV Software (TM)
 */
public interface RoleBuilder
{
	public String name();

	public Collection<? extends Role> roles();

	public Collection<? extends Permission> permissions();

	public RoleBuilder name(String name);

	public RoleBuilder grant(Role role);

	public RoleBuilder grant(Permission permission);

	public RoleBuilder roles(Collection<? extends Role> roles);

	public RoleBuilder permissions(Collection<? extends Permission> permissions);

	/**
	 * Resets this {@link RoleBuilder} instance's internal state to default values (effectively "clearing" the state).
	 *
	 * @return this.
	 */
	public RoleBuilder reset();

	/**
	 * Builds a {@link Role} by creating a new instance of that type, using the assembly data currently available
	 * to the builder instance. The builder's internal data is NOT resetted after the instantiation is completed.
	 * This is useful if another instance with the exactely or largely the same data and maybe just little changes
	 * shall be created subsequently.
	 *
	 * @return a new {@link Role} instance built from the available data.
	 * @see #build()
	 * @see #reset()
	 */
	public Role buildUnresetted();

	/**
	 * Builds a {@link Role} instance from the assembly data currently available to the builder instance and then
	 * resets the builder internally.
	 *
	 * @return a new {@link Role} instance built from the available data.
	 * @see #buildUnresetted()
	 * @see #reset()
	 */
	public default Role build()
	{
		final Role newRole = this.buildUnresetted();
		this.reset();
		return newRole;
	}


	/**
	 * Creates a new {@link RoleBuilder} instance connected to be passed {@link RoleManager}, using a
	 * {@link Role.Creator} default implementation.
	 *
	 * @param roleManager the {@link RoleManager} for which this builder shall build {@link Role} instances.
	 * @return a new {@link RoleBuilder} instance
	 */
	public static RoleBuilder New(final RoleManager roleManager)
	{
		return New(roleManager, Role::New);
	}

	/**
	 * Creates a new {@link RoleBuilder} instance connected to be passed {@link RoleManager}, using the passed
	 * {@link Role.Creator} instance.
	 *
	 * @param roleManager the {@link RoleManager} for which this builder shall build {@link Role} instances.
	 * @param roleCreator the {@link Role.Creator} instance to be used for creating new {@link Role} instances.
	 * @return a new {@link RoleBuilder} instance
	 */
	public static RoleBuilder New(final RoleManager roleManager, final Role.Creator roleCreator)
	{
		return new Implementation(
			notNull(roleCreator),
			notNull(roleManager)
		);
	}



	/**
	 * A simple {@link RoleBuilder} default implementation.
	 * All methods that are mutating state are synchronized and building a new {@link Role} instances participates
	 * in the connected {@link RoleManager}'s locking.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class Implementation implements RoleBuilder
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private       String               buildingName        = null;
		private final HashSet<Role>       buildingRoles       = new HashSet<>();
		private final HashSet<Permission> buildingPermissions = new HashSet<>();
		private final Role.Creator         roleCreator        ;
		private final RoleManager          roleManager        ;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(final Role.Creator roleCreator, final RoleManager roleManager)
		{
			super();
			this.roleCreator = roleCreator;
			this.roleManager = roleManager;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String name()
		{
			return this.buildingName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<? extends Role> roles()
		{
			return this.buildingRoles;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<? extends Permission> permissions()
		{
			return this.buildingPermissions;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized RoleBuilder reset()
		{
			this.buildingName = null ;
			this.buildingRoles       .clear();
			this.buildingPermissions .clear();
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized RoleBuilder name(final String name)
		{
			this.buildingName = name;
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized RoleBuilder grant(final Role role)
		{
			this.buildingRoles.add(role);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized RoleBuilder grant(final Permission permission)
		{
			this.buildingPermissions.add(permission);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized RoleBuilder roles(final Collection<? extends Role> roles)
		{
			this.buildingRoles.addAll(roles);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized RoleBuilder permissions(final Collection<? extends Permission> permissions)
		{
			this.buildingPermissions.addAll(permissions);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized Role buildUnresetted()
		{
			synchronized(this.roleManager.lockRoleRegistry())
			{
				// validate name viability before creator is called
				if(this.roleManager.roles().keySet().contains(this.buildingName))
				{
					// (10.06.2014 TM)TODO: proper exception
					throw new RuntimeException("Role already exists with name "+this.buildingName);
				}

				// call creator only when lock and validation guarantee viability
				final Role role = this.roleCreator.createRole(this.buildingName, this.buildingRoles, this.buildingPermissions);

				// if add fails nevertheless, something is wrong (e.g. inconsistent use of lock instance or lock loophole)
				if(this.roleManager.roles().putIfAbsent(this.buildingName, role) != role)
				{
					// (10.06.2014 TM)TODO: proper exception
					throw new ConcurrentModificationException("Illegal registry state for role "+this.buildingName);
				}

				// at this point, the role has been consistently created and registered
				return role;
			}
		}

	}

}
