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

package com.xdev.mobile.service.barcodescanner;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceDescriptor;
import com.xdev.mobile.service.MobileServiceError;

import elemental.json.JsonArray;
import elemental.json.JsonObject;


/**
 * Service for scanning barcodes of various types.
 * <p>
 * The following barcode types are currently supported:
 * <p>
 * Android
 * <ul>
 * <li>{@link BarcodeFormat#QR_CODE QR_CODE}</li>
 * <li>{@link BarcodeFormat#DATA_MATRIX DATA_MATRIX}</li>
 * <li>{@link BarcodeFormat#UPC_E UPC_E}</li>
 * <li>{@link BarcodeFormat#UPC_A UPC_A}</li>
 * <li>{@link BarcodeFormat#EAN_8 EAN_8}</li>
 * <li>{@link BarcodeFormat#EAN_13 EAN_13}</li>
 * <li>{@link BarcodeFormat#CODE_128 CODE_128}</li>
 * <li>{@link BarcodeFormat#CODE_39 CODE_39}</li>
 * <li>{@link BarcodeFormat#CODE_93 CODE_93}</li>
 * <li>{@link BarcodeFormat#CODABAR CODABAR}</li>
 * <li>{@link BarcodeFormat#ITF ITF}</li>
 * <li>{@link BarcodeFormat#RSS14 RSS14}</li>
 * <li>{@link BarcodeFormat#PDF417 PDF417}</li>
 * <li>{@link BarcodeFormat#RSS_EXPANDED RSS_EXPANDED}</li>
 * </ul>
 * <p>
 * iOS
 * <ul>
 * <li>{@link BarcodeFormat#QR_CODE QR_CODE}</li>
 * <li>{@link BarcodeFormat#DATA_MATRIX DATA_MATRIX}</li>
 * <li>{@link BarcodeFormat#UPC_E UPC_E}</li>
 * <li>{@link BarcodeFormat#UPC_A UPC_A}</li>
 * <li>{@link BarcodeFormat#EAN_8 EAN_8}</li>
 * <li>{@link BarcodeFormat#EAN_13 EAN_13}</li>
 * <li>{@link BarcodeFormat#CODE_128 CODE_128}</li>
 * <li>{@link BarcodeFormat#CODE_39 CODE_39}</li>
 * <li>{@link BarcodeFormat#ITF ITF}</li>
 * </ul>
 * <p>
 * Windows
 * <ul>
 * <li>{@link BarcodeFormat#UPC_A UPC_A}</li>
 * <li>{@link BarcodeFormat#UPC_E UPC_E}</li>
 * <li>{@link BarcodeFormat#EAN_8 EAN_8}</li>
 * <li>{@link BarcodeFormat#EAN_13 EAN_13}</li>
 * <li>{@link BarcodeFormat#CODE_39 CODE_39}</li>
 * <li>{@link BarcodeFormat#CODE_93 CODE_93}</li>
 * <li>{@link BarcodeFormat#CODE_128 CODE_128}</li>
 * <li>{@link BarcodeFormat#ITF ITF}</li>
 * <li>{@link BarcodeFormat#CODABAR CODABAR}</li>
 * <li>{@link BarcodeFormat#MSI MSI}</li>
 * <li>{@link BarcodeFormat#RSS14 RSS14}</li>
 * <li>{@link BarcodeFormat#QR_CODE QR_CODE}</li>
 * <li>{@link BarcodeFormat#DATA_MATRIX DATA_MATRIX}</li>
 * <li>{@link BarcodeFormat#AZTEC AZTEC}</li>
 * <li>{@link BarcodeFormat#PDF417 PDF417}</li>
 * </ul>
 *
 * @author XDEV Software
 * 		
 */
@MobileServiceDescriptor("barcodescanner-descriptor.xml")
@JavaScript("barcodescanner.js")
public class BarcodescannerService extends MobileService
{
	/**
	 * Returns the barcodescanner service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.barcodescanner.BarcodescannerService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the barcodescanner service if available
	 */
	public static BarcodescannerService getInstance()
	{
		return getMobileService(BarcodescannerService.class);
	}
	
	private final Map<String, ServiceCall<BarcodeData, MobileServiceError>> scanCalls = new HashMap<>();
	
	
	public BarcodescannerService(final AbstractClientConnector target)
	{
		super(target);
		
		this.addFunction("barcodescanner_scan_success",this::barcodescanner_scan_success);
		this.addFunction("barcodescanner_scan_error",this::barcodescanner_scan_error);
	}


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
	public synchronized void scan(final Consumer<BarcodeData> successCallback)
	{
		this.scan(successCallback,null);
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
	public synchronized void scan(final Consumer<BarcodeData> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<BarcodeData, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.scanCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("barcodescanner_scan('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void barcodescanner_scan_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<BarcodeData, MobileServiceError> call = this.scanCalls.remove(id);
		if(call != null)
		{
			final JsonObject object = arguments.getObject(1);
			if(!isCancelled(object))
			{
				final String format = object.getString("format");
				BarcodeFormat barcodeFormat;
				try
				{
					barcodeFormat = BarcodeFormat.valueOf(format);
				}
				catch(final IllegalArgumentException e)
				{
					barcodeFormat = BarcodeFormat.UNKNOWN;
				}
				
				final String text = object.getString("text");
				
				final BarcodeData barcodeData = new BarcodeData(barcodeFormat,text);
				call.success(barcodeData);
			}
		}
	}


	private boolean isCancelled(final JsonObject object)
	{
		final String cancelled = object.get("cancelled").asString();
		return "1".equals(cancelled) || "true".equalsIgnoreCase(cancelled);
	}
	
	
	private void barcodescanner_scan_error(final JsonArray arguments)
	{
		callError(arguments,this.scanCalls,true);
	}
}
