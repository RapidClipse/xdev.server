/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;


/**
 * TabSheet component.
 *
 * Tabs are typically identified by the component contained on the tab (see
 * {@link ComponentContainer}), and tab metadata (including caption, icon,
 * visibility, enabledness, closability etc.) is kept in separate {@link Tab}
 * instances.
 *
 * Tabs added with {@link #addComponent(Component)} get the caption and the icon
 * of the component at the time when the component is created, and these are not
 * automatically updated after tab creation.
 *
 * A tab sheet can have multiple tab selection listeners and one tab close
 * handler ({@link CloseHandler}), which by default removes the tab from the
 * TabSheet.
 *
 * The {@link TabSheet} can be styled with the .v-tabsheet, .v-tabsheet-tabs and
 * .v-tabsheet-content styles. Themes may also have pre-defined variations of
 * the tab sheet presentation, such as {@link Reindeer#TABSHEET_BORDERLESS},
 * {@link Runo#TABSHEET_SMALL} and several other styles in {@link Reindeer}.
 *
 * The current implementation does not load the tabs to the UI before the first
 * time they are shown, but this may change in future releases.
 *
 * @author XDEV Software
 *
 */
public class XdevTabSheet extends TabSheet
{
	/**
	 * Constructs a new TabSheet. A TabSheet is immediate by default, and the
	 * default close handler removes the tab being closed.
	 */
	public XdevTabSheet()
	{
		super();
	}
	
	
	/**
	 * Constructs a new TabSheet containing the given components.
	 * 
	 * @param components
	 *            The components to add to the tab sheet. Each component will be
	 *            added to a separate tab.
	 */
	public XdevTabSheet(final Component... components)
	{
		super(components);
	}
	
	
	/**
	 * Sets the selected index for this tabbedpane. The index must be a valid
	 * tab index or -1, which indicates that no tab should be selected (can also
	 * be used when there are no tabs in the tabbedpane). If a -1 value is
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


	public Tab addTab(final Component tabComponent, final boolean closable)
	{
		return addTab(tabComponent,tabComponent.getCaption(),tabComponent.getIcon(),
				getComponentCount(),closable);
	}
	
	
	public Tab addTab(final Component tabComponent, final String caption, final Resource icon,
			final int position, final boolean closable)
	{
		final Tab tab = addTab(tabComponent,caption,icon,position);
		tab.setClosable(closable);
		return tab;
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
