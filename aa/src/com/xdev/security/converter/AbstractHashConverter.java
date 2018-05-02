/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


import java.util.Locale;
import java.util.function.Supplier;

import org.apache.shiro.codec.Hex;

import com.vaadin.v7.data.util.converter.Converter;
import com.xdev.security.authentication.jpa.HashStrategy;


/**
 * @author XDEV Software
 * @since 3.1
 */
@SuppressWarnings("deprecation")
public class AbstractHashConverter implements Converter<String, String>
{
	private final Supplier<HashStrategy>	hashStrategySupplier;
	private final int						hashLength;


	protected AbstractHashConverter(final Supplier<HashStrategy> hashStrategySupplier,
			final int hashLength)
	{
		this.hashStrategySupplier = hashStrategySupplier;
		this.hashLength = hashLength;
	}


	@Override
	public String convertToModel(final String value, final Class<? extends String> targetType,
			final Locale locale) throws Converter.ConversionException
	{
		if(value == null || value.length() == 0)
		{
			return null;
		}

		final byte[] bytes = value.getBytes();

		if(bytes.length == this.hashLength)
		{
			return value;
		}
		
		return Hex.encodeToString(this.hashStrategySupplier.get().hashPassword(bytes));
	}


	@Override
	public String convertToPresentation(final String value,
			final Class<? extends String> targetType, final Locale locale)
			throws Converter.ConversionException
	{
		return value;
	}


	@Override
	public Class<String> getModelType()
	{
		return String.class;
	}


	@Override
	public Class<String> getPresentationType()
	{
		return String.class;
	}
}
