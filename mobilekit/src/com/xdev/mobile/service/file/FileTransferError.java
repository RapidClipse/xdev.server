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
public class FileTransferError
{
	private double	code;
	private String	source;
	private String	target;
	private double	http_status;
	
	
	
	enum FileTransferErrorEnum
	{
		FILE_NOT_FOUND_ERR, INVALID_URL_ERR, CONNECTION_ERR, ABORT_ERR;
	}


	/**
	 * @param code
	 * @param source
	 * @param target
	 * @param http_status
	 */
	public FileTransferError(final double code, final String source, final String target,
			final double http_status)
	{
		super();
		this.code = code;
		this.source = source;
		this.target = target;
		this.http_status = http_status;
	}


	/**
	 *
	 */
	public FileTransferError()
	{
		super();
	}


	/**
	 * @return the code
	 */
	public double getCode()
	{
		return this.code;
	}


	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(final double code)
	{
		this.code = code;
	}


	/**
	 * @return the source
	 */
	public String getSource()
	{
		return this.source;
	}


	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(final String source)
	{
		this.source = source;
	}


	/**
	 * @return the target
	 */
	public String getTarget()
	{
		return this.target;
	}


	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(final String target)
	{
		this.target = target;
	}


	/**
	 * @return the http_status
	 */
	public double getHttp_status()
	{
		return this.http_status;
	}


	/**
	 * @param http_status
	 *            the http_status to set
	 */
	public void setHttp_status(final double http_status)
	{
		this.http_status = http_status;
	};

}
