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


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class ColumnsAndRowHelper
{
	
	@SuppressWarnings("unused")
	private static <T> DataTable initColumnsAndData(final Class<T> clazz, final List<T> alldata,
			final DataTable table)
	{

		final Field[] fields = clazz.getDeclaredFields();
		// Columns
		for(int i = 0; i < fields.length; i++)
		{
			final Field field = fields[i];
			
			final Annotation annotation = field.getAnnotation(ColumnCaption.class);
			final Annotation annotation2 = field.getAnnotation(ColumnDataRole.class);

			if(annotation instanceof ColumnCaption)
			{
				final ColumnCaption caption = (ColumnCaption)annotation;
				final String name = caption.name();
				final boolean hidden = caption.hidden();

				if(field.getType().equals(String.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.StringColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.StringColumn(field.getName(),"hidden"));
					}
				}
				if(field.getType().equals(Integer.class) || field.getType().equals(int.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),"hidden"));
					}
				}
				if(field.getType().equals(Date.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.DateColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.DateColumn(field.getName(),"hidden"));
					}
				}
				if(field.getType().equals(Long.class) || field.getType().equals(Double.class)
						|| field.getType().equals(BigDecimal.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),"hidden"));
					}
				}
			}

			if(annotation2 instanceof ColumnDataRole)
			{
				final ColumnDataRole role = (ColumnDataRole)annotation2;

				if(field.getType().equals(String.class))
				{
					if(role.role().equals(DataRoleType.TOOLTIP))
					{
						table.getColumns()
								.add(Column.DataRoleColumn(ColumnType.STRING,role.role()));
					}
					else
					{
						table.getColumns()
								.add(Column.DataRoleColumn(ColumnType.STRING,role.role()));
					}

				}
			}
		}
		// Rows
		for(int i = 0; i < alldata.size(); i++)
		{
			final T value = alldata.get(i);
			
			final ArrayList<Object> insertValue = new ArrayList<>();
			
			for(final Field field : fields)
			{
				field.setAccessible(true);
				
				try
				{
					if(field.getType().equals(Date.class))
					{
						final Date object = (Date)field.get(value);
						if(object != null)
						{
							final Calendar cal = Calendar.getInstance();
							cal.setTime(object);

							final int day = cal.get(Calendar.DAY_OF_MONTH);
							final int month = cal.get(Calendar.MONTH);
							final int year = cal.get(Calendar.YEAR);
							final int hour = cal.get(Calendar.HOUR_OF_DAY);
							final int min = cal.get(Calendar.MINUTE);
							final int y = year - 1900;
							
							final String date = "Date(" + y + ", " + month + ", " + day + ", "
									+ hour + ", " + min + ",0)";
							insertValue.add(date);
						}
						else
						{
							insertValue.add(null);
						}
					}
					else
					{
						final Object object = field.get(value);
						
						insertValue.add(object);
					}
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}

			table.getRows().add(Row.create(insertValue.stream().toArray()));
		}
		
		return table;
	}
	
	
	@SuppressWarnings("unused")
	private static <T> DataTable initColumns(final Class<T> clazz, final DataTable table)
	{

		final Field[] fields = clazz.getDeclaredFields();
		// Columns
		for(int i = 0; i < fields.length; i++)
		{
			final Field field = fields[i];
			
			final Annotation annotation = field.getAnnotation(ColumnCaption.class);
			final Annotation annotation2 = field.getAnnotation(ColumnDataRole.class);

			if(annotation instanceof ColumnCaption)
			{
				final ColumnCaption caption = (ColumnCaption)annotation;
				final String name = caption.name();
				final boolean hidden = caption.hidden();

				if(field.getType().equals(String.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.StringColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.StringColumn(field.getName(),"hidden"));
					}
				}
				if(field.getType().equals(Integer.class) || field.getType().equals(int.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),"hidden"));
					}
				}
				if(field.getType().equals(Date.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.DateColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.DateColumn(field.getName(),"hidden"));
					}
				}
				if(field.getType().equals(Long.class) || field.getType().equals(Double.class)
						|| field.getType().equals(BigDecimal.class))
				{
					if(hidden == false)
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),name));
					}
					else
					{
						table.getColumns().add(Column.NumberColumn(field.getName(),"hidden"));
					}
				}
			}

			if(annotation2 instanceof ColumnDataRole)
			{
				final ColumnDataRole role = (ColumnDataRole)annotation2;

				if(field.getType().equals(String.class))
				{
					if(role.role().equals(DataRoleType.TOOLTIP))
					{
						table.getColumns()
								.add(Column.DataRoleColumn(ColumnType.STRING,role.role()));
					}
					else
					{
						table.getColumns()
								.add(Column.DataRoleColumn(ColumnType.STRING,role.role()));
					}

				}
			}
		}
		System.out.println(table);
		System.out.println(table.getColumns().size());
		return table;
	}
}
