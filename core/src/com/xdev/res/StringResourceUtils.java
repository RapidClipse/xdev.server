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


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.function.Function;

import org.apache.log4j.Logger;


/**
 * <p>
 * The <code>StringResourceUtils</code> class provides utility methods for
 * String resource handling.
 * </p>
 *
 * @author XDEV Software
 */
public final class StringResourceUtils
{
	private StringResourceUtils()
	{
	}
	
	private static StringResourceProvider stringResourceProvider;
	
	
	/**
	 * Returns the {@link StringResourceProvider}.
	 *
	 * @return the {@link StringResourceProvider}.
	 */
	public static StringResourceProvider getStringResourceProvider()
	{
		if(stringResourceProvider == null)
		{
			stringResourceProvider = new StringResourceProvider.Implementation();
		}
		
		return stringResourceProvider;
	}
	
	
	/**
	 * @param stringResourceProvider
	 *            the stringResourceProvider to set
	 */
	public static void setStringResourceProvider(
			final StringResourceProvider stringResourceProvider)
	{
		StringResourceUtils.stringResourceProvider = stringResourceProvider;
	}
	
	
	/**
	 * Returns the localized String for the given <code>key</code>.
	 *
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String
	 * </p>
	 *
	 * @param key
	 *            to get localized String for.
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * @see Locale
	 */
	public static String getResourceString(final String key) throws MissingResourceException
	{
		return getResourceString(key,null,null);
	}
	
	
	/**
	 * Returns the localized String for the given <code>key</code>.
	 *
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String
	 * </p>
	 *
	 * @param key
	 *            to get localized String for.
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * @see Locale
	 */
	public static String getResourceString(final String key, final Object requestor)
			throws MissingResourceException
	{
		return getResourceString(key,null,requestor);
	}
	
	
	/**
	 * Returns the localized String for the given <code>key</code> and
	 * <code>locale</code>.
	 *
	 *
	 * @param key
	 *            to get localized String for.
	 * @param locale
	 *            to lookup the {@link String} for.
	 *
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 *
	 * @see Locale
	 */
	public static String getResourceString(final String key, final Locale locale)
			throws MissingResourceException
	{
		return getResourceString(key,locale,null);
	}
	
	
	/**
	 * Returns the localized String for the given <code>key</code> and
	 * <code>locale</code>.
	 *
	 *
	 * @param key
	 *            to get localized String for.
	 * @param locale
	 *            to lookup the {@link String} for.
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 *
	 * @see Locale
	 */
	public static String getResourceString(final String key, final Locale locale,
			final Object requestor) throws MissingResourceException
	{
		return getStringResourceProvider().lookupResourceString(key,locale,requestor);
	}
	
	
	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code>. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 *
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String.
	 * </p>
	 * <p>
	 * <strong>Hint: </strong> The {@link MissingResourceException} is
	 * suppressed by the method. The stack trace of the
	 * {@link MissingResourceException} will be logged.
	 * </p>
	 *
	 * @param str
	 *            to localize
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>str</code>.
	 *
	 * @see Locale
	 */
	public static String optLocalizeString(final String str, final Object requestor)
	{
		try
		{
			return localizeString(str,requestor);
		}
		catch(final MissingResourceException e)
		{
			Logger.getLogger(StringResourceUtils.class).error(e);
			return str;
		}
	}
	
	
	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code> as char if the result is a 1-length String, '\0'
	 * otherwise. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 *
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String.
	 * </p>
	 * <p>
	 * <strong>Hint: </strong> The {@link MissingResourceException} is
	 * suppressed by the method. The stack trace of the
	 * {@link MissingResourceException} will be logged.
	 * </p>
	 *
	 * @param str
	 *            to localize
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized char for the given <code>str</code> or '\0'.
	 *
	 * @see Locale
	 */
	public static char optLocalizeChar(String str, final Object requestor)
	{
		int length = str.length();
		switch(length)
		{
			case 0:
				return '\0';
			case 1:
				return str.charAt(0);
			default:
				str = optLocalizeString(str,requestor);
				length = str.length();
				return length == 1 ? str.charAt(0) : '\0';
		}
	}
	
	
	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code>. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 *
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String.
	 * </p>
	 *
	 *
	 *
	 * @param str
	 *            to localize
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 *
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 *
	 * @see Locale
	 */
	public static String localizeString(final String str, final Object requestor)
			throws MissingResourceException
	{
		return localizeString(str,null,requestor);
	}
	
	
	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code>. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 *
	 *
	 * @param str
	 *            to localize
	 *
	 * @param locale
	 *            to lookup the {@link String} for.
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 *
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 *
	 * @see Locale
	 */
	public static String localizeString(final String str, final Locale locale,
			final Object requestor) throws MissingResourceException
	{
		return format(str,key -> getResourceString(key,locale,requestor));
	}
	
	
	/**
	 * Formats the {@link String} <code>str</code> by replacing the variables
	 * with values provided by <code>parameterProvider</code>.
	 * <p>
	 * Variables have the format <code>{$variableName}</code>.
	 * <p>
	 *
	 * <pre>
	 * Given: parameter provider (name -&gt; Peter, color -&gt; blue)
	 * Result of format("{$name} loves the color {$color}.",provider) is "Peter loves the color blue".
	 * </pre>
	 *
	 * @param str
	 *            the {@link String} to format
	 * @param parameterProvider
	 * @return a formatted {@link String}
	 */
	public static String format(String str, final Function<String, String> parameterProvider)
	{
		final boolean isRichText = str.startsWith("{\\rtf");
		
		int start;
		int searchStart = 0;
		while((start = str.indexOf("{$",searchStart)) >= 0)
		{
			final int end = str.indexOf("}",start + 2);
			if(end > start)
			{
				String key = str.substring(start + 2,end);
				if(isRichText)
				{
					// remove optional rich text escapes
					
					if(start > 0 && str.charAt(start - 1) == '\\')
					{
						start--;
					}
					
					final int len = key.length();
					if(len > 0 && key.charAt(len - 1) == '\\')
					{
						key = key.substring(0,len - 1);
					}
				}
				
				final String value = parameterProvider.apply(key);
				
				final StringBuilder sb = new StringBuilder();
				sb.append(str.substring(0,start));
				sb.append(value);
				sb.append(str.substring(end + 1));
				str = sb.toString();
				
				searchStart = start + value.length();
			}
			else
			{
				break;
			}
		}
		
		return str;
	}
}
