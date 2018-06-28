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

package com.xdev.communication;


import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;


/**
 * @author XDEV Software
 * @since 4.0
 */
public class XdevURLStreamHandlerFactory implements URLStreamHandlerFactory
{
	public static void installIfNeeded()
	{
		try
		{
			new URL("classpath:test").openConnection();
		}
		catch(final MalformedURLException e)
		{
			// no classpath handler available
			install();
		}
		catch(final IOException e)
		{
		}
	}
	
	
	public static void install()
	{
		try
		{
			final Field field = URL.class.getDeclaredField("factory");
			field.setAccessible(true);
			final URLStreamHandlerFactory factory = (URLStreamHandlerFactory)field.get(null);
			field.set(null,new XdevURLStreamHandlerFactory(factory));
		}
		catch(NoSuchFieldException | SecurityException | IllegalArgumentException
				| IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private final URLStreamHandlerFactory	delegate;
	private final ClasspathURLStreamHandler	classpathURLStreamHandler	= new ClasspathURLStreamHandler();
	
	
	public XdevURLStreamHandlerFactory(final URLStreamHandlerFactory delegate)
	{
		super();
		this.delegate = delegate;
	}
	
	
	@Override
	public URLStreamHandler createURLStreamHandler(final String protocol)
	{
		if(protocol.toLowerCase().equals("classpath"))
		{
			return this.classpathURLStreamHandler;
		}
		
		if(this.delegate != null)
		{
			return this.delegate.createURLStreamHandler(protocol);
		}
		
		return null;
	}
}
