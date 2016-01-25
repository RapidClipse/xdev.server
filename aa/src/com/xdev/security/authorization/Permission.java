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
 */

package com.xdev.security.authorization;


import static com.xdev.security.Utils.notNull;


/**
 * The type representing an permission information for a specific
 * {@link Resource} instance.
 * <p>
 * Every permission has a "factor" value that represents the type and weight of
 * the permission. A negative value (< 0) means denial of access to the resource
 * (or negative permission), a non-negative value (>= 0) means permission of
 * access to the resource (or positive permission). The higher the absolute
 * value of a factor, the higher is its "weight" or priority compared to
 * conflicting permissions defined in other roles. The default factor is 0, so
 * in the simple case of just granting a permission, the user does not have to
 * care about factors at all.
 *
 * @author XDEV Software (TM)
 */
public interface Permission
{
	/**
	 * The resource whose access this {@link Permission} instance describes.
	 *
	 * @return the associated {@link Resource} instance.
	 */
	public Resource resource();


	/**
	 * The factor of this {@link Permission} instance. See {@link Permission}
	 * for details.
	 *
	 * @return the factor of this {@link Permission} instance.
	 */
	public int factor();


	/**
	 * Evaluates if the passed {@link Subject} instance is granted access to the
	 * {@link Resource} instance associated with this {@link Permission}
	 * instance. Custom implementations of this method can use arbitrary complex
	 * logic to perform the evaluation.
	 *
	 * @param subject
	 *            the {@link Subject} instance to be evaluated.
	 * @return whether or not the {@link Subject} instance is granted access to
	 *         this instance's {@link Resource}.
	 */
	public default boolean evaluate(final Subject subject)
	{
		final Permission subjectPermission = subject.effectivePermissions().get(this.resource());
		return subjectPermission != null && subjectPermission.factor() >= 0;
	}


	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Creates a new {@link Permission} instance for the passed {@link Resource}
	 * instance and the passed factor. Note that factor can be any value, no
	 * restriction or validation is wanted in the default implementation.
	 *
	 * @param resource
	 *            the resource to be associated with the {@link Permission}
	 *            instance.
	 * @param factor
	 *            the value used as the {@link Permission} instance's factor.
	 * @return a new {@link Permission} instance for the passed values.
	 */
	public static Permission New(final Resource resource, final int factor)
	{
		return new Permission.Implementation(resource,factor);
	}


	/**
	 * Creates a new {@link Permission} instance for the passed {@link Resource}
	 * instance and a default factor of 0.
	 *
	 * @param resource
	 *            resource the resource to be associated with the
	 *            {@link Permission} instance.
	 * @return a new {@link Permission} instance for the passed values.
	 */
	public static Permission New(final Resource resource)
	{
		return New(resource,0);
	}



	/**
	 * Simple default implementation of a {@link Permission}.
	 *
	 * @author XDEV Software (TM)
	 */
	public class Implementation implements Permission
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final Resource	resource;
		final int		factor;


		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		protected Implementation(final Resource resource, final int factor)
		{
			super();
			this.resource = notNull(resource);
			this.factor = factor;
		}


		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Resource resource()
		{
			return this.resource;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public final int factor()
		{
			return this.factor;
		}


		@Override
		public String toString()
		{
			return this.resource + " " + this.factor;
		}
	}

}
