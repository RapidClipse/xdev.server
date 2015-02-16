
package com.xdev.server.reports;


import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class JRDataSourceMapper implements JRDataSource
{
	
	private final JRDataSource			wrappedDataSource;
	private final Map<String, String>	fieldMapping;
	

	public JRDataSourceMapper(final JRDataSource dataSource, final Map<String, String> fieldMapping)
	{
		this.wrappedDataSource = dataSource;
		this.fieldMapping = fieldMapping;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean next() throws JRException
	{
		return this.wrappedDataSource.next();
	}
	

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public Object getFieldValue(final JRField jrField) throws JRException
	{		
		String mappedName = this.fieldMapping.get(jrField.getName());
		
		JRField field = jrField;
		if(mappedName != null)
		{
			field = new JRMappedField(jrField,mappedName);
		}
		
		return this.wrappedDataSource.getFieldValue(field);
	}
	
}
