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


import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Component;


/**
 * Extension of {@link XdevGridLayout}, use
 * {@link #addComponent(Component, Constraint)} to add components to this
 * layout.
 * <p>
 * Components are placed in a border region or in the center depending on the
 * {@link Constraint} setting.
 *
 * <pre>
 * +---------------------------------------+
 * |                North                  |
 * |---------------------------------------|
 * |      |                         |      |
 * |      |                         |      |
 * | West |         Center          | East |
 * |      |                         |      |
 * |      |                         |      |
 * |---------------------------------------|
 * |                South                  |
 * +---------------------------------------+
 * </pre>
 *
 * If {@link #setMode(Mode)} is set to {@link Mode#WEST_EAST_FULL_HEIGHT} the
 * layout will look like this:
 *
 * <pre>
 * +---------------------------------------+
 * |      |         North           |      |
 * |      |-------------------------|      |
 * |      |                         |      |
 * |      |                         |      |
 * | West |         Center          | East |
 * |      |                         |      |
 * |      |                         |      |
 * |      |-------------------------|      |
 * |      |         South           |      |
 * +---------------------------------------+
 * </pre>
 *
 * @author XDEV Software
 *
 */
@SuppressWarnings("serial")
public class XdevBorderLayout extends XdevGridLayout implements XdevComponent
{
	public static enum Constraint
	{
		/**
		 * The center layout constraint (middle of container).
		 */
		CENTER,

		/**
		 * The north layout constraint (top of container).
		 */
		NORTH,

		/**
		 * The south layout constraint (bottom of container).
		 */
		SOUTH,

		/**
		 * The east layout constraint (right side of container).
		 */
		EAST,

		/**
		 * The west layout constraint (left side of container).
		 */
		WEST
	}



	/**
	 * Mode enumeration for the border layout behavior as it's described in
	 * {@link XdevBorderLayout}.
	 */
	public static enum Mode
	{
		NORTH_SOUTH_FULL_WIDTH,

		WEST_EAST_FULL_HEIGHT
	}

	private final Extensions					extensions	= new Extensions();
	private final Map<Component, Constraint>	components	= new HashMap<>();
	private Mode								mode		= Mode.NORTH_SOUTH_FULL_WIDTH;
	
	
	/**
	 * Creates a new empty border layout.
	 */
	public XdevBorderLayout()
	{
		super(3,3);

		setColumnExpandRatio(1,1f);
		setRowExpandRatio(1,1f);
		setHideEmptyRowsAndColumns(true);
		setMargin(true);
		setSpacing(true);
	}


	/**
	 * @return the mode
	 */
	public Mode getMode()
	{
		return this.mode;
	}


	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(final Mode mode)
	{
		this.mode = mode;

		if(getComponentCount() > 0)
		{
			rebuild();
		}
	}


	protected void rebuild()
	{
		final Map<Component, Constraint> components = new HashMap<>(this.components);
		removeAllComponents();
		for(final Component component : components.keySet())
		{
			addComponent(component,components.get(component));
		}
	}


	public void addComponent(final Component component, final Constraint constraint)
	{
		this.components.put(component,constraint);

		switch(constraint)
		{
			case CENTER:
			{
				addComponent(component,1,1);
				component.setSizeFull();
			}
			break;
			
			case NORTH:
			{
				switch(this.mode)
				{
					case NORTH_SOUTH_FULL_WIDTH:
						addComponent(component,0,0,2,0);
					break;
					
					case WEST_EAST_FULL_HEIGHT:
						addComponent(component,1,0);
					break;
				}
				component.setWidth(100,Unit.PERCENTAGE);
			}
			break;
			
			case SOUTH:
			{
				switch(this.mode)
				{
					case NORTH_SOUTH_FULL_WIDTH:
						addComponent(component,0,2,2,2);
					break;
					
					case WEST_EAST_FULL_HEIGHT:
						addComponent(component,1,2);
					break;
				}
				component.setWidth(100,Unit.PERCENTAGE);
			}
			break;
			
			case WEST:
			{
				switch(this.mode)
				{
					case NORTH_SOUTH_FULL_WIDTH:
						addComponent(component,0,1);
					break;
					
					case WEST_EAST_FULL_HEIGHT:
						addComponent(component,0,0,0,2);
					break;
				}
				component.setHeight(100,Unit.PERCENTAGE);
			}
			break;
			
			case EAST:
			{
				switch(this.mode)
				{
					case NORTH_SOUTH_FULL_WIDTH:
						addComponent(component,2,1);
					break;
					
					case WEST_EAST_FULL_HEIGHT:
						addComponent(component,2,0,2,2);
					break;
				}
				component.setHeight(100,Unit.PERCENTAGE);
			}
			break;
		}
	}


	@Override
	public void removeComponent(final Component component)
	{
		super.removeComponent(component);

		this.components.remove(component);
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
}
