/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout.OutOfBoundsException;
import com.vaadin.ui.GridLayout.OverlapsException;
import com.vaadin.ui.VerticalLayout;


/**
 * Vertical layout
 *
 * <code>VerticalLayout</code> is a component container, which shows the
 * subcomponents in the order of their addition (vertically). A vertical layout
 * is by default 100% wide.
 *
 * @author XDEV Software
 *
 */
public class XdevVerticalLayout extends VerticalLayout
{
	/**
	 * Constructs an empty VerticalLayout.
	 */
	public XdevVerticalLayout()
	{
		super();
	}
	
	
	/**
	 * Constructs a VerticalLayout with the given components. The components are
	 * added in the given order.
	 * 
	 * @see AbstractOrderedLayout#addComponents(Component...)
	 * 
	 * @param children
	 *            The components to add.
	 */
	public XdevVerticalLayout(final Component... children)
	{
		super(children);
	}

	// init defaults
	{
		setMargin(true);
		setSpacing(true);
	}
	
	
	/**
	 * Add a component into this container. The component is added to the right
	 * or under the previous component.
	 *
	 * @param component
	 *            the component to be added.
	 * @param alignment
	 *            the Alignment value to be set
	 */
	public void addComponent(final Component component, final Alignment alignment)
			throws OverlapsException, OutOfBoundsException
	{
		addComponent(component);
		setComponentAlignment(component,alignment);
	}
	
	
	public void setExpandRatios(final float... ratios)
	{
		for(int i = 0; i < ratios.length; i++)
		{
			setExpandRatio(getComponent(i),ratios[i]);
		}
	}
	
	
	public void addSpacer()
	{
		if(!hasExpandingComponent())
		{
			final Spacer spacer = new Spacer();
			addComponent(spacer);
			setExpandRatio(spacer,1f);
		}
	}
	
	
	public boolean hasExpandingComponent()
	{
		for(int column = 0, columnCount = getComponentCount(); column < columnCount; column++)
		{
			if(getExpandRatio(getComponent(column)) > 0f)
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
