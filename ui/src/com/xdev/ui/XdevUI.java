/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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


import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ErrorHandlingRunnable;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import com.xdev.communication.Conversationables;
import com.xdev.ui.event.FocusChangeEvent;
import com.xdev.ui.event.FocusChangeListener;
import com.xdev.ui.util.UIUtils;
import com.xdev.util.ExtensionUtils;
import com.xdev.util.concurrent.XdevExecutorService;


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
public abstract class XdevUI extends UI implements XdevComponent
{
	/**
	 * Convenience method to call {@link UI#access(Runnable)} from a task inside the
	 * {@link XdevExecutorService} without interfering with the
	 * {@link Conversationables}' session handling.
	 *
	 * @param updater
	 * @since 3.0
	 */
	public static void update(final Runnable updater)
	{
		((XdevUI)getCurrent()).accessDefault(updater);
	}
	
	
	/**
	 * Convenience method to call {@link UI#accessSynchronously(Runnable)} from a
	 * task inside the {@link XdevExecutorService} without interfering with the
	 * {@link Conversationables}' session handling.
	 *
	 * @param updater
	 * @since 3.0
	 */
	public static void updateSynchronously(final Runnable updater)
	{
		((XdevUI)getCurrent()).accessSynchronouslyDefault(updater);
	}
	
	private final Extensions				extensions			= new Extensions();
	private final FocusListener				focusNotifier		= this::focusedComponentChanged;
	private final ComponentAttachListener	focusAttachListener	= event -> addFocusWatcher(
			event.getAttachedComponent());
	private final ComponentDetachListener	focusDetachListener	= event -> removeFocusWatcher(
			event.getDetachedComponent());
	private Component						lastFocusedComponent;
	
	
	/**
	 * Creates a new empty UI without a caption. The content of the UI must be set
	 * by calling {@link #setContent(Component)} before using the UI.
	 */
	public XdevUI()
	{
		super();
	}
	
	
	/**
	 * Creates a new UI with the given component (often a layout) as its content.
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
		
		try
		{
			final List<XdevUIExtension> extensions = ExtensionUtils.readExtensions("ui",
					XdevUIExtension.class);
			
			for(final XdevUIExtension extension : extensions)
			{
				extension.uiInitialized(this);
			}
		}
		catch(final Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
	
	
	/**
	 * Ensures {@link UI#access(Runnable)} runs inside an {@link UIAccessWrapper}.
	 * <p>
	 * If you don't want a wrapper use {@link #accessDefault(Runnable)}.
	 */
	@Override
	public Future<Void> access(final Runnable runnable)
	{
		final VaadinSession session = getSession();
		
		if(session == null)
		{
			throw new UIDetachedException();
		}
		
		final Runnable accessRunnable = getAccessRunnable(runnable);
		return session.access(new ErrorHandlingRunnable()
		{
			@Override
			public void run()
			{
				accessSynchronouslyDefault(accessRunnable);
			}
			
			
			@Override
			public void handleError(final Exception exception)
			{
				try
				{
					if(runnable instanceof ErrorHandlingRunnable)
					{
						final ErrorHandlingRunnable errorHandlingRunnable = (ErrorHandlingRunnable)runnable;
						
						errorHandlingRunnable.handleError(exception);
					}
					else
					{
						final ConnectorErrorEvent errorEvent = new ConnectorErrorEvent(XdevUI.this,
								exception);
						
						ErrorHandler errorHandler = com.vaadin.server.ErrorEvent
								.findErrorHandler(XdevUI.this);
						
						if(errorHandler == null)
						{
							errorHandler = new DefaultErrorHandler();
						}
						
						errorHandler.error(errorEvent);
					}
				}
				catch(final Exception e)
				{
					Logger.getLogger(UI.class.getName()).log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
	}
	
	
	/**
	 * Ensures {@link UI#accessSynchronously(Runnable)} runs inside an
	 * {@link UIAccessWrapper}.
	 * <p>
	 * If you don't want a wrapper use
	 * {@link #accessSynchronouslyDefault(Runnable)}.
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
	protected UIAccessWrapper getAccessRunnable(final Runnable runnable)
	{
		if(runnable instanceof UIAccessWrapper)
		{
			return (UIAccessWrapper)runnable;
		}
		return new UIAccessWrapper(runnable);
	}
	
	
	/**
	 * Calls {@link UI#access(Runnable)} without an {@link UIAccessWrapper}.
	 *
	 * @see #access(Runnable)
	 * @see UI#access(Runnable)
	 */
	public Future<Void> accessDefault(final Runnable runnable)
	{
		final VaadinSession session = getSession();
		
		if(session == null)
		{
			throw new UIDetachedException();
		}
		
		return session.access(new ErrorHandlingRunnable()
		{
			@Override
			public void run()
			{
				accessSynchronouslyDefault(runnable);
			}
			
			
			@Override
			public void handleError(final Exception exception)
			{
				try
				{
					if(runnable instanceof ErrorHandlingRunnable)
					{
						final ErrorHandlingRunnable errorHandlingRunnable = (ErrorHandlingRunnable)runnable;
						
						errorHandlingRunnable.handleError(exception);
					}
					else
					{
						final ConnectorErrorEvent errorEvent = new ConnectorErrorEvent(XdevUI.this,
								exception);
						
						ErrorHandler errorHandler = com.vaadin.server.ErrorEvent
								.findErrorHandler(XdevUI.this);
						
						if(errorHandler == null)
						{
							errorHandler = new DefaultErrorHandler();
						}
						
						errorHandler.error(errorEvent);
					}
				}
				catch(final Exception e)
				{
					Logger.getLogger(UI.class.getName()).log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
	}
	
	
	/**
	 * Calls {@link UI#accessSynchronouslyDefault(Runnable)} without an
	 * {@link UIAccessWrapper}.
	 *
	 * @see #accessSynchronouslyDefault(Runnable)
	 * @see UI#accessSynchronouslyDefault(Runnable)
	 */
	public void accessSynchronouslyDefault(final Runnable runnable) throws UIDetachedException
	{
		super.accessSynchronously(runnable);
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
