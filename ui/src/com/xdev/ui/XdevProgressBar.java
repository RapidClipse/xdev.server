/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.ui.ProgressBar;


/**
 * Shows the current progress of a long running task.
 * <p>
 * The default mode is to show the current progress internally represented by a
 * floating point value between 0 and 1 (inclusive). The progress bar can also
 * be in an indeterminate mode showing an animation indicating that the task is
 * running but without providing any information about the current progress.
 *
 * @author XDEV Software
 *
 */
public class XdevProgressBar extends ProgressBar
{
	/**
	 * Creates a new progress bar initially set to 0% progress.
	 */
	public XdevProgressBar()
	{
		super();
	}
	
	
	/**
	 * Creates a new progress bar with the given initial value.
	 * 
	 * @param progress
	 *            the initial progress value
	 */
	public XdevProgressBar(final float progress)
	{
		super(progress);
	}
	
	
	/**
	 * Creates a new progress bar bound to the given data source.
	 * 
	 * @param dataSource
	 *            the property to bind this progress bar to
	 */
	public XdevProgressBar(final Property<?> dataSource)
	{
		super(dataSource);
	}
}
