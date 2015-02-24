
package com.xdev.server.util.wizard;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.xdev.server.reports.JRDataSourceMapper;
import com.xdev.server.reports.ReportBuilder;
import com.xdev.server.reports.ReportException;


public class EntityReportCreator extends AbstractReportCreator implements ReportCreator
{
	
	public EntityReportCreator(Object... args)
	{
		// super(args);
	}
	
	
	protected JRDataSource createDataSource(Collection<?> beanCollection,
			boolean isUseFieldDescription)
	{
		JRDataSource dataSource = new JRBeanCollectionDataSource(beanCollection,
				isUseFieldDescription);
		return dataSource;
	}
	
	
	protected JRDataSource createDataSource(Collection<?> beanCollection,
			boolean isUseFieldDescription, String... mapping)
	{
		JRDataSource dataSource = new JRBeanCollectionDataSource(beanCollection,
				isUseFieldDescription);
		
		if(mapping != null && mapping.length > 0)
		{
			Map<String, String> fieldMapping = new HashMap<String, String>();
			for(int i = 0; i < mapping.length;)
			{
				fieldMapping.put(mapping[i++],mapping[i++]);
			}
			return new JRDataSourceMapper(dataSource,fieldMapping);
		}
		
		return dataSource;
	}
	
	
	public void execute()
	{
		try
		{
			ReportBuilder builder = new ReportBuilder(this.getReportStub());
			
			switch(this.getTarget())
			{
				case PREVIEW:
				{
					builder.previewReport();
				}
				break;
				
				case PRINT:
				{
					builder.print(this.getDisplayPageDialog(),this.getDisplayPageDialogOnlyOnce(),
							this.getDisplayPrintDialog(),this.getDisplayPrintDialogOnlyOnce());
				}
				break;
				
				case FILE:
				case STREAM:
				{
					if(this.getTarget() == Target.FILE)
					{
						this.setOutputStream(new FileOutputStream(this.getFile()));
					}
					OutputStream outputStream = this.getOutputStream();
					try
					{
						switch(this.getOutputFormat())
						{
							case PDF:
							{
								builder.writePdfFile(outputStream);
							}
							break;
							
							case HMTL:
							{
								builder.writeHtmlFile(outputStream);
							}
							break;
							
							case EXCEL:
							{
								builder.writeExcelFile(outputStream);
							}
							break;
							
							case RTF:
							{
								builder.writeRtfFile(outputStream);
							}
							break;
							
							case CSV:
							{
								builder.writeCsvFile(outputStream);
							}
							break;
							
							case XML:
							{
								builder.writeXmlFile(outputStream);
							}
							break;
							
							case TEXT:
							{
								builder.writeTextFile(outputStream);
							}
							break;
						}
					}
					finally
					{
						outputStream.close();
					}
					
				}
				break;
			}
		}
		catch(IOException e)
		{
			throw new ReportException(e);
		}
		catch(JRException e)
		{
			throw new ReportException(e);
		}
	}
}
