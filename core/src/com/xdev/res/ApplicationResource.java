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

package com.xdev.res;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;


/**
 * @author XDEV Software
 *		
 */
public class ApplicationResource extends StreamResource
{
	public ApplicationResource(final Class<?> requestor, final String path)
	{
		super(new ApplicationStreamSource(requestor,path),getFileName(path));
	}


	private static String getFileName(final String path)
	{
		int i = path.lastIndexOf('/');
		if(i == -1)
		{
			i = path.lastIndexOf('\\');
		}
		if(i != -1)
		{
			return path.substring(i + 1);
		}
		return path;
	}



	private static class ApplicationStreamSource implements StreamSource
	{
		private final Class<?>	requestor;
		private final String	path;


		public ApplicationStreamSource(final Class<?> requestor, final String path)
		{
			this.requestor = requestor;
			this.path = path;
		}


		@Override
		public InputStream getStream()
		{
			InputStream stream = getInputStream(this.path);
			if(stream != null)
			{
				return stream;
			}

			final String webContent = "WebContent/";
			if(this.path.startsWith(webContent))
			{
				stream = getInputStream(this.path.substring(webContent.length()));
				if(stream != null)
				{
					return stream;
				}
			}

			throw new RuntimeException("'" + this.path + "' could not be found in application.");
		}


		private InputStream getInputStream(final String path)
		{
			final File file = new File(VaadinService.getCurrent().getBaseDirectory(),path);
			if(file.exists())
			{
				try
				{
					return new FileInputStream(file);
				}
				catch(final FileNotFoundException e)
				{
				}
			}

			Class<?> clazz = this.requestor;
			if(clazz == null)
			{
				clazz = getClass();
			}
			final InputStream stream = clazz.getResourceAsStream(path);
			if(stream != null)
			{
				return stream;
			}

			return null;
		}
	}
}
