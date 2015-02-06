/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
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
public class XdevBorderLayout extends XdevGridLayout
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
}
