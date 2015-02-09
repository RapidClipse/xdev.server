
package com.xdev.server.ui.locking;


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
