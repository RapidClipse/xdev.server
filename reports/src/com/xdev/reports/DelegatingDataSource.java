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

package com.xdev.reports;


import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


/**
 * @author XDEV Software
 *
 */
public class DelegatingDataSource implements JRDataSource
{
	private final JRDataSource delegate;


	public DelegatingDataSource(final JRDataSource delegate)
	{
		this.delegate = delegate;
	}


	@Override
	public boolean next() throws JRException
	{
		return this.delegate.next();
	}


	@Override
	public Object getFieldValue(final JRField jrField) throws JRException
	{
		return this.delegate.getFieldValue(jrField);
	}
}
