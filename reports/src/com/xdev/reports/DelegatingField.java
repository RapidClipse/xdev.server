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

package com.xdev.reports;


import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;


/**
 * @author XDEV Software
 *
 */
public abstract class DelegatingField implements JRField
{
	private final JRField delegate;


	public DelegatingField(final JRField delegate)
	{
		this.delegate = delegate;
	}


	@Override
	public String getDescription()
	{
		return this.delegate.getDescription();
	}


	@Override
	public String getName()
	{
		return this.delegate.getName();
	}


	@Override
	public Class<?> getValueClass()
	{
		return this.delegate.getValueClass();
	}


	@Override
	public String getValueClassName()
	{
		return this.delegate.getValueClassName();
	}


	@Override
	public void setDescription(final String arg0)
	{
		this.delegate.setDescription(arg0);
	}


	@Override
	public JRPropertiesHolder getParentProperties()
	{
		return this.delegate.getParentProperties();
	}


	@Override
	public JRPropertiesMap getPropertiesMap()
	{
		return this.delegate.getPropertiesMap();
	}


	@Override
	public boolean hasProperties()
	{
		return this.delegate.hasProperties();
	}


	@Override
	public JRPropertyExpression[] getPropertyExpressions()
	{
		return this.delegate.getPropertyExpressions();
	}


	@Override
	public Object clone()
	{
		return this.delegate.clone();
	}
}
