/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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

package com.xdev.security.converter;


import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;


/**
 * @author XDEV Software
 * @since 3.1
 */
public class StringToByteArrayConverter implements Converter<String, byte[]>
{
	@Override
	public byte[] convertToModel(final String value, final Class<? extends byte[]> targetType,
			final Locale locale) throws Converter.ConversionException
	{
		if(value != null)
		{
			return value.getBytes(StandardCharsets.UTF_8);
		}
		
		return null;
	}
	
	
	@Override
	public String convertToPresentation(final byte[] value,
			final Class<? extends String> targetType, final Locale locale)
			throws Converter.ConversionException
	{
		if(value != null)
		{
			return new String(value,StandardCharsets.UTF_8);
		}
		
		return null;
	}
	
	
	@Override
	public Class<byte[]> getModelType()
	{
		return byte[].class;
	}
	
	
	@Override
	public Class<String> getPresentationType()
	{
		return String.class;
	}
}
