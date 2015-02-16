
package com.xdev.server.reports;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


public abstract class AbstractReportStub implements ReportStub
{
	private JRDataSource		dataSource	= null;
	private String				jasperFilePath;
	private List<ReportStub>	subreports	= new ArrayList<ReportStub>();
	
	
	@Override
	public void setDataSource(JRDataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	
	@Override
	public JRDataSource getDataSource()
	{
		return dataSource;
	}
	
	
	public void setJasperFilePath(String jasperFilePath)
	{
		this.jasperFilePath = jasperFilePath;
	}
	
	
	@Override
	public String getJasperFilePath()
	{
		return jasperFilePath;
	}
	
	
	public void addSubreport(ReportStub subreport)
	{
		subreports.add(subreport);
	}
	
	
	@Override
	public JasperReport getReportTemplate() throws JRException
	{
		return (JasperReport)JRLoader.loadObject(new File(jasperFilePath));
	}
	
	
	@Override
	public Map<String, Object> getParameters()
	{
		Map<String, Object> parameters = new HashMap<String, Object>()
		{
			private static final long	serialVersionUID	= -1993902777738483071L;
			
			
			@Override
			public Object put(String key, Object value)
			{
				if(value != null)
				{
					return super.put(key,value);
				}
				return null;
			}
		};
		
		for(ReportStub subreport : this.subreports)
		{
			parameters.putAll(subreport.getParameters());
		}
		
		collectParameters(parameters);
		
		return parameters;
	}
	
	
	protected abstract void collectParameters(Map<String, Object> map);
}
