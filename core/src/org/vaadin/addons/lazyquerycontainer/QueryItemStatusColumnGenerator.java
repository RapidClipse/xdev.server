/**
 * Copyright 2010 Tommi S.E. Laukkanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.addons.lazyquerycontainer;


import java.io.Serializable;

import com.vaadin.server.ClassResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.Property.ValueChangeNotifier;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.ColumnGenerator;


/**
 * Helper class for Vaadin tables to generate status column.
 *
 * @author Tommi S.E. Laukkanen
 */
@SuppressWarnings("deprecation")
public final class QueryItemStatusColumnGenerator
		implements ColumnGenerator, ValueChangeListener, Serializable
{
	/**
	 * Serial version UID of this class.
	 */
	private static final long	serialVersionUID	= 1L;
	/**
	 * Icon resource for none state.
	 */
	private Resource			noneIconResource;
	/**
	 * Icon resource for added state.
	 */
	private Resource			addedIconResource;
	/**
	 * Icon resource for modified state.
	 */
	private Resource			modifiedIconResource;
	/**
	 * Icon resource for removed state.
	 */
	private Resource			removedIconResource;
	/**
	 * The status icon Vaadin component.
	 */
	private Image				statusIcon;


	/**
	 * Construct which sets the application instance.
	 */
	public QueryItemStatusColumnGenerator()
	{
	}


	/**
	 * Generates cell component.
	 *
	 * @param source
	 *            The table this cell is generated for.
	 * @param itemId
	 *            ID of the item this cell is presenting property of.
	 * @param columnId
	 *            ID of the column this cell is located at.
	 * @return Component used to render this cell.
	 */
	@Override
	public Component generateCell(final Table source, final Object itemId, final Object columnId)
	{
		final Property statusProperty = source.getItem(itemId).getItemProperty(columnId);

		this.noneIconResource = new ClassResource(QueryItemStatusColumnGenerator.class,
				"images/textfield.png");
		this.addedIconResource = new ClassResource(QueryItemStatusColumnGenerator.class,
				"images/textfield_add.png");
		this.modifiedIconResource = new ClassResource(QueryItemStatusColumnGenerator.class,
				"images/textfield_rename.png");
		this.removedIconResource = new ClassResource(QueryItemStatusColumnGenerator.class,
				"images/textfield_delete.png");

		this.statusIcon = new Image(null,this.noneIconResource);
		this.statusIcon.setHeight("16px");

		if(statusProperty instanceof ValueChangeNotifier)
		{
			final ValueChangeNotifier notifier = (ValueChangeNotifier)statusProperty;
			notifier.addValueChangeListener(this);
		}

		refreshImage(statusProperty);

		return this.statusIcon;
	}


	/**
	 * Event handler for ValueChangeEvent.
	 *
	 * @param event
	 *            The event to be handled.
	 */
	@Override
	public void valueChange(final ValueChangeEvent event)
	{
		refreshImage(event.getProperty());
		this.statusIcon.markAsDirty();
	}


	/**
	 * Refreshes the status Icon according to the property value.
	 *
	 * @param statusProperty
	 *            The property according to which status is updated.
	 */
	private void refreshImage(final Property statusProperty)
	{
		if(statusProperty.getValue() == null)
		{
			this.statusIcon.setSource(this.noneIconResource);
			return;
		}
		final QueryItemStatus status = (QueryItemStatus)statusProperty.getValue();
		if(status == QueryItemStatus.None)
		{
			this.statusIcon.setSource(this.noneIconResource);
		}
		if(status == QueryItemStatus.Modified)
		{
			this.statusIcon.setSource(this.modifiedIconResource);
		}
		if(status == QueryItemStatus.Added)
		{
			this.statusIcon.setSource(this.addedIconResource);
		}
		if(status == QueryItemStatus.Removed)
		{
			this.statusIcon.setSource(this.removedIconResource);
		}
	}

}
