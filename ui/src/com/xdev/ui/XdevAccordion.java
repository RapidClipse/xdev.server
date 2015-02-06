/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.server.Resource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;


/**
 * An accordion is a component similar to a {@link XdevTabSheet}, but with a
 * vertical orientation and the selected component presented between tabs.
 *
 * Closable tabs are not supported by the accordion.
 *
 * The {@link Accordion} can be styled with the .v-accordion, .v-accordion-item,
 * .v-accordion-item-first and .v-accordion-item-caption styles.
 *
 * @author XDEV Software
 *
 */
public class XdevAccordion extends Accordion
{
	/**
	 * Creates an empty accordion.
	 */
	public XdevAccordion()
	{
		super();
	}
	
	
	/**
	 * Constructs a new accordion containing the given components.
	 * 
	 * @param components
	 *            The components to add to the accordion. Each component will be
	 *            added to a separate tab.
	 */
	public XdevAccordion(final Component... components)
	{
		super(components);
	}
	
	
	/**
	 * Sets the selected index for this accordion. The index must be a valid tab
	 * index or -1, which indicates that no tab should be selected (can also be
	 * used when there are no tabs in the tabbedpane). If a -1 value is
	 * specified when the tabbedpane contains one or more tabs, then the results
	 * will be implementation defined.
	 *
	 * @param index
	 *            the index to be selected
	 */
	public void setSelectedIndex(final int index)
	{
		super.setSelectedTab(index);
	}
	
	
	/**
	 * Returns the currently selected index for this tabbedpane. Returns -1 if
	 * there is no currently selected tab.
	 *
	 * @return the index of the selected tab
	 * @see #setSelectedIndex
	 */
	public int getSelectedIndex()
	{
		final Component selectedTab = getSelectedTab();
		if(selectedTab != null)
		{
			return getTabPosition(getTab(selectedTab));
		}
		return -1;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tab addTab(final Component tabComponent, final String caption, final Resource icon,
			final int position)
	{
		final Tab tab = super.addTab(tabComponent,caption,icon,position);
		tab.setDescription(tabComponent.getDescription());
		tab.setEnabled(tabComponent.isEnabled());
		tab.setVisible(tabComponent.isVisible());
		tab.setStyleName(tabComponent.getStyleName());
		return tab;
	}
}
