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

package com.xdev.mobile.service.camera;


import java.util.function.Consumer;

import com.xdev.mobile.service.MobileServiceError;


/**
 * @author XDEV Software
 *
 */
public interface CameraServiceAccess
{
	/**
	 * Takes a photo using the camera, or retrieves a photo from the device's
	 * image gallery. The image is passed to the success callback as a
	 * base64-encoded String, or as the URI for the image file.
	 * <p>
	 * Photo resolution on newer devices is quite good. Photos selected from the
	 * device's gallery are not downscaled to a lower quality, even if a quality
	 * parameter is specified. To avoid common memory problems, set
	 * destinationType to {@link DestinationType#FILE_URI} rather than
	 * {@link DestinationType#IMAGE}.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows</li>
	 * <li>Windows Phone 8</li>
	 * </ul>
	 *
	 */
	default public void getPicture(final CameraOptions options,
			final Consumer<ImageData> successCallback)
	{
		getPicture(options,successCallback,null);
	}


	/**
	 * Takes a photo using the camera, or retrieves a photo from the device's
	 * image gallery. The image is passed to the success callback as a
	 * base64-encoded String, or as the URI for the image file.
	 * <p>
	 * Photo resolution on newer devices is quite good. Photos selected from the
	 * device's gallery are not downscaled to a lower quality, even if a quality
	 * parameter is specified. To avoid common memory problems, set
	 * destinationType to {@link DestinationType#FILE_URI} rather than
	 * {@link DestinationType#IMAGE}.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows</li>
	 * <li>Windows Phone 8</li>
	 * </ul>
	 *
	 */
	public void getPicture(CameraOptions options, Consumer<ImageData> successCallback,
			Consumer<MobileServiceError> errorCallback);
}
