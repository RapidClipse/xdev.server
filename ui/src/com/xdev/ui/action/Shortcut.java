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

package com.xdev.ui.action;


import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;


/**
 * Holds information for a keyboard shortcut which is associated with an
 * {@link Action}.
 * <p>
 * It consists of a <code>keyCode</code> and <code>modifiers</code>, e.g.
 * {@link KeyCode#S} and {@link ModifierKey#CTRL}.
 *
 * @author XDEV Software
 *
 */
public class Shortcut implements KeyCode, ModifierKey
{
	private final int	keyCode;
	private final int[]	modifiers;


	/**
	 * Creates a shortcut object with the given key code and optional modifiers.
	 *
	 * @param keyCode
	 *            {@link KeyCode} that the shortcut reacts to
	 * @param modifiers
	 *            optional {@link ModifierKey}s
	 */
	public Shortcut(final int keyCode, final int... modifiers)
	{
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}


	/**
	 * Get the {@link KeyCode} that this shortcut reacts to (in combination with
	 * the {@link ModifierKey}s).
	 *
	 * @return keycode for this shortcut
	 */
	public int getKeyCode()
	{
		return this.keyCode;
	}


	/**
	 * Get the {@link ModifierKey}s required for the shortcut to react.
	 *
	 * @return modifier keys for this shortcut
	 */
	public int[] getModifiers()
	{
		return this.modifiers;
	}
}
