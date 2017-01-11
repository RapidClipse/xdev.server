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

package com.xdev.ui;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.gridlayout.GridLayoutState.ChildComponentData;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.declarative.DesignAttributeHandler;
import com.vaadin.ui.declarative.DesignContext;


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

	/*
	 ***************************************************************************
	 * Workaround for a bug in com.vaadin.ui.GridLayout#readDesign. It sets rows
	 * and columns always, this causes problems if the layout already contains
	 * children (subclass which adds children by default). See XWS-1170
	 *************************************************************************/

	private boolean readingDesign = false;


	@Override
	public void readDesign(final Element design, final DesignContext designContext)
	{
		try
		{
			this.readingDesign = true;

			super.readDesign(design,designContext);
		}
		finally
		{
			this.readingDesign = false;
		}
	}


	@Override
	public void setRows(final int rows)
	{
		try
		{
			super.setRows(rows);
		}
		catch(final RuntimeException e)
		{
			if(!this.readingDesign)
			{
				throw e;
			}
		}
	}


	@Override
	public void setColumns(final int columns)
	{
		try
		{
			super.setColumns(columns);
		}
		catch(final RuntimeException e)
		{
			if(!this.readingDesign)
			{
				throw e;
			}
		}
	}


	/*
	 ***************************************************************************
	 * Workaround for a bug in com.vaadin.ui.GridLayout#writeDesign. Spans cause
	 * problems resulting in an ArrayIndexOutOfBoundsException. XWS-1177
	 *************************************************************************/

	@Override
	public void writeDesign(final Element design, final DesignContext designContext)
	{
		AbstractComponent_writeDesign(design,designContext);
		GridLayout_writeDesign(design,designContext);
	}


	private void GridLayout_writeDesign(final Element design, final DesignContext designContext)
	{
		final GridLayout def = designContext.getDefaultInstance(this);

		writeMargin(design,getMargin(),def.getMargin(),designContext);

		if(!designContext.shouldWriteChildren(this,def))
		{
			return;
		}

		final List<Component> components = new ArrayList<>();
		final Iterator<Component> it = iterator();
		while(it.hasNext())
		{
			components.add(it.next());
		}

		if(components.isEmpty())
		{
			writeEmptyColsAndRows(design,designContext);
			return;
		}

		final Map<Connector, ChildComponentData> childData = getState().childData;

		// Make a 2D map of component areas.
		final Component[][] componentMap = new Component[getState().rows][getState().columns];
		final Component dummyComponent = new Label("");

		for(final Component component : components)
		{
			final ChildComponentData coords = childData.get(component);
			for(int row = coords.row1; row <= coords.row2; ++row)
			{
				for(int col = coords.column1; col <= coords.column2; ++col)
				{
					componentMap[row][col] = component;
				}
			}
		}

		// Go through the map and write only needed column tags
		final Set<Connector> visited = new HashSet<Connector>();

		// Skip the dummy placeholder
		visited.add(dummyComponent);

		for(int i = 0; i < componentMap.length; ++i)
		{
			final Element row = design.appendElement("row");

			// Row Expand
			DesignAttributeHandler.writeAttribute("expand",row.attributes(),
					(int)getRowExpandRatio(i),0,int.class);

			int colspan = 1;
			Element col;
			for(int j = 0; j < componentMap[i].length; ++j)
			{
				final Component child = componentMap[i][j];
				if(child != null)
				{
					if(visited.contains(child))
					{
						// Child has already been written in the design
						continue;
					}
					visited.add(child);

					final Element childElement = designContext.createElement(child);
					col = row.appendElement("column");

					// Write child data into design
					final ChildComponentData coords = childData.get(child);

					final Alignment alignment = getComponentAlignment(child);
					DesignAttributeHandler.writeAlignment(childElement,alignment);

					col.appendChild(childElement);
					if(coords.row1 != coords.row2)
					{
						col.attr("rowspan","" + (1 + coords.row2 - coords.row1));
					}

					colspan = 1 + coords.column2 - coords.column1;
					if(colspan > 1)
					{
						col.attr("colspan","" + colspan);
					}

				}
				else
				{
					boolean hasExpands = false;
					if(i == 0 && lastComponentOnRow(componentMap[i],j,visited))
					{
						// A column with expand and no content in the end of
						// first row needs to be present.
						for(int c = j; c < componentMap[i].length; ++c)
						{
							if((int)getColumnExpandRatio(c) > 0)
							{
								hasExpands = true;
							}
						}
					}

					if(lastComponentOnRow(componentMap[i],j,visited) && !hasExpands)
					{
						continue;
					}

					// Empty placeholder tag.
					col = row.appendElement("column");

					// Use colspan to make placeholders more pleasant
					while(j + colspan < componentMap[i].length
							&& componentMap[i][j + colspan] == child)
					{
						++colspan;
					}

					final int rowspan = getRowSpan(componentMap,i,j,colspan,child);
					if(colspan > 1)
					{
						col.attr("colspan","" + colspan);
					}
					if(rowspan > 1)
					{
						col.attr("rowspan","" + rowspan);
					}
					for(int x = 0; x < rowspan; ++x)
					{
						for(int y = 0; y < colspan; ++y)
						{
							// Mark handled columns
							try
							{
								componentMap[i + x][j + y] = dummyComponent;
							}
							catch(final ArrayIndexOutOfBoundsException e)
							{
								// swallow (XWS-1177)
							}
						}
					}
				}

				// Column expands
				if(i == 0)
				{
					// Only do expands on first row
					String expands = "";
					boolean expandRatios = false;
					for(int c = 0; c < colspan; ++c)
					{
						final int colExpand = (int)getColumnExpandRatio(j + c);
						if(colExpand > 0)
						{
							expandRatios = true;
						}
						expands += (c > 0 ? "," : "") + colExpand;
					}
					if(expandRatios)
					{
						col.attr("expand",expands);
					}
				}

				j += colspan - 1;
			}
		}
	}


	private void writeEmptyColsAndRows(final Element design, final DesignContext designContext)
	{
		final int rowCount = getState(false).rows;
		final int colCount = getState(false).columns;

		// only write cols and rows tags if size is not 1x1
		if(rowCount == 1 && colCount == 1)
		{
			return;
		}

		for(int i = 0; i < rowCount; i++)
		{
			final Element row = design.appendElement("row");
			for(int j = 0; j < colCount; j++)
			{
				row.appendElement("column");
			}
		}

	}


	private int getRowSpan(final Component[][] compMap, final int i, final int j, final int colspan,
			final Component child)
	{
		int rowspan = 1;
		while(i + rowspan < compMap.length && compMap[i + rowspan][j] == child)
		{
			for(int k = 0; k < colspan; ++k)
			{
				if(compMap[i + rowspan][j + k] != child)
				{
					return rowspan;
				}
			}
			rowspan++;
		}
		return rowspan;
	}


	private boolean lastComponentOnRow(final Component[] componentArray, int j,
			final Set<Connector> visited)
	{
		while((++j) < componentArray.length)
		{
			final Component child = componentArray[j];
			if(child != null && !visited.contains(child))
			{
				return false;
			}
		}
		return true;
	}


	private void AbstractComponent_writeDesign(final Element design,
			final DesignContext designContext)
	{
		final AbstractComponent def = designContext.getDefaultInstance(this);
		final Attributes attr = design.attributes();
		// handle default attributes
		for(final String attribute : getDefaultAttributes())
		{
			DesignAttributeHandler.writeAttribute(this,attribute,attr,def);
		}
		// handle immediate
		final Boolean explicitImmediateValue = getExplicitImmediateValue();
		if(explicitImmediateValue != null)
		{
			DesignAttributeHandler.writeAttribute("immediate",attr,explicitImmediateValue,
					def.isImmediate(),Boolean.class);
		}
		// handle locale
		if(getLocale() != null
				&& (getParent() == null || !getLocale().equals(getParent().getLocale())))
		{
			design.attr("locale",getLocale().toString());
		}
		// handle size
		writeSize(attr,def);
		// handle component error
		final String errorMsg = getComponentError() != null
				? getComponentError().getFormattedHtmlMessage() : null;
		final String defErrorMsg = def.getComponentError() != null
				? def.getComponentError().getFormattedHtmlMessage() : null;
		if(!SharedUtil.equals(errorMsg,defErrorMsg))
		{
			attr.put("error",errorMsg);
		}
		// handle tab index
		if(this instanceof Focusable)
		{
			DesignAttributeHandler.writeAttribute("tabindex",attr,((Focusable)this).getTabIndex(),
					((Focusable)def).getTabIndex(),Integer.class);
		}
		// handle custom attributes
		final Map<String, String> customAttributes = designContext.getCustomAttributes(this);
		if(customAttributes != null)
		{
			for(final Entry<String, String> entry : customAttributes.entrySet())
			{
				attr.put(entry.getKey(),entry.getValue());
			}
		}
	}


	private Collection<String> getDefaultAttributes()
	{
		final Collection<String> attributes = DesignAttributeHandler
				.getSupportedAttributes(this.getClass());
		attributes.removeAll(getCustomAttributes());
		return attributes;
	}


	private void writeSize(final Attributes attributes, final Component defaultInstance)
	{
		if(hasEqualSize(defaultInstance))
		{
			// we have default values -> ignore
			return;
		}
		final boolean widthFull = getWidth() == 100f
				&& getWidthUnits().equals(Sizeable.Unit.PERCENTAGE);
		final boolean heightFull = getHeight() == 100f
				&& getHeightUnits().equals(Sizeable.Unit.PERCENTAGE);
		final boolean widthAuto = getWidth() == -1;
		final boolean heightAuto = getHeight() == -1;

		// first try the full shorthands
		if(widthFull && heightFull)
		{
			attributes.put("size-full",true);
		}
		else if(widthAuto && heightAuto)
		{
			attributes.put("size-auto",true);
		}
		else
		{
			// handle width
			if(!hasEqualWidth(defaultInstance))
			{
				if(widthFull)
				{
					attributes.put("width-full",true);
				}
				else if(widthAuto)
				{
					attributes.put("width-auto",true);
				}
				else
				{
					final String widthString = DesignAttributeHandler.getFormatter()
							.format(getWidth()) + getWidthUnits().getSymbol();
					attributes.put("width",widthString);

				}
			}
			if(!hasEqualHeight(defaultInstance))
			{
				// handle height
				if(heightFull)
				{
					attributes.put("height-full",true);
				}
				else if(heightAuto)
				{
					attributes.put("height-auto",true);
				}
				else
				{
					final String heightString = DesignAttributeHandler.getFormatter()
							.format(getHeight()) + getHeightUnits().getSymbol();
					attributes.put("height",heightString);
				}
			}
		}
	}


	private boolean hasEqualWidth(final Component component)
	{
		return getWidth() == component.getWidth()
				&& getWidthUnits().equals(component.getWidthUnits());
	}


	private boolean hasEqualHeight(final Component component)
	{
		return getHeight() == component.getHeight()
				&& getHeightUnits().equals(component.getHeightUnits());
	}


	private boolean hasEqualSize(final Component component)
	{
		return hasEqualWidth(component) && hasEqualHeight(component);
	}
}
