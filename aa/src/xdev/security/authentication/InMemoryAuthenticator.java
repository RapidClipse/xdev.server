/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authentication;

import net.jadoth.collections.EqConstHashTable;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.collections.types.XImmutableMap;
import net.jadoth.util.KeyValue;


/**
 * Trivial in-memory implementation of an {@link Authenticator}.
 * <p>
 * Note that this implementation stores passwords in plain text in memory and is therefor only suitable
 * for examples or very trivial, non-security-critical use cases.
 *
 * @author XDEV Software (TM)
 */
public final class InMemoryAuthenticator implements Authenticator<CredentialsUsernamePassword, Boolean>
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Creates a new {@link InMemoryAuthenticator} instance using a copy of the passed username/password map.
	 * The entries are interpreted as the keys representing the key and the associated value representing the
	 * associated password.
	 *
	 * @param usernamePasswords the username/password data.
	 * @return a new {@link InMemoryAuthenticator} instance using the passed data as a user inventory.
	 */
	public static final InMemoryAuthenticator New(final XGettingMap<String, String> usernamePasswords)
	{
		// immure to ensure immutability for central, potentially concurrently used instance
		return new InMemoryAuthenticator(usernamePasswords.immure());
	}

	/**
	 * Creates a new {@link InMemoryAuthenticator} instance using a copy of the passed username/password collection.
	 * The entries are interpreted as the keys representing the key and the associated value representing the
	 * associated password.
	 *
	 * @param usernamePasswords the username/password data.
	 * @return a new {@link InMemoryAuthenticator} instance using the passed data as a user inventory.
	 */
	public static final InMemoryAuthenticator New(
		final XGettingCollection<? extends KeyValue<String, String>> usernamePasswords
	)
	{
		return new InMemoryAuthenticator(EqConstHashTable.New(usernamePasswords));
	}

	/**
	 * Creates a new {@link InMemoryAuthenticator} instance using a copy of the passed username/password array.
	 * The entries are interpreted as the keys representing the key and the associated value representing the
	 * associated password.
	 *
	 * @param usernamePasswords the username/password data.
	 * @return a new {@link InMemoryAuthenticator} instance using the passed data as a user inventory.
	 */
	@SafeVarargs
	public static final InMemoryAuthenticator New(final KeyValue<String, String>... usernamePasswords)
	{
		return new InMemoryAuthenticator(EqConstHashTable.New(usernamePasswords));
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/**
	 * The internally used authentication map.
	 */
	final XImmutableMap<String, String> usernamePasswords;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Internal implementation-detail constructor.
	 *
	 * @param usernamePasswords
	 */
	InMemoryAuthenticator(final XImmutableMap<String, String> usernamePasswords)
	{
		super();
		this.usernamePasswords = usernamePasswords;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final boolean authenticate(final String username, final String password) throws AuthenticationFailedException
	{
		return this.authenticate(CredentialsUsernamePassword.New(username, password));
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * Returns {@link Boolean#FALSE} if the attempt to authenticate the passed credentials fails, {@link Boolean#TRUE}
	 * otherwise.
	 *
	 * @param credentials the credentials instance to be authenticated.
	 * @return a non-null {@link Boolean} instance indicating the result of the authentication attempt.
	 * @throws AuthenticationFailedException never gets thrown in this simple implementation.
	 */
	@Override
	public final Boolean authenticate(final CredentialsUsernamePassword credentials) throws AuthenticationFailedException
	{
		// lookup stored password
		final String storedPassword = this.usernamePasswords.get(credentials.username());

		/* if no password found for the given username or passwords don't match, the authentication failed.
		 * Note that authentication logic does intentionally NOT give any clue about why username and password are
		 * not valid.
		 */
		return storedPassword != null && storedPassword.equals(credentials.password());
	}



	/**
	 * An {@link InMemoryAuthenticator}-specific provider type.
	 *
	 * @author XDEV Software (TM)
	 */
	public interface Provider extends AuthenticatorProvider<CredentialsUsernamePassword, Boolean>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public InMemoryAuthenticator provideAuthenticator();
	}

}
