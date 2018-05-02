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

package com.xdev.util;


import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.google.common.collect.Lists;
import com.xdev.persistence.PersistenceUtils;


/**
 * @author XDEV Software
 *
 * @noapi <strong>For internal use only. This class is subject to change in the
 *        future.</strong>
 */
public final class JPAMetaDataUtils
{
	private JPAMetaDataUtils()
	{
	}


	public static <C> ManagedType<C> getManagedType(final Class<C> entityClass)
	{
		final EntityManager entityManager = PersistenceUtils.getEntityManager(entityClass);
		if(entityManager == null)
		{
			throw new IllegalArgumentException("Not an entity class: " + entityClass.getName());
		}

		return entityManager.getMetamodel().managedType(entityClass);
	}


	public static Attribute<?, ?> resolveAttribute(final Class<?> entityClass,
			final String propertyPath)
	{
		final List<Attribute<?, ?>> attributes = resolveAttributes(entityClass,propertyPath);
		return attributes == null || attributes.isEmpty() ? null
				: attributes.get(attributes.size() - 1);
	}


	/**
	 * @since 3.0
	 */
	public static List<Attribute<?, ?>> resolveAttributes(final Class<?> entityClass,
			final String propertyPath)
	{
		try
		{
			final List<Attribute<?, ?>> attributes = new ArrayList<>();

			Class<?> current = entityClass;

			for(final String pathItem : propertyPath.split("\\."))
			{
				final Attribute<?, ?> attribute = getManagedType(current).getAttribute(pathItem);
				attributes.add(attribute);
				if(attribute instanceof PluralAttribute)
				{
					current = ((PluralAttribute<?, ?, ?>)attribute).getElementType().getJavaType();
				}
				else
				{
					current = attribute.getJavaType();
				}
			}

			return attributes;
		}
		catch(final IllegalArgumentException e)
		{
			// attribute not found or not a managed type, XWS-870
			return null;
		}
	}


	public static boolean isManaged(final Class<?> clazz)
	{
		return clazz.getAnnotation(Entity.class) != null
				|| clazz.getAnnotation(Embeddable.class) != null
				|| clazz.getAnnotation(MappedSuperclass.class) != null;
	}


	/**
	 * @since 3.0
	 */
	public static void verifyPath(final Attribute<?, ?>... path)
	{
		verifyPath(Lists.newArrayList(path));
	}


	/**
	 * @since 3.0
	 */
	@SuppressWarnings("rawtypes")
	public static void verifyPath(final List<Attribute<?, ?>> path)
	{
		final List<Attribute<?, ?>> attributes = new ArrayList<>(path);
		Class<?> from = null;
		if(attributes.get(0).isCollection())
		{
			from = ((PluralAttribute)attributes.get(0)).getElementType().getJavaType();
		}
		else
		{
			from = attributes.get(0).getJavaType();
		}
		attributes.remove(0);
		for(final Attribute<?, ?> attribute : attributes)
		{
			if(!attribute.getDeclaringType().getJavaType().isAssignableFrom(from))
			{
				throw new IllegalStateException("Wrong path.");
			}
			from = attribute.getJavaType();
		}
	}


	/**
	 * @since 3.0
	 */
	public static String toPath(final List<Attribute<?, ?>> attributes)
	{
		return attributes.stream().map(Attribute::getName).collect(Collectors.joining("."));
	}


	/**
	 * @since 3.0
	 */
	public static Attribute<?, ?> getIdAttribute(final Class<?> entityClass)
	{
		final ManagedType<?> managedType = getManagedType(entityClass);
		if(managedType != null)
		{
			for(final Attribute<?, ?> attribute : managedType.getAttributes())
			{
				final Member javaMember = attribute.getJavaMember();
				if(javaMember instanceof AnnotatedElement
						&& ((AnnotatedElement)javaMember).getAnnotation(Id.class) != null)
				{
					return attribute;
				}
			}
		}

		return null;
	}


	/**
	 * @since 3.0
	 */
	public static Attribute<?, ?> getEmbeddedIdAttribute(final Class<?> entityClass)
	{
		final ManagedType<?> managedType = getManagedType(entityClass);
		if(managedType != null)
		{
			for(final Attribute<?, ?> attribute : managedType.getAttributes())
			{
				final Member javaMember = attribute.getJavaMember();
				if(javaMember instanceof AnnotatedElement
						&& ((AnnotatedElement)javaMember).getAnnotation(EmbeddedId.class) != null)
				{
					return attribute;
				}
			}
		}

		return null;
	}
	
	
	/**
	 * @since 3.0
	 */
	public static <T, A> SingularAttribute<? super T, A> singularAttribute(
			final ManagedType<? super T> managedType, final Attribute<? super T, A> attribute)
	{
		return managedType.getSingularAttribute(attribute.getName(),attribute.getJavaType());
	}


	/**
	 * @since 3.0
	 */
	public static <T> SingularAttribute<? super T, String> stringAttribute(
			final ManagedType<? super T> managedType, final Attribute<? super T, ?> attribute)
	{
		return managedType.getSingularAttribute(attribute.getName(),String.class);
	}
}
