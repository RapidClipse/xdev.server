/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.file;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;
import com.xdev.mobile.service.file.FileServiceError.Reason;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;


/**
 * This service implements a File API allowing read/write access to files
 * residing on the device.
 *
 * @author XDEV Software
 *
 */

@MobileService(plugins = @Plugin(name = "cordova-plugin-file", spec = "4.2.0") )
@JavaScript("file.js")
public class FileService extends AbstractMobileService
{
	/**
	 * Returns the file service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.file.FileService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the file service if available
	 */
	public static FileService getInstance()
	{
		return getMobileService(FileService.class);
	}
	
	private final Map<String, ServiceCall<FileData, FileServiceError>> readFileCalls = new HashMap<>();
	
	
	public FileService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);
		
		this.addFunction("file_readFile_success",this::file_readFile_success);
		this.addFunction("file_readFile_error",this::file_readFile_error);
	}
	
	
	public void readFile(final String path, final Consumer<FileData> successCallback,
			final Consumer<FileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<FileData, FileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.readFileCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		
		js.append("file_readFile('").append(id).append("','").append(path).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void file_readFile_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<FileData, FileServiceError> call = this.readFileCalls.remove(id);
		if(call != null)
		{
			call.success(new FileData(arguments.get(1).asString()));
		}
	}
	
	
	private void file_readFile_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<FileData, FileServiceError> call = this.readFileCalls.remove(id);
		if(call != null)
		{
			call.error(createFileServiceError("Error reading file",arguments.get(1)));
		}
	}


	private FileServiceError createFileServiceError(final String message, final JsonValue value)
	{
		Reason reason = null;
		if(value instanceof JsonObject)
		{
			try
			{
				final int code = (int)((JsonObject)value).getNumber("code");
				reason = Reason.getByCode(code);
			}
			catch(final Exception e)
			{
				// swallow
			}
		}
		return new FileServiceError(this,message,reason);
	}
}
