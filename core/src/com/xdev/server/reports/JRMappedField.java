/*
 * XDEV BI Suite
 * 
 * Copyright (c) 2011 - 2013, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package com.xdev.server.reports;


import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;


public class JRMappedField implements JRField
{
	
	private final String	name;
	private final JRField	wrappedField;
	

	public JRMappedField(final JRField jrField, final String name)
	{
		this.name = name;
		this.wrappedField = jrField;
	}
	

	@Override
	public String getDescription()
	{
		return this.wrappedField.getDescription();
	}
	

	@Override
	public String getName()
	{
		return this.name;
	}
	

	@Override
	public Class<?> getValueClass()
	{
		return this.wrappedField.getValueClass();
	}
	

	@Override
	public String getValueClassName()
	{
		return this.wrappedField.getValueClassName();
	}
	

	@Override
	public void setDescription(String arg0)
	{
		this.wrappedField.setDescription(arg0);
	}
	

	@Override
	public JRPropertiesHolder getParentProperties()
	{
		return this.wrappedField.getParentProperties();
	}
	

	@Override
	public JRPropertiesMap getPropertiesMap()
	{
		return this.wrappedField.getPropertiesMap();
	}
	

	@Override
	public boolean hasProperties()
	{
		return this.wrappedField.hasProperties();
	}
	

	public Object clone()
	{
		return this.wrappedField.clone();
	}
	
}
