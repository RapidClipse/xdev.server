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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceError;
import com.xdev.mobile.ui.MobileUI;

import elemental.json.JsonArray;
import elemental.json.JsonObject;


/**
 * @author XDEV Software
 *
 */

@JavaScript("file.js")
public class FileService extends MobileService
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
	 * @see MobileUI#getMobileService(Class)
	 * @return the file service if available
	 */
	public static FileService getInstance()
	{
		return getServiceHelper(FileService.class);
	}
	
	
	
	private static class ReadEntriesCall
	{
		final Consumer<List<Entry>>			successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		ReadEntriesCall(final Consumer<List<Entry>> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class GetMetaDataCall
	{
		final Consumer<Metadata>			successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		GetMetaDataCall(final Consumer<Metadata> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	private final Map<String, ReadEntriesCall>	readEntriesCall	= new HashMap<>();
	private final Map<String, GetMetaDataCall>	getMetaDataCall	= new HashMap<>();
	
	
	public FileService(final AbstractClientConnector connector)
	{
		super(connector);
		
		this.addFunction("file_readEntries_success",this::file_readDirectoryEntries_success);
		this.addFunction("file_readEntries_error",this::file_readDirectoryEntries_error);
		
		this.addFunction("file_getMetadata_success",this::file_getMetadata_success);
		this.addFunction("file_getMetadata_error",this::file_getMetadata_error);
	}
	
	
	private void file_readDirectoryEntries_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ReadEntriesCall call = this.readEntriesCall.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final List<Entry> entries = new ArrayList<Entry>();
		
		final JsonArray arrayData = arguments.get(1);
		for(int i = 0; i < arrayData.length(); i++)
		{
			final Gson gson = new Gson();
			final JsonObject jsonObject = arrayData.get(i);
			final Boolean isFile = jsonObject.get("isFile");
			if(isFile)
			{
				final FileEntry entry = gson.fromJson(jsonObject.toJson(),FileEntry.class);
				entries.add(entry);
			}
			else
			{
				final DirectoryEntry entry = gson
						.fromJson(jsonObject.toJson(),DirectoryEntry.class);
				entries.add(entry);
			}
		}
		
		call.successCallback.accept(entries);
	}
	
	
	private void file_readDirectoryEntries_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ReadEntriesCall call = this.readEntriesCall.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void file_getMetadata_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final GetMetaDataCall call = this.getMetaDataCall.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final Gson gson = new Gson();
		final JsonObject jsonObject = arguments.getObject(1);
		
		// nicht getestet! jsonObject.toJson() könnte auch long zurückliefern
		// und müsste auf date gecastet werden
		
		final Metadata metadata = gson.fromJson(jsonObject.toJson(),Metadata.class);
		
		call.successCallback.accept(metadata);
	}
	
	
	private void file_getMetadata_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ReadEntriesCall call = this.readEntriesCall.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	/**
	 * Read the entries in this directory.
	 *
	 * @param successCallback
	 * @param errorCallback
	 */
	public synchronized void readEntries(final Consumer<List<Entry>> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ReadEntriesCall call = new ReadEntriesCall(successCallback,errorCallback);
		this.readEntriesCall.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		
		js.append("file_readDirectoryEntries('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	// TODO uebergabe parameter (file oder directory) von dem die metadatan
	// abgefragt werden soll
	/**
	 * Read the metadata of a file or directory.
	 *
	 * @param successCallback
	 * @param errorCallback
	 */
	public synchronized void getMetaData(final Consumer<Metadata> successCallback,
			final Consumer<MobileServiceError> errorCallback, final Entry entry)
	{
		
		final String id = generateCallerID();
		final GetMetaDataCall call = new GetMetaDataCall(successCallback,errorCallback);
		this.getMetaDataCall.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		
		js.append("file_getMetaData('").append(id).append("','").append(entry).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
}
