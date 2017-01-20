/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.push;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.InitScriptProvider;


/**
 * @author XDEV Software
 * 		
 */
public class PushInitScriptProvider implements InitScriptProvider
{
	@Override
	public String getInitScript(final MobileServiceConfiguration configuration) throws IOException
	{
		final String senderID = configuration.getParameters().get("SENDER_ID");
		if(senderID == null || senderID.length() == 0)
		{
			return null;
		}

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("push-init.js"))))
		{
			final StringBuilder sb = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null)
			{
				sb.append(line);
				sb.append('\n');
			}

			return sb.toString().replace("${senderID}",senderID);
		}
	}
}
