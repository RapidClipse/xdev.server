/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.collections.types.XMap;
import xdev.security.authentication.AuthenticationFailedException;
import xdev.security.authentication.Authenticator;
import xdev.security.authorization.AuthorizationManager;
import xdev.security.authorization.AuthorizationRegistry;
import xdev.security.authorization.Permission;
import xdev.security.authorization.Resource;
import xdev.security.authorization.Role;
import xdev.security.authorization.Subject;

/**
 * Security managing type that combines {@link Authenticator} and {@link AuthorizationManager} aspects.
 *
 * @param <C> the type of the credentials instance to be authenticated.
 * @param <R> the type of the result/response instance to be returned upon an authentication attempt.
 *
 * @author XDEV Software (TM)
 */
public interface XdevSecurityManager<C, R> extends Authenticator<C, R>, AuthorizationManager
{
	public static <C, R> XdevSecurityManager<C, R> New(
		final Authenticator<C, R>  authenticator       ,
		final AuthorizationManager authorizationManager
	)
	{
		return new Implementation<>(
			notNull(authenticator),
			notNull(authorizationManager)
		);
	}



	/**
	 * Default {@link XdevSecurityManager} implementation that wraps delegate {@link Authenticator} and
	 * {@link AuthorizationManager} instances.
	 * <p>
	 * This implementation is immutable.
	 *
	 * @param <C> the type of the credentials instance to be authenticated.
	 * @param <R> the type of the result/response instance to be returned upon an authentication attempt.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class Implementation<C, R> implements XdevSecurityManager<C, R>
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private final Authenticator<C, R>  authenticator       ;
		private final AuthorizationManager authorizationManager;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(
			final Authenticator<C, R>  authenticator       ,
			final AuthorizationManager authorizationManager
		)
		{
			super();
			this.authenticator        = authenticator       ;
			this.authorizationManager = authorizationManager;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final R authenticate(final C credentials) throws AuthenticationFailedException
		{
			return this.authenticator.authenticate(credentials);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Permission providePermission(final Resource resource, final Integer factor)
		{
			return this.authorizationManager.providePermission(resource, factor);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XMap<String, Role> roles()
		{
			return this.authorizationManager.roles();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XMap<String, Subject> subjects()
		{
			return this.authorizationManager.subjects();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Permission providePermission(final Resource resource)
		{
			return this.authorizationManager.providePermission(resource);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Permission permission(final Resource resource, final Integer factor)
		{
			return this.authorizationManager.permission(resource, factor);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Role role(final String roleName)
		{
			return this.authorizationManager.role(roleName);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Subject subject(final String subjectName)
		{
			return this.authorizationManager.subject(subjectName);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockPermissionRegistry()
		{
			return this.authorizationManager.lockPermissionRegistry();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockRoleRegistry()
		{
			return this.authorizationManager.lockRoleRegistry();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockSubjectRegistry()
		{
			return this.authorizationManager.lockSubjectRegistry();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Permission permission(final Resource resource)
		{
			return this.authorizationManager.permission(resource);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final AuthorizationRegistry authorizationRegistry()
		{
			return this.authorizationManager.authorizationRegistry();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void reloadAuthorizations()
		{
			this.authorizationManager.reloadAuthorizations();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Resource resource(final String name)
		{
			return this.authorizationManager.resource(name);
		}

	}

}
