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

package com.xdev.mobile.service.camera;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceError;
import com.xdev.mobile.ui.MobileUI;

import elemental.json.JsonArray;


/**
 * Service for taking pictures and for choosing images from the system's image
 * library.
 *
 * @author XDEV Software
 *
 */
/*
 * TODO camera.cleanup() see
 * https://github.com/apache/cordova-plugin-camera#cameracleanup
 */
@JavaScript("camera.js")
public class CameraService extends MobileService
{
	/**
	 * Returns the camera service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.camera.CameraService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @see MobileUI#getMobileService(Class)
	 * @return the camera service if available
	 */
	public static CameraService getInstance()
	{
		return getServiceHelper(CameraService.class);
	}
	
	
	
	private static class GetPictureCall
	{
		final CameraOptions					options;
		final Consumer<ImageData>			successCallback;
		final Consumer<MobileServiceError>	errorCallback;
											
											
		GetPictureCall(final CameraOptions options, final Consumer<ImageData> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.options = options;
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	private final Map<String, GetPictureCall> getPictureCalls = new HashMap<>();
	
	
	public CameraService(final AbstractClientConnector target)
	{
		super(target);
		
		this.addFunction("camera_getPicture_success",this::camera_getPicture_success);
		this.addFunction("camera_getPicture_error",this::camera_getPicture_error);
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
	 * <li>BlackBerry 10</li>
	 * <li>Firefox OS</li>
	 * <li>Amazon FireOS</li>
	 * <li>iOS</li>
	 * <li>Windows</li>
	 * <li>Windows Phone 8</li>
	 * </ul>
	 *
	 */
	public synchronized void getPicture(final CameraOptions options,
			final Consumer<ImageData> successCallback)
	{
		this.getPicture(options,successCallback,null);
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
	 * <li>BlackBerry 10</li>
	 * <li>Firefox OS</li>
	 * <li>Amazon FireOS</li>
	 * <li>iOS</li>
	 * <li>Windows</li>
	 * <li>Windows Phone 8</li>
	 * </ul>
	 *
	 */
	public synchronized void getPicture(final CameraOptions options,
			final Consumer<ImageData> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final GetPictureCall call = new GetPictureCall(options,successCallback,errorCallback);
		this.getPictureCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("camera_getPicture('").append(id).append("',");
		appendOptions(js,options);
		js.append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void appendOptions(final StringBuilder js, final CameraOptions options)
	{
		js.append("{\n");
		js.append("quality: ").append(options.getQuality()).append(",\n");
		js.append("destinationType: Camera.DestinationType.")
				.append(options.getDestinationType().getFieldName()).append(",\n");
		js.append("sourceType: Camera.PictureSourceType.")
				.append(options.getSourceType().getFieldName()).append(",\n");
		js.append("allowEdit: ").append(options.isAllowEdit()).append(",\n");
		js.append("encodingType: Camera.EncodingType.").append(options.getEncodingType())
				.append(",\n");
		final Integer targetWidth = options.getTargetWidth();
		final Integer targetHeight = options.getTargetHeight();
		if(targetWidth != null && targetHeight != null)
		{
			js.append("targetWidth: ").append(targetWidth).append(",\n");
			js.append("targetHeight: ").append(targetHeight).append(",\n");
		}
		js.append("mediaType: Camera.MediaType.").append(options.getMediaType()).append(",\n");
		js.append("correctOrientation: ").append(options.isCorrectOrientation()).append(",\n");
		js.append("saveToPhotoAlbum: ").append(options.isSaveToPhotoAlbum()).append(",\n");
		js.append("cameraDirection: Camera.Direction.").append(options.getDirection())
				.append("\n}");
	}
	
	
	private void camera_getPicture_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final GetPictureCall call = this.getPictureCalls.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		call.successCallback.accept(new ImageData(call.options,arguments.getString(1)));
	}
	
	
	private void camera_getPicture_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final GetPictureCall call = this.getPictureCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		
		call.errorCallback.accept(new MobileServiceError(this,arguments.getString(1)));
	}
}
