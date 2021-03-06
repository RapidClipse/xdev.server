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

package com.xdev.ui.masterdetail;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.util.DTOUtils;
import com.xdev.util.JPAMetaDataUtils;


/**
 * Utility class to establish master detail connections between
 * {@link BeanComponent}s and/or {@link BeanFieldGroup}s.
 *
 * @author XDEV Software
 * @since 3.0
 */
public final class MasterDetail
{
	/**
	 * Connects two {@link BeanComponent}s. The relation is resolved
	 * automatically.
	 *
	 * @see FilterMasterDetailConnection
	 * @see LazyMasterDetailConnection
	 *
	 * @param <M>
	 *            the master type
	 * @param <D>
	 *            the detail type
	 * @param master
	 *            the master component
	 * @param detail
	 *            the detail component
	 * @return the master detail handle
	 * @throws IllegalStateException
	 *             if no distinct relation can be resolved
	 */
	@SuppressWarnings("unchecked")
	public static <M, D> MasterDetailConnection connect(final BeanComponent<M> master,
			final BeanComponent<D> detail) throws IllegalStateException
	{
		final Class<? super M> masterClass = master.getBeanContainerDataSource().getBeanType();
		final Class<? super D> detailClass = detail.getBeanContainerDataSource().getBeanType();
		try
		{
			if(detail.isAutoQueryData())
			{
				final String detailProperty = getDetailAttributeName(masterClass,detailClass);
				return connect(master,detail,detailProperty);
			}
			else
			{
				final String masterProperty = getMasterAttributeName(masterClass,detailClass);
				return connect(master,detail,masterValue -> {
					final Object value = DTOUtils.resolveAttributeValue(masterValue,masterProperty);
					if(value == null)
					{
						return Collections.emptyList();
					}
					if(value instanceof Collection)
					{
						return (Collection<D>)value;
					}
					final List<D> list = new ArrayList<>(1);
					list.add((D)value);
					return list;
				});
			}
		}
		catch(final IllegalStateException e)
		{
			throw new RuntimeException("Unable to auto-resolve master detail relation. "
					+ "Please use another MasterDetail#connect method and "
					+ "supply more detailed information for the relation.",e);
		}
	}


	/**
	 * Creates a lazy {@link MasterDetailConnection} between two
	 * {@link BeanComponent}s.
	 *
	 * @param <M>
	 *            the master type
	 * @param <D>
	 *            the detail type
	 * @param master
	 *            the master component
	 * @param detail
	 *            the detail component
	 * @param masterToDetail
	 *            the detail data provider
	 * @return the master detail handle
	 */
	public static <M, D> MasterDetailConnection connect(final BeanComponent<M> master,
			final BeanComponent<D> detail,
			final Function<M, Collection<? extends D>> masterToDetail)
	{
		return new LazyMasterDetailConnection<>(master,detail,masterToDetail);
	}


	/**
	 * Creates a filtered {@link MasterDetailConnection} between two
	 * {@link BeanComponent}s.
	 *
	 * @param <M>
	 *            the master type
	 * @param <D>
	 *            the detail type
	 * @param master
	 *            the master component
	 * @param detail
	 *            the detail component
	 * @param detailPropertyId
	 *            the detail property id for the filter
	 * @return the master detail handle
	 *
	 * @see #connect(BeanComponent, BeanComponent, Function, Object)
	 */
	public static <M, D> MasterDetailConnection connect(final BeanComponent<M> master,
			final BeanComponent<D> detail, final Object detailPropertyId)
	{
		return new FilterMasterDetailConnection<>(master,detail,value -> value,detailPropertyId);
	}


	/**
	 * Creates a filtered {@link MasterDetailConnection} between two
	 * {@link BeanComponent}s.
	 *
	 * @param <M>
	 *            the master type
	 * @param <D>
	 *            the detail type
	 * @param master
	 *            the master component
	 * @param detail
	 *            the detail component
	 * @param masterToFilterValue
	 *            the value for the filter
	 * @param detailPropertyId
	 *            the detail property id for the filter
	 * @return the master detail handle
	 */
	public static <M, D> MasterDetailConnection connect(final BeanComponent<M> master,
			final BeanComponent<D> detail, final Function<M, Object> masterToFilterValue,
			final Object detailPropertyId)
	{
		return new FilterMasterDetailConnection<>(master,detail,masterToFilterValue,
				detailPropertyId);
	}


	/**
	 * Connects a {@link BeanComponent} with a {@link BeanFieldGroup}.
	 *
	 * @see FieldGroupMasterDetailConnection
	 *
	 * @param <T>
	 *            common data type of the two components
	 *
	 * @param master
	 *            the master component
	 * @param detail
	 *            the detail component
	 * @return the master detail connection handle
	 */
	public static <T> MasterDetailConnection connect(final BeanComponent<T> master,
			final BeanFieldGroup<T> detail)
	{
		return new FieldGroupMasterDetailConnection<>(master,detail);
	}


	/**
	 * Auto detail attribute resolver from old master detail implementation,
	 * which is used if no detailed information for the relation is provided.
	 * But this one throws an {@link IllegalStateException} if no distinct
	 * relation can be resolved.
	 */
	private static String getDetailAttributeName(final Class<?> masterEntity,
			final Class<?> detailEntity) throws IllegalStateException
	{
		final ManagedType<?> managedType = JPAMetaDataUtils.getManagedType(detailEntity);

		final List<String> matchingAttributeNames = managedType.getAttributes().stream()
				.filter(SingularAttribute.class::isInstance).map(SingularAttribute.class::cast)
				.filter(pa -> pa.getBindableJavaType().equals(masterEntity)
						&& (pa.getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE
								|| pa.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE))
				.map(Attribute::getName).collect(Collectors.toList());

		if(matchingAttributeNames.isEmpty())
		{
			throw new IllegalStateException("No matching reference attribute for relation: "
					+ detailEntity.getCanonicalName() + " -> " + masterEntity.getCanonicalName());
		}

		if(matchingAttributeNames.size() > 1)
		{
			throw new IllegalStateException("Multiple matching reference attributes for relation: "
					+ detailEntity.getCanonicalName() + " -> " + masterEntity.getCanonicalName()
					+ " [" + matchingAttributeNames.stream().collect(Collectors.joining(", "))
					+ "]");
		}

		return matchingAttributeNames.get(0);
	}


	/**
	 * Auto master attribute resolver from old master detail implementation,
	 * which is used if no detailed information for the relation is provided.
	 * But this one throws an {@link IllegalStateException} if no distinct
	 * relation can be resolved.
	 */
	private static String getMasterAttributeName(final Class<?> masterEntity,
			final Class<?> detailEntity) throws IllegalStateException
	{
		final ManagedType<?> managedType = JPAMetaDataUtils.getManagedType(masterEntity);

		final List<String> matchingAttributeNames = managedType.getAttributes().stream()
				.filter(PluralAttribute.class::isInstance).map(PluralAttribute.class::cast)
				.filter(pa -> pa.getElementType().getJavaType().equals(detailEntity)
						&& pa.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_MANY)
				.map(Attribute::getName).collect(Collectors.toList());

		if(matchingAttributeNames.isEmpty())
		{
			throw new IllegalStateException("No matching reference attribute for relation: "
					+ masterEntity.getCanonicalName() + " -> " + detailEntity.getCanonicalName());
		}

		if(matchingAttributeNames.size() > 1)
		{
			throw new IllegalStateException("Multiple matching reference attributes for relation: "
					+ detailEntity.getCanonicalName() + " -> " + detailEntity.getCanonicalName()
					+ " [" + matchingAttributeNames.stream().collect(Collectors.joining(", "))
					+ "]");
		}

		return matchingAttributeNames.get(0);
	}


	private MasterDetail()
	{
	}
}
