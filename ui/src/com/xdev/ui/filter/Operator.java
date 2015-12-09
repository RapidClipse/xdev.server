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

package com.xdev.ui.filter;


import com.xdev.res.StringResourceUtils;
import com.xdev.ui.filter.OperatorHandler.Between;
import com.xdev.ui.filter.OperatorHandler.Equals;
import com.xdev.ui.filter.OperatorHandler.Greater;
import com.xdev.ui.filter.OperatorHandler.GreaterEqual;
import com.xdev.ui.filter.OperatorHandler.Is;
import com.xdev.ui.filter.OperatorHandler.IsNot;
import com.xdev.ui.filter.OperatorHandler.Less;
import com.xdev.ui.filter.OperatorHandler.LessEqual;
import com.xdev.ui.filter.OperatorHandler.StartsWith;


/**
 * @author XDEV Software
 *
 */
public enum Operator
{
	EQUALS(new Equals()),
	
	STARTS_WITH(new StartsWith()),
	
	IS(new Is()),
	
	IS_NOT(new IsNot()),
	
	GREATER(new Greater()),
	
	LESS(new Less()),
	
	GREATER_EQUAL(new GreaterEqual()),
	
	LESS_EQUAL(new LessEqual()),
	
	BETWEEN(new Between());

	private OperatorHandler	handler;
	private final String	caption;


	private Operator(final OperatorHandler handler)
	{
		this.handler = handler;
		this.caption = StringResourceUtils
				.getResourceString("ContainerFilterComponent.Operator." + name(),this);
	}
	
	
	/**
	 * @return the handler
	 */
	public OperatorHandler getHandler()
	{
		return this.handler;
	}
	
	
	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(final OperatorHandler handler)
	{
		this.handler = handler;
	}
	
	
	@Override
	public String toString()
	{
		return this.caption;
	}
}
