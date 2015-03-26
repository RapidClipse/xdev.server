/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
package com.xdev.ui;


import com.vaadin.ui.Upload;


/**
 * Component for uploading files from client to server.
 *
 * <p>
 * The visible component consists of a file name input box and a browse button
 * and an upload submit button to start uploading.
 *
 * <p>
 * The Upload component needs a java.io.OutputStream to write the uploaded data.
 * You need to implement the Upload.Receiver interface and return the output
 * stream in the receiveUpload() method.
 *
 * <p>
 * You can get an event regarding starting (StartedEvent), progress
 * (ProgressEvent), and finishing (FinishedEvent) of upload by implementing
 * StartedListener, ProgressListener, and FinishedListener, respectively. The
 * FinishedListener is called for both failed and succeeded uploads. If you wish
 * to separate between these two cases, you can use SucceededListener
 * (SucceededEvenet) and FailedListener (FailedEvent).
 *
 * <p>
 * The upload component does not itself show upload progress, but you can use
 * the ProgressIndicator for providing progress feedback by implementing
 * ProgressListener and updating the indicator in updateProgress().
 *
 * <p>
 * Setting upload component immediate initiates the upload as soon as a file is
 * selected, instead of the common pattern of file selection field and upload
 * button.
 *
 * <p>
 * Note! Because of browser dependent implementations of <input type="file">
 * element, setting size for Upload component is not supported. For some
 * browsers setting size may work to some extend.
 *
 * @author XDEV Software
 *
 */
public class XdevUpload extends Upload
{
	/**
	 * Creates a new instance of Upload.
	 *
	 * The receiver must be set before performing an upload.
	 */
	public XdevUpload()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param uploadReceiver
	 */
	public XdevUpload(final String caption, final Receiver uploadReceiver)
	{
		super(caption,uploadReceiver);
	}
}
