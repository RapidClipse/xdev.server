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

package com.xdev.charts;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.xml.bind.DatatypeConverter;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

import elemental.json.JsonArray;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
@JavaScript({"https://www.gstatic.com/charts/loader.js","xdev-chart-utils.js"})
public abstract class AbstractXdevChart extends AbstractJavaScriptComponent implements XdevChart
{
	private static final long						serialVersionUID	= 1L;

	private final Extensions						extensions			= new Extensions();

	private String									divId				= null;
	private byte[]									chart_image			= null;
	private final Map<String, Consumer<byte[]>>		getPrintCalls		= new HashMap<>();
	private final ArrayList<ValueChangeListener>	listeners			= new ArrayList<>();


	public AbstractXdevChart()
	{
		addFunction("select",this::select);
		addFunction("divId",this::addDivId);
		addFunction("print_success",this::print_success);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}


	public void getPrint(final Consumer<byte[]> successCallback)
	{
		if(this.divId != null)
		{
			com.vaadin.ui.JavaScript.getCurrent()
					.execute("$(" + this.divId + ").trigger('printImage');");

			this.getPrintCalls.put("1",successCallback);
		}
	}


	private void print_success(final JsonArray arguments)
	{
		if(arguments.length() == 1)
		{
			final Consumer<byte[]> consumer = this.getPrintCalls.get("1");

			final String base64Image = arguments.getString(0).split(",")[1];

			this.chart_image = DatatypeConverter.parseBase64Binary(base64Image);

			consumer.accept(this.chart_image);
		}
	}


	private void addDivId(final JsonArray arguments)
	{
		if(arguments.length() == 1)
		{
			this.divId = arguments.getString(0);
		}
	}



	public interface ValueChangeListener extends Serializable
	{
		void valueChange(final String json);
	}


	public void addValueChangeListener(final ValueChangeListener listener)
	{
		this.listeners.add(listener);
	}


	public void select(final JsonArray arguments)
	{
		final int length = arguments.toJson().length();
		final String json = arguments.toJson().substring(1,length - 1);
		
		this.listeners.forEach(listener -> {
			listener.valueChange(json);
		});
	}
}
