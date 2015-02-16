
package com.xdev.server.reports;


import java.io.File;
import java.io.OutputStream;

import com.xdev.server.lang.CommandObject;


public interface ReportCreator extends CommandObject
{
	public static enum Target
	{
		PREVIEW, PRINT, FILE, STREAM
	}
	
	
	
	public static enum OutputFormat
	{
		PDF("pdf"), HMTL("html"), EXCEL("xls"), RTF("rtf"), CSV("csv"), XML("xml"), TEXT("txt");
		
		private String	suffix;
		
		
		private OutputFormat(final String suffix)
		{
			this.suffix = suffix;
		}
		
		
		public String getSuffix()
		{
			return suffix;
		}
	}
	
	
	public abstract ReportStub getReportStub();
	
	
	public abstract void setReportStub(ReportStub reportStub);
	
	
	public abstract Target getTarget();
	
	
	public abstract void setTarget(Target target);
	
	
	public abstract boolean getDisplayPageDialog();
	
	
	public abstract void setDisplayPageDialog(boolean displayPageDialog);
	
	
	public abstract boolean getDisplayPageDialogOnlyOnce();
	
	
	public abstract void setDisplayPageDialogOnlyOnce(boolean displayPageDialogOnlyOnce);
	
	
	public abstract boolean getDisplayPrintDialog();
	
	
	public abstract void setDisplayPrintDialog(boolean displayPrintDialog);
	
	
	public abstract boolean getDisplayPrintDialogOnlyOnce();
	
	
	public abstract void setDisplayPrintDialogOnlyOnce(boolean displayPrintDialogOnlyOnce);
	
	
	public abstract OutputFormat getOutputFormat();
	
	
	public abstract void setOutputFormat(OutputFormat outputFormat);
	
	
	public abstract File getFile();
	
	
	public abstract void setFile(File file);
	
	
	public abstract OutputStream getOutputStream();
	
	
	public abstract void setOutputStream(OutputStream outputStream);
	
}