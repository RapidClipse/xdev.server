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
import net.jadoth.collections.X;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingEnum;


/**
 * The type representing a resource that shall be handled by the authorization framework.
 * When building authorization structures, every permission instance is associated with exactely one resource instance
 * with a specific weight value.
 * <p>
 * A resource can potentially have linked child resources
 *
 * @author XDEV Software (TM)
 */
public interface Resource
{
	/**
	 * Returns children {@link Resource} instances associated with this instance, potentially none
	 * (empty collection, but never <tt>null</tt>).
	 *
	 * @return the child resources of this resource, potentially none.
	 */
	public default XGettingEnum<? extends Resource> children()
	{
		return X.empty();
	}


	/**
	 * Creates a new {@link Resource} instance of a simple default type with the passed resource name.
	 * The passed name may not be null.
	 *
	 * @param name the name of the new {@link Resource} instance.
	 * @return a new {@link Resource} instance with the passed name.
	 */
	public static Resource New(final String name)
	{
		// simple StringResource type as default
		return StringResource.New(name);
	}

	/**
	 * Provides a {@link Resource} instance for the passed name. If the passed existing instance is not <tt>null</tt>,
	 * it is validated for the specified values, otherwise, a new {@link Resource} instance of a simple default type
	 * is created and returned.
	 *
	 * @param name the identifying name of the {@link Resource} to be provided
	 * @param existingInstance an already existing {@link Resource} instance for the passed name, potentially <tt>null</tt>.
	 * @param children the identifying names of the specified resource's child resources, potentially empty.
	 * @return a {@link Resource} instance that is guaranteed to have the specified name.
	 */
	public static Resource provide(
		final Resource             existingInstance,
		final String               name            ,
		final XGettingEnum<String> children
	)
	{
		// delegate to simple StringResource's provide
		return StringResource.provide(name, existingInstance, children);
	}

	/**
	 * Updates the passed {@link Resource} instance for the passed values, where updating means simply
	 * validating the instance in the simple default case.
	 *
	 * @param resourceName the resource's identifying name.
	 * @param resource the {@link Resource} instance to be updated.
	 * @param children the resource's children, potentially none.
	 */
	public static void update(
		final Resource               resource    ,
		final String                 resourceName,
		final XGettingEnum<Resource> children
	)
	{
		// delegate to simple StringResource's update
		StringResource.update(resourceName, resource, children);
	}



	/**
	 * Simple value type wrapper implementation for a resource {@link String}.
	 * String resources are immutable value types and never have any children.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class StringResource implements Resource
	{
		///////////////////////////////////////////////////////////////////////////
		// static methods //
		///////////////////

		public static Resource provide(
			final String               name            ,
			final Resource             existingInstance,
			final XGettingEnum<String> children
		)
		{
			validateName(name);
			validateChildren(children);

			if(existingInstance == null)
			{
				return New(name);
			}

			validateResource(name, existingInstance);
			return existingInstance;
		}

		public static void update(
			final String                 name    ,
			final Resource               resource,
			final XGettingEnum<Resource> children
		)
		{
			validateName(name);
			validateChildren(children);
			validateResource(name, resource);
		}

		private static void validateName(final String name)
		{
			if(name == null)
			{
				throw new IllegalArgumentException("No resource name given.");
			}
		}

		private static void validateChildren(final XGettingCollection<?> children)
		{
			if(children != null && !children.isEmpty())
			{
				throw new IllegalArgumentException("Default by-name resources cannot reference child resources.");
			}
		}

		private static void validateResource(final String name, final Resource resource)
		{
			if(!(resource instanceof StringResource))
			{
				throw new IllegalArgumentException("Passed resource is not a default by-name resource.");
			}
			if(!name.equals(((StringResource)resource).name()))
			{
				throw new IllegalArgumentException("Passed resource's name does not match the provided name.");
			}
		}


		public static StringResource New(final String name)
		{
			return new StringResource(notNull(name));
		}



		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private final String name;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		StringResource(final String name)
		{
			super();
			this.name = name;
		}



		///////////////////////////////////////////////////////////////////////////
		// declared methods //
		/////////////////////

		public final String name()
		{
			return this.name;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public final boolean equals(final Object other)
		{
			return other == this || other instanceof StringResource && this.name.equals(((StringResource)other).name);
		}

		@Override
		public final int hashCode()
		{
			return this.name.hashCode();
		}

	}

}
