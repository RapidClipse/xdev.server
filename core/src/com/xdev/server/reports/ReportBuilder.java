
package com.xdev.server.reports;


import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;


// migrated from xdev4
@SuppressWarnings({"unchecked","rawtypes"})
// Because JasperAPI maps ship without generic types specified
public class ReportBuilder
{
	/**
	 * Primary data source.
	 */
	private final JRDataSource	dataSource;
	
	/**
	 * Compiled report design.
	 */
	private final JasperReport	report;
	
	/**
	 * Parameters.
	 */
	private final Map			parameters			= new HashMap();
	
	/**
	 * The cached JasperPrint object for this {@link ReportBuilder}.
	 */
	private JasperPrint			cachedJasperPrint	= null;
	
	
	public ReportBuilder(final ReportStub reportStub) throws JRException
	{
		this.parameters.putAll(reportStub.getParameters());
		this.report = reportStub.getReportTemplate();
		this.dataSource = reportStub.getDataSource();
	}
	
	/**
	 * Creates a {@link ReportBuilder} with all needed information to build many
	 * different outputs of your report.
	 * 
	 * @param report
	 *            {@link JasperReport} instance to fill. Must not be null.
	 * 
	 * @param parameters
	 *            Map with parameters which are not in the DataSource
	 * @param dataSource
	 *            JRDataSource which is suitable for your Report. Must not be
	 *            null.
	 */
	public ReportBuilder(final JasperReport report, final Map parameters,
			final JRDataSource dataSource)
	{
		super();
		
		if(report == null)
		{
			throw new IllegalArgumentException("report must not be null");
		}
		else
		{
			this.report = report;
			
			if(parameters != null)
			{
				this.parameters.putAll(parameters);
			}
			
			/*
			 * prohibit null vales for dataSource as it is a common pitfall for
			 * empty reports
			 */
			if(dataSource == null)
			{
				throw new IllegalArgumentException(
						"dataSource must not be null. The use of JREmptyDataSource may be appropriate.");
			}
			else
			{
				this.dataSource = dataSource;
			}
		}
		
	}
	
	
	/**
	 * Creates a {@link JasperPrint} instance using the previously set
	 * parameters, data source and report template, if there is not already a
	 * cached {@link JasperPrint} object for this {@link ReportBuilder}
	 * available.
	 * 
	 * @return a {@link JasperPrint} instance
	 * @throws JRException
	 *             Indicates problems creating the {@link JasperPrint}
	 */
	protected JasperPrint getJasperPrint() throws JRException
	{
		if(this.cachedJasperPrint == null)
		{
			this.cachedJasperPrint = JasperFillManager.fillReport(this.report,new HashMap(
					this.parameters),this.dataSource);
		}
		
		return this.cachedJasperPrint;
	}
	
	
	/**
	 * Returns a new {@link JRViewer} instance for this {@link ReportBuilder}.
	 * 
	 * @param withToolbar
	 *            <code>true</code> to show the toolbar, <code>false</code> to
	 *            hide it
	 * 
	 * @return JRViewer a new {@link JRViewer} instance.
	 * @throws JRException
	 *             Indicates problems creating the {@link JRViewer}
	 */
	public JRViewer getJRViewer(final boolean withToolbar) throws JRException
	{
		final JRViewer viewer = new JRViewer(this.getJasperPrint());
		if(!withToolbar)
		{
			viewer.remove(0);
		}
		return viewer;
	}
	
	
	/**
	 * Opens the JasperViewer in a separate Window, with the given Content.
	 * 
	 * @throws JRException
	 *             if a problem occurs while filling the report
	 */
	public void previewReport() throws JRException
	{
		JasperViewer.viewReport(this.getJasperPrint(),false);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} to the provided {@link JRExporter}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * @param exporter
	 *            the used exporter
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeReport(final OutputStream out, final JRExporter exporter) throws JRException
	{
		this.exportReport(out,exporter);
	}
	
	
	protected void exportReport(final OutputStream out, final JRExporter exporter)
			throws JRException
	{
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,out);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT,this.getJasperPrint());
		exporter.exportReport();
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as PDF to the provided
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writePdfFile(final OutputStream out) throws JRException
	{
		final JRPdfExporter exporter = new JRPdfExporter();
		this.exportReport(out,exporter);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as HTML to the provided
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeHtmlFile(final OutputStream out) throws JRException
	{
		final HtmlExporter exporter = new HtmlExporter();
		this.exportReport(out,exporter);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as XLS (MS Excel) to the
	 * provided {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeExcelFile(final OutputStream out) throws JRException
	{
		final JRXlsExporter exporter = new JRXlsExporter();
		this.exportReport(out,exporter);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as RTF to the provided
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeRtfFile(final OutputStream out) throws JRException
	{
		final JRRtfExporter exporter = new JRRtfExporter();
		this.exportReport(out,exporter);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as CSV to the provided
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeCsvFile(final OutputStream out) throws JRException
	{
		final JRCsvExporter exporter = new JRCsvExporter();
		this.exportReport(out,exporter);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as XML to the provided
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeXmlFile(final OutputStream out) throws JRException
	{
		final JRXmlExporter exporter = new JRXmlExporter();
		this.exportReport(out,exporter);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as plain text to the provided
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * 
	 * 
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeTextFile(final OutputStream out) throws JRException
	{
		writeTextFile(out,80,30);
	}
	
	
	/**
	 * Writes this {@link ReportBuilder} formatted as plain text to the provided
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            {@link OutputStream} to write to.
	 * 
	 * @param pageWidth
	 *            TODO
	 * 
	 * @param pageHeight
	 *            TODO
	 * 
	 * 
	 * @throws JRException
	 *             if a problem occurs while writing the report.
	 */
	public void writeTextFile(final OutputStream out, final int pageWidth, final int pageHeight)
			throws JRException
	{
		final JRTextExporter exporter = new JRTextExporter();
		exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH,pageWidth);
		exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT,pageHeight);
		this.exportReport(out,exporter);
	}
	
	
	public void print() throws JRException
	{
		print(false,false);
	}
	
	
	public void print(boolean displayPageDialog, boolean displayPrintDialog) throws JRException
	{
		print(displayPageDialog,false,displayPrintDialog,false);
	}
	
	
	public void print(boolean displayPageDialog, boolean displayPageDialogOnlyOnce,
			boolean displayPrintDialog, boolean displayPrintDialogOnlyOnce) throws JRException
	{
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT,this.getJasperPrint());
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,displayPageDialog);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG_ONLY_ONCE,
				displayPageDialogOnlyOnce);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
				displayPrintDialog);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG_ONLY_ONCE,
				displayPrintDialogOnlyOnce);
		exporter.exportReport();
	}
	
	
	/**
	 * @return the dataSource
	 * @see #dataSource
	 */
	protected JRDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	
	/**
	 * @return the report
	 * @see #report
	 */
	protected JasperReport getReport()
	{
		return this.report;
	}
	
	
	/**
	 * @return the parameters
	 * @see #parameters
	 */
	protected Map getParameters()
	{
		return parameters;
	}
}
