/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.entitycomponent.hierarchical;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.XdevTree;
import com.xdev.ui.hierarchical.DynamicHierarchicalContainer;
import com.xdev.ui.hierarchical.TreeDataProvider;
import com.xdev.util.Caption;


/**
 * @deprecated replaced by {@link TreeDataProvider}
 *			
 * @see TreeDataProvider
 * @see DynamicHierarchicalContainer
 * @see XdevHierarchicalBeanItemContainer
 * @see XdevTree#setContainerDataSource(TreeDataProvider, boolean)
 */
@Deprecated
public interface FillTree
{
	/**
	 * @deprecated see {@link FillTree} for more information
	 */
	@Deprecated
	public static enum Strategy
	{
		/**
		 * Creates the hierarchical structure beginning with the root entries.
		 * All entries are going to be added, even if the entries have no
		 * children. This is the default strategy.
		 */
		TOP_DOWN,
		
		/**
		 * Creates the hierarchical structure beginning with the bottom most
		 * child entries. Only entries which have children are going to be
		 * added.
		 */
		BOTTOM_UP
	}
	
	
	public void setHierarchicalReceiver(AbstractSelect treeComponent);
	
	
	public AbstractSelect getHierarchicalReceiver();
	
	
	public void setStrategy(Strategy strategy);
	
	
	public <T> Group addRootGroup(Class<T> clazz);
	
	
	/**
	 * @deprecated use {@link #addRootGroup(Class)} instead, caption is now
	 *             handled by {@link Caption}
	 */
	@Deprecated
	public default Group addRootGroup(final Class<?> clazz, final Field caption)
	{
		return addRootGroup(clazz);
	}
	
	
	public Group addGroup(Class<?> clazz, Field parentReference);
	
	
	/**
	 * @deprecated use {@link #addGroup(Class, Field)} instead, caption is now
	 *             handled by {@link Caption}
	 */
	@Deprecated
	public default Group addGroup(final Class<?> clazz, final Field parentReference,
			final Field caption)
	{
		return addGroup(clazz,parentReference);
	}
	
	
	public <T> void setGroupData(Class<T> groupClass, Collection<T> data);
	
	
	public void fillTree(HierarchicalContainer container);
	
	
	
	/**
	 * @deprecated see {@link FillTree} for more information
	 */
	@Deprecated
	public class Implementation implements FillTree
	{
		public static final Group		ROOT_IDENTIFIER		= null;
															
		private AbstractSelect			treeComponent;
										
		// no/null parent means root group
		// group as key, parent as value - map
		private final Map<Group, Group>	treeReferenceMap	= new HashMap<Group, Group>();
															
		private Strategy				strategy			= Strategy.TOP_DOWN;
															
															
		@Override
		public void setHierarchicalReceiver(final AbstractSelect treeComponent)
		{
			this.treeComponent = treeComponent;
		}
		
		
		@Override
		public AbstractSelect getHierarchicalReceiver()
		{
			return this.treeComponent;
		}
		
		
		@Override
		public void setStrategy(final Strategy strategy)
		{
			this.strategy = strategy;
		}
		
		
		@Override
		public <T> Group addRootGroup(final Class<T> clazz)
		{
			final Group node = new Group.Implementation(clazz,null);
			
			this.treeReferenceMap.put(node,ROOT_IDENTIFIER);
			
			return node;
		}
		
		
		@Override
		public Group addGroup(final Class<?> clazz, final Field parentReference)
		{
			final Group childGroup = new Group.Implementation(clazz,parentReference);
			final Group parentGroup = getParentGroup(parentReference);
			this.treeReferenceMap.put(childGroup,parentGroup);
			return childGroup;
		}
		
		
		protected Group getParentGroup(final Field parentReference)
		{
			final Class<?> referenceType = parentReference.getType();
			if(List.class.isAssignableFrom(referenceType)
					|| Set.class.isAssignableFrom(referenceType))
			{
				final Type genericType = parentReference.getGenericType();
				if(genericType instanceof ParameterizedType)
				{
					final Type[] typeArguments = ((ParameterizedType)genericType)
							.getActualTypeArguments();
					if(typeArguments != null && typeArguments.length == 1)
					{
						final Type collectionType = typeArguments[0];
						if(collectionType instanceof Class)
						{
							return getParentGroup((Class<?>)collectionType);
						}
					}
				}
			}
			else
			{
				return getParentGroup(referenceType);
			}
			
			return null;
		}
		
		
		protected Group getParentGroup(final Class<?> referenceType)
		{
			return this.treeReferenceMap.keySet().stream()
					.filter(node -> node.getGroupClass().equals(referenceType)).findFirst()
					.orElse(null);
		}
		
		
		@Override
		public <T> void setGroupData(final Class<T> groupClass, final Collection<T> data)
		{
			for(final Group node : this.treeReferenceMap.keySet())
			{
				if(node.getGroupClass().equals(groupClass))
				{
					node.setGroupData(data);
				}
			}
		}
		
		
		@Override
		public void fillTree(final HierarchicalContainer container)
		{
			switch(this.strategy)
			{
				case TOP_DOWN:
					fillTopDown(container);
				break;
				
				case BOTTOM_UP:
					fillBottomUp(container);
				break;
			}
			
			// Hide tree icon for leaf items
			for(final Object id : container.getItemIds())
			{
				if(!container.hasChildren(id))
				{
					container.setChildrenAllowed(id,false);
				}
			}
			
			this.getHierarchicalReceiver().setContainerDataSource(container);
		}
		
		
		protected void fillTopDown(final HierarchicalContainer container)
		{
			for(final Group parentGroup : this.treeReferenceMap.keySet())
			{
				if(this.treeReferenceMap.get(parentGroup) == ROOT_IDENTIFIER)
				{
					for(final Object rootValue : parentGroup.getGroupData())
					{
						container.addItem(rootValue);
						addChildren(container,parentGroup,rootValue);
					}
				}
			}
		}
		
		
		protected void addChildren(final HierarchicalContainer container, final Group parentGroup,
				final Object parentValue)
		{
			for(final Group childGroup : this.getChildGroups(parentGroup))
			{
				for(final Object childValue : childGroup.getGroupData())
				{
					if(isChild(childGroup,parentValue,childValue))
					{
						container.addItem(childValue);
						container.setParent(childValue,parentValue);
						
						addChildren(container,childGroup,childValue);
					}
				}
			}
		}
		
		
		protected void fillBottomUp(final HierarchicalContainer container)
		{
			for(final Group parentGroup : this.treeReferenceMap.keySet())
			{
				addChildren(container,parentGroup);
			}
		}
		
		
		protected void addChildren(final HierarchicalContainer container, final Group parentGroup)
		{
			for(final Group childGroup : this.getChildGroups(parentGroup))
			{
				for(final Object parentValue : parentGroup.getGroupData())
				{
					for(final Object childValue : childGroup.getGroupData())
					{
						if(isChild(childGroup,parentValue,childValue))
						{
							container.addItem(parentValue);
							container.addItem(childValue);
							container.setParent(childValue,parentValue);
						}
					}
				}
			}
		}
		
		
		// -------------- TREE UTILITIES -------------------
		
		protected Collection<Group> getChildGroups(final Group group)
		{
			final Collection<Group> children = new ArrayList<Group>();
			
			// key set equals child nodes
			for(final Group node : this.treeReferenceMap.keySet())
			{
				// value equals related parent node
				final Group parentNode = this.treeReferenceMap.get(node);
				if(parentNode != ROOT_IDENTIFIER && parentNode.equals(group))
				{
					children.add(node);
				}
			}
			return children;
		}
		
		
		private boolean isChild(final Group childGroup, final Object parentValue,
				final Object childValue)
		{
			final Object referenceValue = this.getReferenceValue(childValue,
					childGroup.getReference());
			if(referenceValue instanceof Collection)
			{
				return ((Collection<?>)referenceValue).contains(parentValue);
			}
			return parentValue.equals(referenceValue);
		}
		
		
		// TODO create tailored exception type
		private Object getReferenceValue(final Object referrer, final Field referenceField)
				throws RuntimeException
		{
			try
			{
				referenceField.setAccessible(true);
				final Object referenceValue = referenceField.get(referrer);
				return referenceValue;
			}
			catch(IllegalArgumentException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		
		protected Group getRootGroup()
		{
			for(final Group parent : this.treeReferenceMap.values())
			{
				if(parent == ROOT_IDENTIFIER)
				{
					for(final Group node : this.treeReferenceMap.keySet())
					{
						if(treeReferenceMap.get(node) == parent)
						{
							return node;
						}
					}
				}
			}
			return null;
		}
	}
}
