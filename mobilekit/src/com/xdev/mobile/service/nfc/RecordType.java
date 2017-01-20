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

package com.xdev.mobile.service.nfc;


import java.util.Arrays;


/**
 * @author XDEV Software
 *
 */
public enum RecordType
{
	RTD_TEXT(0x54), // "T"
	RTD_URI(0x55), // "U"
	RTD_SMART_POSTER(0x53, 0x70), // "Sp"
	RTD_ALTERNATIVE_CARRIER(0x61, 0x63), // "ac"
	RTD_HANDOVER_CARRIER(0x48, 0x63), // "Hc"
	RTD_HANDOVER_REQUEST(0x48, 0x72), // "Hr"
	RTD_HANDOVER_SELECT(0x48, 0x73); // "Hs"

	public static RecordType byData(final byte[] data)
	{
		return Arrays.stream(values()).filter(rt -> Arrays.equals(rt.data,data)).findAny()
				.orElseThrow(() -> new IllegalArgumentException(
						"No RecordType for data: " + Arrays.toString(data)));
	}

	private byte[] data;


	private RecordType(final int... data)
	{
		this.data = new byte[data.length];
		for(int i = 0; i < data.length; i++)
		{
			this.data[i] = (byte)data[i];
		}
	}


	/**
	 * @return the data
	 */
	public byte[] getData()
	{
		return this.data;
	}
}
