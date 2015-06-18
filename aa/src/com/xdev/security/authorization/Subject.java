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


import net.jadoth.collections.ConstHashEnum;
import net.jadoth.collections.HashEnum;
import net.jadoth.collections.X;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.collections.types.XImmutableEnum;
import net.jadoth.collections.types.XImmutableMap;


/**
 * The type representing a subject for the purpose of authorization management.
 * A subject is usually an authenticated user, but can also be a communicating
 * service, mashine, or an abstract entity.
 *
 * Application-specific types that shall be handled by the authorization
 * framework have to implement this interface.
 *
 * @author XDEV Software (TM)
 */
public interface Subject
{
	/**
	 * @return the subject's unchangable name that identifies a particular
	 *         instance.
	 */
	public String name();
	
	
	/**
	 * @return this subject's explicitely defined roles.
	 */
	public XGettingEnum<Role> roles();
	
	
	// /////////////////////////////////////////////////////////////////////////
	// default methods //
	// ///////////////////
	
	// may be runtime-determined from declared groups or cached by
	// implementation
	/**
	 * Returns a collection containing all effective roles, meaning all
	 * explicitely defined roles and all recursively inherited roles.
	 *
	 * @return all effective (recursively reachable) roles for this instance.
	 * @see #roles()
	 */
	public default XGettingEnum<Role> effectiveRoles()
	{
		return Role.collectEffectiveRoles(this.roles(),HashEnum.<Role> New());
	}
	
	
	/**
	 * Returns a collection containing all effective permissions, meaning the
	 * combined permissions of all effective roles as returned by
	 * {@link #effectiveRoles()}. If multiple permissions apply to the same
	 * {@link Resource}, the permission with the highest significance (highest
	 * absolut value of its weight) supersedes the others.
	 *
	 * @return a map containing all effective permissions and their applying
	 *         resources.
	 */
	public default XGettingMap<Resource, Permission> effectivePermissions()
	{
		return Role.collectEffectivePermissions(this.roles());
	}
	
	
	/**
	 * Queries if this subject instance has permission for the passed
	 * {@link Resource} instance. The used logic refers to the effective
	 * permissions as determines by {@link #effectivePermissions()}, either by
	 * directly calling it or by caching the result in some form.
	 *
	 * @param resource
	 *            the {@link Resource} instance to be tested.
	 * @return whether this subject instance has permission for the passed
	 *         resource.
	 */
	public default boolean hasPermission(final Resource resource)
	{
		final Permission permission = this.effectivePermissions().get(resource);
		return permission != null && permission.evaluate(this);
	}
	
	
	
	/**
	 * Marker interface that defines a {@link Subject} type to be mutable in
	 * order to set (update) the explicit roles after the instance has been
	 * created. Implementing this interface is only required if the updating of
	 * role shall be handled generically instead of by an explicitly provided
	 * specific updater.
	 *
	 * @author XDEV Software (TM)
	 */
	public interface Mutable extends Subject
	{
		/**
		 * Updates the explicit roles of this subject instance, i.e. the roles
		 * returned by {@link Subject#roles()}.
		 *
		 * @param roles
		 *            the new {@link Role} instances to be used.
		 */
		public void setRoles(XGettingCollection<? extends Role> roles);
	}
	
	
	/**
	 * Generic subject updating logic operating in three steps:
	 * <ol>
	 * <li>If the passed existing subject instance is <tt>null</tt>, a new
	 * instance of type {@link Subject.Implementation} with the passed name and
	 * roles is returned</li>
	 * <li>If the passed existing subject instance is {@link Subject.Mutable},
	 * its roles are updated by calling
	 * {@link Subject.Mutable#setRoles(XGettingCollection)}.</li>
	 * <li>Otherwise, a {@link IllegalArgumentException} is thrown</li>
	 * </ol>
	 * Note that this method assumes that the passed subject name is equal to a
	 * passed non-null existing subject instance's name as the calling context
	 * has already validated it or looked up the subject instance by name in the
	 * first place.
	 *
	 * @param subjectName
	 *            the name of the subject to be updated.
	 * @param existingSubject
	 *            an already existing subject instance, potentially
	 *            <tt>null</tt>.
	 * @param roles
	 *            the new roles to be used in the updated subject instance.
	 * @return an updated or newly created subject instance guaranteed to use
	 *         the passed {@link Role} instances.
	 * @throws IllegalArgumentException
	 *             if the passed non-null existing subject is not
	 *             {@link Mutable}.
	 */
	public static Subject update(final Subject existingSubject, final String subjectName,
			final XGettingEnum<Role> roles) throws IllegalArgumentException
	{
		if(existingSubject == null)
		{
			return new Implementation(subjectName,roles);
		}
		
		if(!(existingSubject instanceof Mutable))
		{
			throw new IllegalArgumentException(
					"Passed subject is not of a generically mutable type");
		}
		((Mutable)existingSubject).setRoles(roles);
		return existingSubject;
	}
	
	
	
	/**
	 * Simple default implementation for a {@link Mutable} {@link Subject}.
	 * Initializing / updating the explicit roles returned by {@link #roles()}
	 * is thread-safe.
	 *
	 * @author XDEV Software (TM)
	 *
	 */
	public class Implementation implements Subject.Mutable
	{
		// /////////////////////////////////////////////////////////////////////////
		// instance fields //
		// //////////////////
		
		private final String									name;
		private volatile XImmutableEnum<Role>					roles;
		private transient XImmutableMap<Resource, Permission>	effectivePermissions	= null;
		
		
		// /////////////////////////////////////////////////////////////////////////
		// constructors //
		// ///////////////
		
		/**
		 * Implementation detail constructor that might change in the future.
		 */
		public Implementation(final String name, final XGettingEnum<Role> roles)
		{
			super();
			this.name = name;
			this.roles = roles.immure();
		}
		
		
		// /////////////////////////////////////////////////////////////////////////
		// declared methods //
		// ///////////////////
		
		public final synchronized void initializeEffectivePermissions()
		{
			// check again if effective permissions have been initialized while
			// waiting for the lock
			if(this.effectivePermissions != null)
			{
				return;
			}
			this.reinitializeEffectivePermissions();
		}
		
		
		public final synchronized void reinitializeEffectivePermissions()
		{
			this.effectivePermissions = Role.collectEffectivePermissions(this.roles()).immure();
		}
		
		
		/**
		 * Implementation-specific method to clear the cached effective roles.
		 * This should never be needed for a basically immutable implementation,
		 * but in order to give at least some possibibilty to retrigger the
		 * caching, here's the way.
		 */
		public final synchronized void clearCachedEffectivePermissions()
		{
			this.effectivePermissions = null;
		}
		
		
		// /////////////////////////////////////////////////////////////////////////
		// override methods //
		// ///////////////////
		
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
		public final XImmutableEnum<Role> roles()
		{
			return this.roles;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XImmutableMap<Resource, Permission> effectivePermissions()
		{
			if(this.effectivePermissions == null)
			{
				this.initializeEffectivePermissions();
			}
			return this.effectivePermissions;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized void setRoles(final XGettingCollection<? extends Role> roles)
		{
			this.roles = roles == null ? X.empty() : ConstHashEnum.New(roles);
			this.clearCachedEffectivePermissions();
		}
		
	}
	
}
