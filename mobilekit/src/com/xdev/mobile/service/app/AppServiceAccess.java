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

package com.xdev.mobile.service.app;


/**
 * @author XDEV Software
 *
 */
public interface AppServiceAccess
{

	/**
	 * Closes the app.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>Windows Phone 8</li>
	 * </ul>
	 *
	 */
	public void closeApp();


	/**
	 * Adds an handler for the pause event.
	 * <p>
	 * The pause event fires when the native platform puts the application into
	 * the background, typically when the user switches to a different
	 * application.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows</li>
	 * </ul>
	 *
	 * @param handler
	 */
	public void addPauseHandler(AppEventHandler handler);


	/**
	 * Removes the previously registered pause handler.
	 *
	 * @param handler
	 * @see #addPauseHandler(AppEventHandler)
	 */
	public void removePauseHandler(AppEventHandler handler);


	/**
	 * Adds an handler for the resume event.
	 * <p>
	 * The resume event fires when the native platform pulls the application out
	 * from the background.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows</li>
	 * </ul>
	 *
	 * @param handler
	 */
	public void addResumeHandler(AppEventHandler handler);


	/**
	 * Removes the previously registered resume handler.
	 *
	 * @param handler
	 * @see #addResumeHandler(AppEventHandler)
	 */
	public void removeResumeHandler(AppEventHandler handler);


	/**
	 * Adds an handler for the backbutton event.
	 * <p>
	 * The event fires when the user presses the back button. To override the
	 * default back-button behavior, register an event listener for the
	 * backbutton event.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>Windows</li>
	 * </ul>
	 *
	 * @param handler
	 */
	public void addBackButtonHandler(AppEventHandler handler);


	/**
	 * Removes the previously registered backbutton handler.
	 *
	 * @param handler
	 * @see #addBackButtonHandler(AppEventHandler)
	 */
	public void removeBackButtonHandler(AppEventHandler handler);


	/**
	 * Adds an handler for the menubutton event.
	 * <p>
	 * The event fires when the user presses the menu button. Applying an event
	 * handler overrides the default menu button behavior.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 *
	 * @param handler
	 */
	public void addMenuButtonHandler(AppEventHandler handler);


	/**
	 * Removes the previously registered menubutton handler.
	 *
	 * @param handler
	 * @see #addMenuButtonHandler(AppEventHandler)
	 */
	public void removeMenuButtonHandler(AppEventHandler handler);


	/**
	 * Adds an handler for the searchbutton event.
	 * <p>
	 * The event fires when the user presses the search button on Android. If
	 * you need to override the default search button behavior on Android you
	 * can register an event listener for the 'searchbutton' event.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 *
	 * @param handler
	 */
	public void addSearchButtonHandler(AppEventHandler handler);


	/**
	 * Removes the previously registered searchbutton handler.
	 *
	 * @param handler
	 * @see #addSearchButtonHandler(AppEventHandler)
	 */
	public void removeSearchButtonHandler(AppEventHandler handler);


	/**
	 * Adds an handler for the volumedownbutton event.
	 * <p>
	 * The event fires when the user presses the volume down button. If you need
	 * to override the default volume down behavior you can register an event
	 * listener for the volumedownbutton event.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 *
	 * @param handler
	 */
	public void addVolumeDownButtonHandler(AppEventHandler handler);


	/**
	 * Removes the previously registered volumedownbutton handler.
	 *
	 * @param handler
	 * @see #addVolumeDownButtonHandler(AppEventHandler)
	 */
	public void removeVolumeDownButtonHandler(AppEventHandler handler);


	/**
	 * Adds an handler for the volumeupbutton event.
	 * <p>
	 * The event fires when the user presses the volume up button. If you need
	 * to override the default volume up behavior you can register an event
	 * listener for the volumeupbutton event.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 *
	 * @param handler
	 */
	public void addVolumeUpButtonHandler(AppEventHandler handler);


	/**
	 * Removes the previously registered volumeupbutton handler.
	 *
	 * @param handler
	 * @see #addVolumeUpButtonHandler(AppEventHandler)
	 */
	public void removeVolumeUpButtonHandler(AppEventHandler handler);
}
