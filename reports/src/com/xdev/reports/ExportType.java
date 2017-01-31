/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;


public enum ExportType
{
	XML("xml", "application/xml", (builder, stream) -> builder.toXml(stream)),
	HTML("html", "application/html", (builder, stream) -> builder.toHtml(stream)),
	CSV("csv", "text/comma-separated-values", (builder, stream) -> builder.toCsv(stream)),
	PDF("pdf", "application/pdf", (builder, stream) -> builder.toPdf(stream)),
	RTF("rtf", "application/rtf", (builder, stream) -> builder.toRtf(stream)),
	DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
			(builder, stream) -> builder.toDocx(stream)),
	EXCEL("xlsx", "application/x-xls", (builder, stream) -> builder.toXlsx(stream)),
	ODT("odt", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
			(builder, stream) -> builder.toOdt(stream));

	@FunctionalInterface
	private static interface Exporter
	{
		public void export(JasperReportBuilder builder, OutputStream stream) throws DRException;
	}

	private final String	fileSuffix;
	private final String	mimeType;
	private final Exporter	exporter;


	private ExportType(final String fileSuffix, final String mimeType, final Exporter exporter)
	{
		this.fileSuffix = fileSuffix;
		this.mimeType = mimeType;
		this.exporter = exporter;
	}


	/**
	 * @return the fileSuffix
	 */
	public String getFileSuffix()
	{
		return this.fileSuffix;
	}


	/**
	 * @return the mimeType
	 */
	public String getMimeType()
	{
		return this.mimeType;
	}


	public Resource exportToResource(final JasperReportBuilder reportBuilder) throws DRException
	{
		return exportToResource(reportBuilder,"report" + System.currentTimeMillis());
	}


	public Resource exportToResource(final JasperReportBuilder reportBuilder, final String fileName)
			throws DRException
	{
		final byte[] bytes = exportToBytes(reportBuilder);
		final StreamResource.StreamSource source = () -> new ByteArrayInputStream(bytes);
		final StreamResource resource = new StreamResource(source,fileName + "." + this.fileSuffix);
		resource.setMIMEType(this.mimeType);
		return resource;
	}


	public byte[] exportToBytes(final JasperReportBuilder reportBuilder) throws DRException
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		export(reportBuilder,stream);
		return stream.toByteArray();
	}


	public void export(final JasperReportBuilder reportBuilder, final OutputStream stream)
			throws DRException
	{
		this.exporter.export(reportBuilder,stream);
	}
}
