
package com.xdev.server.reports;


import com.xdev.server.lang.CommandObject;


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
	public static void create(CommandObject info) throws ReportException
	{
		info.init();
		info.execute(info);
	}
}
