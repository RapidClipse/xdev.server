/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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


/**
 * @author XDEV Software
 *
 */
public abstract class Entry
{
	private boolean	isFile;
	private boolean	isDirectory;
	private String	name;
	private String	fullPath;


	// NOTE: The following attributes are defined by the W3C specification, but
	// are not supported by Cordova:
	// fileSystem

	/**
	 *
	 */
	public Entry()
	{
	}


	/**
	 * @param isFile
	 * @param isDirectory
	 * @param name
	 * @param fullPath
	 */
	public Entry(final boolean isFile, final boolean isDirectory, final String name,
			final String fullPath)
	{
		super();
		this.isFile = isFile;
		this.isDirectory = isDirectory;
		this.name = name;
		this.fullPath = fullPath;
	}


	/**
	 * @return the isFile
	 */
	public boolean isFile()
	{
		return this.isFile;
	}


	/**
	 * @param isFile
	 *            the isFile to set
	 */
	public void setFile(final boolean isFile)
	{
		this.isFile = isFile;
	}


	/**
	 * @return the isDirectory
	 */
	public boolean isDirectory()
	{
		return this.isDirectory;
	}


	/**
	 * @param isDirectory
	 *            the isDirectory to set
	 */
	public void setDirectory(final boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}


	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}


	/**
	 * @return the fullPath
	 */
	public String getFullPath()
	{
		return this.fullPath;
	}


	/**
	 * @param fullPath
	 *            the fullPath to set
	 */
	public void setFullPath(final String fullPath)
	{
		this.fullPath = fullPath;
	}
}
