
package com.xdev.server.reports;


import java.io.File;
import java.io.OutputStream;

import com.xdev.server.lang.CommandException;


public abstract class AbstractReportCreator implements ReportCreator
{
	
	private ReportStub		reportStub;
	
	private boolean			displayPageDialog;
	private boolean			displayPageDialogOnlyOnce;
	private boolean			displayPrintDialog;
	private boolean			displayPrintDialogOnlyOnce;
	private boolean			setOpenFile;
	
	private Target			target;
	private File			file;
	
	private OutputFormat	outputFormat;
	private OutputStream	outputStream;
	
	
	@Override
	public ReportStub getReportStub()
	{
		if(this.reportStub != null)
		{
			return this.reportStub;
		}
		CommandException.throwMissingParameter("reportStub");
		return null;
	}
	
	
	@Override
	public void setReportStub(ReportStub reportStub)
	{
		this.reportStub = reportStub;
	}
	
	
	@Override
	public Target getTarget()
	{
		if(this.target != null)
		{
			return target;
		}
		CommandException.throwMissingParameter("target");
		return null;
	}
	
	
	@Override
	public void setTarget(Target target)
	{
		this.target = target;
	}
	
	
	@Override
	public boolean getDisplayPageDialog()
	{
		return displayPageDialog;
	}
	
	
	@Override
	public void setDisplayPageDialog(boolean displayPageDialog)
	{
		this.displayPageDialog = displayPageDialog;
	}
	
	
	@Override
	public boolean getDisplayPageDialogOnlyOnce()
	{
		return displayPageDialogOnlyOnce;
	}
	
	
	@Override
	public void setDisplayPageDialogOnlyOnce(boolean displayPageDialogOnlyOnce)
	{
		this.displayPageDialogOnlyOnce = displayPageDialogOnlyOnce;
	}
	
	
	@Override
	public boolean getDisplayPrintDialog()
	{
		return displayPrintDialog;
	}
	
	
	@Override
	public void setDisplayPrintDialog(boolean displayPrintDialog)
	{
		this.displayPrintDialog = displayPrintDialog;
	}
	
	
	@Override
	public boolean getDisplayPrintDialogOnlyOnce()
	{
		return displayPrintDialogOnlyOnce;
	}
	
	
	@Override
	public void setDisplayPrintDialogOnlyOnce(boolean displayPrintDialogOnlyOnce)
	{
		this.displayPrintDialogOnlyOnce = displayPrintDialogOnlyOnce;
	}
	
	
	public boolean isSetOpenFile()
	{
		return setOpenFile;
	}
	
	
	public void setSetOpenFile(boolean setOpenFile)
	{
		this.setOpenFile = setOpenFile;
	}
	
	
	@Override
	public OutputFormat getOutputFormat()
	{
		if(this.outputFormat != null)
		{
			return outputFormat;
		}
		CommandException.throwMissingParameter("outputFormat");
		return null;
	}
	
	
	@Override
	public void setOutputFormat(OutputFormat outputFormat)
	{
		this.outputFormat = outputFormat;
	}
	
	
	@Override
	public File getFile()
	{
		if(this.file != null)
		{
			return file;
		}
		CommandException.throwMissingParameter("file");
		return file;
	}
	
	
	@Override
	public void setFile(File file)
	{
		this.file = file;
	}
	
	
	@Override
	public OutputStream getOutputStream()
	{
		if(this.outputStream != null)
		{
			return outputStream;
		}
		CommandException.throwMissingParameter("outputStream");
		return null;
	}
	
	
	@Override
	public void setOutputStream(OutputStream outputStream)
	{
		this.outputStream = outputStream;
	}
}
