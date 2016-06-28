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

package com.xdev.mobile.config;


import java.util.Collections;
import java.util.Map;

import com.xdev.mobile.service.AbstractMobileService;


/**
 * @author XDEV Software
 * 		
 */
public interface MobileServiceConfiguration
{
	public Class<? extends AbstractMobileService> getServiceClass();


	public Map<String, String> getParameters();



	public static class Default implements MobileServiceConfiguration
	{
		private Class<? extends AbstractMobileService>	serviceClass;
		private Map<String, String>				params;


		public Default()
		{
		}


		@Override
		public Class<? extends AbstractMobileService> getServiceClass()
		{
			return this.serviceClass;
		}


		public void setServiceClass(final Class<? extends AbstractMobileService> serviceClass)
		{
			this.serviceClass = serviceClass;
		}


		@Override
		public Map<String, String> getParameters()
		{
			return this.params;
		}


		public void setParameters(final Map<String, String> params)
		{
			this.params = Collections.unmodifiableMap(params);
		}
	}
}
