/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;

import net.sf.dynamicreports.design.transformation.StyleResolver;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.style.DRFont;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;


public enum ExportType
{
	PDF("pdf", "application/pdf", (builder, stream) -> builder.toPdf(stream),
			(print, stream) -> JasperExportManager.exportReportToPdfStream(print,stream)),
	XML("xml", "text/xml", (builder, stream) -> builder.toXml(stream),
			(print, stream) -> JasperExportManager.exportReportToXmlStream(print,stream)),
	HTML("html", "text/html", (builder, stream) -> builder.toHtml(stream), (print, stream) -> {
		final HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleHtmlExporterOutput(stream));
		exporter.exportReport();
	}),
	TEXT("txt", "text/plain", (builder, stream) -> builder.toText(stream), (print, stream) -> {
		final JRTextExporter exporter = new JRTextExporter(
				DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
		final SimpleTextReportConfiguration configuration = new SimpleTextReportConfiguration();
		final DRFont font = Defaults.getDefaults().getFont();
		configuration.setCharWidth(new Float(StyleResolver.getFontWidth(font)));
		configuration.setCharHeight(new Float(StyleResolver.getFontHeight(font)));
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}),
	RTF("rtf", "text/rtf", (builder, stream) -> builder.toRtf(stream), (print, stream) -> {
		final JRRtfExporter exporter = new JRRtfExporter(DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
		exporter.exportReport();
	}),
	CSV("csv", "text/comma-separated-values", (builder, stream) -> builder.toCsv(stream),
			(print, stream) -> {
				final JRCsvExporter exporter = new JRCsvExporter(
						DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleWriterExporterOutput(stream));
				exporter.exportReport();
			}),
	XLS("xls", "application/msexcel", (builder, stream) -> builder.toXls(stream),
			(print, stream) -> {
				final JRXlsExporter exporter = new JRXlsExporter(
						DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			}),
	XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
			(builder, stream) -> builder.toXlsx(stream), (print, stream) -> {
				final JRXlsxExporter exporter = new JRXlsxExporter(
						DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			}),
	DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
			(builder, stream) -> builder.toDocx(stream), (print, stream) -> {
				final JRDocxExporter exporter = new JRDocxExporter(
						DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			}),
	PPTX("pptx", "application/mspowerpoint", (builder, stream) -> builder.toPptx(stream),
			(print, stream) -> {
				final JRPptxExporter exporter = new JRPptxExporter(
						DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			}),
	ODT("odt", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
			(builder, stream) -> builder.toOdt(stream), (print, stream) -> {
				final JROdtExporter exporter = new JROdtExporter(
						DefaultJasperReportsContext.getInstance());
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));
				exporter.exportReport();
			});

	@FunctionalInterface
	private static interface DynamicExporter
	{
		public void export(JasperReportBuilder builder, OutputStream stream) throws DRException;
	}



	@FunctionalInterface
	private static interface PlainExporter
	{
		public void export(JasperPrint print, OutputStream stream) throws JRException;
	}

	private final String			fileSuffix;
	private final String			mimeType;
	private final DynamicExporter	dynamicExporter;
	private final PlainExporter		plainExporter;


	private ExportType(final String fileSuffix, final String mimeType,
			final DynamicExporter dynamicExporter, final PlainExporter plainExporter)
	{
		this.fileSuffix = fileSuffix;
		this.mimeType = mimeType;
		this.dynamicExporter = dynamicExporter;
		this.plainExporter = plainExporter;
	}


	public String getFileSuffix()
	{
		return this.fileSuffix;
	}


	public String getMimeType()
	{
		return this.mimeType;
	}
	
	
	private String getDefaultFileNamePrefix()
	{
		return "report" + System.currentTimeMillis();
	}
	
	
	private Resource createResource(final byte[] bytes, final String fileNamePrefix)
	{
		final StreamResource.StreamSource source = () -> new ByteArrayInputStream(bytes);
		final StreamResource resource = new StreamResource(source,
				fileNamePrefix + "." + this.fileSuffix);
		resource.setMIMEType(this.mimeType);
		return resource;
	}


	public Resource exportToResource(final JasperReportBuilder reportBuilder)
	{
		return exportToResource(reportBuilder,getDefaultFileNamePrefix());
	}


	public Resource exportToResource(final JasperReportBuilder reportBuilder,
			final String fileNamePrefix)
	{
		return createResource(exportToBytes(reportBuilder),fileNamePrefix);
	}


	public byte[] exportToBytes(final JasperReportBuilder reportBuilder)
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		export(reportBuilder,stream);
		return stream.toByteArray();
	}


	public void export(final JasperReportBuilder reportBuilder, final OutputStream stream)
	{
		try
		{
			this.dynamicExporter.export(reportBuilder,stream);
		}
		catch(final DRException e)
		{
			throw new ReportException(e);
		}
	}
	
	
	public Resource exportToResource(final InputStream jrxml, final JRDataSource dataSource,
			final Map<String, Object> parameters)
	{
		return exportToResource(jrxml,dataSource,parameters,getDefaultFileNamePrefix());
	}
	
	
	public Resource exportToResource(final InputStream jrxml, final JRDataSource dataSource,
			final Map<String, Object> parameters, final String fileNamePrefix)
	{
		return createResource(exportToBytes(jrxml,dataSource,parameters),fileNamePrefix);
	}
	
	
	public byte[] exportToBytes(final InputStream jrxml, final JRDataSource dataSource,
			final Map<String, Object> parameters)
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		export(jrxml,dataSource,parameters,stream);
		return stream.toByteArray();
	}


	public void export(final InputStream jrxml, final JRDataSource dataSource,
			final Map<String, Object> parameters, final OutputStream stream)
	{
		try
		{
			final JasperReport report = JasperCompileManager.compileReport(jrxml);
			final JasperPrint print = JasperFillManager.fillReport(report,parameters,dataSource);
			export(print,stream);
		}
		catch(final JRException e)
		{
			throw new ReportException(e);
		}
	}


	public void export(final JasperPrint print, final OutputStream stream)
	{
		try
		{
			this.plainExporter.export(print,stream);
		}
		catch(final JRException e)
		{
			throw new ReportException(e);
		}
	}
}
