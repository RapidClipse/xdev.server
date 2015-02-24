
package com.xdev.ui.paging;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Container.PropertySetChangeNotifier;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.ContainerHelpers;
import com.vaadin.data.Item;
import com.vaadin.data.Property;


/**
 * LazyQueryContainer provides lazy loading of items from business services. See
 * package level documentation for detailed description. This implements event
 * notification functionality and delegates other methods to QueryView
 * aggregate.
 *
 */
// copied from LazyQueryContainer to become extendable
public class XdevEntityLazyQueryContainer implements Indexed, Sortable, ItemSetChangeNotifier,
		PropertySetChangeNotifier, Buffered, Container.Filterable, Serializable
{
	/**
	 * Java serialization UID.
	 */
	private static final long						serialVersionUID			= 1L;
	/**
	 * QueryView where LazyQueryContainer delegates method calls to.
	 */
	// hard typed because of method addition to framework interface
	private final EntityLazyQueryView				queryView;
	/**
	 * List of registered ItemSetChangeListener.
	 */
	private final List<ItemSetChangeListener>		itemSetChangeListeners		= new ArrayList<ItemSetChangeListener>();
	/**
	 * List of registered PropertySetChangeListeners.
	 */
	private final List<PropertySetChangeListener>	propertySetChangeListeners	= new ArrayList<PropertySetChangeListener>();
	
	
	/**
	 * Constructs LazyQueryContainer with LazyQueryView and given queryFactory.
	 *
	 * @param queryFactory
	 *            The query factory to be used.
	 * @param idPropertyId
	 *            The ID of the ID property or null if item index is used as ID.
	 * @param batchSize
	 *            The batch size to be used when loading data.
	 * @param compositeItems
	 *            True if items are wrapped to CompositeItems.
	 */
	public XdevEntityLazyQueryContainer(final QueryFactory queryFactory, final Object idPropertyId,
			final int batchSize, final boolean compositeItems)
	{
		this.queryView = new EntityLazyQueryView(new LazyQueryDefinition(compositeItems,batchSize,
				idPropertyId),queryFactory);
	}
	
	
	/**
	 * Constructs LazyQueryContainer with LazyQueryView and given queryFactory
	 * and queryDefinition.
	 *
	 * @param queryFactory
	 *            The query factory to be used.
	 * @param queryDefinition
	 *            The query definition to be used.
	 */
	public XdevEntityLazyQueryContainer(final QueryDefinition queryDefinition,
			final QueryFactory queryFactory)
	{
		this.queryView = new EntityLazyQueryView(queryDefinition,queryFactory);
	}
	
	
	/**
	 * Constructs LazyQueryContainer with the given QueryView. This constructor
	 * role is to enable use of custom view implementations.
	 *
	 * @param queryView
	 *            The query view to be used.
	 */
	public XdevEntityLazyQueryContainer(final EntityLazyQueryView queryView)
	{
		this.queryView = queryView;
	}
	
	
	/**
	 * Sets new sort state and refreshes container.
	 *
	 * @param sortPropertyIds
	 *            The IDs of the properties participating in sort.
	 * @param ascendingStates
	 *            The sort state of the properties participating in sort. True
	 *            means ascending.
	 */
	@Override
	public final void sort(final Object[] sortPropertyIds, final boolean[] ascendingStates)
	{
		this.queryView.sort(sortPropertyIds,ascendingStates);
	}
	
	
	/**
	 * Lists of the property IDs queried.
	 *
	 * @return A list of property IDs queried.
	 */
	@Override
	public final Collection<?> getContainerPropertyIds()
	{
		return this.queryView.getQueryDefinition().getPropertyIds();
	}
	
	
	/**
	 * List of the property IDs which can be sorted.
	 *
	 * @return A list of the property IDs which can be sorted.
	 */
	@Override
	public final Collection<?> getSortableContainerPropertyIds()
	{
		return this.queryView.getQueryDefinition().getSortablePropertyIds();
	}
	
	
	/**
	 * Gets the property value class of the given property.
	 *
	 * @param propertyId
	 *            If of the property of interest.
	 * @return The value class of the given property.
	 */
	@Override
	public final Class<?> getType(final Object propertyId)
	{
		return this.queryView.getQueryDefinition().getPropertyType(propertyId);
	}
	
	
	/**
	 * Adds a new property to the definition.
	 *
	 * @param propertyId
	 *            Id of the property.
	 * @param type
	 *            Value class of the property.
	 * @param defaultValue
	 *            Default value of the property.
	 * @return always true.
	 */
	@Override
	public final boolean addContainerProperty(final Object propertyId, final Class<?> type,
			final Object defaultValue)
	{
		this.queryView.getQueryDefinition().addProperty(propertyId,type,defaultValue,true,false);
		notifyPropertySetChanged();
		return true;
	}
	
	
	/**
	 * Adds a new property to the definition.
	 *
	 * @param propertyId
	 *            Id of the property.
	 * @param type
	 *            Value class of the property.
	 * @param defaultValue
	 *            Default value of the property.
	 * @param readOnly
	 *            Read only state of the property.
	 * @param sortable
	 *            Sortable state of the property.
	 * @return always true.
	 */
	public final boolean addContainerProperty(final Object propertyId, final Class<?> type,
			final Object defaultValue, final boolean readOnly, final boolean sortable)
	{
		this.queryView.getQueryDefinition().addProperty(propertyId,type,defaultValue,readOnly,
				sortable);
		notifyPropertySetChanged();
		return true;
	}
	
	
	/**
	 * Removes the given property from the definition.
	 *
	 * @param propertyId
	 *            If of the property to be removed.
	 * @return always true.
	 */
	@Override
	public final boolean removeContainerProperty(final Object propertyId)
	{
		this.queryView.getQueryDefinition().removeProperty(propertyId);
		notifyPropertySetChanged();
		return true;
	}
	
	
	/**
	 * Number of items in the container.
	 *
	 * @return number of items.
	 */
	@Override
	public final int size()
	{
		return this.queryView.size();
	}
	
	
	/**
	 * Gets list of item indexes in the container.
	 *
	 * @return Collection of Integers.
	 */
	@Override
	public final Collection<?> getItemIds()
	{
		return this.queryView.getItemIdList();
	}
	
	
	/**
	 * Gets item at given index.
	 *
	 * @param itemId
	 *            index of the item.
	 * @return Item at the given index.
	 */
	@Override
	public final Item getItem(final Object itemId)
	{
		if(itemId == null)
		{
			return null;
		}
		else
		{
			return this.queryView.getItem(this.queryView.getItemIdList().indexOf(itemId));
		}
	}
	
	
	/**
	 * Gets property of an item.
	 *
	 * @param itemId
	 *            The index of the item.
	 * @param propertyId
	 *            ID of the property.
	 * @return the property corresponding to given IDs.
	 */
	@Override
	public final Property<?> getContainerProperty(final Object itemId, final Object propertyId)
	{
		return getItem(itemId).getItemProperty(propertyId);
	}
	
	
	/**
	 * Gets ID of given index. In other words returns the index itself.
	 *
	 * @param index
	 *            The index of the item.
	 * @return the object ID i.e. index.
	 */
	@Override
	public final Object getIdByIndex(final int index)
	{
		return this.queryView.getItemIdList().get(index);
	}
	
	
	/**
	 * Gets index of given ID. In other words returns the index itself.
	 *
	 * @param itemId
	 *            the item ID.
	 * @return the index.
	 */
	@Override
	public final int indexOfId(final Object itemId)
	{
		return this.queryView.getItemIdList().indexOf(itemId);
	}
	
	
	/**
	 * True if container contains the given index.
	 *
	 * @param itemId
	 *            Item index.
	 * @return true if container contains the given index.
	 */
	@Override
	public final boolean containsId(final Object itemId)
	{
		return this.queryView.getItemIdList().contains(itemId);
	}
	
	
	/**
	 * Check if given index is first i.e. 0.
	 *
	 * @param itemId
	 *            the object index.
	 * @return true if index is 0.
	 */
	@Override
	public final boolean isFirstId(final Object itemId)
	{
		return this.queryView.getItemIdList().indexOf(itemId) == 0;
	}
	
	
	/**
	 * Check if given index is first i.e. size() - 1.
	 *
	 * @param itemId
	 *            the object index.
	 * @return true if index is size() - 1.
	 */
	@Override
	public final boolean isLastId(final Object itemId)
	{
		return this.queryView.getItemIdList().indexOf(itemId) == size() - 1;
	}
	
	
	/**
	 * @return first item ID i.e. 0.
	 */
	@Override
	public final Object firstItemId()
	{
		return this.queryView.getItemIdList().get(0);
	}
	
	
	/**
	 * @return last item ID i.e. size() - 1
	 */
	@Override
	public final Object lastItemId()
	{
		return this.queryView.getItemIdList().get(size() - 1);
	}
	
	
	/**
	 * @param itemId
	 *            the item index
	 * @return itemId + 1, or <code>null</code> if no such item
	 */
	@Override
	public final Object nextItemId(final Object itemId)
	{
		final List<?> itemIdList = this.queryView.getItemIdList();
		final int currentIndex = itemIdList.indexOf(itemId);
		if(currentIndex == -1 || currentIndex == itemIdList.size() - 1)
		{
			return null;
		}
		else
		{
			return itemIdList.get(currentIndex + 1);
		}
	}
	
	
	/**
	 * @param itemId
	 *            the item index
	 * @return itemId - 1, or <code>null</code> if no such item
	 */
	@Override
	public final Object prevItemId(final Object itemId)
	{
		final List<?> itemIdList = this.queryView.getItemIdList();
		final int currentIndex = itemIdList.indexOf(itemId);
		if(currentIndex == -1 || currentIndex == 0)
		{
			return null;
		}
		else
		{
			return itemIdList.get(currentIndex - 1);
		}
	}
	
	
	/**
	 * Not supported.
	 *
	 * @param index
	 *            item index
	 * @return new item ID.
	 */
	@Override
	public final Object addItemAt(final int index)
	{
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Not supported.
	 *
	 * @param previousItemId
	 *            ID of previous item.
	 * @return new item ID.
	 */
	@Override
	public final Object addItemAfter(final Object previousItemId)
	{
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Not supported.
	 *
	 * @param index
	 *            item index
	 * @param newItemId
	 *            ID of new item
	 * @return new Item
	 */
	@Override
	public final Item addItemAt(final int index, final Object newItemId)
	{
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Not supported.
	 *
	 * @param previousItemId
	 *            ID of previous item.
	 * @param newItemId
	 *            ID of new item
	 * @return new Item
	 */
	@Override
	public final Item addItemAfter(final Object previousItemId, final Object newItemId)
	{
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Not supported.
	 *
	 * @param itemId
	 *            itemId
	 * @return new Item
	 */
	@Override
	public final Item addItem(final Object itemId)
	{
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Constructs and adds new item.
	 *
	 * @return item index.
	 */
	@Override
	public final Object addItem()
	{
		final int itemIndex = this.queryView.addItem();
		final Object itemId = this.queryView.getItemIdList().get(itemIndex);
		notifyItemSetChanged();
		return itemId;
	}
	
	
	/**
	 * Removes Item at given index.
	 *
	 * @param itemId
	 *            Item index.
	 * @return always true.
	 */
	@Override
	public final boolean removeItem(final Object itemId)
	{
		this.queryView.removeItem(indexOfId(itemId));
		notifyItemSetChanged();
		return true;
	}
	
	
	/**
	 * Removes all items.
	 *
	 * @return always true.
	 */
	@Override
	public final boolean removeAllItems()
	{
		this.queryView.removeAllItems();
		refresh();
		return true;
	}
	
	
	/**
	 * Adds ItemSetChangeListener.
	 *
	 * @param listener
	 *            ItemSetChangeListener to be added.
	 */
	@Override
	@Deprecated
	public final void addListener(final ItemSetChangeListener listener)
	{
		addItemSetChangeListener(listener);
		
	}
	
	
	/**
	 * Removes ItemSetChangeListener.
	 *
	 * @param listener
	 *            ItemSetChangeListener to be removed.
	 */
	@Override
	@Deprecated
	public final void removeListener(final ItemSetChangeListener listener)
	{
		removeItemSetChangeListener(listener);
	}
	
	
	@Override
	public final void removeItemSetChangeListener(final ItemSetChangeListener listener)
	{
		this.itemSetChangeListeners.remove(listener);
	}
	
	
	/**
	 * Refreshes container.
	 */
	public final void refresh()
	{
		this.queryView.refresh();
		notifyItemSetChanged();
	}
	
	
	/**
	 * Notifies that item set has been changed.
	 */
	protected void notifyItemSetChanged()
	{
		final QueryItemSetChangeEvent event = new QueryItemSetChangeEvent(this);
		for(final ItemSetChangeListener listener : this.itemSetChangeListeners)
		{
			listener.containerItemSetChange(event);
		}
	}
	
	
	/**
	 * Adds PropertySetChangeListener.
	 *
	 * @param listener
	 *            PropertySetChangeListener to be added.
	 */
	@Override
	@Deprecated
	public final void addListener(final PropertySetChangeListener listener)
	{
		addPropertySetChangeListener(listener);
	}
	
	
	@Override
	public final void addItemSetChangeListener(final ItemSetChangeListener listener)
	{
		this.itemSetChangeListeners.add(listener);
	}
	
	
	@Override
	public final void addPropertySetChangeListener(final PropertySetChangeListener listener)
	{
		this.propertySetChangeListeners.add(listener);
	}
	
	
	/**
	 * Removes PropertySetChangeListener.
	 *
	 * @param listener
	 *            PropertySetChangeListener to be removed.
	 */
	@Override
	@Deprecated
	public final void removeListener(final PropertySetChangeListener listener)
	{
		removePropertySetChangeListener(listener);
	}
	
	
	@Override
	public final void removePropertySetChangeListener(final PropertySetChangeListener listener)
	{
		this.propertySetChangeListeners.remove(listener);
	}
	
	
	/**
	 * Notifies that property set has been changed.
	 */
	private void notifyPropertySetChanged()
	{
		final QueryPropertySetChangeEvent event = new QueryPropertySetChangeEvent(this);
		for(final PropertySetChangeListener listener : this.propertySetChangeListeners)
		{
			listener.containerPropertySetChange(event);
		}
	}
	
	
	@Override
	public void addContainerFilter(final Filter filter)
	{
		getQueryView().addFilter(filter);
		notifyItemSetChanged();
	}
	
	
	@Override
	public void removeContainerFilter(final Filter filter)
	{
		getQueryView().removeFilter(filter);
		notifyItemSetChanged();
	}
	
	
	@Override
	public void removeAllContainerFilters()
	{
		getQueryView().removeFilters();
		notifyItemSetChanged();
	}
	
	
	@Override
	public Collection<Filter> getContainerFilters()
	{
		return getQueryView().getFilters();
	}
	
	
	/**
	 * Adds default filter to underlying QueryDefinition.
	 *
	 * @param filter
	 *            the default filter
	 */
	public void addDefaultFilter(final Filter filter)
	{
		getQueryView().getQueryDefinition().addDefaultFilter(filter);
	}
	
	
	/**
	 * Removes default filter from underlying QueryDefinition.
	 *
	 * @param filter
	 *            the default filter
	 */
	public void removeDefaultFilter(final Filter filter)
	{
		getQueryView().getQueryDefinition().removeDefaultFilter(filter);
	}
	
	
	/**
	 * Removes all default filters from underlying QueryDefinition.
	 */
	public void removeDefaultFilters()
	{
		getQueryView().getQueryDefinition().removeDefaultFilters();
	}
	
	
	
	/**
	 * Private ItemSetChangeEvent implementation.
	 *
	 * @author Tommi Laukkanen
	 */
	private class QueryItemSetChangeEvent implements ItemSetChangeEvent
	{
		/**
		 * Java serialization version UID.
		 */
		private static final long					serialVersionUID	= 1L;
		/**
		 * The container where event occurred.
		 */
		private final XdevEntityLazyQueryContainer	container;
		
		
		/**
		 * Constructor for setting the container.
		 *
		 * @param container
		 *            the Container.
		 */
		public QueryItemSetChangeEvent(final XdevEntityLazyQueryContainer container)
		{
			this.container = container;
		}
		
		
		/**
		 * Gets the container where event occurred.
		 *
		 * @return the Container.
		 */
		@Override
		public Container getContainer()
		{
			return this.container;
		}
		
	}
	
	
	
	/**
	 * Private PropertySetChangeEvent implementation.
	 *
	 * @author Tommi Laukkanen
	 */
	private class QueryPropertySetChangeEvent implements PropertySetChangeEvent
	{
		/**
		 * Java serialization version UID.
		 */
		private static final long					serialVersionUID	= 1L;
		/**
		 * The container where event occurred.
		 */
		private final XdevEntityLazyQueryContainer	container;
		
		
		/**
		 * Constructor for setting the container.
		 *
		 * @param container
		 *            the Container.
		 */
		public QueryPropertySetChangeEvent(final XdevEntityLazyQueryContainer container)
		{
			this.container = container;
		}
		
		
		/**
		 * Gets the container where event occurred.
		 *
		 * @return the Container.
		 */
		@Override
		public Container getContainer()
		{
			return this.container;
		}
		
	}
	
	
	/**
	 * Commits changed and refreshes container.
	 */
	@Override
	public final void commit()
	{
		this.queryView.commit();
		refresh();
	}
	
	
	/**
	 * Discards changes and refreshes container.
	 */
	@Override
	public final void discard()
	{
		this.queryView.discard();
		refresh();
	}
	
	
	/**
	 * @return true if container contains not committed modifications.
	 */
	@Override
	public final boolean isModified()
	{
		return this.queryView.isModified();
	}
	
	
	/**
	 * @return the queryView
	 */
	public final EntityLazyQueryView getQueryView()
	{
		return this.queryView;
	}
	
	
	@Override
	public final void setBuffered(final boolean buffered)
	{
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public final boolean isBuffered()
	{
		return true;
	}
	
	
	@Override
	public final List<?> getItemIds(final int startIndex, final int numberOfItems)
	{
		return ContainerHelpers.getItemIdsUsingGetIdByIndex(startIndex,numberOfItems,this);
	}
	
}
