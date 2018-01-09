/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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

package com.xdev.communication;


import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;


/**
 * Information class for user-agent information with operating system and
 * browser details.
 *
 * @author XDEV Software
 *
 * @see WebBrowser
 * @see UserAgent
 */
public class ClientInfo implements Serializable
{
	/**
	 * Gets the currently used session's client info.
	 *
	 * @return the current client info if available, otherwise <code>null</code>
	 */
	public static ClientInfo getCurrent()
	{
		final VaadinSession session = VaadinSession.getCurrent();
		return session != null ? session.getAttribute(ClientInfo.class) : null;
	}
	
	
	/**
	 * Gets the appropriate {@link ClientInfo} for the client request.
	 */
	public static ClientInfo get(final VaadinRequest request)
	{
		return new ClientInfo(request);
	}
	
	private final UserAgent		userAgent;
	private final WebBrowser	weBrowser;
								
								
	private ClientInfo(final VaadinRequest request)
	{
		this.userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
		
		this.weBrowser = new WebBrowser();
		this.weBrowser.updateRequestDetails(request);
	}
	
	
	/**
	 * Detects if the client is a standard desktop or laptop computer
	 *
	 * @see #isTablet()
	 * @see #isMobile()
	 */
	public boolean isComputer()
	{
		return this.userAgent.getOperatingSystem().getDeviceType() == DeviceType.COMPUTER;
	}
	
	
	/**
	 * Detects if the client is a small tablet type computer
	 *
	 * @see #isComputer()
	 * @see #isMobile()
	 */
	public boolean isTablet()
	{
		return this.userAgent.getOperatingSystem().getDeviceType() == DeviceType.TABLET;
	}
	
	
	/**
	 * Detects if the client is a mobile phone or similar small mobile device
	 *
	 * @see #isComputer()
	 * @see #isTablet()
	 */
	public boolean isMobile()
	{
		return this.userAgent.getOperatingSystem().getDeviceType() == DeviceType.MOBILE;
	}
	
	
	/**
	 * Detects if the client is run on Android.
	 */
	public boolean isAndroid()
	{
		return isOperatingSystem(OperatingSystem.ANDROID);
	}
	
	
	/**
	 * Detects if the client is run on iOS.
	 */
	public boolean isIOS()
	{
		return isOperatingSystem(OperatingSystem.IOS);
	}
	
	
	/**
	 * Detects if the client is run on MacOS-X.
	 */
	public boolean isMacOSX()
	{
		return isOperatingSystem(OperatingSystem.MAC_OS_X);
	}
	
	
	/**
	 * Detects if the client is run on Windows.
	 */
	public boolean isWindows()
	{
		return isOperatingSystem(OperatingSystem.WINDOWS);
	}
	
	
	/**
	 * Detects if the client is run on Linux.
	 */
	public boolean isLinux()
	{
		return isOperatingSystem(OperatingSystem.LINUX);
	}
	
	
	private boolean isOperatingSystem(final OperatingSystem test)
	{
		final OperatingSystem os = this.userAgent.getOperatingSystem();
		return os == test || os.getGroup() == test;
	}
	
	
	/**
	 * Detects if the client is using the Chrome browser.
	 */
	public boolean isChrome()
	{
		return isBrowser(Browser.CHROME);
	}
	
	
	/**
	 * Detects if the client is using the Firefox browser.
	 */
	public boolean isFirefox()
	{
		return isBrowser(Browser.FIREFOX);
	}
	
	
	/**
	 * Detects if the client is using the Internet Explorer browser.
	 */
	public boolean isInternetExplorer()
	{
		return isBrowser(Browser.IE);
	}
	
	
	/**
	 * Detects if the client is using the Edge browser.
	 */
	public boolean isEdge()
	{
		return isBrowser(Browser.EDGE);
	}
	
	
	/**
	 * Detects if the client is using the Safari browser.
	 */
	public boolean isSafari()
	{
		return isBrowser(Browser.SAFARI);
	}
	
	
	/**
	 * Detects if the client is using the Operabrowser.
	 */
	public boolean isOpera()
	{
		return isBrowser(Browser.OPERA);
	}
	
	
	private boolean isBrowser(final Browser test)
	{
		final Browser browser = this.userAgent.getBrowser();
		return browser == test || browser.getGroup() == test;
	}
	
	
	/**
	 * Gets the height of the screen in pixels. This is the full screen
	 * resolution and not the height available for the application.
	 *
	 * @return the height of the screen in pixels.
	 */
	public int getScreenHeight()
	{
		return this.weBrowser.getScreenHeight();
	}
	
	
	/**
	 * Gets the width of the screen in pixels. This is the full screen
	 * resolution and not the width available for the application.
	 *
	 * @return the width of the screen in pixels.
	 */
	public int getScreenWidth()
	{
		return this.weBrowser.getScreenWidth();
	}
	
	
	/**
	 * Returns the IP-address of the client. If the application is running
	 * inside a portlet, this method will return null. *
	 */
	public String getAddress()
	{
		return this.weBrowser.getAddress();
	}
	
	
	/**
	 * Returns the locale of the client browser.
	 */
	public Locale getLocale()
	{
		return this.weBrowser.getLocale();
	}
	
	
	/**
	 * Returns the browser-reported TimeZone offset in milliseconds from GMT.
	 * This includes possible daylight saving adjustments, to figure out which
	 * TimeZone the user actually might be in, see
	 * {@link #getRawTimezoneOffset()}.
	 *
	 * @see WebBrowser#getRawTimezoneOffset()
	 * @return timezone offset in milliseconds, 0 if not available
	 */
	public int getTimezoneOffset()
	{
		return this.weBrowser.getTimezoneOffset();
	}
	
	
	/**
	 * Returns the browser-reported TimeZone offset in milliseconds from GMT
	 * ignoring possible daylight saving adjustments that may be in effect in
	 * the browser.
	 * <p>
	 * You can use this to figure out which TimeZones the user could actually be
	 * in by calling {@link TimeZone#getAvailableIDs(int)}.
	 * </p>
	 * <p>
	 * If {@link #getRawTimezoneOffset()} and {@link #getTimezoneOffset()}
	 * returns the same value, the browser is either in a zone that does not
	 * currently have daylight saving time, or in a zone that never has daylight
	 * saving time.
	 * </p>
	 *
	 * @return timezone offset in milliseconds excluding DST, 0 if not available
	 */
	public int getRawTimezoneOffset()
	{
		return this.weBrowser.getRawTimezoneOffset();
	}
	
	
	/**
	 * Returns the offset in milliseconds between the browser's GMT TimeZone and
	 * DST.
	 *
	 * @return the number of milliseconds that the TimeZone shifts when DST is
	 *         in effect
	 */
	public int getDSTSavings()
	{
		return this.weBrowser.getDSTSavings();
	}
}
