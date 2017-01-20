/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.barcodescanner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Optional parameters to customize the usage of the
 * {@link BarcodescannerService}.
 *
 * @author XDEV Software
 *
 */
public class BarcodescannerOptions
{
	public static BarcodescannerOptions defaults()
	{
		return new BarcodescannerOptions().showFlipCameraButton(true);
	}

	private Boolean						preferFrontCamera;
	private Boolean						showFlipCameraButton;
	private String						prompt;
	private final List<BarcodeFormat>	formats	= new ArrayList<>();
	private Orientation					orientation;


	public BarcodescannerOptions()
	{
	}


	/**
	 * Set to <code>true</code> if the device's front camera should be
	 * preferred.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * </ul>
	 */
	public Boolean getPreferFrontCamera()
	{
		return this.preferFrontCamera;
	}


	/**
	 * Set to <code>true</code> if the device's front camera should be
	 * preferred.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * </ul>
	 */
	public BarcodescannerOptions preferFrontCamera(final boolean preferFrontCamera)
	{
		this.preferFrontCamera = preferFrontCamera;
		return this;
	}


	/**
	 * Set to <code>true</code> if the flip camera button should be shown.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * </ul>
	 */
	public Boolean getShowFlipCameraButton()
	{
		return this.showFlipCameraButton;
	}


	/**
	 * Set to <code>true</code> if the flip camera button should be shown.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * </ul>
	 */
	public BarcodescannerOptions showFlipCameraButton(final boolean showFlipCameraButton)
	{
		this.showFlipCameraButton = showFlipCameraButton;
		return this;
	}


	/**
	 * Shows a prompt message.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 */
	public String getPrompt()
	{
		return this.prompt;
	}


	/**
	 * Shows a prompt message.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 */
	public BarcodescannerOptions prompt(final String prompt)
	{
		this.prompt = prompt;
		return this;
	}


	/**
	 * List of allowed formats. All formats are allowed if empty.
	 */
	public List<BarcodeFormat> getFormats()
	{
		return this.formats;
	}


	/**
	 * List of allowed formats. All formats are allowed if empty.
	 */
	public BarcodescannerOptions formats(final BarcodeFormat... formats)
	{
		this.formats.addAll(Arrays.asList(formats));
		return this;
	}


	/**
	 * List of allowed formats. All formats are allowed if empty.
	 */
	public BarcodescannerOptions formats(final List<BarcodeFormat> formats)
	{
		this.formats.addAll(formats);
		return this;
	}


	/**
	 * Preferred orientation. If not set the scanner rotates with the device.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 */
	public Orientation getOrientation()
	{
		return this.orientation;
	}


	/**
	 * Preferred orientation. If not set the scanner rotates with the device.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * </ul>
	 */
	public BarcodescannerOptions orientation(final Orientation orientation)
	{
		this.orientation = orientation;
		return this;
	}
}
