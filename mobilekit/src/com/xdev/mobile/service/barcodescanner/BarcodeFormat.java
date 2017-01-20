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


/**
 * Supported formats of the {@link BarcodescannerService}.
 * <p>
 * For more information about platform support see {@link BarcodescannerService}
 *
 * @author XDEV Software
 *
 */
public enum BarcodeFormat
{
	/**
	 * Quick response matrix barcode
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/QR_code">Wikipedia</a>
	 */
	QR_CODE,

	/**
	 * Two dimensional matrix barcode
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Data_Matrix">Wikipedia</a>
	 */
	DATA_MATRIX,

	/**
	 * Universal Product Code
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Universal_Product_Code">
	 *      Wikipedia</a>
	 */
	UPC_E,

	/**
	 * Universal Product Code
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Universal_Product_Code">
	 *      Wikipedia</a>
	 */
	UPC_A,

	/**
	 * International Article Number
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/EAN-8">Wikipedia</a>
	 */
	EAN_8,

	/**
	 * International Article Number
	 *
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/International_Article_Number_(EAN)">
	 *      Wikipedia</a>
	 */
	EAN_13,

	/**
	 * High-density barcode
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Code_128">Wikipedia</a>
	 */
	CODE_128,

	/**
	 * Variable length, discrete barcode
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Code_39">Wikipedia</a>
	 */
	CODE_39,

	/**
	 * Enhanced version of {@link #CODE_39}
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Code_93">Wikipedia</a>
	 */
	CODE_93,

	/**
	 * Linear barcode
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Codabar">Wikipedia</a>
	 */
	CODABAR,

	/**
	 * Interleaved 2 of 5 barcode to encode a Global Trade Item Number
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/ITF-14">Wikipedia</a>
	 */
	ITF,

	/**
	 * Reduced Space Symbology
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/GS1_DataBar">Wikipedia</a>
	 */
	RSS14,

	/**
	 * Portable Data File, a stacked linear barcode symbol format
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/PDF417">Wikipedia</a>
	 */
	PDF417,

	/**
	 * Reduced Space Symbology
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/GS1_DataBar">Wikipedia</a>
	 */
	RSS_EXPANDED,

	/**
	 * Barcode symbology developed by the MSI Data Corporation, based on the
	 * original Plessey Code
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/MSI_Barcode">Wikipedia</a>
	 */
	MSI,

	/**
	 * 2D barcode
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Aztec_Code">Wikipedia</a>
	 */
	AZTEC,

	/**
	 * Unknown barcode format, used if the format of the barcode cannot be
	 * determined.
	 */
	UNKNOWN
}
