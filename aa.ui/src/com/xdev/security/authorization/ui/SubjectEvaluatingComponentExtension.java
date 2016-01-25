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
 */

package com.xdev.security.authorization.ui;


import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

import java.util.Collection;

import com.xdev.security.authorization.Permission;
import com.xdev.security.authorization.Resource;
import com.xdev.security.authorization.Subject;
import com.xdev.ui.XdevComponent;


/**
 * A component extension which evaluates a passed subject and reacts
 * accordingly. Which of the subject's information is used and what exactly the
 * reaction will be is of course implementation- specific, however the basic
 * intention of this type is to evaluate the subject has sufficient permission
 * for {@link Resource}s associated with a component and react accordingly (e.g.
 * by making the component invisible).
 *
 * @author XDEV Software
 */
@FunctionalInterface
public interface SubjectEvaluatingComponentExtension
{
	/**
	 * Evaluates the passed {@link Subject} instance by checking if it has
	 * sufficient {@link Permission}s for the component's {@link Resource}s.
	 *
	 * @param component
	 * @param subject
	 *            the {@link Subject} instance to be evaluated.
	 */
	public void evaluateSubject(final XdevComponent component, final Subject subject);


	public static SubjectEvaluatingComponentExtension New(final SubjectEvaluationStrategy strategy,
			final Resource... resources)
	{
		requireNonNull(resources);
		return New(strategy,asList(resources));
	}


	public static SubjectEvaluatingComponentExtension New(final SubjectEvaluationStrategy strategy,
			final Collection<Resource> resources)
	{
		return new Implementation(strategy,resources);
	}



	public static class Implementation implements SubjectEvaluatingComponentExtension
	{
		protected final SubjectEvaluationStrategy	strategy;
		protected final Collection<Resource>		resources;


		protected Implementation(final SubjectEvaluationStrategy strategy,
				final Collection<Resource> resources)
		{
			requireNonNull(strategy);
			requireNonNull(resources);

			this.strategy = strategy;
			this.resources = resources;
		}


		@Override
		public void evaluateSubject(final XdevComponent component, final Subject subject)
		{
			final Resource denied = this.resources.stream()
					.filter(resource -> !subject.hasPermission(resource)).findAny().orElse(null);
			final boolean hasPermission = denied == null;
			this.strategy.subjectEvaluated(component,hasPermission);
		}
	}
}
