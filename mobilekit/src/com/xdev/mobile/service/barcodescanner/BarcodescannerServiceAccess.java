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

package com.xdev.mobile.service.barcodescanner;


import java.util.function.Consumer;

import com.xdev.mobile.service.MobileServiceError;


/**
 * @author XDEV Software
 *
 */
public interface BarcodescannerServiceAccess
{
	/**
	 * Opens the barcode scanner and passes the result to the callback if the
	 * scan was completed successfully.
	 * <p>
	 * For more information on suported formats see {@link BarcodescannerService
	 * class doc}.
	 *
	 * @param successCallback
	 *
	 * @see BarcodescannerService
	 * @see BarcodeFormat
	 * @see BarcodeData
	 */
	default public void scan(final Consumer<BarcodeData> successCallback)
	{
		scan(BarcodescannerOptions.defaults(),successCallback,null);
	}


	/**
	 * Opens the barcode scanner and passes the result to the callback if the
	 * scan was completed successfully.
	 * <p>
	 * For more information on suported formats see {@link BarcodescannerService
	 * class doc}.
	 *
	 * @param successCallback
	 * @param errorCallback
	 *
	 * @see BarcodescannerService
	 * @see BarcodeFormat
	 * @see BarcodeData
	 */
	default public void scan(final Consumer<BarcodeData> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		scan(BarcodescannerOptions.defaults(),successCallback,errorCallback);
	}


	/**
	 * Opens the barcode scanner and passes the result to the callback if the
	 * scan was completed successfully.
	 * <p>
	 * For more information on suported formats see {@link BarcodescannerService
	 * class doc}.
	 *
	 * @param options
	 * @param successCallback
	 *
	 * @see BarcodescannerService
	 * @see BarcodeFormat
	 * @see BarcodeData
	 */
	default public void scan(final BarcodescannerOptions options,
			final Consumer<BarcodeData> successCallback)
	{
		scan(options,successCallback,null);
	}


	/**
	 * Opens the barcode scanner and passes the result to the callback if the
	 * scan was completed successfully.
	 * <p>
	 * For more information on suported formats see {@link BarcodescannerService
	 * class doc}.
	 *
	 * @param options
	 * @param successCallback
	 * @param errorCallback
	 *
	 * @see BarcodescannerService
	 * @see BarcodeFormat
	 * @see BarcodeData
	 */
	public void scan(BarcodescannerOptions options, Consumer<BarcodeData> successCallback,
			Consumer<MobileServiceError> errorCallback);
}
