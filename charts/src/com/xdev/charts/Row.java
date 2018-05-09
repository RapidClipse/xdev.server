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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class Row implements Serializable
{
	private List<Value> c = new ArrayList<>();
	
	
	public Row()
	{
	};
	
	
	public static Row create(final Object... v)
	{

		final Row row = new Row();

		for(int i = 0; i < v.length; i++)
		{
			row.c.add(new Value(v[i]));
		}

		return row;
	}
	
	
	public static Row create(final MultiValue v)
	{
		final Row row = new Row();

		v.getV().forEach(value -> {
			row.c.add(value);
		});

		return row;
	}
	
	
	public static List<Row> createFromHashmap(
			final LinkedHashMap<Object, LinkedHashMap<String, Object>> values)
	{
		final Set<Object> keySet = values.keySet();
		final List<Row> rows = new ArrayList<>();

		keySet.forEach(key -> {
			final Row row = new Row();

			row.c.add(new Value(key));

			final HashMap<String, Object> hashMap = values.get(key);

			hashMap.forEach((k, value) -> {
				if(value instanceof Object[])
				{
					final Object[] rowValues = (Object[])value;
					// [0] = Data value [1] = DataRole value
					row.c.add(new Value(rowValues[0]));
					row.c.add(new Value(rowValues[1]));
				}
				else
				{
					row.c.add(new Value(value));
				}

			});

			rows.add(row);
		});

		return rows;
	}
	
	
	public List<Value> getC()
	{
		return this.c;
	}
	
	
	public void setC(final List<Value> c)
	{
		this.c = c;
	}
}
