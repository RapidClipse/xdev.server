
package com.xdev.ui.entitycomponent.hierarchical;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ItemSorter;
import com.vaadin.data.util.filter.UnsupportedFilterException;


/**
 *
 * @param <IDTYPE>
 *            The type of the item identifier
 * @param <BEANTYPE>
 *            The type of the Bean
 *
 *
 * @see BeanContainer
 * @see ContainerHierarchicalWrapper
 * @see Hierarchical
 */
public abstract class AbstractHierarchicalBeanContainer<IDTYPE, BEANTYPE> extends
		BeanContainer<IDTYPE, BEANTYPE> implements Container.Hierarchical,
		Container.ItemSetChangeListener, Container.PropertySetChangeListener
{

	private static final long								serialVersionUID	= 1L;

	/**
	 * The {@link Hierarchical} wrapper.
	 */
	private final ContainerHierarchicalWrapper				wrapper;

	/**
	 * The wrapped {@link Container} ({@link BeanContainer})
	 */
	private final BeanContainer<IDTYPE, BEANTYPE>			container;

	private final HierarchicalBeanBuilder<IDTYPE, BEANTYPE>	hierarchicalBeanBuilder;


	/**
	 * Create a {@link HierarchicalBeanContainer} with the given IDTYPE and
	 * BEANTYPE.
	 *
	 * @param type
	 *            The type of the Bean
	 * @param container
	 *            The container to be wrapped
	 */
	public AbstractHierarchicalBeanContainer(final Class<BEANTYPE> type,
			final BeanContainer<IDTYPE, BEANTYPE> container,
			final HierarchicalBeanBuilder<IDTYPE, BEANTYPE> builder)
	{
		super(type);
		this.container = container;
		this.hierarchicalBeanBuilder = builder;
		
		this.wrapper = new ContainerHierarchicalWrapper(container);
		this.wrapper.addItemSetChangeListener(this);
		this.wrapper.addPropertySetChangeListener(this);
	}


	@Override
	public void containerPropertySetChange(final PropertySetChangeEvent event)
	{
		// nottin to do here
	}


	@Override
	public void containerItemSetChange(final ItemSetChangeEvent event)
	{
		// Map the parent and their children
		final Map<IDTYPE, Collection<IDTYPE>> mapParent = new HashMap<IDTYPE, Collection<IDTYPE>>();
		// Traverse the container
		for(final IDTYPE itemId : this.container.getItemIds())
		{
			final BEANTYPE bean = this.container.getItem(itemId).getBean();
			final Collection<IDTYPE> ids = this.hierarchicalBeanBuilder.getChildren(bean);
			if(ids != null && !ids.isEmpty())
			{
				mapParent.put(itemId,ids);
			}
		}
		// Traverse the map / set parents
		for(final Entry<IDTYPE, Collection<IDTYPE>> entry : mapParent.entrySet())
		{
			final IDTYPE parent = entry.getKey();
			this.wrapper.setChildrenAllowed(parent,true);
			for(final IDTYPE id : entry.getValue())
			{
				this.wrapper.setParent(id,parent);
			}
		}
	}


	// /**
	// * Set the {@link HierarchicalBeanBuilder}
	// *
	// * @param hierarchicalBeanBuilder
	// */
	// public void setHierarchicalBeanBuilder(
	// final HierarchicalBeanBuilder<IDTYPE, BEANTYPE> hierarchicalBeanBuilder)
	// {
	// this.hierarchicalBeanBuilder = hierarchicalBeanBuilder;
	// }

	/**
	 * Get the {@link ContainerHierarchicalWrapper} wrapper
	 *
	 * @return the wrapper
	 */
	protected ContainerHierarchicalWrapper getWrapper()
	{
		return this.wrapper;
	}


	/**
	 * Get the wrapped container
	 *
	 * @return the container
	 */
	protected BeanContainer<IDTYPE, BEANTYPE> getContainer()
	{
		return this.container;
	}


	/**
	 * Get the {@link HierarchicalBeanBuilder}
	 *
	 * @return the {@link HierarchicalBeanBuilder}
	 */
	public HierarchicalBeanBuilder<IDTYPE, BEANTYPE> getHierarchicalBeanBuilder()
	{
		return this.hierarchicalBeanBuilder;
	}


	// wrapper forwarding
	@Override
	public Collection<?> getChildren(final Object itemId)
	{
		return this.wrapper.getChildren(itemId);
	}


	@Override
	public Object getParent(final Object itemId)
	{
		return this.wrapper.getParent(itemId);
	}


	@Override
	public Collection<?> rootItemIds()
	{
		return this.wrapper.rootItemIds();
	}


	@Override
	public boolean setParent(final Object itemId, final Object newParentId)
			throws UnsupportedOperationException
	{
		return this.wrapper.setParent(itemId,newParentId);
	}


	@Override
	public boolean areChildrenAllowed(final Object itemId)
	{
		return this.wrapper.areChildrenAllowed(itemId);
	}


	@Override
	public boolean setChildrenAllowed(final Object itemId, final boolean areChildrenAllowed)
			throws UnsupportedOperationException
	{
		return this.wrapper.setChildrenAllowed(itemId,areChildrenAllowed);
	}


	@Override
	public boolean isRoot(final Object itemId)
	{
		return this.wrapper.isRoot(itemId);
	}


	@Override
	public boolean hasChildren(final Object itemId)
	{
		return this.wrapper.hasChildren(itemId);
	}


	@Override
	public BeanItem<BEANTYPE> addItem(final IDTYPE itemId, final BEANTYPE bean)
	{
		return this.container.addItem(itemId,bean);
	}


	@Override
	public BeanItem<BEANTYPE> addItemAfter(final IDTYPE previousItemId, final IDTYPE newItemId,
			final BEANTYPE bean)
	{
		return this.container.addItemAfter(previousItemId,newItemId,bean);
	}


	@Override
	public BeanItem<BEANTYPE> addItemAt(final int index, final IDTYPE newItemId, final BEANTYPE bean)
	{
		return this.container.addItemAt(index,newItemId,bean);
	}


	@Override
	public abstract void setBeanIdProperty(Object propertyId);


	@Override
	public void setBeanIdResolver(
			final com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver<IDTYPE, BEANTYPE> beanIdResolver)
	{
		this.container.setBeanIdResolver(beanIdResolver);
	}


	@Override
	public BeanItem<BEANTYPE> addBean(final BEANTYPE bean) throws IllegalStateException,
			IllegalArgumentException
	{
		return this.container.addBean(bean);
	}


	@Override
	public BeanItem<BEANTYPE> addBeanAfter(final IDTYPE previousItemId, final BEANTYPE bean)
			throws IllegalStateException, IllegalArgumentException
	{
		return this.container.addBeanAfter(previousItemId,bean);
	}


	@Override
	public BeanItem<BEANTYPE> addBeanAt(final int index, final BEANTYPE bean)
			throws IllegalStateException, IllegalArgumentException
	{
		return this.container.addBeanAt(index,bean);
	}


	@Override
	public void addAll(final Collection<? extends BEANTYPE> collection)
			throws IllegalStateException
	{
		this.container.addAll(collection);
	}


	@Override
	public Class<?> getType(final Object propertyId)
	{
		return this.container.getType(propertyId);
	}


	@Override
	public Class<? super BEANTYPE> getBeanType()
	{
		return this.container.getBeanType();
	}


	@Override
	public Collection<String> getContainerPropertyIds()
	{
		return this.container.getContainerPropertyIds();
	}


	@Override
	public boolean removeAllItems()
	{
		return this.container.removeAllItems();
	}


	@Override
	public BeanItem<BEANTYPE> getItem(final Object itemId)
	{
		return this.container.getItem(itemId);
	}


	@Override
	public List<IDTYPE> getItemIds()
	{
		return this.container.getItemIds();
	}


	@Override
	public Property<?> getContainerProperty(final Object itemId, final Object propertyId)
	{
		return this.container.getContainerProperty(itemId,propertyId);
	}


	@Override
	public boolean removeItem(final Object itemId)
	{
		return this.container.removeItem(itemId);
	}


	@Override
	public void valueChange(final ValueChangeEvent event)
	{
		this.container.valueChange(event);
	}


	@Override
	public void addContainerFilter(final Object propertyId, final String filterString,
			final boolean ignoreCase, final boolean onlyMatchPrefix)
	{
		this.container.addContainerFilter(propertyId,filterString,ignoreCase,onlyMatchPrefix);
	}


	@Override
	public void removeAllContainerFilters()
	{
		this.container.removeAllContainerFilters();
	}


	@Override
	public void removeContainerFilters(final Object propertyId)
	{
		this.container.removeContainerFilters(propertyId);
	}


	@Override
	public void addContainerFilter(final Filter filter) throws UnsupportedFilterException
	{
		this.container.addContainerFilter(filter);
	}


	@Override
	public void removeContainerFilter(final Filter filter)
	{
		this.container.removeContainerFilter(filter);
	}


	@Override
	public Collection<?> getSortableContainerPropertyIds()
	{
		return this.container.getSortableContainerPropertyIds();
	}


	@Override
	public void sort(final Object[] propertyId, final boolean[] ascending)
	{
		this.container.sort(propertyId,ascending);
	}


	@Override
	public ItemSorter getItemSorter()
	{
		return this.container.getItemSorter();
	}


	@Override
	public void setItemSorter(final ItemSorter itemSorter)
	{
		this.container.setItemSorter(itemSorter);
	}


	@Override
	public com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver<IDTYPE, BEANTYPE> getBeanIdResolver()
	{
		return this.container.getBeanIdResolver();
	}


	@Override
	public void addListener(final PropertySetChangeListener listener)
	{
		this.container.addPropertySetChangeListener(listener);
	}


	@Override
	public void removeListener(final PropertySetChangeListener listener)
	{
		this.container.removePropertySetChangeListener(listener);
	}


	@Override
	public boolean addNestedContainerProperty(final String propertyId)
	{
		return this.container.addNestedContainerProperty(propertyId);
	}


	@Override
	public boolean removeContainerProperty(final Object propertyId)
			throws UnsupportedOperationException
	{
		return this.container.removeContainerProperty(propertyId);
	}


	@Override
	public int size()
	{
		return this.container.size();
	}


	@Override
	public boolean containsId(final Object itemId)
	{
		return this.container.containsId(itemId);
	}


	@Override
	public IDTYPE nextItemId(final Object itemId)
	{
		return this.container.nextItemId(itemId);
	}


	@Override
	public IDTYPE prevItemId(final Object itemId)
	{
		return this.container.prevItemId(itemId);
	}


	@Override
	public IDTYPE firstItemId()
	{
		return this.container.firstItemId();
	}


	@Override
	public IDTYPE lastItemId()
	{
		return this.container.lastItemId();
	}


	@Override
	public boolean isFirstId(final Object itemId)
	{
		return this.container.isFirstId(itemId);
	}


	@Override
	public boolean isLastId(final Object itemId)
	{
		return this.container.isLastId(itemId);
	}


	@Override
	public IDTYPE getIdByIndex(final int index)
	{
		return this.container.getIdByIndex(index);
	}


	@Override
	public int indexOfId(final Object itemId)
	{
		return this.container.indexOfId(itemId);
	}


	@Override
	public Object addItemAt(final int index) throws UnsupportedOperationException
	{
		return this.container.addItemAt(index);
	}


	@Override
	public Item addItemAt(final int index, final Object newItemId)
			throws UnsupportedOperationException
	{
		return this.container.addItemAt(index,newItemId);
	}


	@Override
	public Object addItemAfter(final Object previousItemId) throws UnsupportedOperationException
	{
		return this.container.addItemAfter(previousItemId);
	}


	@Override
	public Item addItemAfter(final Object previousItemId, final Object newItemId)
			throws UnsupportedOperationException
	{
		return this.container.addItemAfter(previousItemId,newItemId);
	}


	@Override
	public Item addItem(final Object itemId) throws UnsupportedOperationException
	{
		return this.container.addItem(itemId);
	}


	@Override
	public Object addItem() throws UnsupportedOperationException
	{
		return this.container.addItem();
	}


	@Override
	public void addListener(final ItemSetChangeListener listener)
	{
		this.container.addItemSetChangeListener(listener);
	}


	@Override
	public void removeListener(final ItemSetChangeListener listener)
	{
		this.container.removeItemSetChangeListener(listener);
	}


	@Override
	public Collection<?> getListeners(final Class<?> eventType)
	{
		return this.container.getListeners(eventType);
	}

}