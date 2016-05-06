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

package com.xdev.mobile.service.file;


import java.io.ByteArrayInputStream;

import org.apache.commons.codec.binary.Base64;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;


/**
 * @author XDEV Software
 *
 */
public class FileData
{
	private final String base64data;


	public FileData(final String base64data)
	{
		this.base64data = base64data;
	}
	
	
	public String getBase64data()
	{
		return this.base64data;
	}
	
	
	public byte[] toRawData()
	{
		final String encodingPrefix = "base64,";
		final int contentStartIndex = this.base64data.indexOf(encodingPrefix)
				+ encodingPrefix.length();
		final String data = contentStartIndex == -1 ? this.base64data
				: this.base64data.substring(contentStartIndex);
		return new Base64(true).decode(data);
	}
	
	
	public Resource toResource()
	{
		return new StreamResource(() -> new ByteArrayInputStream(toRawData()),toString());
	}
}
