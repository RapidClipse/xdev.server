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

package com.xdev.mobile.service.nfc;


import java.util.function.Consumer;

import com.xdev.mobile.service.MobileServiceError;


/**
 * @author XDEV Software
 *
 */
public interface NfcServiceAccess
{
	/**
	 * Registers an event listener for any NDEF tag.
	 * <p>
	 * A ndef event is fired when a NDEF tag is read.
	 *
	 * @param callback
	 *            The callback that is called when an NDEF tag is read.
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            added.
	 */
	default public void startNdefListener(final Consumer<Ndef> callback,
			final Consumer<String> successCallback)
	{
		startNdefListener(callback,successCallback,null);
	}


	/**
	 * Registers an event listener for any NDEF tag.
	 * <p>
	 * A ndef event is fired when a NDEF tag is read.
	 *
	 * @param callback
	 *            The callback that is called when an NDEF tag is read.
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            added.
	 * @param errorCallback
	 *            (Optional) The callback that is called if there was an error.
	 */
	public void startNdefListener(Consumer<Ndef> callback, Consumer<String> successCallback,
			Consumer<MobileServiceError> errorCallback);


	/**
	 * Removes the previously registered event listener for NDEF tags added via
	 * {@link #addNdefListener(Consumer, Consumer, Consumer)}.
	 *
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            successfully removed.
	 */
	default public void stopNdefListener(final Consumer<String> successCallback)
	{
		stopNdefListener(successCallback,null);
	}


	/**
	 * Removes the previously registered event listener for NDEF tags added via
	 * {@link #addNdefListener(Consumer, Consumer, Consumer)}.
	 *
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            successfully removed.
	 * @param errorCallback
	 *            (Optional) The callback that is called if there was an error
	 *            during removal.
	 */
	public void stopNdefListener(Consumer<String> successCallback,
			Consumer<MobileServiceError> errorCallback);


	/**
	 * Registers an event listener for tags matching any tag type.
	 * <p>
	 * This event occurs when any tag is detected by the phone.
	 *
	 * @param callback
	 *            The callback that is called when a tag is detected.
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            added.
	 */
	default public void startTagDiscoveredListener(final Consumer<Ndef> callback,
			final Consumer<String> successCallback)
	{
		startTagDiscoveredListener(callback,successCallback,null);
	}


	/**
	 * Registers an event listener for tags matching any tag type.
	 * <p>
	 * This event occurs when any tag is detected by the phone.
	 *
	 * @param callback
	 *            The callback that is called when a tag is detected.
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            added.
	 * @param errorCallback
	 *            (Optional) The callback that is called if there was an error.
	 */
	public void startTagDiscoveredListener(Consumer<Ndef> callback,
			Consumer<String> successCallback, Consumer<MobileServiceError> errorCallback);


	/**
	 * Removes the previously registered event listener added via
	 * {@link #startTagDiscoveredListener(Consumer, Consumer, Consumer)}.
	 *
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            successfully removed.
	 */
	default public void stopTagDiscoveredListener(final Consumer<String> successCallback)
	{
		stopTagDiscoveredListener(successCallback,null);
	}


	/**
	 * Removes the previously registered event listener added via
	 * {@link #startTagDiscoveredListener(Consumer, Consumer, Consumer)}.
	 *
	 * @param successCallback
	 *            (Optional) The callback that is called when the listener is
	 *            successfully removed.
	 * @param errorCallback
	 *            (Optional) The callback that is called if there was an error
	 *            during removal.
	 */
	public void stopTagDiscoveredListener(Consumer<String> successCallback,
			Consumer<MobileServiceError> errorCallback);


	/**
	 * Erase a NDEF tag by writing an empty message. Will format unformatted
	 * tags before writing.
	 *
	 * @param successCallback
	 */
	default public void eraseTag(final Consumer<String> successCallback)
	{
		eraseTag(successCallback,null);
	}


	/**
	 * Erase a NDEF tag by writing an empty message. Will format unformatted
	 * tags before writing.
	 *
	 * @param successCallback
	 * @param errorCallback
	 */
	public void eraseTag(Consumer<String> successCallback,
			Consumer<MobileServiceError> errorCallback);


	/**
	 * Writes an NDEF Message to a NFC tag.
	 * <p>
	 * A NDEF Message is an array of one or more NDEF Records
	 *
	 * @param message
	 * @param successCallback
	 */
	default public void write(final Consumer<String> successCallback, final Record... records)
	{
		write(successCallback,null,records);
	}


	/**
	 * Writes an NDEF Message to a NFC tag.
	 * <p>
	 * A NDEF Message is an array of one or more NDEF Records
	 *
	 * @param message
	 * @param successCallback
	 * @param errorCallback
	 */
	public void write(Consumer<String> successCallback, Consumer<MobileServiceError> errorCallback,
			Record... records);


	/**
	 * Makes a NFC tag read only.
	 * <p>
	 * <b> Warning this is permanent.</b>
	 *
	 * @param successCallback
	 */
	default public void makeReadOnly(final Consumer<String> successCallback)
	{
		makeReadOnly(successCallback,null);
	}


	/**
	 * Makes a NFC tag read only.
	 * <p>
	 * <b> Warning this is permanent.</b>
	 *
	 * @param successCallback
	 * @param errorCallback
	 */
	public void makeReadOnly(Consumer<String> successCallback,
			Consumer<MobileServiceError> errorCallback);


	/**
	 * Show the NFC settings on the device.
	 *
	 * @param successCallback
	 */
	default public void showSettings(final Consumer<String> successCallback)
	{
		showSettings(successCallback,null);
	}


	/**
	 * Show the NFC settings on the device.
	 *
	 * @param successCallback
	 * @param errorCallback
	 */
	public void showSettings(Consumer<String> successCallback,
			Consumer<MobileServiceError> errorCallback);


	public void removeAllListener();
}
