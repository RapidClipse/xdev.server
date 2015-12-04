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

package com.xdev.mobile.communication;


import com.xdev.mobile.service.MobileService;


/**
 * @author XDEV Software
 *
 */
public interface MobileConfiguration
{
	public Class<? extends MobileService>[] getMobileServices();



	public static class Default implements MobileConfiguration
	{
		private Class<? extends MobileService>[] mobileServices;
		
		
		public Default()
		{
			super();
		}


		@Override
		public Class<? extends MobileService>[] getMobileServices()
		{
			return this.mobileServices;
		}


		public void setMobileServices(final Class<? extends MobileService>[] mobileServices)
		{
			this.mobileServices = mobileServices;
		}
	}
}
