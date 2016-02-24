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

package com.xdev.security.authentication.jpa;


import java.util.Arrays;
import java.util.List;

import com.xdev.dal.DAOs;
import com.xdev.security.authentication.AuthenticationFailedException;
import com.xdev.security.authentication.Authenticator;
import com.xdev.security.authentication.CredentialsUsernamePassword;


/**
 * @author XDEV Software
 */
public class JPAAuthenticator
		implements Authenticator<CredentialsUsernamePassword, CredentialsUsernamePassword>
{

	private final Class<? extends CredentialsUsernamePassword>	authenticationEntityType;
	private HashStrategy										hashStrategy	= new HashStrategy.SHA2();


	/**
	 *
	 */
	public JPAAuthenticator(
			final Class<? extends CredentialsUsernamePassword> authenticationEntityType)
	{
		this.authenticationEntityType = authenticationEntityType;
	}


	public final CredentialsUsernamePassword authenticate(final String username,
			final String password) throws AuthenticationFailedException
	{
		return this.authenticate(CredentialsUsernamePassword.New(username,password.getBytes()));
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
		final byte[] hashedPassword = this.hashStrategy.hashPassword(credentials.password());
		final List<? extends CredentialsUsernamePassword> entities = DAOs
				.getByEntityType(this.authenticationEntityType).findAll();
		for(final Object object : entities)
		{
			final CredentialsUsernamePassword entity = (CredentialsUsernamePassword)object;
			if(entity.username().equals(credentials.username())
					&& Arrays.equals(hashedPassword,entity.password()))
			{
				return entity;
			}
		}

		throw new AuthenticationFailedException();
	}


	public HashStrategy getHashStrategy()
	{
		return this.hashStrategy;
	}


	public void setHashStrategy(final HashStrategy hashStrategy)
	{
		this.hashStrategy = hashStrategy;
	}
}
