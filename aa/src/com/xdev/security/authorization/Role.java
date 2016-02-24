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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.xdev.security.Utils;


/**
 * The type representing a role for the purpose of authorization management. A
 * role is defined as a collection of permissions and other roles whose
 * permissions are applied recursively to it.
 *
 * Application-specific types that shall be handled by the authorization
 * framework have to implement this interface.
 *
 * @author XDEV Software (TM)
 */
public interface Role
{
	/**
	 * @return The role's unchangable name that identifies a particular
	 *         instance.
	 */
	public String name();


	/**
	 * @return The roles this role instance has been defined by.
	 */
	public Collection<Role> roles();


	/**
	 * @return The permissions this role instance has been defined by.
	 */
	public Collection<Permission> permissions();


	///////////////////////////////////////////////////////////////////////////
	// default methods //
	/////////////////////

	/**
	 * Returns the effective roles of this {@link Role} instance, meaning a
	 * collection containing all the roles that this instance has been defined
	 * by and all their effective roles. Every {@link Role} instance is only
	 * returned once (identity uniqueness).
	 *
	 * @return the effective roles.
	 */
	public default Set<Role> effectiveRoles()
	{
		return collectEffectiveRoles(this.roles(),new HashSet<>());
	}


	/**
	 * Returns the effective permissions of this {@link Role} instance, meaning
	 * a collection containing all the permissions that this instance has been
	 * defined by and the effective permissions of all its roles. For every
	 * unique {@link Resource} instance the permission with the highest absolute
	 * value is returned.
	 *
	 * @return the effective permissions.
	 */
	public default Map<Resource, Permission> effectivePermissions()
	{
		return collectEffectivePermissions(Utils.HashSet(this));
	}


	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Utility method that collects the effective permissions from a collection
	 * of passed roles.
	 *
	 * @param roles
	 *            the {@link Role} instances whose effective permissions shall
	 *            be determined.
	 * @return the passed roles' collected effective permissions.
	 * @see #effectivePermissions()
	 */
	public static Map<Resource, Permission> collectEffectivePermissions(
			final Collection<Role> roles)
	{
		return collectEffectivePermissions(roles,new HashSet<>(),new HashSet<>(),new HashSet<>(),
				new HashMap<>());
	}


	public static <M extends Map<Resource, Permission>> M collectEffectivePermissions(
			final Collection<Role> roles, final Set<Role> handledRoles,
			final Set<Permission> handledPermissions, final Set<Resource> handledResources,
			final M collectedPermissions)
	{
		for(final Role role : roles)
		{
			if(handledRoles.add(role))
			{
				collectEffectivePermissions(role.permissions(),handledPermissions,handledResources,
						collectedPermissions);
				collectEffectivePermissions(role.roles(),handledRoles,handledPermissions,
						handledResources,collectedPermissions);
			}
		}

		return collectedPermissions;
	}


	public static <R, C extends Set<? super Role>> C collectEffectiveRoles(
			final Collection<Role> roles, final C effectiveRoles)
	{
		// more specific roles take precedence, so it's desired to use add
		// instead of put
		/*
		 * (19.06.2015 TM)NOTE: might be destroyed be using old JDK collections:
		 * There is no Set type with proper order (like "Enum" in Jadoth Core
		 * library) List would allow duplicates which is false, Set has no
		 * order. JDK collections are just not sufficient for proper software
		 * development.
		 */
		for(final Role role : roles)
		{
			if(effectiveRoles.add(role))
			{
				// for every newly added role (not contained yet) collect it's
				// roles recursively
				collectEffectiveRoles(roles,effectiveRoles);
			}
		}

		return effectiveRoles;
	}


	public static void collectEffectivePermissions(final Iterable<Permission> permissions,
			final Set<Permission> handledPermissions, final Set<Resource> handledResources,
			final Map<Resource, Permission> effectivePermissions)
	{
		for(final Permission permission : permissions)
		{
			if(!handledPermissions.add(permission))
			{
				continue; // this permission (identity) has already been handled
							// via another role, so skip it
			}

			final Resource resource = permission.resource();

			/*
			 * must clear the handledResources collection for every unique
			 * permission as the current permission might handle a previously
			 * handled resource again with potentially higher factor and
			 * therefor required update to effectivePermissions. The
			 * handledResources is still necessary to recognize recursive
			 * definitions.
			 */
			handledResources.clear();
			collectEffectivePermissions(resource,permission,handledResources,effectivePermissions);
		}
	}


	public static void collectEffectivePermissions(final Resource resource,
			final Permission permission, final Set<Resource> handledResources,
			final Map<Resource, Permission> effectivePermissions)
	{
		if(!handledResources.add(resource))
		{
			return; // already handled
		}

		final Permission collectedPermission = effectivePermissions.get(resource);
		if(collectedPermission == null
				|| Math.abs(collectedPermission.factor()) < Math.abs(permission.factor()))
		{
			effectivePermissions.put(resource,permission);
		}

		for(final Resource child : resource.children())
		{
			collectEffectivePermissions(child,permission,handledResources,effectivePermissions);
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Returns a new {@link Role} instance defined by the passed values.
	 *
	 * @param name
	 *            the unchangeable identifying name of the role.
	 * @param roles
	 *            the parent roles of the new role, potentially none.
	 * @param permissions
	 *            the explicitely granted permissions for the new role,
	 *            potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(final String name, final Set<? extends Role> roles,
			final Set<? extends Permission> permissions)
	{
		return new Role.Implementation(name,roles,permissions);
	}


	/**
	 * Returns a new {@link Role} instance defined by the passed values with no
	 * explicit permissions.
	 *
	 * @param name
	 *            the unchangeable identifying name of the role.
	 * @param roles
	 *            the parent roles of the new role, potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role NewFromRoles(final String name, final Set<? extends Role> roles)
	{
		return new Role.Implementation(name,roles,Collections.emptySet());
	}


	/**
	 * Returns a new {@link Role} instance defined by the passed values with no
	 * explicit parent roles.
	 *
	 * @param name
	 *            the unchangeable identifying name of the role.
	 * @param permissions
	 *            the explicitely granted permissions for the new role,
	 *            potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role NewFromPermissions(final String name,
			final Set<? extends Permission> permissions)
	{
		return new Role.Implementation(name,Collections.emptySet(),permissions);
	}


	/**
	 * Returns a new {@link Role} instance with the passed name that is
	 * otherwise empty.
	 *
	 * @param name
	 *            the unchangeable identifying name of the role.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(final String name)
	{
		return new Role.Implementation(name,Collections.emptySet(),Collections.emptySet());
	}


	/**
	 * Returns a new {@link Role} instance defined by the passed values with no
	 * explicit permissions.
	 *
	 * @param name
	 *            the unchangeable identifying name of the role.
	 * @param roles
	 *            the parent roles of the new role, potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(final String name, final Role... roles)
	{
		return new Role.Implementation(name,
				roles == null ? Collections.emptySet() : Utils.HashSet(roles),
				Collections.emptySet());
	}


	/**
	 * Returns a new {@link Role} instance defined by the passed values with no
	 * explicit parent roles.
	 *
	 * @param name
	 *            the unchangeable identifying name of the role.
	 * @param permissions
	 *            the explicitely granted permissions for the new role,
	 *            potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(final String name, final Permission... permissions)
	{
		return new Role.Implementation(name,Collections.emptySet(),
				permissions == null ? Collections.emptySet() : Utils.HashSet(permissions));
	}


	/**
	 * Validates and returns a passed non-null {@link Role} instance or creates
	 * a new one in case of a null refrence. In this simple defeault
	 * implementation, validating simply means to check if the passed role's
	 * name is equal to the passed name.
	 *
	 * @param role
	 *            an already existing {@link Role} instance for that name or
	 *            null.
	 * @param name
	 *            the indentifying name of the role. May not be <tt>null</tt>.
	 * @param parentRoles
	 *            the names of the parent roles as per definition.
	 * @param permissions
	 *            the names of the explicit permissions as per definition.
	 * @return the non-null passed {@link Role} instance, otherwise a new one.
	 */
	public static Role provide(final Role role, final String name, final Set<String> parentRoles,
			final Set<String> permissions)
	{
		if(name == null)
		{
			// (25.06.2014 TM)TODO: all plain string exceptions should be
			// refactored to proper types
			throw new IllegalArgumentException("No role name given.");
		}

		if(role != null)
		{
			if(!name.equals(role.name()))
			{
				throw new IllegalArgumentException("Invalid name for existing role: " + name);
			}

			// (25.06.2014 TM)TODO: validate parent roles and permissions by
			// name
			return role;
		}

		// instantiate only with name, roles and permissions get updated later
		return New(name);
	}


	/**
	 * Updates the passed {@link Role} instance for the given values. If
	 * inconsistencies are detected or the instance is not {@link Mutable}, an
	 * {@link IllegalArgumentException} is thrown.
	 *
	 * @param role
	 *            the {@link Role} instance to be updated.
	 * @param name
	 *            the identifiying name of the role.
	 * @param parentRoles
	 *            the parent roles that the passed {@link Role} instance shall
	 *            be updated with.
	 * @param permissions
	 *            the explicit permissions that the passed {@link Role} instance
	 *            shall be updated with.
	 * @throws IllegalArgumentException
	 *             if name is null, names do not match or the role is not
	 *             {@link Mutable}.
	 */
	public static void update(final Role role, final String name, final Set<Role> parentRoles,
			final Set<Permission> permissions)
	{
		if(name == null)
		{
			throw new IllegalArgumentException("No role name given.");
		}

		if(!name.equals(role.name()))
		{
			throw new IllegalArgumentException("Invalid name for role: " + name);
		}

		if(!(role instanceof Mutable))
		{
			throw new IllegalArgumentException(
					"Passed subject is not of a generically mutable type");
		}

		// ensure that all updating methods are called atomically
		synchronized(role)
		{
			((Mutable)role).setRoles(parentRoles);
			((Mutable)role).setPermissions(permissions);
		}
	}



	///////////////////////////////////////////////////////////////////////////
	// member types //
	/////////////////////

	/**
	 * Interface equivalent of a constructor for the interface type {@link Role}
	 * .
	 *
	 * @author XDEV Software (TM)
	 */
	@FunctionalInterface
	public interface Creator
	{
		public Role createRole(String name, Set<? extends Role> roles,
				Set<? extends Permission> permissions);
	}



	/**
	 * Extension of the type {@link Role} with mutability methods. This concept
	 * is useful to have a properly typed representation of optional mutability.
	 *
	 * @author XDEV Software (TM)
	 */
	public interface Mutable extends Role
	{
		/**
		 * Sets the passed {@link Permission} instances as the new explicit
		 * permissions for this {@link Role} instance.
		 *
		 * @param permissions
		 *            the {@link Permission} instances to be set.
		 */
		public void setPermissions(Collection<? extends Permission> permissions);


		/**
		 * Sets the passed {@link Role} instances as the new parent roles for
		 * this {@link Role} instance.
		 *
		 * @param roles
		 *            the {@link Role} instances to be set.
		 */
		public void setRoles(Collection<? extends Role> roles);
	}



	/**
	 * A simple {@link Role.Mutable} default implementation.
	 *
	 * @author XDEV Software (TM)
	 */
	public class Implementation implements Role.Mutable
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private final String					name;
		private volatile Collection<Role>		roles;
		private volatile Collection<Permission>	permissions;


		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(final String name, final Collection<? extends Role> roles,
				final Collection<? extends Permission> permissions)
		{
			super();
			this.name = notNull(name);
			this.setRoles(roles);
			this.setPermissions(permissions);

			/*
			 * note: cannot cache effective items immediately in unupdatable /
			 * immutable implementation as the referenced roles might not be
			 * complete yet.
			 */
		}


		/**
		 * Sets the passed {@link Permission} instances in a internally newly
		 * instantiated collection.
		 *
		 * @param permissions
		 *            the {@link Permission} instances to be set.
		 */
		@Override
		public void setPermissions(final Collection<? extends Permission> permissions)
		{
			this.permissions = permissions == null ? Collections.emptySet()
					: new HashSet<>(permissions);
		}


		/**
		 * Sets the passed {@link Role} instances in a internally newly
		 * instantiated collection.
		 *
		 * @param roles
		 *            the {@link Role} instances to be set.
		 */
		@Override
		public void setRoles(final Collection<? extends Role> roles)
		{
			this.roles = roles == null ? Collections.emptySet() : new HashSet<>(roles);
		}


		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final String name()
		{
			return this.name;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Collection<Role> roles()
		{
			return this.roles;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Collection<Permission> permissions()
		{
			return this.permissions;
		}


		@Override
		public String toString()
		{
			return this.name;
		}
	}

}
