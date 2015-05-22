/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;


/**
 * Function type that provides {@link AuthorizationConfiguration} instances.
 *
 * @author XDEV Software (TM)
 */
@FunctionalInterface
public interface AuthorizationConfigurationProvider
{
	/**
	 * Provides an authorization configuration in generic form from one or more sources.
	 *
	 * @return an authorization configuration instance.
	 */
	public AuthorizationConfiguration provideConfiguration();
}
