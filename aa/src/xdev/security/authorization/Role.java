/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.collections.ConstHashEnum;
import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.HashEnum;
import net.jadoth.collections.X;
import net.jadoth.collections.types.XAddingEnum;
import net.jadoth.collections.types.XEnum;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XImmutableEnum;
import net.jadoth.collections.types.XMap;


/**
 * The type representing a role for the purpose of authorization management. A role is defined as a collection of
 * permissions and other roles whose permissions are applied recursively to it.
 *
 * Application-specific types that shall be handled by the authorization framework have to implement this interface.
 *
 * @author XDEV Software (TM)
 */
public interface Role
{
	/**
	 * @return The role's unchangable name that identifies a particular instance.
	 */
	public String name();

	/**
	 * @return The roles this role instance has been defined by.
	 */
	public XGettingEnum<Role> roles();

	/**
	 * @return The permissions this role instance has been defined by.
	 */
	public XGettingEnum<Permission> permissions();



	///////////////////////////////////////////////////////////////////////////
	// default methods  //
	/////////////////////

	/**
	 * Returns the effective roles of this {@link Role} instance, meaning a collection containing all the roles
	 * that this instance has been defined by and all their effective roles.
	 * Every {@link Role} instance is only returned once (identity uniqueness).
	 *
	 * @return the effective roles.
	 */
	public default XGettingEnum<Role> effectiveRoles()
	{
		return collectEffectiveRoles(this.roles(), HashEnum.New());
	}

	/**
	 * Returns the effective permissions of this {@link Role} instance, meaning a collection containing all the
	 * permissions that this instance has been defined by and the effective permissions of all its roles.
	 * For every unique {@link Resource} instance the permission with the highest absolute value is returned.
	 *
	 * @return the effective permissions.
	 */
	public default XGettingMap<Resource, Permission> effectivePermissions()
	{
		return collectEffectivePermissions(X.Reference(this));
	}



	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Utility method that collects the effective permissions from a collection of passed roles.
	 *
	 * @param roles the {@link Role} instances whose effective permissions shall be determined.
	 * @return the passed roles' collected effective permissions.
	 * @see #effectivePermissions()
	 */
	public static XGettingMap<Resource, Permission> collectEffectivePermissions(final XGettingSequence<Role> roles)
	{
		return collectEffectivePermissions(roles, HashEnum.New(), HashEnum.New(), HashEnum.New(), EqHashTable.New());
	}

	public static <M extends XMap<Resource, Permission>> M collectEffectivePermissions(
		final XGettingCollection<Role> roles               ,
		final XEnum<Role>              handledRoles        ,
		final XEnum<Permission>        handledPermissions  ,
		final XEnum<Resource>          handledResources    ,
		final M                        collectedPermissions
	)
	{
		for(final Role role : roles)
		{
			if(handledRoles.add(role))
			{
				collectEffectivePermissions(role.permissions(), handledPermissions, handledResources, collectedPermissions);
				collectEffectivePermissions(role.roles(), handledRoles, handledPermissions, handledResources, collectedPermissions);
			}
		}

		return collectedPermissions;
	}

	public static <R, C extends XAddingEnum<? super Role>> C collectEffectiveRoles(
		final XGettingSequence<Role> roles         ,
		final C                      effectiveRoles
	)
	{
		// more specific roles take precedence, so it's desired to use add instead of put
		for(final Role role : roles)
		{
			if(effectiveRoles.add(role))
			{
				// for every newly added role (not contained yet) collect it's roles recursively
				collectEffectiveRoles(roles, effectiveRoles);
			}
		}

		return effectiveRoles;
	}

	public static void collectEffectivePermissions(
		final Iterable<Permission>       permissions       ,
		final XEnum<Permission>          handledPermissions,
		final XEnum<Resource>            handledResources  ,
		final XMap<Resource, Permission> effectivePermissions
	)
	{
		for(final Permission permission : permissions)
		{
			if(!handledPermissions.add(permission))
			{
				continue; // this permission (identity) has already been handled via another role, so skip it
			}

			final Resource resource = permission.resource();

			/* must clear the handledResources collection for every unique permission as the current permission
			 * might handle a previously handled resource again with potentially higher factor and therefor required
			 * update to effectivePermissions.
			 * The handledResources is still necessary to recognize recursive definitions.
			 */
			handledResources.clear();
			collectEffectivePermissions(resource, permission, handledResources, effectivePermissions);
		}
	}

	public static void collectEffectivePermissions(
		final Resource                   resource            ,
		final Permission                 permission          ,
		final XEnum<Resource>            handledResources    ,
		final XMap<Resource, Permission> effectivePermissions
	)
	{
		if(!handledResources.add(resource))
		{
			return; // already handled
		}

		final Permission collectedPermission = effectivePermissions.get(resource);
		if(collectedPermission == null || Math.abs(collectedPermission.factor()) < Math.abs(permission.factor()))
		{
			effectivePermissions.put(resource, permission);
		}

		for(final Resource child : resource.children())
		{
			collectEffectivePermissions(child, permission, handledResources, effectivePermissions);
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Returns a new {@link Role} instance defined by the passed values.
	 *
	 * @param name the unchangeable identifying name of the role.
	 * @param roles the parent roles of the new role, potentially none.
	 * @param permissions the explicitely granted permissions for the new role, potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(
		final String                             name       ,
		final XGettingEnum<? extends Role>       roles      ,
		final XGettingEnum<? extends Permission> permissions
	)
	{
		return new Role.Implementation(name, roles, permissions);
	}

	/**
	 * Returns a new {@link Role} instance defined by the passed values with no explicit permissions.
	 *
	 * @param name the unchangeable identifying name of the role.
	 * @param roles the parent roles of the new role, potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role NewFromRoles(final String name, final XGettingEnum<? extends Role> roles)
	{
		return new Role.Implementation(name, roles, X.empty());
	}

	/**
	 * Returns a new {@link Role} instance defined by the passed values with no explicit parent roles.
	 *
	 * @param name the unchangeable identifying name of the role.
	 * @param permissions the explicitely granted permissions for the new role, potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role NewFromPermissions(final String name , final XGettingEnum<? extends Permission> permissions)
	{
		return new Role.Implementation(name, X.empty(), permissions);
	}

	/**
	 * Returns a new {@link Role} instance with the passed name that is otherwise empty.
	 *
	 * @param name the unchangeable identifying name of the role.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(final String name)
	{
		return new Role.Implementation(name, X.empty(), X.empty());
	}

	/**
	 * Returns a new {@link Role} instance defined by the passed values with no explicit permissions.
	 *
	 * @param name the unchangeable identifying name of the role.
	 * @param roles the parent roles of the new role, potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(final String name , final Role... roles)
	{
		return new Role.Implementation(name, roles == null ?X.empty() :X.ConstEnum(roles), X.empty());
	}

	/**
	 * Returns a new {@link Role} instance defined by the passed values with no explicit parent roles.
	 *
	 * @param name the unchangeable identifying name of the role.
	 * @param permissions the explicitely granted permissions for the new role, potentially none.
	 * @return a new {@link Role} instance.
	 */
	public static Role New(final String name, final Permission... permissions)
	{
		return new Role.Implementation(name, X.empty(), permissions == null ?X.empty() :X.ConstEnum(permissions));
	}

	/**
	 * Validates and returns a passed non-null {@link Role} instance or creates a new one in case of a null refrence.
	 * In this simple defeault implementation, validating simply means to check if the passed role's name is equal to
	 * the passed name.
	 *
	 * @param  role an already existing {@link Role} instance for that name or null.
	 * @param  name the indentifying name of the role. May not be <tt>null</tt>.
	 * @param  parentRoles the names of the parent roles as per definition.
	 * @param  permissions the names of the explicit permissions as per definition.
	 * @return the non-null passed {@link Role} instance, otherwise a new one.
	 */
	public static Role provide(
		final Role                 role       ,
		final String               name       ,
		final XGettingEnum<String> parentRoles,
		final XGettingEnum<String> permissions
	)
	{
		if(name == null)
		{
			// (25.06.2014 TM)TODO: all plain string exceptions should be refactored to proper types
			throw new IllegalArgumentException("No role name given.");
		}

		if(role != null)
		{
			if(!name.equals(role.name()))
			{
				throw new IllegalArgumentException("Invalid name for existing role: "+name);
			}

			// (25.06.2014 TM)TODO: validate parent roles and permissions by name
			return role;
		}

		// instantiate only with name, roles and permissions get updated later
		return New(name);
	}

	/**
	 * Updates the passed {@link Role} instance for the given values.
	 * If inconsistencies are detected or the instance is not {@link Mutable}, an {@link IllegalArgumentException} is
	 * thrown.
	 *
	 * @param  role the {@link Role} instance to be updated.
	 * @param  name the identifiying name of the role.
	 * @param  parentRoles the parent roles that the passed {@link Role} instance shall be updated with.
	 * @param  permissions the explicit permissions that the passed {@link Role} instance shall be updated with.
	 * @throws IllegalArgumentException if name is null, names do not match or the role is not {@link Mutable}.
	 */
	public static void update(
		final Role                     role       ,
		final String                   name       ,
		final XGettingEnum<Role>       parentRoles,
		final XGettingEnum<Permission> permissions
	)
	{
		if(name == null)
		{
			throw new IllegalArgumentException("No role name given.");
		}

		if(!name.equals(role.name()))
		{
			throw new IllegalArgumentException("Invalid name for role: "+name);
		}

		if(!(role instanceof Mutable))
		{
			throw new IllegalArgumentException("Passed subject is not of a generically mutable type");
		}

		// ensure that all updating methods are called atomically
		synchronized(role)
		{
			((Mutable)role).setRoles(parentRoles);
			((Mutable)role).setPermissions(permissions);
		}
	}



	///////////////////////////////////////////////////////////////////////////
	// member types     //
	/////////////////////

	/**
	 * Interface equivalent of a constructor for the interface type {@link Role}.
	 *
	 * @author XDEV Software (TM)
	 */
	@FunctionalInterface
	public interface Creator
	{
		public Role createRole(String name, XGettingEnum<? extends Role> roles, XGettingEnum<? extends Permission> permissions);
	}

	/**
	 * Extension of the type {@link Role} with mutability methods. This concept is useful to have a properly typed
	 * representation of optional mutability.
	 *
	 * @author XDEV Software (TM)
	 */
	public interface Mutable extends Role
	{
		/**
		 * Sets the passed {@link Permission} instances as the new explicit permissions for this {@link Role} instance.
		 *
		 * @param permissions the {@link Permission} instances to be set.
		 */
		public void setPermissions(XGettingCollection<? extends Permission> permissions);

		/**
		 * Sets the passed {@link Role} instances as the new parent roles for this {@link Role} instance.
		 *
		 * @param roles the {@link Role} instances to be set.
		 */
		public void setRoles(XGettingCollection<? extends Role> roles);
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

		private final    String                     name       ;
		private volatile XImmutableEnum<Role>       roles      ;
		private volatile XImmutableEnum<Permission> permissions;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(
			final String                                   name       ,
			final XGettingCollection<? extends Role>       roles      ,
			final XGettingCollection<? extends Permission> permissions
		)
		{
			super();
			this.name = notNull(name);
			this.setRoles(roles);
			this.setPermissions(permissions);

			/* note:
			 * cannot cache effective items immediately in unupdatable / immutable implementation as the referenced
			 * roles might not be complete yet.
			 */
		}

		/**
		 * Sets the passed {@link Permission} instances in a internally newly instantiated collection.
		 * @param permissions the {@link Permission} instances to be set.
		 */
		@Override
		public void setPermissions(final XGettingCollection<? extends Permission> permissions)
		{
			this.permissions = permissions == null
				?X.empty()
				:ConstHashEnum.New(permissions)
			;
		}

		/**
		 * Sets the passed {@link Role} instances in a internally newly instantiated collection.
		 * @param roles the {@link Role} instances to be set.
		 */
		@Override
		public void setRoles(final XGettingCollection<? extends Role> roles)
		{
			this.roles = roles == null
				?X.empty()
				:ConstHashEnum.New(roles)
			;
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
		public final XGettingEnum<Role> roles()
		{
			return this.roles;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XGettingEnum<Permission> permissions()
		{
			return this.permissions;
		}

	}

}
