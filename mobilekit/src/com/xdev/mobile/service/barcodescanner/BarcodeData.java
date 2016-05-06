/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.mobile.service.barcodescanner;


/**
 * Result of the {@link BarcodescannerService#scan} methods. It contains the
 * format of the scanned barcode and its data as text.
 *
 *
 * @author XDEV Software
 *
 */
public class BarcodeData
{
	private final BarcodeFormat	format;
	private final String		data;


	public BarcodeData(final BarcodeFormat format, final String data)
	{
		this.format = format;
		this.data = data;
	}


	/**
	 *
	 * @return the format of the scanned barcode
	 */
	public BarcodeFormat getFormat()
	{
		return this.format;
	}


	/**
	 *
	 * @return the data of the scanned barcode
	 */
	public String getData()
	{
		return this.data;
	}


	@Override
	public String toString()
	{
		return this.format + ": " + this.data;
	}
}
