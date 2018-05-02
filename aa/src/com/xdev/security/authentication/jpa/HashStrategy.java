/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * @author XDEV Software
 */
public interface HashStrategy
{
	public byte[] hashPassword(final byte[] password);



	public static abstract class MessageDigestStrategy implements HashStrategy
	{
		@Override
		public byte[] hashPassword(final byte[] password)
		{
			try
			{
				return MessageDigest.getInstance(getAlgorithm()).digest(password);
			}
			catch(final NoSuchAlgorithmException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		
		public abstract String getAlgorithm();
	}



	public static class MD5 extends MessageDigestStrategy
	{
		@Override
		public String getAlgorithm()
		{
			return "MD5";
		}
	}



	public static class SHA2 extends MessageDigestStrategy
	{
		@Override
		public String getAlgorithm()
		{
			return "SHA-256";
		}
	}



	public static class SHA1 extends MessageDigestStrategy
	{
		@Override
		public String getAlgorithm()
		{
			return "SHA-1";
		}
	}



	public static class PBKDF2WithHmacSHA1 implements HashStrategy
	{
		@Override
		public byte[] hashPassword(final byte[] password)
		{
			final byte[] salt = new byte[16];
			new Random().nextBytes(salt);

			byte[] hash = null;

			try
			{
				final KeySpec spec = new PBEKeySpec(new String(password).toCharArray(),salt,65536,
						128);
				final SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				hash = f.generateSecret(spec).getEncoded();

			}
			catch(final NoSuchAlgorithmException | InvalidKeySpecException e)
			{
				throw new RuntimeException(e);
			}

			return hash;
		}
	}
}
