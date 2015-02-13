
package com.xdev.ui.entitycomponent.hierarchical;


import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;


/**
 *
 *
 * @param <IDTYPE>
 *            The type of the item identifier
 * @param <BEANTYPE>
 *            The type of the Bean
 *
 *
 * @see AbstractHierarchicalBeanContainer
 * @see BeanContainer
 * @see ContainerHierarchicalWrapper
 * @see Hierarchical
 */
public class HierarchicalBeanContainer<IDTYPE, BEANTYPE> extends
		AbstractHierarchicalBeanContainer<IDTYPE, BEANTYPE>
{

	private static final long	serialVersionUID	= 2360235181091674900L;


	/**
	 * Create a {@link HierarchicalBeanContainer} with the given IDTYPE and
	 * BEANTYPE.
	 *
	 * @param type
	 *            The type of the Bean
	 * @param hierarchicalBeanBuilder
	 *            The builder for {@link Hierarchical}
	 */
	public HierarchicalBeanContainer(final Class<BEANTYPE> type,
			final HierarchicalBeanBuilder<IDTYPE, BEANTYPE> hierarchicalBeanBuilder)
	{
		this(type,new BeanContainer<IDTYPE, BEANTYPE>(type),hierarchicalBeanBuilder);
	}


	public HierarchicalBeanContainer(final Class<BEANTYPE> type,
			final BeanContainer<IDTYPE, BEANTYPE> container,
			final HierarchicalBeanBuilder<IDTYPE, BEANTYPE> hierarchicalBeanBuilder)
	{
		super(type,container,hierarchicalBeanBuilder);
	}


	@Override
	public void setBeanIdProperty(final Object propertyId)
	{
		getContainer().setBeanIdProperty(propertyId);
	}

}