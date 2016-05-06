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


import java.io.ByteArrayInputStream;
import java.util.Base64;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;


/**
 * Helper class to retrieve image data from the {@link CameraService}.
 *
 * @author XDEV Software
 *
 */
public class ImageData
{
	private String	base64data;
	private String	uri;


	public ImageData(final CameraOptions options, final String value)
	{
		if(options.getDestinationType() == DestinationType.IMAGE)
		{
			this.base64data = value;
		}
		else
		{
			this.uri = value;
		}
	}


	public String getBase64data()
	{
		return this.base64data;
	}


	public byte[] toRawData()
	{
		if(this.base64data == null)
		{
			throw new IllegalArgumentException("ImageData contains only URI");
		}

		return Base64.getDecoder().decode(this.base64data);
	}


	public Resource toResource()
	{
		if(this.base64data == null)
		{
			throw new IllegalArgumentException("ImageData contains only URI");
		}

		return new StreamResource(() -> new ByteArrayInputStream(toRawData()),toString());
	}


	public String getURI()
	{
		return this.uri;
	}
}
