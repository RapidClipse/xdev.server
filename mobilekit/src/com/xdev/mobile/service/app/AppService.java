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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.mobile.service.app;


import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;

import elemental.json.JsonArray;


/**
 * Service which provides hooks for app lifecycle and device button events.
 *
 * @author XDEV Software
 *
 */
@MobileService(plugins = {@Plugin(name = "cordova-plugin-exitapp", spec = "1.0.0"),
		@Plugin(name = "cordova-plugin-cache", spec = "1.0.5")})
@JavaScript("app.js")
public class AppService extends AbstractMobileService
{
	public static AppService getInstance()
	{
		return getMobileService(AppService.class);
	}

	private final List<AppEventHandler>	pauseHandlers				= new ArrayList<>();
	private final List<AppEventHandler>	resumeHandlers				= new ArrayList<>();
	private final List<AppEventHandler>	backButtonHandlers			= new ArrayList<>();
	private final List<AppEventHandler>	menuButtonHandlers			= new ArrayList<>();
	private final List<AppEventHandler>	searchButtonHandlers		= new ArrayList<>();
	private final List<AppEventHandler>	volumeDownButtonHandlers	= new ArrayList<>();
	private final List<AppEventHandler>	volumeUpButtonHandlers		= new ArrayList<>();


	public AppService(final AbstractClientConnector target,
			final MobileServiceConfiguration configuration)
	{
		super(target,configuration);

		addFunction("app_onPause",this::app_onPause);
		addFunction("app_onResume",this::app_onResume);
		addFunction("app_onBackButton",this::app_onBackButton);
		addFunction("app_onMenuButton",this::app_onMenuButton);
		addFunction("app_onSearchButton",this::app_onSearchButton);
		addFunction("app_onVolumeDownButton",this::app_onVolumeDownButton);
		addFunction("app_onVolumeUpButton",this::app_onVolumeUpButton);
	}


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
	public void closeApp()
	{
		Page.getCurrent().getJavaScript().execute("app_closeApp()");
	}
	
	
	/**
	 * Clears the local cache of the application.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * </ul>
	 *
	 */
	public void clearCache()
	{
		Page.getCurrent().getJavaScript().execute("app_clearCache()");
	}


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
	public void addPauseHandler(final AppEventHandler handler)
	{
		addEventHandler(handler,this.pauseHandlers,"app_addPauseHandler()");
	}


	/**
	 * Removes the previously registered pause handler.
	 *
	 * @param handler
	 * @see #addPauseHandler(AppEventHandler)
	 */
	public void removePauseHandler(final AppEventHandler handler)
	{
		removeEventHandler(handler,this.pauseHandlers,"app_removePauseHandler()");
	}


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
	public void addResumeHandler(final AppEventHandler handler)
	{
		addEventHandler(handler,this.resumeHandlers,"app_addResumeHandler()");
	}


	/**
	 * Removes the previously registered resume handler.
	 *
	 * @param handler
	 * @see #addResumeHandler(AppEventHandler)
	 */
	public void removeResumeHandler(final AppEventHandler handler)
	{
		removeEventHandler(handler,this.resumeHandlers,"app_removeResumeHandler()");
	}


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
	public void addBackButtonHandler(final AppEventHandler handler)
	{
		addEventHandler(handler,this.backButtonHandlers,"app_addBackButtonHandler()");
	}


	/**
	 * Removes the previously registered backbutton handler.
	 *
	 * @param handler
	 * @see #addBackButtonHandler(AppEventHandler)
	 */
	public void removeBackButtonHandler(final AppEventHandler handler)
	{
		removeEventHandler(handler,this.backButtonHandlers,"app_removeBackButtonHandler()");
	}


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
	public void addMenuButtonHandler(final AppEventHandler handler)
	{
		addEventHandler(handler,this.menuButtonHandlers,"app_addMenuButtonHandler()");
	}


	/**
	 * Removes the previously registered menubutton handler.
	 *
	 * @param handler
	 * @see #addMenuButtonHandler(AppEventHandler)
	 */
	public void removeMenuButtonHandler(final AppEventHandler handler)
	{
		removeEventHandler(handler,this.menuButtonHandlers,"app_removeMenuButtonHandler()");
	}


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
	public void addSearchButtonHandler(final AppEventHandler handler)
	{
		addEventHandler(handler,this.searchButtonHandlers,"app_addSearchButtonHandler()");
	}


	/**
	 * Removes the previously registered searchbutton handler.
	 *
	 * @param handler
	 * @see #addSearchButtonHandler(AppEventHandler)
	 */
	public void removeSearchButtonHandler(final AppEventHandler handler)
	{
		removeEventHandler(handler,this.searchButtonHandlers,"app_removeSearchButtonHandler()");
	}


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
	public void addVolumeDownButtonHandler(final AppEventHandler handler)
	{
		addEventHandler(handler,this.volumeDownButtonHandlers,"app_addVolumeDownButtonHandler()");
	}


	/**
	 * Removes the previously registered volumedownbutton handler.
	 *
	 * @param handler
	 * @see #addVolumeDownButtonHandler(AppEventHandler)
	 */
	public void removeVolumeDownButtonHandler(final AppEventHandler handler)
	{
		removeEventHandler(handler,this.volumeDownButtonHandlers,
				"app_removeVolumeDownButtonHandler()");
	}


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
	public void addVolumeUpButtonHandler(final AppEventHandler handler)
	{
		addEventHandler(handler,this.volumeUpButtonHandlers,"app_addVolumeUpButtonHandler()");
	}


	/**
	 * Removes the previously registered volumeupbutton handler.
	 *
	 * @param handler
	 * @see #addVolumeUpButtonHandler(AppEventHandler)
	 */
	public void removeVolumeUpButtonHandler(final AppEventHandler handler)
	{
		removeEventHandler(handler,this.volumeUpButtonHandlers,"app_removeVolumeUpButtonHandler()");
	}


	private void addEventHandler(final AppEventHandler handler, final List<AppEventHandler> list,
			final String javaScript)
	{
		if(list.isEmpty())
		{
			Page.getCurrent().getJavaScript().execute(javaScript);
		}

		list.add(handler);
	}


	private void removeEventHandler(final AppEventHandler handler, final List<AppEventHandler> list,
			final String javaScript)
	{
		list.remove(handler);

		if(list.isEmpty())
		{
			Page.getCurrent().getJavaScript().execute(javaScript);
		}
	}


	private void app_onPause(final JsonArray arguments)
	{
		handleEvent(this.pauseHandlers);
	}


	private void app_onResume(final JsonArray arguments)
	{
		handleEvent(this.resumeHandlers);
	}


	private void app_onBackButton(final JsonArray arguments)
	{
		handleEvent(this.backButtonHandlers);
	}


	private void app_onMenuButton(final JsonArray arguments)
	{
		handleEvent(this.menuButtonHandlers);
	}


	private void app_onSearchButton(final JsonArray arguments)
	{
		handleEvent(this.searchButtonHandlers);
	}


	private void app_onVolumeDownButton(final JsonArray arguments)
	{
		handleEvent(this.volumeDownButtonHandlers);
	}


	private void app_onVolumeUpButton(final JsonArray arguments)
	{
		handleEvent(this.volumeUpButtonHandlers);
	}


	private void handleEvent(final List<AppEventHandler> handlers)
	{
		if(handlers.isEmpty())
		{
			return;
		}

		final AppEvent event = new AppEvent(this);

		for(int i = handlers.size(); --i >= 0;)
		{
			handlers.get(i).handleAppEvent(event);
			if(event.isConsumed())
			{
				break;
			}
		}
	}
}
