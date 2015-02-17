
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
	public static ReportCreator create(ReportCreator info) throws ReportException
	{
		info.init();
		info.execute(info);
		return info;
	}
}
