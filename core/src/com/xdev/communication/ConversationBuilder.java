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

package com.xdev.communication;


import java.util.ArrayList;
import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;


/**
 * @author XDEV Software (JW)
 * 		
 */
public abstract class ConversationBuilder
{
	public static UIBoundConversationBuilder uiBound(final UI ui)
	{
		return new UIBoundConversationBuilder(ui);
	}


	public void startConversation()
	{
		ConversationUtils.startConversation();
	}



	public static class UIBoundConversationBuilder extends ConversationBuilder
	{
		private final UI			ui;
		private ViewChangeListener	viewChangeListener;
		private List<String>		allowedNavigationViews;
		private DetachListener		detachListener;
		private List<Component>		detachComponents;
		
		
		public UIBoundConversationBuilder(final UI ui)
		{
			this.ui = ui;
		}


		public UIBoundConversationBuilder endOnDetach(final Component... components)
		{
			if(this.detachListener == null)
			{
				this.detachListener = event -> endConversation();
				this.detachComponents = new ArrayList<>();
			}

			for(final Component c : components)
			{
				c.addDetachListener(this.detachListener);
				this.detachComponents.add(c);
			}

			return this;
		}


		public UIBoundConversationBuilder endOnNavigateOut(final String... views)
		{
			final Navigator navigator = this.ui.getNavigator();
			if(navigator == null)
			{
				throw new IllegalArgumentException("no navigator present");
			}

			if(this.allowedNavigationViews == null)
			{
				this.allowedNavigationViews = new ArrayList<>();
			}
			for(final String view : views)
			{
				this.allowedNavigationViews.add(view);
			}

			if(this.viewChangeListener == null)
			{
				this.viewChangeListener = new ViewChangeListener()
				{
					@Override
					public boolean beforeViewChange(final ViewChangeEvent event)
					{
						return true;
					}


					@Override
					public void afterViewChange(final ViewChangeEvent event)
					{
						final String viewName = event.getViewName();
						if(!UIBoundConversationBuilder.this.allowedNavigationViews
								.contains(viewName))
						{
							endConversation();
						}
					}
				};

				navigator.addViewChangeListener(this.viewChangeListener);
			}

			return this;
		}


		private void endConversation()
		{
			if(this.detachListener != null)
			{
				for(final Component c : this.detachComponents)
				{
					c.removeDetachListener(this.detachListener);
				}
				this.detachListener = null;
				this.detachComponents.clear();
				this.detachComponents = null;
			}

			if(this.viewChangeListener != null)
			{
				this.ui.getNavigator().removeViewChangeListener(this.viewChangeListener);
				this.allowedNavigationViews.clear();
				this.allowedNavigationViews = null;
			}

			ConversationUtils.endConversation();
		}
	}
}
