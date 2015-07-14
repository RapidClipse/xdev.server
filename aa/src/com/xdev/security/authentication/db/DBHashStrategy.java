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


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public interface DBHashStrategy
{
	// TODO customize salt strength?
	public byte[] hashPassword(final String password);
	
	
	
	public class PBKDF2WithHmacSHA1 implements DBHashStrategy
	{
		@Override
		public byte[] hashPassword(final String password)
		{
			final byte[] salt = new byte[16];
			byte[] hash = null;
			for(int i = 0; i < 16; i++)
			{
				salt[i] = (byte)i;
			}
			try
			{
				final KeySpec spec = new PBEKeySpec(password.toCharArray(),salt,65536,128);
				final SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				hash = f.generateSecret(spec).getEncoded();
				
			}
			catch(final NoSuchAlgorithmException nsale)
			{
				nsale.printStackTrace();
				
			}
			catch(final InvalidKeySpecException ikse)
			{
				ikse.printStackTrace();
			}
			return hash;
		}
	}
}
