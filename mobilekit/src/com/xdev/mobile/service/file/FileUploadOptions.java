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

package com.xdev.mobile.service.file;


/**
 * @author XDEV Software
 *
 */
public class FileUploadOptions
{

	private String	fileKey;
	private String	fileName;
	private String	mimeType;
	
	
	// TODO params, headers
	
	public FileUploadOptions()
	{
		super();
	}


	/**
	 * @param fileKey
	 * @param fileName
	 * @param mimeType
	 */
	public FileUploadOptions(final String fileKey, final String fileName, final String mimeType)
	{
		super();
		this.fileKey = fileKey;
		this.fileName = fileName;
		this.mimeType = mimeType;
	}


	/**
	 * @return the fileKey
	 */
	public String getFileKey()
	{
		return this.fileKey;
	}
	
	
	/**
	 * @param fileKey
	 *            the fileKey to set
	 */
	public void setFileKey(final String fileKey)
	{
		this.fileKey = fileKey;
	}
	
	
	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return this.fileName;
	}
	
	
	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}
	
	
	/**
	 * @return the mimeType
	 */
	public String getMimeType()
	{
		return this.mimeType;
	}
	
	
	/**
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(final String mimeType)
	{
		this.mimeType = mimeType;
	}
}
