/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.security.authorization;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.LockedMap;
import net.jadoth.collections.types.XMap;

/**
 * Manager type that extends {@link SubjectRegistry} with functionality for mutating (managing) the registered entries.
 *
 * @author XDEV Software (TM)
 */
public interface SubjectManager extends SubjectRegistry
{
	/**
	 * Provides access to the registry entries to be used in a thread safe way according to the internal
	 * logic. Note that this method does not necessarily have to return the actual map instance but
	 * potentially only a relay e.g. a {@link LockedMap}.
	 * <p>
	 * Rationale for this approach:
	 * The obvious (and naive) concept would be to implement a registerSubject() method.
	 * But as a consequence, that would also require a removeSubject() method, then a subjectSize() and iterateSubjects()
	 * methods and then it becomes clear that managing subjects (in an efficient, comfortable way) requires a complete
	 * collection logic.
	 * So instead of reimplementing a complete collection in every managing type, the managing type might as well
	 * provide mutating access to its internal collection, however in a safe way (e.g. wrapped in locking logic
	 * and/or via a relay instance with hooked-in logic).
	 *
	 * @return an accessing instance to the internal registry.
	 */
	@Override
	public XMap<String, Subject> subjects();



	/**
	 * Creates a new {@link SubjectManager} instance with no entries and an exclusive locking instance.
	 *
	 * @return a new {@link SubjectManager} instance
	 */
	public static SubjectManager New()
	{
		return New(new Object());
	}

	/**
	 * Creates a new {@link SubjectManager} instance with no entries and the passed instance as a shared locking instance.
	 *
	 * @param registryLock the shared locking instance to be used.
	 * @return a new {@link SubjectManager} instance
	 */
	public static SubjectManager New(final Object registryLock)
	{
		return new Implementation(
			notNull(registryLock),
			EqHashTable.New()
		);
	}

	/**
	 * Creates a new {@link SubjectManager} instance with the passed map used as its internal entries datastructure
	 * and an exclusive locking instance.
	 *
	 * @param map the entries datastructure to be used internally.
	 * @return a new {@link SubjectManager} instance.
	 */
	public static SubjectManager New(final XMap<String, Subject> map)
	{
		return new Implementation(
			new Object(),
			notNull(map)
		);
	}

	/**
	 * Creates a new {@link SubjectManager} instance with the passed map used as its internal entries datastructure
	 * and the passed instance as a shared locking instance.
	 *
	 * @param  registryLock the shared locking instance to be used.
	 * @param  map the entries datastructure to be used internally.
	 * @return a new {@link SubjectManager} instance.
	 */
	public static SubjectManager New(final Object registryLock, final XMap<String, Subject> map)
	{
		return new Implementation(
			notNull(registryLock),
			notNull(map)
		);
	}



	/**
	 * A simple {@link SubjectManager} default implementation that uses a shared synchronization lock and a
	 * {@link LockedMap} implementation to allow locking-supervised access to the registry entries.
	 *
	 * @author XDEV Software (TM)
	 */
	public final class Implementation implements SubjectManager
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final XMap<String, Subject>      map            ;
		final Object                     registryLock   ;
		final LockedMap<String, Subject> lockedMap      ;
		final SubjectRegistry            subjectRegistry;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * Implementation detail constructor that might change in the future.
		 */
		Implementation(final Object registryLock, final XMap<String, Subject> map)
		{
			super();
			this.registryLock    = registryLock;
			this.map             = map;
			this.lockedMap       = LockedMap.New(map, registryLock);
			this.subjectRegistry = SubjectRegistry.New(map, registryLock);
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * {@inheritDoc}
		 */
		@Override
		public XMap<String, Subject> subjects()
		{
			return this.lockedMap;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Subject subject(final String subjectName)
		{
			return this.subjectRegistry.subject(subjectName);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object lockSubjectRegistry()
		{
			return this.registryLock;
		}

	}

}
