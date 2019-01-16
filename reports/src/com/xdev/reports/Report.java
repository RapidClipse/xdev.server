/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.reports;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Resource;
import com.xdev.res.ApplicationResource;
import com.xdev.ui.entitycomponent.XdevBeanContainer;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


/**
 * @author XDEV Software
 *
 */
public interface Report
{
	public static Report New()
	{
		return new Implementation();
	}
	
	
	public Report jrxml(final InputStream jrxmlInputStream);
	
	
	public Report jrxml(final String jrxmlPath);
	
	
	public Report dataSource(final JRDataSource dataSource);
	
	
	public Report dataSource(Collection<?> beans);
	
	
	public Report dataSource(BeanItemContainer<?> container);
	
	
	public Report dataSource(XdevBeanContainer<?> container);
	
	
	public Report parameters(final Map<String, Object> parameters);
	
	
	public Report parameter(final String name, final Object value);
	
	
	public Report subreport(final String name, final InputStream jrxmlInputStream);
	
	
	public Report subreport(final String name, final String jrxmlPath);
	
	
	public Report mapFields(Map<String, String> fieldNameMapping);
	
	
	public Report mapField(String from, String to);
	
	
	public void export(final ExportType type, final OutputStream stream);
	
	
	public byte[] exportToBytes(final ExportType type);
	
	
	public Resource exportToResource(final ExportType type);
	
	
	public Resource exportToResource(final ExportType type, final String fileNamePrefix);
	
	
	
	public static class Implementation implements Report
	{
		private InputStream					jrxmlInputStream;
		private String						jrxmlPath;
		private JRDataSource				dataSource;
		private final Map<String, Object>	parameters			= new HashMap<>();
		private final Map<String, String>	fieldNameMapping	= new HashMap<>();
		
		
		@Override
		public Report jrxml(final InputStream jrxmlInputStream)
		{
			this.jrxmlInputStream = jrxmlInputStream;
			return this;
		}
		
		
		@Override
		public Report jrxml(final String jrxmlPath)
		{
			this.jrxmlPath = jrxmlPath;
			return this;
		}
		
		
		@Override
		public Report dataSource(final JRDataSource dataSource)
		{
			this.dataSource = dataSource;
			return this;
		}
		
		
		@Override
		public Report dataSource(final Collection<?> beans)
		{
			this.dataSource = new JRBeanCollectionDataSource(beans);
			return this;
		}
		
		
		@Override
		public Report dataSource(final BeanItemContainer<?> container)
		{
			this.dataSource = new BeanItemContainerDataSource(container);
			return this;
		}
		
		
		@Override
		public Report dataSource(final XdevBeanContainer<?> container)
		{
			this.dataSource = new BeanItemContainerDataSource(container);
			return this;
		}
		
		
		@Override
		public Report parameters(final Map<String, Object> parameters)
		{
			this.parameters.putAll(parameters);
			return this;
		}
		
		
		@Override
		public Report parameter(final String name, final Object value)
		{
			this.parameters.put(name,value);
			return this;
		}
		
		
		@Override
		public Report subreport(final String name, final InputStream jrxmlInputStream)
		{
			try
			{
				final JasperReport subreport = JasperCompileManager
						.compileReport(getJrxml(jrxmlInputStream,null));
				this.parameters.put(name,subreport);
				return this;
			}
			catch(final JRException e)
			{
				throw new ReportException(e);
			}
		}
		
		
		@Override
		public Report subreport(final String name, final String jrxmlPath)
		{
			try
			{
				final JasperReport subreport = JasperCompileManager
						.compileReport(getJrxml(null,jrxmlPath));
				this.parameters.put(name,subreport);
				return this;
			}
			catch(final JRException e)
			{
				throw new ReportException(e);
			}
		}
		
		
		@Override
		public Report mapFields(final Map<String, String> fieldNameMapping)
		{
			this.fieldNameMapping.putAll(fieldNameMapping);
			return this;
		}
		
		
		@Override
		public Report mapField(final String from, final String to)
		{
			this.fieldNameMapping.put(from,to);
			return this;
		}
		
		
		@Override
		public void export(final ExportType type, final OutputStream stream)
		{
			type.export(getJrxml(),getDataSource(),parameters,stream);
		}
		
		
		@Override
		public byte[] exportToBytes(final ExportType type)
		{
			return type.exportToBytes(getJrxml(),getDataSource(),parameters);
		}
		
		
		@Override
		public Resource exportToResource(final ExportType type)
		{
			return type.exportToResource(getJrxml(),getDataSource(),parameters);
		}
		
		
		@Override
		public Resource exportToResource(final ExportType type, final String fileNamePrefix)
		{
			return type.exportToResource(getJrxml(),getDataSource(),parameters,fileNamePrefix);
		}
		
		
		protected InputStream getJrxml()
		{
			return getJrxml(this.jrxmlInputStream,this.jrxmlPath);
		}
		
		
		protected InputStream getJrxml(final InputStream jrxmlInputStream, final String jrxmlPath)
		{
			InputStream jrxml = jrxmlInputStream;
			if(jrxml == null)
			{
				if(jrxmlPath == null)
				{
					throw new IllegalStateException(
							"No input specified, provide either a jrxml inputstream or path");
				}
				
				jrxml = new ApplicationResource(getCallerClass(),jrxmlPath).getStreamSource()
						.getStream();
			}
			
			return jrxml;
		}
		
		
		protected Class<?> getCallerClass()
		{
			for(final StackTraceElement element : Thread.currentThread().getStackTrace())
			{
				final String className = element.getClassName();
				if(!className.startsWith("java.") && !className.startsWith("com.xdev.reports."))
				{
					try
					{
						return Class.forName(className);
					}
					catch(final Exception e)
					{
						return null;
					}
				}
			}
			
			return null;
		}
		
		
		protected JRDataSource getDataSource()
		{
			JRDataSource dataSource = this.dataSource;
			if(dataSource == null)
			{
				throw new IllegalStateException("No data source specified");
			}
			if(!this.fieldNameMapping.isEmpty())
			{
				dataSource = MappedDataSource.create(dataSource,fieldNameMapping);
			}
			return dataSource;
		}
	}
}
