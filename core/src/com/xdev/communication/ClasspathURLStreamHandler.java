/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;


/**
 * @author XDEV Software
 * @since 4.0
 */
public class ClasspathURLStreamHandler extends URLStreamHandler
{
	@Override
	protected URLConnection openConnection(final URL url) throws IOException
	{
		final String path = url.getPath();
		
		URL classpathUrl = Thread.currentThread().getContextClassLoader().getResource(path);
		if(classpathUrl == null)
		{
			classpathUrl = ClasspathURLStreamHandler.class.getResource(path);
		}
		
		if(classpathUrl == null)
		{
			throw new FileNotFoundException(path);
		}
		
		return classpathUrl.openConnection();
	}
}
