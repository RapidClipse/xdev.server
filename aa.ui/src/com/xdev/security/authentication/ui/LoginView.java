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
 */

package com.xdev.security.authentication.ui;


import com.vaadin.navigator.View;
import com.xdev.ui.XdevView;


/**
 * A login view is used for authentication in an application.
 *
 * @see XdevAuthenticationNavigator
 * @see XdevView
 *		
 * @author XDEV Software
 *		
 */
public interface LoginView extends View
{
	/**
	 * Returns the username of the login form.
	 *
	 * @return the username
	 */
	public String getUsername();
	
	
	/**
	 * Returns the password of the login form.
	 *
	 * @return the password
	 */
	public String getPassword();
}
