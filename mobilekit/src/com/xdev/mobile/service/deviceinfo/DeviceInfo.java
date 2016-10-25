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

package com.xdev.mobile.service.deviceinfo;


/**
 * Information about the devices hardware and software.
 *
 *
 * @author XDEV Software
 *
 */
public class DeviceInfo
{
	private final String	model;
	private final String	platform;
	private final String	uuid;
	private final String	version;
	private final String	manufacturer;
	private final boolean	virtual;
	private final String	serial;


	DeviceInfo(final String model, final String platform, final String uuid, final String version,
			final String manufacturer, final boolean virtual, final String serial)
	{
		this.model = model;
		this.platform = platform;
		this.uuid = uuid;
		this.version = version;
		this.manufacturer = manufacturer;
		this.virtual = virtual;
		this.serial = serial;
	}


	/**
	 * Returns the name of the device's model or product. The value is set by
	 * the device manufacturer and may be different across versions of the same
	 * product.
	 */
	public String getModel()
	{
		return this.model;
	}
	
	
	/**
	 * Returns the device's operating system name.
	 */
	public String getPlatform()
	{
		return this.platform;
	}
	
	
	/**
	 * Returns the device's Universally Unique Identifier (UUID).
	 */
	public String getUuid()
	{
		return this.uuid;
	}
	
	
	/**
	 * Returns the operating system version.
	 */
	public String getVersion()
	{
		return this.version;
	}
	
	
	/**
	 * Returns the device's manufacturer.
	 */
	public String getManufacturer()
	{
		return this.manufacturer;
	}
	
	
	/**
	 * Returns whether the device is running on a simulator.
	 */
	public boolean isVirtual()
	{
		return this.virtual;
	}
	
	
	/**
	 * Returns the device hardware serial number.
	 */
	public String getSerial()
	{
		return this.serial;
	}
}
