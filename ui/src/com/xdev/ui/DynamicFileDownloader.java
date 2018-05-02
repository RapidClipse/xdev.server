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

package com.xdev.ui;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.ui.AbstractComponent;
import com.xdev.Application;


/**
 * @author XDEV Software
 * @since 3.1
 */
public class DynamicFileDownloader extends FileDownloader
{
	public static void install(final AbstractComponent target,
			final Supplier<Resource> resourceSupplier)
	{
		new DynamicFileDownloader(resourceSupplier).extend(target);
	}
	
	private final Supplier<Resource> resourceSupplier;


	public DynamicFileDownloader(final Supplier<Resource> resourceSupplier)
	{
		super(createEmptyResource());

		this.resourceSupplier = resourceSupplier;
		
		setOverrideContentType(false);
	}


	private static Resource createEmptyResource()
	{
		final StreamResource resource = new StreamResource(
				() -> new ByteArrayInputStream(new byte[0]),UUID.randomUUID().toString());
		resource.setMIMEType("text/plain");
		return resource;
	}


	@Override
	public boolean handleConnectorRequest(final VaadinRequest request,
			final VaadinResponse response, final String path) throws IOException
	{
		return super.handleConnectorRequest(request,response,path);
	}


	@Override
	public Resource getFileDownloadResource()
	{
		try
		{
			return Application.getExecutorService().submit(this.resourceSupplier::get).get();
		}
		catch(final Exception e)
		{
			Logger.getLogger(getClass()).error(e);

			final byte[] data = ExceptionUtils.getStackTrace(e).getBytes(StandardCharsets.UTF_8);
			final StreamResource resource = new StreamResource(() -> new ByteArrayInputStream(data),
					"error");
			resource.setMIMEType("text/plain");
			return resource;
		}
	}
}
