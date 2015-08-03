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

package com.xdev.security.authentication.db;


import java.util.List;

import com.xdev.dal.DAOs;
import com.xdev.security.authentication.AuthenticationFailedException;
import com.xdev.security.authentication.Authenticator;
import com.xdev.security.authentication.AuthenticatorLoginInfo;
import com.xdev.security.authentication.CredentialsUsernamePassword;


/**
 * @author XDEV Software (JW)
 */

public final class DBAuthenticator
		implements Authenticator<CredentialsUsernamePassword, CredentialsUsernamePassword>,
		AuthenticatorLoginInfo
{

	private final Class<? extends CredentialsUsernamePassword>	authenticationEntityType;
	private DBHashStrategy										hashStrategy	= new DBHashStrategy.PBKDF2WithHmacSHA1();
	private boolean												hasPassedLogin	= false;


	/**
	 *
	 */
	public DBAuthenticator(
			final Class<? extends CredentialsUsernamePassword> authenticationEntityType)
	{
		this.authenticationEntityType = authenticationEntityType;
	}


	public final CredentialsUsernamePassword authenticate(final String username,
			final String password) throws AuthenticationFailedException
	{
		return this.authenticate(CredentialsUsernamePassword.New(username,password));
	}


	@Override
	public CredentialsUsernamePassword authenticate(final CredentialsUsernamePassword credentials)
			throws AuthenticationFailedException
	{
		return checkCredentials(credentials);
	}


	protected CredentialsUsernamePassword checkCredentials(
			final CredentialsUsernamePassword credentials) throws AuthenticationFailedException
	{
		final String hashedInputPassword = new String(
				this.hashStrategy.hashPassword(credentials.password()));
		final List<Object> entities = DAOs.getByEntityType(this.authenticationEntityType).findAll();
		for(final Object object : entities)
		{
			final CredentialsUsernamePassword entity = (CredentialsUsernamePassword)object;
			if(entity.username().equals(credentials.username()))
			{
				if(entity.password().equals(hashedInputPassword))
				{
					this.hasPassedLogin = true;
					return entity;
				}
			}
		}

		throw new AuthenticationFailedException();
	}


	@Override
	public boolean hasPassedLogin()
	{
		return this.hasPassedLogin;
	}


	public DBHashStrategy getHashStrategy()
	{
		return this.hashStrategy;
	}


	public void setHashStrategy(final DBHashStrategy hashStrategy)
	{
		this.hashStrategy = hashStrategy;
	}
}
