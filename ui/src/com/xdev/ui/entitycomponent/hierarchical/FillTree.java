
package com.xdev.ui.entitycomponent.hierarchical;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;


public interface FillTree
{
	
	// public void setHierarchical(Hierarchical treeComponent);
	//
	//
	// public Hierarchical getHierarchical();
	//
	
	// return void or group?
	public <T> void addRootGroup(Class<T> clazz, Field caption);
	
	
	// return void or group?
	public void addGroup(Class<?> clazz, Field parentReference, Field caption);
	
	
	public <T> void setGroupData(Class<T> groupClass, Collection<T> data);
	
	
	public HierarchicalContainer fillTree(HierarchicalContainer container);
	
	
	
	public class Implementation implements FillTree
	{
		public static final Object		ROOT_IDENTIFIER		= null;
		// no/null parent means root group
		// group as key, parent as value - map
		private final Map<Group, Group>	treeReferenceMap	= new HashMap<Group, Group>();
		
		
		// private Hierarchical treeComponent;
		
		// @Override
		// public void setHierarchical(final Hierarchical treeComponent)
		// {
		// this.treeComponent = treeComponent;
		// }
		//
		//
		// @Override
		// public Hierarchical getHierarchical()
		// {
		// return this.treeComponent;
		// }
		
		@Override
		public <T> void addRootGroup(final Class<T> clazz, final Field caption)
		{
			// TODO caption provider
			final Group node = new Group.Implementation(clazz,null,caption);
			
			// TODO null as root identifier
			this.treeReferenceMap.put(node,null);
		}
		
		
		@Override
		public void addGroup(final Class<?> clazz, final Field parentReference, final Field caption)
		{
			final Group childNode = new Group.Implementation(clazz,parentReference,caption);
			
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
		public HierarchicalContainer fillTree(HierarchicalContainer container)
		{
			// final Group rootGroup = this.getRootGroup();
			for(final Group parentNode : this.treeReferenceMap.keySet())
			{
				container = addChildren(container,parentNode);
			}
			return container;
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
						if(parentDataItem.equals(this.getReferenceValue(childDataItem,
								childGroup.getReference())))
						{
							/*
							 * create parent item, child item and add child to
							 * parent item
							 */
							final Object parentCaption = this.getCaptionValue(parentDataItem,
									parentGroup);
							/*
							 * it is possible that parent items may be already
							 * child items from parents at a higher hierarchy so
							 * add only items if not already existent
							 */
							this.addItemIfNotExistent(container,parentCaption);
							
							final Object childCaption = this.getCaptionValue(childDataItem,
									childGroup);
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
		
		
		// TODO create tailored exception type
		private Object getCaptionValue(final Object item, final Group captionGroupInfo)
				throws RuntimeException
		{
			if(captionGroupInfo.getCaption() != null)
			{
				try
				{
					/*
					 * TODO add caption annotation to getter and safely access
					 * annotated getter without the need to manipulate field
					 * access rules
					 */
					captionGroupInfo.getCaption().setAccessible(true);
					final Object captionValue = captionGroupInfo.getCaption().get(item);
					return captionValue;
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
			else
			{
				// toString fallback
				return item.toString();
			}
		}
	}
}
