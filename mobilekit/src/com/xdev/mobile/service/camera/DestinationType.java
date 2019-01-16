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

package com.xdev.mobile.service.camera;


/**
 * The format of the return value the {@link CameraService} uses.
 *
 * @author XDEV Software
 *
 */
public enum DestinationType
{
	/**
	 * Return base64 encoded string
	 */
	IMAGE("DATA_URL"),
	
	/**
	 * Return file uri (content://media/external/images/media/2 for Android)
	 */
	FILE_URI("FILE_URI"),
	
	/**
	 * Return native uri (eg. asset-library://... for iOS)
	 */
	NATIVE_URI("NATIVE_URI");
	
	private String fieldName;
	
	
	private DestinationType(final String fieldName)
	{
		this.fieldName = fieldName;
	}
	
	
	String getFieldName()
	{
		return this.fieldName;
	}
}
