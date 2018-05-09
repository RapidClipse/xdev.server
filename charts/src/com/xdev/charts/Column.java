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

package com.xdev.charts;


import java.io.Serializable;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class Column implements Serializable
{
	private String		id;
	private String		label;
	private String		type;
	private DataRole	p;
	
	
	private Column(final String id, final String label, final ColumnType type)
	{
		this.id = id;
		this.label = label;
		this.type = type.text();
	}
	
	
	private Column(final ColumnType type, final DataRoleType role)
	{
		this.id = "role";
		this.label = "role";
		this.type = type.text();
		this.p = new DataRole(role);
	}
	
	
	public static Column show(final String label, final ColumnType type)
	{
		return create(label,label,type,true);
	}
	
	
	public static Column create(final String id, final String label, final ColumnType type)
	{
		return create(id,label,type,true);
	}
	
	
	public static Column create(final String id, final String label, final ColumnType type,
			final boolean visible)
	{
		if(visible)
		{
			return new Column(id,label,type);
		}
		else
		{
			return new Column(id,"hidden",type);
		}
	}
	
	
	public static Column StringColumn(final String id, final String label)
	{
		return new Column(id,label,ColumnType.STRING);
	}
	
	
	public static Column NumberColumn(final String id, final String label)
	{
		return new Column(id,label,ColumnType.NUMBER);
	}
	
	
	public static Column DateTimeColumn(final String id, final String label)
	{
		return new Column(id,label,ColumnType.DATETIME);
	}
	
	
	public static Column DateColumn(final String id, final String label)
	{
		return new Column(id,label,ColumnType.DATE);
	}
	
	
	public static Column DataRoleColumn(final ColumnType type, final DataRoleType role)
	{
		return new Column(type,role);
	}
	
	
	public static Column CaptionColumnAsString(final String label)
	{
		return new Column("caption",label,ColumnType.STRING);
	}
	
	
	public static Column CaptionColumnAsNumber(final String label)
	{
		return new Column("caption",label,ColumnType.NUMBER);
	}
	
	
	public static Column CaptionColumnAsDate(final String label)
	{
		return new Column("caption",label,ColumnType.DATE);
	}
	
	
	public static Column CaptionColumnAsDateTime(final String label)
	{
		return new Column("caption",label,ColumnType.DATETIME);
	}
	
	
	public String getId()
	{
		return this.id;
	}
	
	
	public void setId(final String id)
	{
		this.id = id;
	}
	
	
	public String getLabel()
	{
		return this.label;
	}
	
	
	public void setLabel(final String label)
	{
		this.label = label;
	}
	
	
	public String getType()
	{
		return this.type;
	}
	
	
	public void setType(final String type)
	{
		this.type = type;
	}
	
	
	public DataRole getP()
	{
		return this.p;
	}
	
	
	public void setP(final DataRole p)
	{
		this.p = p;
	}
	
}
