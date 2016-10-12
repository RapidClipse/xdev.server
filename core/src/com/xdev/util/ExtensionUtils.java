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

package com.xdev.util;


import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * @author XDEV Software
 * @since 1.2
 * @noapi <strong>For internal use only. This class is subject to change in the
 *        future.</strong>
 */
public class ExtensionUtils
{
	private ExtensionUtils()
	{
	}
	
	
	public static <T> List<T> readExtensions(final String type, final Class<T> clazz)
			throws Exception
	{
		final List<T> extensions = new ArrayList<>();
		
		final ClassLoader classLoader = ExtensionUtils.class.getClassLoader();
		for(final Enumeration<URL> e = classLoader
				.getResources("META-INF/xdev-server-extensions.xml"); e.hasMoreElements();)
		{
			final URL url = e.nextElement();
			final Document document = new SAXReader().read(url);
			final Element rootElement = document.getRootElement();
			if(rootElement != null)
			{
				for(final Object o : rootElement.elements(type))
				{
					final String extensionClassName = ((Element)o).getTextTrim();
					final Object extension = classLoader.loadClass(extensionClassName)
							.newInstance();
					if(clazz.isInstance(extension))
					{
						final T t = clazz.cast(extension);
						extensions.add(t);
					}
					else
					{
						new IllegalArgumentException(
								extensionClassName + " is not a " + clazz.getSimpleName());
					}
				}
			}
		}
		
		return extensions;
	}
}
