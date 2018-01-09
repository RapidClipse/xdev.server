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


import javax.persistence.EntityManager;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.xdev.communication.Conversation;
import com.xdev.communication.XdevServlet;
import com.xdev.communication.XdevServletService;


/**
 * Runnable wrapper used in {@link UI#access(Runnable)} and
 * {@link UI#accessSynchronously(Runnable)}.
 * <p>
 * It ensures the proper handling of the application's {@link EntityManager} and
 * conversational state.
 *
 * @see EntityManager
 * @see Conversation
 * 		
 * @author XDEV Software
 * 		
 */
public class UIAccessWrapper implements Runnable
{
	private final Runnable runnable;
	
	
	public UIAccessWrapper(final Runnable runnable)
	{
		this.runnable = runnable;
	}
	
	
	@Override
	public void run()
	{
		final XdevServletService service = XdevServlet.getServlet().getService();
		final VaadinSession session = UI.getCurrent().getSession();

		try
		{
			service.handleRequestStart(session);
			
			this.runnable.run();
		}
		finally
		{
			service.handleRequestEnd(session);
		}
	}
}
