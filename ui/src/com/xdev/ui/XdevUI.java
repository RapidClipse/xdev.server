/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui;


import java.util.Locale;
import java.util.concurrent.Future;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import com.xdev.ui.action.XdevActionManager;
import com.xdev.ui.event.FocusChangeEvent;
import com.xdev.ui.event.FocusChangeListener;
import com.xdev.ui.util.UIUtils;


/**
 * The topmost component in any component hierarchy. There is one UI for every
 * Vaadin instance in a browser window. A UI may either represent an entire
 * browser window (or tab) or some part of a html page where a Vaadin
 * application is embedded.
 * <p>
 * The UI is the server side entry point for various client side features that
 * are not represented as components added to a layout, e.g notifications, sub
 * windows, and executing javascript in the browser.
 * </p>
 * <p>
 * When a new UI instance is needed, typically because the user opens a URL in a
 * browser window which points to e.g. {@link VaadinServlet}, all
 * {@link UIProvider}s registered to the current {@link VaadinSession} are
 * queried for the UI class that should be used. The selection is by default
 * based on the <code>UI</code> init parameter from web.xml.
 * </p>
 * <p>
 * After a UI has been created by the application, it is initialized using
 * {@link #init(VaadinRequest)}. This method is intended to be overridden by the
 * developer to add components to the user interface and initialize
 * non-component functionality. The component hierarchy must be initialized by
 * passing a {@link Component} with the main layout or other content of the view
 * to {@link #setContent(Component)} or to the constructor of the UI.
 * </p>
 *
 * @see #init(VaadinRequest)
 * @see UIProvider
 */
// https://www.xdevissues.com/browse/XWS-666
// @PreserveOnRefresh
public abstract class XdevUI extends UI
{
	private final FocusListener				focusNotifier		= this::focusedComponentChanged;
	private final ComponentAttachListener	focusAttachListener	= event -> addFocusWatcher(
			event.getAttachedComponent());
	private final ComponentDetachListener	focusDetachListener	= event -> removeFocusWatcher(
			event.getDetachedComponent());
	private Component						lastFocusedComponent;
	private XdevActionManager				xdevActionManager;


	/**
	 * Creates a new empty UI without a caption. The content of the UI must be
	 * set by calling {@link #setContent(Component)} before using the UI.
	 */
	public XdevUI()
	{
		super();
	}


	/**
	 * Creates a new UI with the given component (often a layout) as its
	 * content.
	 *
	 * @param content
	 *            the component to use as this UIs content.
	 *
	 * @see #setContent(Component)
	 */
	public XdevUI(final Component content)
	{
		super(content);
	}


	// init defaults
	{
		setLocale(Locale.getDefault());

		// https://www.xdevissues.com/browse/XWS-666
		// System.out.println("attach listener");
		// addDetachListener(new DetachListener()
		// {
		//
		// @Override
		// public void detach(final DetachEvent event)
		// {
		// System.out.println("detached UI");
		// // release possible locks
		// ConversationUtils.releaseConversationLock();
		// // end running conversation
		// ConversationUtils.endConversation();
		// // close session entity manager
		// EntityManagerUtils.getEntityManager().close();
		// }
		// });
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<Void> access(final Runnable runnable)
	{
		return super.access(getAccessRunnable(runnable));
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accessSynchronously(final Runnable runnable) throws UIDetachedException
	{
		super.accessSynchronously(getAccessRunnable(runnable));
	}


	/**
	 * Ensures that <code>runnable</code> is an {@link UIAccessWrapper}.
	 *
	 * @param runnable
	 * @return
	 */
	protected Runnable getAccessRunnable(final Runnable runnable)
	{
		if(runnable instanceof UIAccessWrapper)
		{
			return runnable;
		}
		return new UIAccessWrapper(runnable);
	}


	/**
	 * @return the xdevActionManager
	 */
	public XdevActionManager getXdevActionManager()
	{
		if(this.xdevActionManager == null)
		{
			this.xdevActionManager = new XdevActionManager(this);
		}

		return this.xdevActionManager;
	}


	public void addFocusChangeListener(final FocusChangeListener listener)
	{
		if(getListeners(FocusChangeEvent.class).isEmpty())
		{
			final Component content = getContent();
			if(content != null)
			{
				addFocusWatcher(content);
			}
		}

		addListener(FocusChangeEvent.EVENT_ID,FocusChangeEvent.class,listener,
				FocusChangeListener.focusChangedMethod);
	}


	public void removeFocusChangeListener(final FocusChangeListener listener)
	{
		removeListener(FocusChangeEvent.EVENT_ID,FocusChangeEvent.class,listener);

		if(getListeners(FocusChangeEvent.class).isEmpty())
		{
			final Component content = getContent();
			if(content != null)
			{
				removeFocusWatcher(content);
			}
		}
	}


	private void focusedComponentChanged(final FocusEvent event)
	{
		this.lastFocusedComponent = event.getComponent();
		fireEvent(new FocusChangeEvent(this.lastFocusedComponent));
	}


	@Override
	public void setContent(final Component content)
	{
		super.setContent(content);

		if(content != null && !getListeners(FocusChangeEvent.class).isEmpty())
		{
			addFocusWatcher(content);
		}
	}


	private void addFocusWatcher(final Component root)
	{
		UIUtils.lookupComponentTree(root,c -> {

			if(c instanceof FocusNotifier)
			{
				((FocusNotifier)c).addFocusListener(this.focusNotifier);
			}
			if(c instanceof ComponentAttachDetachNotifier)
			{
				((ComponentAttachDetachNotifier)c)
						.addComponentAttachListener(this.focusAttachListener);
				((ComponentAttachDetachNotifier)c)
						.addComponentDetachListener(this.focusDetachListener);
			}

			return null;
		});

		fireEvent(new FocusChangeEvent(
				this.lastFocusedComponent != null ? this.lastFocusedComponent : this));
	}


	private void removeFocusWatcher(final Component component)
	{
		UIUtils.lookupComponentTree(component,c -> {

			if(c instanceof FocusNotifier)
			{
				((FocusNotifier)c).removeFocusListener(this.focusNotifier);
			}
			if(c instanceof ComponentAttachDetachNotifier)
			{
				((ComponentAttachDetachNotifier)c)
						.removeComponentAttachListener(this.focusAttachListener);
				((ComponentAttachDetachNotifier)c)
						.removeComponentDetachListener(this.focusDetachListener);
			}

			return null;
		});
	}
}
