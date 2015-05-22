/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;

import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingEnum;


/**
 * Function type for updating a passed {@link Role} instance for a given role name and collections of parent roles
 * and explicit permissions.
 * For more details, see {@link #updateRole(Role, String, XGettingEnum, XGettingEnum)}.
 *
 * @author XDEV Software (TM)
 */
@FunctionalInterface
public interface RoleUpdater
{
	/**
	 * Updates the passed {@link Role} instance for the given role name and collections of parent roles
	 * and explicit permissions, where updating can mean anything from just validating to actually changing the passed
	 * subject's state with the passed content.
	 *
	 * @param role the {@link Role} instance to be updated.
	 * @param name the identifying name of the passed {@link Role} instance.
	 * @param parentRoles the parent roles that the {@link Role} instance must reference to be valid.
	 * @param permissions the explicit permissions that the {@link Role} instance must reference to be valid.
	 */
	public void updateRole(Role role, String name, XGettingEnum<Role> parentRoles, XGettingEnum<Permission> permissions);



	/**
	 * Updating preparation callback hook that gets called once on the beginning of very updating process, before any
	 * authorization instances are updated.
	 * The default implementation of this method is empty.
	 * An example usage for this method is to create and store rollback information on the passed roles.
	 *
	 * @param existingRoles the roles already existing before the updating process.
	 */
	public default void prepareRoleUpdate(final XGettingCollection<Role> existingRoles)
	{
		// no-op in default implementation
	}

	/**
	 * Update committing callback hook that gets called once at the end of very successful updating process, after all
	 * authorization instances are updated.
	 * The default implementation of this method is empty.
	 * An example usage for this method is to relay committing of changes to some other data structure or application
	 * part.
	 *
	 * @param updatedRoles the updated and potentially newly created roles.
	 */
	public default void commitRoleUpdate(final XGettingCollection<Role> updatedRoles)
	{
		// no-op in default implementation
	}

	/**
	 * Updating exception handling callback hook that gets called once if any {@link Exception} is encountered during
	 * the updating process.
	 * The default implementation of this method is empty.
	 * An example usage for this method is to rollback (revert) all mutated {@link Role} instances that have
	 * already existed before the updating process.
	 *
	 * @param updatedRoles the updated and potentially newly created, empty or inconsistent roles.
	 */
	public default void rollbackRoleUpdate(final XGettingCollection<Role> updatedRoles, final Exception cause)
	{
		// no-op in default implementation
	}

	/**
	 * Updating preparation callback hook that gets called once at the end of the updating process in any case
	 * (both success and encountered exception).
	 * The default implementation of this method is empty.
	 * An example usage for this method is to clear any internally stored meta data (e.g. rollback information).
	 */
	public default void cleanupRoleUpdate()
	{
		// no-op in default implementation
	}

}
