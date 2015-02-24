/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui;


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
}
