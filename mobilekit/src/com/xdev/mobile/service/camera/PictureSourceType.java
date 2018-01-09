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


/**
 * Set the source of the picture.
 *
 * @author XDEV Software
 * 
 */
public enum PictureSourceType
{
	/**
	 * Choose image from picture library (same as
	 * {@link PictureSourceType#SAVED_PHOTO_ALBUM} for Android)
	 */
	PHOTO_LIBRARY("PHOTOLIBRARY"),
	
	/**
	 * Take picture from camera
	 */
	CAMERA("CAMERA"),
	
	/**
	 * Choose image from picture library (same as
	 * {@link PictureSourceType#PHOTO_LIBRARY} for Android)
	 */
	SAVED_PHOTO_ALBUM("SAVEDPHOTOALBUM");
	
	private String fieldName;
	
	
	private PictureSourceType(final String fieldName)
	{
		this.fieldName = fieldName;
	}
	
	
	public String getFieldName()
	{
		return this.fieldName;
	}
}
