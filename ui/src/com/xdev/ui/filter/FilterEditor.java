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

package com.xdev.ui.filter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Container.Filter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.themes.ValoTheme;
import com.xdev.res.StringResourceUtils;
import com.xdev.ui.filter.FilterField.FilterFieldChangeListener;


/**
 * @author XDEV Software
 *
 */
public class FilterEditor
{
	protected static class PropertyEntry
	{
		private final Object	propertyID;
		private final Class<?>	propertyType;
		private final String	caption;


		public PropertyEntry(final Object propertyID, final Class<?> propertyType,
				final String caption)
		{
			this.propertyID = propertyID;
			this.propertyType = propertyType;
			this.caption = caption;
		}


		/**
		 * @return the propertyID
		 */
		public Object getPropertyId()
		{
			return this.propertyID;
		}


		/**
		 * @return the propertyType
		 */
		public Class<?> getPropertyType()
		{
			return this.propertyType;
		}


		/**
		 * @return the caption
		 */
		public String getCaption()
		{
			return this.caption;
		}


		@Override
		public String toString()
		{
			return this.caption;
		}
	}

	private final XdevContainerFilterComponent	containerFilterComponent;

	private final ComboBox						propertyComboBox;
	private final ComboBox						operatorComboBox;

	private final FilterFieldChangeListener		filterFieldChangeListener;
	private PropertyEntry						selectedPropertyEntry;
	private FilterOperator						selectedOperator;
	private FilterField<?>[]					valueEditors;

	private final Button						removeFilterButton;
	private final Button						addFilterButton;


	public FilterEditor(final XdevContainerFilterComponent containerFilterComponent)
	{
		this.containerFilterComponent = containerFilterComponent;

		final List<PropertyEntry> propertyEntries = getPropertyEntries();

		this.propertyComboBox = createPropertyComboBox();
		this.propertyComboBox.addItems(propertyEntries);
		this.propertyComboBox.addValueChangeListener(event -> propertySelectionChanged());

		this.operatorComboBox = createOperatorComboBox();
		this.operatorComboBox.addValueChangeListener(event -> operatorSelectionChanged());
		this.operatorComboBox.setVisible(false);

		this.filterFieldChangeListener = event -> containerFilterComponent.updateContainerFilter();

		this.removeFilterButton = createRemoveFilterButton();
		this.removeFilterButton
				.addClickListener(event -> containerFilterComponent.removeFilterEditor(this));

		this.addFilterButton = createAddFilterButton();
		this.addFilterButton
				.addClickListener(event -> containerFilterComponent.addFilterEditorAfter(this));
	}


	public FilterEditor(final XdevContainerFilterComponent containerFilterComponent,
			final FilterData data)
	{
		this(containerFilterComponent);

		setFilterData(data);
	}


	public void setFilterData(final FilterData data)
	{
		final Collection<?> containerPropertyIds = this.propertyComboBox.getContainerDataSource()
				.getItemIds();
		final PropertyEntry item = containerPropertyIds.stream().map(PropertyEntry.class::cast)
				.filter(entry -> entry.propertyID.equals(data.getPropertyId())).findFirst()
				.orElse(null);
		this.propertyComboBox.select(item);
		propertySelectionChanged();

		if(item != null)
		{
			final FilterOperator operator = data.getOperator();
			this.operatorComboBox.select(operator);
			operatorSelectionChanged();

			if(operator != null)
			{
				final Object[] values = data.getValues();
				if(values != null && values.length == this.valueEditors.length)
				{
					int i = 0;
					for(final Object value : values)
					{
						this.valueEditors[i++].setFilterValue(value);
					}
				}
			}
		}
	}


	public FilterData getFilterData()
	{
		if(this.selectedPropertyEntry == null || this.selectedOperator == null
				|| this.valueEditors == null)
		{
			return null;
		}

		final Object[] values = Arrays.stream(this.valueEditors).map(FilterField::getFilterValue)
				.toArray();
		return new FilterData(this.selectedPropertyEntry.getPropertyId(),this.selectedOperator,
				values);
	}


	protected XdevContainerFilterComponent getContainerFilterComponent()
	{
		return this.containerFilterComponent;
	}


	protected ComboBox createPropertyComboBox()
	{
		final ComboBox propertyComboBox = new ComboBox();
		propertyComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
		propertyComboBox.addStyleName(XdevContainerFilterComponent.FILTER_COMBOBOX_CLASS);
		propertyComboBox.setTextInputAllowed(false);
		propertyComboBox.setInputPrompt(StringResourceUtils
				.getResourceString("ContainerFilterComponent.selectOption",this));
		propertyComboBox.setNullSelectionAllowed(false);
		return propertyComboBox;
	}


	protected ComboBox getPropertyComboBox()
	{
		return this.propertyComboBox;
	}


	protected ComboBox createOperatorComboBox()
	{
		final ComboBox operatorComboBox = new ComboBox()
		{
			@Override
			public String getItemCaption(final Object itemId)
			{
				if(itemId instanceof FilterOperator)
				{
					return ((FilterOperator)itemId).getName();
				}

				return super.getItemCaption(itemId);
			}
		};
		operatorComboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
		operatorComboBox.addStyleName(XdevContainerFilterComponent.FILTER_COMBOBOX_CLASS);
		operatorComboBox.setTextInputAllowed(false);
		operatorComboBox.setInputPrompt(StringResourceUtils
				.getResourceString("ContainerFilterComponent.selectOption",this));
		operatorComboBox.setNullSelectionAllowed(false);
		operatorComboBox.setItemCaptionMode(ItemCaptionMode.ID_TOSTRING);
		return operatorComboBox;
	}


	protected ComboBox getOperatorComboBox()
	{
		return this.operatorComboBox;
	}


	protected Button createRemoveFilterButton()
	{
		final Button removeFilterButton = new Button();
		removeFilterButton.setIcon(FontAwesome.MINUS);
		removeFilterButton.addStyleName(ValoTheme.BUTTON_SMALL);
		removeFilterButton.addStyleName(XdevContainerFilterComponent.FILTER_REMOVE_BUTTON_CLASS);
		removeFilterButton.setDescription(StringResourceUtils
				.getResourceString("ContainerFilterComponent.removeFilter",this));
		return removeFilterButton;
	}


	protected Button getRemoveFilterButton()
	{
		return this.removeFilterButton;
	}


	protected Button createAddFilterButton()
	{
		final Button addFilterButton = new Button();
		addFilterButton.setIcon(FontAwesome.PLUS);
		addFilterButton.addStyleName(ValoTheme.BUTTON_SMALL);
		addFilterButton.addStyleName(XdevContainerFilterComponent.FILTER_ADD_BUTTON_CLASS);
		addFilterButton.setDescription(
				StringResourceUtils.getResourceString("ContainerFilterComponent.addFilter",this));
		return addFilterButton;
	}


	protected Button getAddFilterButton()
	{
		return this.addFilterButton;
	}


	protected List<PropertyEntry> getPropertyEntries()
	{
		return Arrays.stream(this.containerFilterComponent.getFilterableProperties())
				.map(id -> new PropertyEntry(id,this.containerFilterComponent.getPropertyType(id),
						this.containerFilterComponent.getPropertyCaption(id)))
				.collect(Collectors.toList());
	}


	protected void propertySelectionChanged()
	{
		removeValueEditors();

		final Object value = this.propertyComboBox.getValue();
		if(value instanceof PropertyEntry)
		{
			this.selectedPropertyEntry = (PropertyEntry)value;
			this.operatorComboBox.removeAllItems();
			this.operatorComboBox.addItems(FilterOperatorRegistry.getFilterOperators().stream()
					.filter(op -> op
							.isPropertyTypeSupported(this.selectedPropertyEntry.getPropertyType()))
					.collect(Collectors.toList()));
			this.operatorComboBox.setVisible(true);
		}
		else
		{
			this.selectedPropertyEntry = null;
			this.operatorComboBox.setVisible(false);
		}

		this.containerFilterComponent.layoutFilters();
	}


	protected void operatorSelectionChanged()
	{
		final List<Object> lastValues = removeValueEditors();

		final Object value = this.operatorComboBox.getValue();
		if(value instanceof FilterOperator)
		{
			this.selectedOperator = (FilterOperator)value;
			final FilterContext context = new FilterContext.Implementation(
					this.containerFilterComponent,this.containerFilterComponent.getContainer(),
					this.selectedPropertyEntry.getPropertyId());
			this.valueEditors = this.selectedOperator.createValueEditors(context,
					this.selectedPropertyEntry.getPropertyType());
			for(int i = 0, c = Math.min(lastValues.size(),this.valueEditors.length); i < c; i++)
			{
				this.valueEditors[i].setFilterValue(lastValues.get(i));
			}
			for(int i = 0; i < this.valueEditors.length; i++)
			{
				this.valueEditors[i].addFilterFieldChangeListener(this.filterFieldChangeListener);
			}
		}
		else
		{
			this.selectedOperator = null;
		}

		this.containerFilterComponent.layoutFilters();
	}


	private List<Object> removeValueEditors()
	{
		final List<Object> lastValues = new ArrayList<>();

		if(this.valueEditors != null)
		{
			for(final FilterField<?> valueEditor : this.valueEditors)
			{
				lastValues.add(valueEditor.getFilterValue());
				valueEditor.removeFilterFieldChangeListener(this.filterFieldChangeListener);
			}

			this.valueEditors = null;
		}

		return lastValues;
	}


	protected FilterField<?>[] getValueEditors()
	{
		return this.valueEditors;
	}


	public Filter getFilter()
	{
		if(this.selectedPropertyEntry == null || this.selectedOperator == null
				|| this.valueEditors == null)
		{
			return null;
		}

		final FilterContext context = new FilterContext.Implementation(
				this.containerFilterComponent,this.containerFilterComponent.getContainer(),
				this.selectedPropertyEntry.getPropertyId());
		return this.selectedOperator.createFilter(context,
				this.selectedPropertyEntry.getPropertyType(),this.valueEditors);
	}
}
