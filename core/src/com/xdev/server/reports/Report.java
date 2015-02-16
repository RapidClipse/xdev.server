/*
 * XDEV BI Suite
 * 
 * Copyright (c) 2011 - 2013, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */

package com.xdev.server.reports;


public final class Report
{
	/**
	 * No instanciation.
	 */
	private Report()
	{
	}
	
	
	/**
	 * Creates a report and shows it on the screen or sends it to the printer.
	 * 
	 * @param data
	 *            The {@link EntityReportCreator} containing the report content
	 */
	public static void Create(ReportCreator data) throws ReportException
	{
		data.init();
		data.execute(data);
	}
}
