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

package com.xdev.mobile.service.sms;


/**
 * @author XDEV Software
 *
 */
public class SmsOptions
{
	public static SmsOptions defaults()
	{
		return new SmsOptions();
	}
	
	private boolean	replaceLineBreaks	= false;
	private boolean	androidNativeApp	= false;
	
	
	public SmsOptions()
	{
	}


	/**
	 * <code>true</code> to replace \n by a new line, <code>false</code> by
	 * default
	 */
	public SmsOptions replaceLineBreaks(final boolean replaceLineBreaks)
	{
		this.replaceLineBreaks = replaceLineBreaks;
		return this;
	}
	
	
	/**
	 * @return the replaceLineBreaks setting
	 */
	public boolean isReplaceLineBreaks()
	{
		return this.replaceLineBreaks;
	}


	/**
	 * <code>true</code> if the SMS should be sent with the native android SMS
	 * messaging
	 */
	public SmsOptions androidNativeApp(final boolean androidNativeApp)
	{
		this.androidNativeApp = androidNativeApp;
		return this;
	}
	
	
	/**
	 * @return the androidNativeApp setting
	 */
	public boolean isAndroidNativeApp()
	{
		return this.androidNativeApp;
	}
}
