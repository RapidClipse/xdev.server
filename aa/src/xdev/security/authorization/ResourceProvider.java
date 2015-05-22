/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;

import net.jadoth.collections.types.XGettingEnum;


/**
 * Function type that provides {@link Resource} instances.
 * For details, see {@link #provideResource(Resource, String, XGettingEnum)}.
 *
 * @author XDEV Software (TM)
 */
@FunctionalInterface
public interface ResourceProvider
{
	/**
	 * Provides a suitable {@link Resource} instance based on the passed resource name, a potentially already
	 * existing instance and the collection of names of children (possibly empty).
	 * Providing means either validating an already existing instance or creating a fitting new instance.
	 *
	 * @param  existingInstance the potentially already existing {@link Resource} instance for the passed name.
	 * @param  factor the factor of the access to the passed {@link Resource} instance.
	 *
	 * @return a new {@link Resource} instance that satisfies the specified values.
	 */
	public Resource provideResource(Resource existingInstance, String name, XGettingEnum<String> children);
}
