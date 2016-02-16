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

package com.xdev.ui.filter;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.metamodel.Attribute;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.ReflectTools;
import com.xdev.res.StringResourceUtils;
import com.xdev.ui.XdevComponent;
import com.xdev.ui.XdevGridLayout;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.util.CaptionUtils;
import com.xdev.util.JPAMetaDataUtils;


/**
 * @author XDEV Software
 * 		
 * @since 1.0.2
 */
public class XdevContainerFilterComponent extends CustomComponent
		implements FilterSettings, XdevComponent
{
	public final static String	FILTER_ADD_BUTTON_CLASS		= "x-containerfilter-add-button";
	public final static String	FILTER_REMOVE_BUTTON_CLASS	= "x-containerfilter-remove-button";
	public final static String	FILTER_SEARCHFIELD_CLASS	= "x-containerfilter-searchfield";
	public final static String	FILTER_COMBOBOX_CLASS		= "x-containerfilter-combobox";
	public final static String	FILTER_EDITOR_CLASS			= "x-containerfilter-editor";
															
															
															
	public static class FilterChangeEvent extends Component.Event
	{
		private final Filter filter;
		
		
		public FilterChangeEvent(final Component source, final Filter filter)
		{
			super(source);
			this.filter = filter;
		}
		
		
		public Filter getFilter()
		{
			return this.filter;
		}
	}



	public static interface FilterChangeListener extends EventListener, Serializable
	{
		public static final Method FILTER_CHANGED_METHOD = ReflectTools
				.findMethod(FilterChangeListener.class,"filterChanged",FilterChangeEvent.class);


		public void filterChanged(FilterChangeEvent event);
	}
	
	private final Extensions			extensions				= new Extensions();
																
	private final VerticalLayout		rootLayout;
	private final TextFilterField		searchTextField;
	private final Button				addFilterButton;
	private final XdevGridLayout		filterLayout;
										
	private Container.Filterable		container;
	private Collection<?>				filterableProperties	= Collections.emptyList();
	private Collection<?>				searchableProperties	= Collections.emptyList();
																
	private SearchFilterGenerator		searchFilterGenerator	= new SearchFilterGenerator.Default();
																
	private boolean						caseSensitive			= false;
	private char						wildcard				= '*';
																
	private final List<FilterEditor>	filterEditors			= new ArrayList<>();
	private boolean						layoutFilters			= true;
																
	private Filter						currentFilter;
										
										
	/**
	 *
	 */
	public XdevContainerFilterComponent()
	{
		this.searchTextField = createSearchTextField();
		this.searchTextField.addFilterFieldChangeListener(event -> {
			updateContainerFilter();
		});
		
		this.addFilterButton = createAddFilterButton();
		this.addFilterButton.addClickListener(event -> addFilterEditor(0));
		
		final HorizontalLayout headerLayout = new HorizontalLayout(this.searchTextField,
				this.addFilterButton);
		headerLayout.setMargin(false);
		headerLayout.setSpacing(true);
		
		this.searchTextField.setWidth(100,Unit.PERCENTAGE);
		headerLayout.setExpandRatio(this.searchTextField,1f);
		
		this.filterLayout = new XdevGridLayout();
		this.filterLayout.setMargin(false);
		this.filterLayout.setSpacing(true);
		this.filterLayout.setColumnExpandRatio(2,1f);
		this.filterLayout.setVisible(false);
		
		this.rootLayout = new VerticalLayout(headerLayout,this.filterLayout);
		this.rootLayout.setMargin(false);
		this.rootLayout.setSpacing(true);
		
		headerLayout.setWidth(100,Unit.PERCENTAGE);
		this.rootLayout.setExpandRatio(headerLayout,1f);
		
		this.filterLayout.setWidth(100,Unit.PERCENTAGE);
		this.rootLayout.setExpandRatio(this.filterLayout,1f);
		
		setCompositionRoot(this.rootLayout);
	}
	
	
	protected TextFilterField createSearchTextField()
	{
		final TextFilterField searchTextField = new TextFilterField();
		searchTextField.addStyleName(FILTER_SEARCHFIELD_CLASS);
		return searchTextField;
	}
	
	
	public TextFilterField getSearchTextField()
	{
		return this.searchTextField;
	}
	
	
	protected Button createAddFilterButton()
	{
		final Button addFilterButton = new Button();
		addFilterButton.setIcon(FontAwesome.PLUS);
		addFilterButton.addStyleName(ValoTheme.BUTTON_SMALL);
		addFilterButton.addStyleName(FILTER_ADD_BUTTON_CLASS);
		addFilterButton.setDescription(
				StringResourceUtils.getResourceString("ContainerFilterComponent.addFilter",this));
		return addFilterButton;
	}
	
	
	public Button getAddFilterButton()
	{
		return this.addFilterButton;
	}
	
	
	public void setContainer(final Container.Filterable container,
			final Object... filterableProperties)
	{
		this.container = container;
		this.filterableProperties = filterableProperties != null && filterableProperties.length > 0
				? Arrays.asList(filterableProperties) : container.getContainerPropertyIds();
		setSearchableProperties(this.filterableProperties.stream()
				.filter(p -> getPropertyType(p) == String.class).toArray());
				
		reset();
	}
	
	
	/**
	 * @return the container
	 */
	public Container.Filterable getContainer()
	{
		return this.container;
	}
	
	
	public void setSearchableProperties(final Object... searchableProperties)
	{
		this.searchableProperties = Arrays.asList(searchableProperties);
		
		updateSearchTextFieldInputPrompt();
	}
	
	
	protected void updateSearchTextFieldInputPrompt()
	{
		final String res = StringResourceUtils
				.getResourceString("ContainerFilterComponent.searchTextFieldInputPrompt",this);
		final String properties = this.searchableProperties.stream()
				.map(id -> getPropertyCaption(id)).collect(Collectors.joining(", "));
		final String prompt = MessageFormat.format(res,properties);
		this.searchTextField.setInputPrompt(prompt);
	}
	
	
	@Override
	public Object[] getFilterableProperties()
	{
		return this.filterableProperties.toArray();
	}
	
	
	@Override
	public Object[] getSearchableProperties()
	{
		return this.searchableProperties.toArray();
	}
	
	
	public void setSearchFilterGenerator(final SearchFilterGenerator searchFilterGenerator)
	{
		this.searchFilterGenerator = searchFilterGenerator;
	}
	
	
	public SearchFilterGenerator getSearchFilterGenerator()
	{
		return this.searchFilterGenerator;
	}
	
	
	public void setFilterEnabled(final boolean enabled)
	{
		this.addFilterButton.setVisible(enabled);
	}
	
	
	public boolean isFilterEnabled()
	{
		return this.addFilterButton.isVisible();
	}
	
	
	public void setCaseSensitive(final boolean caseSensitive)
	{
		this.caseSensitive = caseSensitive;
	}
	
	
	@Override
	public boolean isCaseSensitive()
	{
		return this.caseSensitive;
	}
	
	
	public void setWildcard(final char wildcard)
	{
		this.wildcard = wildcard;
	}
	
	
	@Override
	public char getWildcard()
	{
		return this.wildcard;
	}
	
	
	protected void reset()
	{
		if(this.container != null)
		{
			this.container.removeAllContainerFilters();
		}
		this.searchTextField.setValue("");
		this.filterEditors.clear();
		this.filterLayout.removeAllComponents();
	}
	
	
	protected void updateContainerFilter()
	{
		if(this.container != null)
		{
			final Filter newFilter = createFilter();
			
			if(!Objects.equals(newFilter,this.currentFilter))
			{
				if(this.currentFilter != null)
				{
					this.container.removeContainerFilter(this.currentFilter);
				}
				if(newFilter != null)
				{
					this.container.addContainerFilter(newFilter);
				}
				
				this.currentFilter = newFilter;

				fireEvent(new FilterChangeEvent(this,newFilter));
			}
		}
	}
	
	
	protected Filter createFilter()
	{
		final Filter searchFilter = createSearchFilter();
		final Filter valueFilter = createValueFilter();
		if(searchFilter != null && valueFilter != null)
		{
			return new And(searchFilter,valueFilter);
		}
		if(searchFilter != null)
		{
			return searchFilter;
		}
		if(valueFilter != null)
		{
			return valueFilter;
		}
		return null;
	}
	
	
	protected Filter createSearchFilter()
	{
		if(this.searchFilterGenerator != null)
		{
			return this.searchFilterGenerator.createSearchFilter(getSearchText(),this);
		}
		
		return null;
	}
	
	
	protected Filter createValueFilter()
	{
		if(this.filterEditors.isEmpty())
		{
			return null;
		}
		
		final List<Filter> valueFilters = this.filterEditors.stream()
				.map(editor -> editor.getFilter()).filter(Objects::nonNull)
				.collect(Collectors.toList());
		if(valueFilters.isEmpty())
		{
			return null;
		}
		
		final int count = valueFilters.size();
		if(count == 1)
		{
			return valueFilters.get(0);
		}
		
		return new And(valueFilters.toArray(new Filter[count]));
	}
	
	
	public Filter getFilter()
	{
		return this.currentFilter;
	}
	
	
	public String getSearchText()
	{
		return (String)this.searchTextField.getFilterValue();
	}
	
	
	public void setSearchText(final String searchText)
	{
		this.searchTextField.setFilterValue(searchText != null ? searchText : "");
	}
	
	
	public FilterData[] getFilterData()
	{
		final List<FilterData> list = this.filterEditors.stream().map(FilterEditor::getFilterData)
				.filter(Objects::nonNull).collect(Collectors.toList());
		return list.toArray(new FilterData[list.size()]);
	}
	
	
	public void setFilterData(final FilterData[] filterData)
	{
		try
		{
			this.layoutFilters = false;
			
			this.filterEditors.clear();
			if(filterData != null)
			{
				for(final FilterData data : filterData)
				{
					this.filterEditors.add(new FilterEditor(this,data));
				}
			}
		}
		finally
		{
			this.layoutFilters = true;
			layoutFilters();
		}
	}
	
	
	protected FilterEditor addFilterEditorAfter(final FilterEditor filterEditor)
	{
		return addFilterEditor(this.filterEditors.indexOf(filterEditor) + 1);
	}
	
	
	protected FilterEditor addFilterEditor(final int index)
	{
		final FilterEditor editor = createFilterEditor();
		this.filterEditors.add(index,editor);
		layoutFilters();
		return editor;
	}
	
	
	protected FilterEditor createFilterEditor()
	{
		return new FilterEditor(this);
	}
	
	
	protected void removeFilterEditor(final FilterEditor filterEditor)
	{
		this.filterEditors.remove(filterEditor);
		layoutFilters();
	}
	
	
	protected FilterEditor[] getFilterEditors()
	{
		return this.filterEditors.toArray(new FilterEditor[this.filterEditors.size()]);
	}
	
	
	protected void layoutFilters()
	{
		if(!this.layoutFilters)
		{
			return;
		}
		
		this.filterLayout.removeAllComponents();
		this.filterLayout.setRows(1);
		
		int row = 0;
		
		for(final FilterEditor filterEditor : this.filterEditors)
		{
			this.filterLayout.addComponent(filterEditor.getPropertyComboBox(),0,row);
			this.filterLayout.addComponent(filterEditor.getOperatorComboBox(),1,row);
			this.filterLayout.addComponent(filterEditor.getRemoveFilterButton(),3,row);
			this.filterLayout.addComponent(filterEditor.getAddFilterButton(),4,row);
			
			final FilterField<?>[] valueEditors = filterEditor.getValueEditors();
			if(valueEditors != null && valueEditors.length > 0)
			{
				if(valueEditors.length == 1)
				{
					this.filterLayout.addComponent(valueEditors[0],2,row);
				}
				else
				{
					final HorizontalLayout hLayout = new HorizontalLayout();
					hLayout.setMargin(false);
					hLayout.setSpacing(true);
					for(final FilterField<?> valueEditor : valueEditors)
					{
						hLayout.addComponent(valueEditor);
					}
					this.filterLayout.addComponent(hLayout,2,row);
				}
			}
			
			row++;
		}
		
		this.filterLayout.setVisible(row > 0);
		
		updateContainerFilter();
	}
	
	
	protected String getPropertyCaption(final Object propertyId)
	{
		final Container.Filterable container = getContainer();
		if(container instanceof XdevBeanContainer<?>)
		{
			return CaptionUtils.resolveEntityMemberCaption(
					((XdevBeanContainer<?>)container).getBeanType(),propertyId.toString());
		}
		
		return propertyId.toString();
	}
	
	
	protected Class<?> getPropertyType(final Object propertyId)
	{
		final Class<?> propertyType = this.container.getType(propertyId);
		
		if(propertyType == null)
		{
			final Container.Filterable container = getContainer();
			if(container instanceof XdevBeanContainer<?>)
			{
				final Class<?> beanType = ((XdevBeanContainer<?>)container).getBeanType();
				final Attribute<?, ?> attribute = JPAMetaDataUtils.resolveAttribute(beanType,
						propertyId.toString());
				if(attribute != null)
				{
					return attribute.getJavaType();
				}
			}
		}
		
		return propertyType;
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


	public void addFilterChangeListener(final FilterChangeListener listener)
	{
		addListener(FilterChangeEvent.class,listener,FilterChangeListener.FILTER_CHANGED_METHOD);
	}
	
	
	public void removeFilterChangeListener(final FilterChangeListener listener)
	{
		removeListener(FilterChangeEvent.class,listener,FilterChangeListener.FILTER_CHANGED_METHOD);
	}
}
