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

package com.xdev.ui;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;


/**
 * A layout where the components are laid out on a grid using cell coordinates.
 *
 * <p>
 * The GridLayout also maintains a cursor for adding components in
 * left-to-right, top-to-bottom order.
 * </p>
 *
 * <p>
 * Each component in a <code>GridLayout</code> uses a defined
 * {@link GridLayout.Area area} (column1,row1,column2,row2) from the grid. The
 * components may not overlap with the existing components - if you try to do so
 * you will get an {@link OverlapsException}. Adding a component with cursor
 * automatically extends the grid by increasing the grid height.
 * </p>
 *
 * <p>
 * The grid coordinates, which are specified by a row and column index, always
 * start from 0 for the topmost row and the leftmost column.
 * </p>
 *
 * @author XDEV Software
 *		
 */
public class XdevGridLayout extends GridLayout implements XdevComponent
{
	public static enum AutoFill
	{
		BOTH, HORIZONTAL, VERTICAL, NONE
	}
	
	private AutoFill			autoFill	= AutoFill.BOTH;
	private final Extensions	extensions	= new Extensions();
											
											
	/**
	 * Constructs an empty (1x1) grid layout that is extended as needed.
	 */
	public XdevGridLayout()
	{
		super();
	}
	
	
	/**
	 * Constructs a GridLayout of given size (number of columns and rows) and
	 * adds the given components in order to the grid.
	 *
	 * @see #addComponents(Component...)
	 *		
	 * @param columns
	 *            Number of columns in the grid.
	 * @param rows
	 *            Number of rows in the grid.
	 * @param children
	 *            Components to add to the grid.
	 */
	public XdevGridLayout(final int columns, final int rows, final Component... children)
	{
		super(columns,rows,children);
	}
	
	
	/**
	 * Constructor for a grid of given size (number of columns and rows).
	 *
	 * The grid may grow or shrink later. Grid grows automatically if you add
	 * components outside its area.
	 *
	 * @param columns
	 *            Number of columns in the grid.
	 * @param rows
	 *            Number of rows in the grid.
	 */
	public XdevGridLayout(final int columns, final int rows)
	{
		super(columns,rows);
	}
	
	
	// init defaults
	{
		setMargin(true);
		setSpacing(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
	
	
	/**
	 * @param autoFill
	 *            the autoFill to set
	 */
	public void setAutoFill(final AutoFill autoFill)
	{
		this.autoFill = autoFill;
	}
	
	
	/**
	 * @return the autoFill
	 */
	public AutoFill getAutoFill()
	{
		return this.autoFill;
	}
	
	
	/**
	 * Adds the component to the grid in cells column1,row1 (NortWest corner of
	 * the area.) End coordinates (SouthEast corner of the area) are the same as
	 * column1,row1. The coordinates are zero-based. Component width and height
	 * is 1.
	 *
	 * @param component
	 *            the component to be added, not <code>null</code>.
	 * @param column
	 *            the column index, starting from 0.
	 * @param row
	 *            the row index, starting from 0.
	 * @param alignment
	 *            the Alignment value to be set
	 * @throws OverlapsException
	 *             if the new component overlaps with any of the components
	 *             already in the grid.
	 * @throws OutOfBoundsException
	 *             if the cell is outside the grid area.
	 */
	public void addComponent(final Component component, final int column, final int row,
			final Alignment alignment) throws OverlapsException, OutOfBoundsException
	{
		addComponent(component,column,row);
		setComponentAlignment(component,alignment);
	}
	
	
	/**
	 * <p>
	 * Adds a component to the grid in the specified area. The area is defined
	 * by specifying the upper left corner (column1, row1) and the lower right
	 * corner (column2, row2) of the area. The coordinates are zero-based.
	 * </p>
	 *
	 * <p>
	 * If the area overlaps with any of the existing components already present
	 * in the grid, the operation will fail and an {@link OverlapsException} is
	 * thrown.
	 * </p>
	 *
	 * @param component
	 *            the component to be added, not <code>null</code>.
	 * @param column1
	 *            the column of the upper left corner of the area <code>c</code>
	 *            is supposed to occupy. The leftmost column has index 0.
	 * @param row1
	 *            the row of the upper left corner of the area <code>c</code> is
	 *            supposed to occupy. The topmost row has index 0.
	 * @param column2
	 *            the column of the lower right corner of the area
	 *            <code>c</code> is supposed to occupy.
	 * @param row2
	 *            the row of the lower right corner of the area <code>c</code>
	 *            is supposed to occupy.
	 * @param alignment
	 *            the Alignment value to be set
	 * @throws OverlapsException
	 *             if the new component overlaps with any of the components
	 *             already in the grid.
	 * @throws OutOfBoundsException
	 *             if the cells are outside the grid area.
	 */
	public void addComponent(final Component component, final int column1, final int row1,
			final int column2, final int row2, final Alignment alignment)
					throws OverlapsException, OutOfBoundsException
	{
		addComponent(component,column1,row1,column2,row2);
		setComponentAlignment(component,alignment);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(final Component component, final int column1, final int row1,
			final int column2, final int row2) throws OverlapsException, OutOfBoundsException
	{
		// auto-grow
		if(getColumns() <= column2)
		{
			setColumns(column2 + 1);
		}
		if(getRows() <= row2)
		{
			setRows(row2 + 1);
		}
		
		super.addComponent(component,column1,row1,column2,row2);
	}
	
	
	public void setColumnExpandRatios(final float... ratios)
	{
		for(int column = 0; column < ratios.length; column++)
		{
			setColumnExpandRatio(column,ratios[column]);
		}
	}
	
	
	public void setRowExpandRatios(final float... ratios)
	{
		for(int row = 0; row < ratios.length; row++)
		{
			setRowExpandRatio(row,ratios[row]);
		}
	}
	
	
	public void addSpacer()
	{
		addSpacer(this.autoFill == AutoFill.BOTH || this.autoFill == AutoFill.HORIZONTAL,
				this.autoFill == AutoFill.BOTH || this.autoFill == AutoFill.VERTICAL);
	}
	
	
	public void addSpacer(final boolean horizontal, final boolean vertical)
	{
		final int columnCount = getColumns();
		final int rowCount = getRows();
		
		if(!hasExpandingColumn())
		{
			addComponent(new Spacer(),columnCount,0,columnCount,rowCount - 1);
			setColumnExpandRatio(columnCount,1f);
		}
		
		if(!hasExpandingRow())
		{
			addComponent(new Spacer(),0,rowCount,columnCount - 1,rowCount);
			setRowExpandRatio(rowCount,1f);
		}
	}
	
	
	public boolean hasExpandingColumn()
	{
		for(int column = 0, columnCount = getColumns(); column < columnCount; column++)
		{
			if(getColumnExpandRatio(column) > 0f)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public boolean hasExpandingRow()
	{
		for(int row = 0, rowCount = getRows(); row < rowCount; row++)
		{
			if(getRowExpandRatio(row) > 0f)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	
	protected static class Spacer extends CustomComponent
	{
		public Spacer()
		{
			setSizeFull();
		}
	}
}
