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


/**
 * @author XDEV Software
 *
 */
/*
 * TODO popoverOptions
 * https://github.com/apache/cordova-plugin-camera#cameracameraoptions-- object
 */
public class CameraOptions
{
	public static CameraOptions takeAndReturnPicture()
	{
		return new CameraOptions().mediaType(MediaType.PICTURE)
				.destinationType(DestinationType.IMAGE);
	}
	
	private int					quality				= 50;
	private DestinationType		destinationType		= DestinationType.FILE_URI;
	private PictureSourceType	sourceType			= PictureSourceType.CAMERA;
	private final boolean		allowEdit			= false;
	private EncodingType		encodingType		= EncodingType.JPEG;
	private Integer				targetWidth			= null;
	private Integer				targetHeight		= null;
	private MediaType			mediaType			= MediaType.PICTURE;
	private boolean				correctOrientation	= false;
	private boolean				saveToPhotoAlbum	= false;
	private Direction			direction			= Direction.BACK;
	
	
	public CameraOptions()
	{
	}
	
	
	public int getQuality()
	{
		return this.quality;
	}
	
	
	public CameraOptions quality(final int quality)
	{
		this.quality = quality;
		return this;
	}
	
	
	public CameraOptions destinationType(final DestinationType destinationType)
	{
		this.destinationType = destinationType;
		return this;
	}
	
	
	public CameraOptions sourceType(final PictureSourceType sourceType)
	{
		this.sourceType = sourceType;
		return this;
	}
	
	
	/*
	 * https://github.com/apache/cordova-plugin-camera#android-quirks-1
	 */
	// public CameraOptions allowEdit()
	// {
	// this.allowEdit = true;
	// return this;
	// }
	
	public CameraOptions encodingType(final EncodingType encodingType)
	{
		this.encodingType = encodingType;
		return this;
	}
	
	
	public CameraOptions targetSize(final int width, final int height)
	{
		this.targetWidth = width;
		this.targetHeight = height;
		return this;
	}
	
	
	public CameraOptions mediaType(final MediaType mediaType)
	{
		this.mediaType = mediaType;
		return this;
	}
	
	
	public CameraOptions correctOrientation()
	{
		this.correctOrientation = true;
		return this;
	}
	
	
	public CameraOptions saveToPhotoAlbum()
	{
		this.saveToPhotoAlbum = true;
		return this;
	}
	
	
	public CameraOptions direction(final Direction direction)
	{
		this.direction = direction;
		return this;
	}
	
	
	public DestinationType getDestinationType()
	{
		return this.destinationType;
	}
	
	
	public PictureSourceType getSourceType()
	{
		return this.sourceType;
	}
	
	
	public boolean isAllowEdit()
	{
		return this.allowEdit;
	}
	
	
	public EncodingType getEncodingType()
	{
		return this.encodingType;
	}
	
	
	public Integer getTargetWidth()
	{
		return this.targetWidth;
	}
	
	
	public Integer getTargetHeight()
	{
		return this.targetHeight;
	}
	
	
	public MediaType getMediaType()
	{
		return this.mediaType;
	}
	
	
	public boolean isCorrectOrientation()
	{
		return this.correctOrientation;
	}
	
	
	public boolean isSaveToPhotoAlbum()
	{
		return this.saveToPhotoAlbum;
	}
	
	
	public Direction getDirection()
	{
		return this.direction;
	}
}
