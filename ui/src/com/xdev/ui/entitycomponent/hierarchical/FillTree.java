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

package com.xdev.ui.entitycomponent.hierarchical;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.xdev.util.Caption;
import com.xdev.util.CaptionUtils;


public interface FillTree
{

	public void setHierarchicalReceiver(AbstractSelect treeComponent);


	public AbstractSelect getHierarchicalReceiver();


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



	public class Implementation implements FillTree
	{
		public static final Object		ROOT_IDENTIFIER		= null;
		// no/null parent means root group
		// group as key, parent as value - map
		private final Map<Group, Group>	treeReferenceMap	= new HashMap<Group, Group>();

		private AbstractSelect			treeComponent;


		@Override
		public <T> Group addRootGroup(final Class<T> clazz)
		{
			// TODO caption provider
			final Group node = new Group.Implementation(clazz,null);

			// TODO null as root identifier
			this.treeReferenceMap.put(node,null);

			return node;
		}


		@Override
		public Group addGroup(final Class<?> clazz, final Field parentReference)
		{
			final Group childNode = new Group.Implementation(clazz,parentReference);

			// to avoid concurrent modification exception
			Group parentNode = null;
			for(final Group node : this.treeReferenceMap.keySet())
			{
				if(node.getGroupClass().equals(parentReference.getType()))
				{
					parentNode = node;
				}
			}
			this.treeReferenceMap.put(childNode,parentNode);
			return childNode;
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
		public void fillTree(HierarchicalContainer container)
		{
			// final Group rootGroup = this.getRootGroup();
			for(final Group parentNode : this.treeReferenceMap.keySet())
			{
				container = addChildren(container,parentNode);
			}
			this.getHierarchicalReceiver().setContainerDataSource(container);
		}


		public HierarchicalContainer addChildren(final HierarchicalContainer container,
				final Group parentGroup)
		{
			for(final Group childGroup : this.getChildGroups(parentGroup))
			{
				for(final Object parentDataItem : parentGroup.getGroupData())
				{
					for(final Object childDataItem : childGroup.getGroupData())
					{
						if(parentDataItem.equals(
								this.getReferenceValue(childDataItem,childGroup.getReference())))
						{
							/*
							 * create parent item, child item and add child to
							 * parent item
							 */
							final Object parentCaption = this.getCaptionValue(parentDataItem);
							/*
							 * it is possible that parent items may be already
							 * child items from parents at a higher hierarchy so
							 * add only items if not already existent
							 */
							this.addItemIfNotExistent(container,parentCaption);

							final Object childCaption = this.getCaptionValue(childDataItem);
							this.addItemIfNotExistent(container,childCaption);

							container.setParent(childCaption,parentCaption);
						}
					}
				}
			}
			return container;
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


		// TODO create tailored exception type
		private Object getReferenceValue(final Object referrer, final Field referenceField)
				throws RuntimeException
		{
			try
			{
				/*
				 * TODO add caption annotation to getter and safely access
				 * annotated getter without the need to manipulate field access
				 * rules
				 */
				referenceField.setAccessible(true);
				final Object referenceValue = referenceField.get(referrer);
				return referenceValue;
			}
			catch(IllegalArgumentException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}


		private Item addItemIfNotExistent(final Container container, final Object itemID)
		{
			final Item item = container.addItem(itemID);
			if(item == null)
			{
				return container.getItem(itemID);
			}
			return item;
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


		private Object getCaptionValue(final Object item
		// , final Group captionGroupInfo
		)
		// throws RuntimeException
		{
			return CaptionUtils.resolveCaption(item);

			// if(captionGroupInfo.getCaption() != null)
			// {
			// try
			// {
			// /*
			// * TODO add caption annotation to getter and safely access
			// * annotated getter without the need to manipulate field
			// * access rules
			// */
			// captionGroupInfo.getCaption().setAccessible(true);
			// final Object captionValue =
			// captionGroupInfo.getCaption().get(item);
			// return captionValue;
			// }
			// catch(IllegalArgumentException | IllegalAccessException e)
			// {
			// throw new RuntimeException(e);
			// }
			// }
			// else
			// {
			// // toString fallback
			// return item.toString();
			// }
		}


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
	}
}
