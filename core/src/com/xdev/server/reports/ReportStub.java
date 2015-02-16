
package com.xdev.server.reports;


import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;


public interface ReportStub
{
	public String getJasperFilePath();
	
	
	/**
	 * 
	 * @return a COPY of the parameters
	 */
	public Map<String, Object> getParameters();
	
	
	public void setDataSource(JRDataSource dataSource);
	
	
	public JRDataSource getDataSource();
	
	
	public JasperReport getReportTemplate() throws JRException;
}
