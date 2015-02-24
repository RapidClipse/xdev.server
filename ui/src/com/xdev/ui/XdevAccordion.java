/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui;


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
}
