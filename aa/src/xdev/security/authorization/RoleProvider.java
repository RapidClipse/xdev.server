/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;

import net.jadoth.collections.types.XGettingEnum;

/**
 * Function type that provides {@link Role} instances.
 * For details, see {@link #provideRole(Role, String, XGettingEnum, XGettingEnum)}.
 *
 * @see RoleUpdater
 * @author XDEV Software (TM)
 */
@FunctionalInterface
public interface RoleProvider
{
	/**
	 * Provides a suitable {@link Role} instance for the passed role name, parent role and permission names.
	 * Providing means to either validate and return a fitting instance or create a new one.
	 * See {@link RoleUpdater} for setting the actual role and permission values.
	 *
	 * @param role a potentially already existing {@link Role} instance for the passed name or <tt>null</tt>.
	 * @param name the name identifiying the role.
	 * @param parentRoles the names of the role's parent roles for validation purposes.
	 * @param permissions the names of the role's explicit permissions for validation purposes.
	 * @return a {@link Role} instance suitable for the specified values, either already existing or newly created.
	 */
	public Role provideRole(Role existingRole, String name, XGettingEnum<String> parentRoles, XGettingEnum<String> permissions);
}
