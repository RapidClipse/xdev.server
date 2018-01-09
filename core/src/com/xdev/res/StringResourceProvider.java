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

package com.xdev.res;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;


/**
 * Provides {@link String} resources which can be used for internationalization.
 *
 * @author XDEV Software
 *
 */

public interface StringResourceProvider
{
	/**
	 * Searches for a resource entry according to <code>key</code>.<br>
	 * The <code>requestor</code> may be crucial how the strategy is looking for
	 * the ressource.<br>
	 * <br>
	 * If <code>requestor</code> is <code>null</code> only the default resource
	 * bundle is searched through.
	 *
	 * @param key
	 *            the key of the resource's value pair
	 * @param locale
	 *            to lookup the String for
	 * @param requestor
	 *            the origin of the call to this method or <code>null</code>
	 * @return the value mapped to <code>key</code>
	 * @throws MissingResourceException
	 *             if no resource bundle can be found - depending on this search
	 *             strategy - or if the key can not be found in one of the
	 *             resource files
	 * @throws NullPointerException
	 *             if <code>key</code> is <code>null</code>
	 */

	public String lookupResourceString(String key, Locale locale, Object requestor)
			throws MissingResourceException, NullPointerException;



	public static class Implementation implements StringResourceProvider
	{
		protected final Map<Locale, ResourceBundle>	localizedProjectBundles;
		protected final ResourceBundle				defaultProjectBundle;


		public Implementation()
		{
			this.localizedProjectBundles = new HashMap<>();
			this.defaultProjectBundle = loadProjectResourceBundle(null,this);
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public String lookupResourceString(final String key, Locale locale, final Object requestor)
				throws MissingResourceException
		{
			if(locale == null)
			{
				// XDEVSERVER-141
				if(requestor instanceof Component)
				{
					locale = ((Component)requestor).getLocale();
				}
				if(locale == null)
				{
					final UI ui = UI.getCurrent();
					if(ui != null)
					{
						locale = ui.getLocale();
					}
				}
				if(locale == null)
				{
					locale = Locale.getDefault();
				}
			}

			Class<?> clazz = null;
			if(requestor != null)
			{
				if(requestor instanceof Class)
				{
					clazz = (Class<?>)requestor;
				}
				else if(requestor instanceof Member)
				{
					clazz = ((Member)requestor).getDeclaringClass();
				}
				else
				{
					clazz = requestor.getClass();
				}
			}

			if(clazz != null)
			{
				String name = clazz.getName();
				boolean first = true;

				while(true)
				{
					try
					{
						String baseName;
						if(first)
						{
							first = false;
							baseName = name;
						}
						else
						{
							baseName = name.concat(".package");
						}

						return ResourceBundle.getBundle(baseName,locale,clazz.getClassLoader())
								.getString(key);
					}
					catch(final MissingResourceException mre)
					{
					}
					catch(final NullPointerException npe)
					{
					}

					final int lastDot = name.lastIndexOf('.');
					if(lastDot > 0)
					{
						name = name.substring(0,lastDot);
					}
					else
					{
						break;
					}
				}
			}

			ResourceBundle localizedProjectBundle = null;
			if(this.localizedProjectBundles.containsKey(locale))
			{
				localizedProjectBundle = this.localizedProjectBundles.get(locale);
			}
			else
			{
				localizedProjectBundle = loadProjectResourceBundle(locale,requestor);
				this.localizedProjectBundles.put(locale,localizedProjectBundle);
			}

			if(localizedProjectBundle != null)
			{
				try
				{
					return localizedProjectBundle.getString(key);
				}
				catch(final MissingResourceException e)
				{
				}
			}

			if(this.defaultProjectBundle != null)
			{
				try
				{
					return this.defaultProjectBundle.getString(key);
				}
				catch(final MissingResourceException e)
				{
				}
			}

			final String className = clazz != null ? clazz.getName() : getClass().getName();
			throw new MissingResourceException("No resource found for key '" + key
					+ "', requestor = " + className + ", locale = " + locale.getLanguage(),
					className,key);
		}


		protected ResourceBundle loadProjectResourceBundle(final Locale locale,
				final Object requestor)
		{
			try
			{
				return ResourceBundle.getBundle("project");
			}
			catch(final MissingResourceException mre)
			{
				final String localeSuffix = locale != null ? "_" + locale.getLanguage() : "";

				try (InputStream in = getResource(
						getProjectBundlePath() + localeSuffix + ".properties",requestor))
				{
					if(in != null)
					{
						return new PropertyResourceBundle(in);
					}
				}
				catch(final IOException e)
				{
				}

				return null;
			}
		}


		protected String getProjectBundlePath()
		{
			return "WebContent/WEB-INF/resources/project";
		}


		protected InputStream getResource(final String path, final Object requestor)
				throws IOException
		{
			try
			{
				return new ApplicationResource(requestor.getClass(),path).getStream().getStream();
			}
			catch(final Exception e)
			{
				// not found
				return null;
			}
		}
	}
}
