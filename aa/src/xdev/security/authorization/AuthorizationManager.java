/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;

import static net.jadoth.Jadoth.coalesce;
import static net.jadoth.Jadoth.notNull;

import java.io.File;

import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.HashEnum;
import net.jadoth.collections.X;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.collections.types.XGettingTable;
import net.jadoth.collections.types.XMap;
import net.jadoth.util.KeyValue;

/**
 * Composite type that combines {@link AuthorizationRegistry} with managing aspects of {@link PermissionManager},
 * {@link RoleManager} and {@link SubjectManager}.
 *
 * @author XDEV Software (TM)
 */
public interface AuthorizationManager extends AuthorizationRegistry, PermissionManager, RoleManager, SubjectManager
{
	/**
	 * @return the {@link AuthorizationRegistry} instance, possibly this instance itself.
	 */
	public AuthorizationRegistry authorizationRegistry();

	/**
	 * Causes the authorization information to be reloaded and all depending data structures to be rebuilt
	 * accordingly.
	 */
	public void reloadAuthorizations();

	/**
	 * Returns the {@link Resource} instance identified by the passed name.
	 *
	 * @param name the name identifying the desired resource.
	 * @return the {@link Resource} instance identified by the passed name.
	 */
	public Resource resource(String name);


	/**
	 * Creates a new {@link AuthorizationManager} instance configured by the passed part instances.
	 *
	 * @param configurationProvider the source for the {@link AuthorizationConfiguration} to be used.
	 * @param resourceProvider the provider of {@link Resource} instances.
	 * @param resourceUpdater  the updating logic for the {@link Resource} instances provided by the resource provider.
	 * @param permissionProvider the provider of {@link Permission} instances.
	 * @param roleProvider the provider of {@link Role} instances.
	 * @param roleUpdater the updating logic for the {@link Role} instances provided by the role provider.
	 * @param subjectUpdater the providing/updating logic for {@link SubjectManager} instances.
	 * @return a new {@link AuthorizationManager} instance.
	 */
	public static AuthorizationManager New(
		final AuthorizationConfigurationProvider configurationProvider,
		final ResourceProvider                   resourceProvider     ,
		final ResourceUpdater                    resourceUpdater      ,
		final PermissionProvider                 permissionProvider   ,
		final RoleProvider                       roleProvider         ,
		final RoleUpdater                        roleUpdater          ,
		final SubjectUpdater                     subjectUpdater
	)
	{
		return new Implementation(
			notNull(configurationProvider),
			notNull(resourceProvider)     ,
			notNull(resourceUpdater)      ,
			notNull(permissionProvider)   ,
			notNull(roleProvider)         ,
			notNull(roleUpdater)          ,
			notNull(subjectUpdater)
		);
	}

	/**
	 * Creates a new {@link AuthorizationManager} instance using the passed {@link AuthorizationConfigurationProvider}
	 * and default implementations for authorization entity providers and updaters.
	 * Also see {@link #New(AuthorizationConfigurationProvider, ResourceProvider, ResourceUpdater, PermissionProvider, RoleProvider, RoleUpdater, SubjectUpdater)}.
	 *
	 * @param configurationProvider the source for the {@link AuthorizationConfiguration} to be used.
	 * @return a new {@link AuthorizationManager} instance.
	 * @see #New(AuthorizationConfigurationProvider, ResourceProvider, ResourceUpdater, PermissionProvider, RoleProvider, RoleUpdater, SubjectUpdater)
	 */
	public static AuthorizationManager New(final AuthorizationConfigurationProvider configurationProvider)
	{
		return New(
			configurationProvider,
			Resource::provide,
			Resource::update,
			Permission::New,
			Role::provide,
			Role::update,
			Subject::update
		);
	}

	/**
	 * Creates a new {@link AuthorizationManager} instance from a {@link File} assumed to contain an XML configuration
	 * suitable for processing by a {@link XmlAuthorizationConfigurationProvider}.
	 *
	 * @param xmlFile the file containing the XML authorization configuration.
	 * @return a new {@link AuthorizationManager} instance.
	 */
	public static AuthorizationManager NewFromXmlFile(final File xmlFile)
	{
		return New(XmlAuthorizationConfigurationProvider.New(xmlFile));
	}

	/**
	 * @return a new {@link AuthorizationManager.Builder} instance.
	 */
	public static AuthorizationManager.Builder Builder()
	{
		return new AuthorizationManager.Builder.Implementation();
	}



	/**
	 * A builder pattern utility type that allows more a flexible and ergonomical assembly of
	 * {@link AuthorizationManager} instances than using a constructor method directly.
	 *
	 * @author XDEV Software (TM)
	 */
	public interface Builder
	{
		/**
		 * Sets the {@link AuthorizationConfigurationProvider} instance to be used in the assembly.
		 *
		 * @param configurationProvider
		 * @return this
		 */
		public Builder configurationProvider(AuthorizationConfigurationProvider configurationProvider);

		/**
		 * Sets the {@link ResourceProvider} instance to be used in the assembly.
		 *
		 * @param resourceProvider the {@link ResourceProvider} instance to be set.
		 * @return this
		 */
		public Builder resourceProvider(ResourceProvider resourceProvider);

		/**
		 * Sets the {@link ResourceUpdater} instance to be used in the assembly.
		 *
		 * @param resourceUpdater the {@link ResourceUpdater} instance to be set.
		 * @return this
		 */
		public Builder resourceUpdater(ResourceUpdater resourceUpdater);

		/**
		 * Sets the {@link PermissionProvider} instance to be used in the assembly.
		 *
		 * @param permissionProvider the {@link PermissionProvider} instance to be set.
		 * @return this
		 */
		public Builder permissionProvider(PermissionProvider permissionProvider);

		/**
		 * Sets the {@link RoleProvider} instance to be used in the assembly.
		 *
		 * @param roleProvider the {@link RoleProvider} instance to be set.
		 * @return this
		 */
		public Builder roleProvider(RoleProvider roleProvider);

		/**
		 * Sets the {@link RoleUpdater} instance to be used in the assembly.
		 *
		 * @param roleUpdater the {@link RoleUpdater} instance to be set.
		 * @return this
		 */
		public Builder roleUpdater(RoleUpdater roleUpdater);

		/**
		 * Sets the {@link SubjectUpdater} instance to be used in the assembly.
		 *
		 * @param subjectUpdater the {@link SubjectUpdater} instance to be set.
		 * @return this
		 */
		public Builder subjectUpdater(SubjectUpdater subjectUpdater);

		/**
		 * Creates a new {@link AuthorizationManager} instance from the previously set assembly part instances.
		 *
		 * @return a new {@link AuthorizationManager} instance
		 */
		public AuthorizationManager build();



		/**
		 * Simple {@link AuthorizationManager.Builder} implementation.
		 *
		 * @author XDEV Software (TM)
		 */
		public final class Implementation implements Builder
		{
			///////////////////////////////////////////////////////////////////////////
			// instance fields //
			////////////////////

			AuthorizationConfigurationProvider configurationProvider = null;
			ResourceProvider                   resourceProvider      = Resource::provide;
			ResourceUpdater                    resourceUpdater       = Resource::update ;
			PermissionProvider                 permissionProvider    = Permission::New  ;
			RoleProvider                       roleProvider          = Role::provide    ;
			RoleUpdater                        roleUpdater           = Role::update     ;
			SubjectUpdater                     subjectUpdater        = Subject::update  ;



			///////////////////////////////////////////////////////////////////////////
			// override methods //
			/////////////////////

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Builder.Implementation configurationProvider(final AuthorizationConfigurationProvider configurationProvider)
			{
				this.configurationProvider = configurationProvider;
				return this;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Builder.Implementation resourceProvider(final ResourceProvider resourceProvider)
			{
				this.resourceProvider = resourceProvider;
				return this;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Builder.Implementation resourceUpdater(final ResourceUpdater resourceUpdater)
			{
				this.resourceUpdater = resourceUpdater;
				return this;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Builder.Implementation permissionProvider(final PermissionProvider permissionProvider)
			{
				this.permissionProvider = permissionProvider;
				return this;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Builder.Implementation roleProvider(final RoleProvider roleProvider)
			{
				this.roleProvider = roleProvider;
				return this;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Builder.Implementation roleUpdater(final RoleUpdater roleUpdater)
			{
				this.roleUpdater = roleUpdater;
				return this;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Builder.Implementation subjectUpdater(final SubjectUpdater subjectUpdater)
			{
				this.subjectUpdater = subjectUpdater;
				return this;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public AuthorizationManager build()
			{
				return AuthorizationManager.New(
					notNull(this.configurationProvider),
					notNull(this.resourceProvider)     ,
					notNull(this.resourceUpdater)      ,
					notNull(this.permissionProvider)   ,
					notNull(this.roleProvider)         ,
					notNull(this.roleUpdater)          ,
					notNull(this.subjectUpdater)
				);
			}

		}

	}



	/**
	 * Simple {@link AuthorizationManager} default implementation that uses an internally created locking instance
	 * and connects delegate {@link PermissionManager}, {@link RoleManager} and {@link SubjectManager} instances to it.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class Implementation implements AuthorizationManager
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final AuthorizationConfigurationProvider configurationProvider;
		final ResourceProvider                   resourceProvider     ;
		final ResourceUpdater                    resourceUpdater      ;
		final PermissionProvider                 permissionProvider   ;
		final RoleProvider                       roleProvider         ;
		final RoleUpdater                        roleUpdater          ;
		final SubjectUpdater                     subjectUpdater       ;

		final EqHashTable<String, Resource>                           resourceTable   = EqHashTable.New();
		final EqHashTable<Resource, EqHashTable<Integer, Permission>> permissionTable = EqHashTable.New();
		final EqHashTable<String, Role>                               roleTable       = EqHashTable.New();
		final EqHashTable<String, Subject>                            subjectTable    = EqHashTable.New();

		final Object                sharedLock         = new Object();
		final PermissionManager     permissionManager;
		final RoleManager           roleManager      ;
		final SubjectManager        subjectManager   ;

		private boolean initialized = false;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(
			final AuthorizationConfigurationProvider configurationProvider,
			final ResourceProvider                   resourceProvider     ,
			final ResourceUpdater                    resourceUpdater      ,
			final PermissionProvider                 permissionProvider   ,
			final RoleProvider                       roleProvider         ,
			final RoleUpdater                        roleUpdater          ,
			final SubjectUpdater                     subjectUpdater
		)
		{
			super();
			this.configurationProvider = configurationProvider;
			this.resourceProvider      = resourceProvider     ;
			this.resourceUpdater       = resourceUpdater      ;
			this.permissionProvider    = permissionProvider   ;
			this.roleProvider          = roleProvider         ;
			this.roleUpdater           = roleUpdater          ;
			this.subjectUpdater        = subjectUpdater       ;
			this.permissionManager     = PermissionManager.New(this.sharedLock, permissionProvider, this.permissionTable);
			this.roleManager           = RoleManager      .New(this.sharedLock, this.roleTable      );
			this.subjectManager        = SubjectManager   .New(this.sharedLock, this.subjectTable);
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockPermissionRegistry()
		{
			this.ensureInitialized();
			return this.sharedLock;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockRoleRegistry()
		{
			this.ensureInitialized();
			return this.sharedLock;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Object lockSubjectRegistry()
		{
			this.ensureInitialized();
			return this.sharedLock;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Resource resource(final String name)
		{
			this.ensureInitialized();

			// must lock locally as there is no registry instance to handle it
			// (26.06.2014 TM)TODO: resource Registry?
			synchronized(this.sharedLock)
			{
				return this.resourceTable.get(name);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Permission permission(final Resource resource, final Integer factor)
		{
			this.ensureInitialized();
			return this.permissionManager.permission(resource, factor);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Role role(final String roleName)
		{
			this.ensureInitialized();
			return this.roleManager.role(roleName);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Subject subject(final String subjectName)
		{
			this.ensureInitialized();
			return this.subjectManager.subject(subjectName);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final Permission providePermission(final Resource resource, final Integer factor)
		{
			this.ensureInitialized();
			return this.permissionManager.providePermission(resource, factor);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XMap<String, Role> roles()
		{
			this.ensureInitialized();
			return this.roleManager.roles();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final XMap<String, Subject> subjects()
		{
			this.ensureInitialized();
			return this.subjectManager.subjects();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final AuthorizationRegistry authorizationRegistry()
		{
			this.ensureInitialized();

			// through the registry encapsulation this manager implementation itself acts as a registry.
			return this;
		}

		private void ensureInitialized()
		{
			synchronized(this.sharedLock)
			{
				if(this.initialized)
				{
					return;
				}
				this.build();
				this.initialized = true;
			}
		}

		private void build()
		{
			final EqHashTable<String, Resource>                           resources   = EqHashTable.New();
			final EqHashTable<Resource, EqHashTable<Integer, Permission>> permissions = EqHashTable.New();
			final EqHashTable<String, Role>                               roles       = EqHashTable.New();
			final EqHashTable<String, Subject>                            subjects    = EqHashTable.New();

			// prepare updaters (normaly no-op, but important hooking opportunity for custom logic)
			this.resourceUpdater.prepareResourceUpdate(this.resourceTable.values());
			this.roleUpdater.prepareRoleUpdate(this.roleTable.values());
			this.subjectUpdater.prepareSubjectUpdate(this.subjectTable.values());

			try
			{
				/* note that building mutates already existing instances.
				 * This can't be prevented if instance identity (outside references) is to be preserved.
				 */
				final AuthorizationConfiguration configuration = this.configurationProvider.provideConfiguration();

				// update existing or get new instances via the provided configuration
				this.buildResourceTable(resources, configuration)                        ;
				this.buildRoleTable    (roles    , configuration, resources, permissions);
				this.buildSubjectTable (subjects , configuration, roles)                 ;

				// signal updaters that the process has been completed successfully
				// (26.06.2014 TM)TODO: separate into dedicated trys with exception collecting?
				this.resourceUpdater.commitResourceUpdate(resources.values());
				this.roleUpdater.commitRoleUpdate(roles.values());
				this.subjectUpdater.commitSubjectUpdate(subjects.values());
			}
			catch(final Exception e)
			{
				// rollback the updating process on any exception
				this.resourceUpdater.rollbackResourceUpdate(resources.values(), e);
				this.roleUpdater.rollbackRoleUpdate(roles.values(), e);
				this.subjectUpdater.rollbackSubjectUpdate(subjects.values(), e);

				// pass exception along
				throw e;
			}
			finally
			{
				// clean up after logic is completed, one way or another
				this.resourceUpdater.cleanupResourceUpdate();
				this.roleUpdater.cleanupRoleUpdate();
				this.subjectUpdater.cleanupSubjectUpdate();
			}

			/* clear and update registry tables. Must be the same instances as they are referenced by / linked into
			 * registry instances. Note that these methods "should" be exception-free.
			 */
			this.resourceTable  .clear();
			this.permissionTable.clear();
			this.roleTable      .clear();
			this.subjectTable   .clear();

			this.resourceTable  .addAll(resources)  ;
			this.permissionTable.addAll(permissions);
			this.roleTable      .addAll(roles)      ;
			this.subjectTable   .addAll(subjects)   ;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void reloadAuthorizations()
		{
			synchronized(this.sharedLock)
			{
				this.initialized = false;
				this.ensureInitialized();
			}
		}

		private static final <E> XGettingEnum<E> resolve(
			final XGettingEnum<String>   names,
			final EqHashTable<String, E> mapping
		)
		{
			if(names == null)
			{
				return null;
			}

			final HashEnum<E> resolved = HashEnum.NewCustom(names.size());

			for(final String name : names)
			{
				final E element = mapping.get(name);
				if(element == null)
				{
					throw new RuntimeException("Missing element: "+name);
				}
				resolved.put(element);
			}

			return resolved;
		}

		private static final Permission lookupPermission(
			final EqHashTable<Resource, EqHashTable<Integer, Permission>> table   ,
			final Resource                                                resource,
			final Integer                                                 factor
		)
		{
			final EqHashTable<Integer, Permission> subTable = table.get(resource);
			return subTable == null ?null :subTable.get(factor);
		}

		private static final void putPermission(
			final EqHashTable<Resource, EqHashTable<Integer, Permission>> table     ,
			final Resource                                                resource  ,
			final Integer                                                 factor    ,
			final Permission                                              permission
		)
		{
			EqHashTable<Integer, Permission> subTable = table.get(resource);
			if(subTable == null)
			{
				table.put(resource, subTable = EqHashTable.New());
			}
			subTable.put(factor, permission);
		}

		private static final XGettingEnum<Permission> resolvePermissions(
			final EqHashTable<Resource, EqHashTable<Integer, Permission>> oldPermissionTable,
			final EqHashTable<Resource, EqHashTable<Integer, Permission>> newPermissionTable,
			final XGettingTable<String, Integer>                          definitions       ,
			final XGettingMap<String, Resource>                           resources         ,
			final PermissionProvider                                      permissionProvider
		)
		{
			if(definitions == null)
			{
				return null;
			}

			final HashEnum<Permission> permissions = HashEnum.New();

			for(final KeyValue<String, Integer> definition : definitions)
			{
				final Resource resource = resources.get(definition.key());
				if(resource == null)
				{
					throw new RuntimeException("Resource not found: "+definition.key()); // (18.06.2014 TM)TODO: proper exception
				}

				Permission permission = lookupPermission(newPermissionTable, resource, definition.value());
				if(permission == null)
				{
					permission = permissionProvider.providePermission(resource, definition.value());
				}
				if(permission == null)
				{
					throw new RuntimeException("Permission provider failure for "+definition.key()+" ("+definition.value()+")"); // (18.06.2014 TM)TODO: proper exception
				}
				putPermission(newPermissionTable, resource, definition.value(), permission);
				permissions.put(permission);
			}

			return permissions;
		}

		private void buildResourceTable(
			final EqHashTable<String, Resource> newResources ,
			final AuthorizationConfiguration                 configuration
		)
		{
			final ResourceProvider              resourceProvider = this.resourceProvider;
			final ResourceUpdater               resourceUpdater  = this.resourceUpdater;
			final EqHashTable<String, Resource> oldResources     = this.resourceTable  ;

			final XGettingTable<String, ? extends XGettingEnum<String>> resourcesDefinitions =
				configuration.resourceResources()
			;

			/* resolve instance names to actual instances by looking up already registered existing ones or
			 * getting new ones from the provider. String references are passed as a means for early validation.
			 */
			for(final KeyValue<String, ? extends XGettingEnum<String>> resDef : resourcesDefinitions)
			{
				final String   resourceName = resDef.key();
				final Resource resource     = resourceProvider.provideResource(
					oldResources.get(resourceName),
					resourceName,
					resDef.value()
				);

				// always put returned instance in case the provider discarded the old instance and created a new one.
				newResources.put(resourceName, resource);
			}

			/* Update the instances in a second pass after all instances have been resolved.
			 * Children collections have to be resolved locally for passing the actual collections to the updater.
			 */
			for(final KeyValue<String, Resource> resource : newResources)
			{
				final String name = resource.key();
				resourceUpdater.updateResource(
					resource.value(),
					name,
					resolve(resourcesDefinitions.get(name), newResources)
				);
			}
		}

		private void buildRoleTable(
			final EqHashTable<String, Role>                               newRoles          ,
			final AuthorizationConfiguration                                           config            ,
			final XGettingMap<String, Resource>                           resources         ,
			final EqHashTable<Resource, EqHashTable<Integer, Permission>> newPermissionTable
		)
		{
			final RoleProvider                                            roleProvider       = this.roleProvider      ;
			final PermissionProvider                                      permissionProvider = this.permissionProvider;
			final RoleUpdater                                             roleUpdater        = this.roleUpdater       ;
			final EqHashTable<String, Role>                               oldRoles           = this.roleTable         ;
			final EqHashTable<Resource, EqHashTable<Integer, Permission>> oldPermissionTable = this.permissionTable   ;

			final XGettingTable<String, ? extends XGettingEnum<String>>           roleRoles       = config.roleRoles()      ;
			final XGettingTable<String, ? extends XGettingTable<String, Integer>> rolePermissions = config.rolePermissions();

			/* resolve instance names to actual instances by looking up already registered existing ones or
			 * getting new ones from the provider. String references are passed as a means for early validation.
			 */
			for(final KeyValue<String, ? extends XGettingEnum<String>> roleRolesDef : roleRoles)
			{
				final String                         roleName     = roleRolesDef.key();
				final XGettingTable<String, Integer> permissions  = rolePermissions.get(roleName);
				final Role                           existingRole = oldRoles.get(roleName);
				final Role                           role         = roleProvider.provideRole(
					existingRole,
					roleName,
					roleRolesDef.value(),
					coalesce(permissions, X.<String,XGettingEnum<String>>emptyTable()).keys()
				);

				// always put returned instance in case the provider discarded the old instance and created a new one.
				newRoles.put(roleName, role);
			}
			// complementary pass to cover definitions that only have permissions, no parent roles
			for(final KeyValue<String, ? extends XGettingTable<String, Integer>> rolePermDef : rolePermissions)
			{
				final String roleName = rolePermDef.key();
				if(newRoles.get(roleName) != null)
				{
					// role already covered, continue loop
					continue;
				}

				final Role role = roleProvider.provideRole(
					oldRoles.get(roleName),
					roleName,
					X.empty(), // obviously empty, otherwise first loop would have covered it already
					rolePermDef.value().keys()
				);

				// always put returned instance in case the provider discarded the old instance and created a new one.
				newRoles.put(roleName, role);
			}

			/* Update the instances in a second pass after all instances have been resolved.
			 * Children collections have to be resolved locally for passing the actual collections to the updater.
			 */
			for(final KeyValue<String, Role> role : newRoles)
			{
				final String name = role.key();
				roleUpdater.updateRole(
					role.value(),
					name,
					resolve(roleRoles.get(name), newRoles),
					resolvePermissions(
						oldPermissionTable,
						newPermissionTable,
						rolePermissions.get(name),
						resources,
						permissionProvider
					)
				);
			}
		}

		private void buildSubjectTable(
			final EqHashTable<String, Subject> newSubjects,
			final AuthorizationConfiguration                configuration,
			final EqHashTable<String, Role>    roles
		)
		{
			final SubjectUpdater               subjectUpdater = this.subjectUpdater;
			final EqHashTable<String, Subject> oldSubjects    = this.subjectTable  ;

			final XGettingTable<String, ? extends XGettingEnum<String>> subjectDefinitions = configuration.subjectRoles();


			/* subject resolving and updating can be done in one pass as it has no type-recursion
			 * (subject knows other subjects) like resource or role.
			 */
			for(final KeyValue<String, ? extends XGettingEnum<String>> subjDef : subjectDefinitions)
			{
				final String  subjectName = subjDef.key();
				final Subject subject     = subjectUpdater.updateSubject(
					oldSubjects.get(subjectName),
					subjectName,
					resolve(subjDef.value(), roles)
				);
				if(subject == null){
					throw new RuntimeException("Subject providing failure for "+subjectName); // (18.06.2014 TM)TODO: proper exception
				}

				// always put returned instance in case the provider discarded the old instance and created a new one.
				newSubjects.put(subjectName, subject);
			}
		}

	}

}
