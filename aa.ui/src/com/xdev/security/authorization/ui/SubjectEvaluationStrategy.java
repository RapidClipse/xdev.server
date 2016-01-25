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


import com.xdev.ui.XdevComponent;


/**
 * Strategy used by {@link SubjectEvaluatingComponentExtension} to change a
 * component's state after a subject was evaluated.
 */
@FunctionalInterface
public interface SubjectEvaluationStrategy
{
	/**
	 * Default strategy which enabled or disabled a component depending on the
	 * evaluation's result.
	 */
	public final static SubjectEvaluationStrategy	ENABLED		= (component, hasPermissions) -> {
																	if(component
																			.isEnabled() != hasPermissions)
																	{
																		component.setEnabled(
																				hasPermissions);
																	}
																};

	/**
	 * Default strategy which shows or hides a component depending on the
	 * evaluation's result.
	 */
	public final static SubjectEvaluationStrategy	VISIBLE		= (component, hasPermissions) -> {
																	if(component
																			.isVisible() != hasPermissions)
																	{
																		component.setVisible(
																				hasPermissions);
																	}
																};

	/**
	 * Default strategy which sets read only mode for the component depending on
	 * the evaluation's result.
	 */
	public final static SubjectEvaluationStrategy	READ_ONLY	= (component, hasPermissions) -> {
																	if(component
																			.isReadOnly() == hasPermissions)
																	{
																		component.setReadOnly(
																				!hasPermissions);
																	}
																};


	/**
	 * Called after a subject was evaluated.
	 *
	 * @param component
	 *            the component to change
	 * @param hasPermissions
	 *            <code>true</code> if the subject had all necessary
	 *            permissions, <code>false</code> otherwise
	 */
	public void subjectEvaluated(XdevComponent component, boolean hasPermissions);
}
