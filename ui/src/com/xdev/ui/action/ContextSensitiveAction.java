/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.action;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.vaadin.ui.Component;
import com.xdev.ui.event.ContextSensitiveHandlerChangeEvent;
import com.xdev.ui.event.ContextSensitiveHandlerChangeListener;


/**
 * Action which is enabled when a certain context gets active. In most cases a
 * {@link Component} provides/implements this context. The action will be
 * enabled if a component inside the provider receives the focus or only one
 * provider of this type is available in the current UI.
 * <p>
 * First you need a handler:
 *
 * <pre>
 * public interface SaveHandler extends ContextSensitiveHandler
 * {
 * 	public void save();
 * }
 * </pre>
 *
 * The action may look like this:
 *
 * <pre>
 * public class SaveAction extends ContextSensitiveAction&lt;SaveHandler&gt;
 * {
 * 	public SaveAction(XdevUI ui)
 * 	{
 * 		super(ui,SaveHandler.class);
 * 		setCaption("Save");
 * 	}
 *	
 *	
 * 	&#64;Override
 * 	public void actionPerformed(ActionEvent e)
 * 	{
 * 		getHandler().save();
 * 	}
 * }
 * </pre>
 *
 * Then any {@link Component} can provide the handler:
 *
 * <pre>
 * public class EditorView extends XdevView implements SaveHandler
 * {
 * 	public EditorView()
 * 	{
 * 		super();
 * 		this.initUI();
 * 	}
 *	
 *	
 * 	&#64;Override
 * 	public void save()
 * 	{
 * 		// Implement save routine here
 * 	}
 *	
 *	
 * 	private void initUI()
 * 	{
 * 		// ...
 * 	}
 * }
 * </pre>
 *
 * For example in the main UI there is a button bar or menu where the action is
 * visible:
 *
 * <pre>
 * public class MainUI extends XdevUI
 * {
 * 	&#64;Override
 * 	public void init(VaadinRequest request)
 * 	{
 * 		this.initUI();
 * 		...
 * 		SaveAction saveAction = new SaveAction(this);
 * 		XdevButton cmdSave = new XdevButton(action);
 * 		...
 * 		// or otherwise
 * 		cmdSave.setAction(saveAction);
 * 	}
 * }
 * </pre>
 *
 * @see #getHandler()
 * @see #handlerChanged(ContextSensitiveHandler, ContextSensitiveHandler)
 * @see ContextSensitiveHandler
 * @see XdevActionManager
 *
 * @param <T>
 *            the handler type
 *
 * @author XDEV Software
 */
public abstract class ContextSensitiveAction<T extends ContextSensitiveHandler>
		extends AbstractAction implements ContextSensitiveHandlerChangeListener
{
	private T handler;


	/**
	 * Creates a action bound to the given UI and handler type.
	 *
	 * @param ui
	 */
	protected ContextSensitiveAction()
	{
		super();

		setEnabled(false);

		final XdevActionManager manager = XdevActionManager.getCurrent();
		if(manager != null)
		{
			manager.registerContextSensitiveAction(this,determineHandlerType());
		}
	}


	@SuppressWarnings("unchecked")
	private Class<T> determineHandlerType()
	{
		final Type genericSuperclass = getClass().getGenericSuperclass();
		if(genericSuperclass instanceof ParameterizedType)
		{
			final Type rootType = ((ParameterizedType)genericSuperclass)
					.getActualTypeArguments()[0];
			if(rootType instanceof Class)
			{
				return (Class<T>)rootType;
			}
		}

		throw new IllegalStateException("no handler type parameter defined");
	}


	/**
	 * Returns the active handler, or <code>null</code> if no handler was found.
	 *
	 * @return the active handler
	 */
	protected T getHandler()
	{
		return this.handler;
	}


	/**
	 * Called by {@link XdevActionManager}.
	 *
	 * @param handler
	 *            the new handler
	 */
	void setHandler(final T handler)
	{
		final T oldHandler = this.handler;
		this.handler = handler;
		handlerChanged(oldHandler,handler);
	}


	/**
	 * Invoked when the active handler changes. May be used to register
	 * listeners to the new handler.
	 * <p>
	 * The default implementation just sets the enabled state:
	 *
	 * <pre>
	 * setEnabled(newHandler != null);
	 * </pre>
	 *
	 * @param oldHandler
	 * @param newHandler
	 */
	protected void handlerChanged(final T oldHandler, final T newHandler)
	{
		update(newHandler);
	}
	
	
	@Override
	public void contextSensitiveHandlerChanged(final ContextSensitiveHandlerChangeEvent event)
	{
		update(getHandler());
	}
	
	
	protected void update(final T handler)
	{
		setEnabled(handler != null);
	}
}
