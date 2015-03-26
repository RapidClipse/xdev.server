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
 
package com.xdev.ui.locking;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


public class OptimisticLockingExceptionDialog extends Window
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1949157884343636557L;
	
	
	public OptimisticLockingExceptionDialog(Object lockedObject)
	{
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		setContent(verticalLayout);
		
		setCaption("Changes not submitted");
		setModal(true);
		setResizable(false);
		
		HorizontalLayout headerLayout = new HorizontalLayout();
		Label text = new Label(
				"This record has already been updated by another user - Changes made to the record will be rolled back");
		text.setStyleName(ValoTheme.LABEL_FAILURE);
		headerLayout.addComponent(text);
		verticalLayout.addComponent(headerLayout);
		
		verticalLayout.setComponentAlignment(headerLayout,Alignment.MIDDLE_CENTER);
	}
}
