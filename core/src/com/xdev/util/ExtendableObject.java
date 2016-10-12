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


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * An extendable object provides a registry which external users can use to
 * register extensions.
 *
 *
 * @author XDEV Software
 * @since 1.1
 */
public interface ExtendableObject
{
	/**
	 * Adds an extension to this component. It can be retrieved later via the
	 * type.
	 *
	 * @param type
	 *            the type to register the extension for
	 * @param extension
	 *            the extension
	 * @return the previously added extension for this type, or
	 *         <code>null</code>
	 * @see #getExtension(Class)
	 */
	public <E> E addExtension(Class<? super E> type, E extension);


	/**
	 * Gets the registered extension for the specified type.
	 *
	 * @param type
	 *            the type the extension was registered with
	 * @return the extension or <code>null</code> if none was added before for
	 *         this type
	 * @see #addExtension(Class, Object)
	 */
	public <E> E getExtension(Class<E> type);



	/**
	 * Helper class for implementors of {@link ExtendableObject}.
	 *
	 * <pre>
	 * public class MyObject extends ... implements ExtendableObject
	 * {
	 * 	private Extensions extension = new Extensions();
	 *
	 * 	&#64;Override
	 *	public <E> E addExtension(Class&lt;? super E&gt; type, E extension)
	 *	{
	 *		return this.extension.add(type,extension);
	 *	}
	 *
	 *	&#64;Override
	 *	public <E> E getExtension(Class&lt;E&gt; type)
	 *	{
	 *		return this.extension.get(type);
	 *	}
	 * }
	 * </pre>
	 *
	 * @author XDEV Software
	 *
	 */
	public static class Extensions implements Serializable
	{
		private transient Map<Class<?>, Object> registry;


		@SuppressWarnings("unchecked")
		public <E> E add(final Class<? super E> type, final E extension)
		{
			if(this.registry == null)
			{
				this.registry = new HashMap<>();
			}
			return (E)this.registry.put(type,extension);
		}


		@SuppressWarnings("unchecked")
		public <E> E get(final Class<E> type)
		{
			if(this.registry == null)
			{
				return null;
			}
			return (E)this.registry.get(type);
		}
	}
}
