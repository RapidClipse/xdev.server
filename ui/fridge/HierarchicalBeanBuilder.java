
package com.xdev.ui.entitycomponent.hierarchical;


import java.util.Collection;


/**
 *
 *
 * @param <IDTYPE>
 *            The type of the item identifier
 * @param <BEANTYPE>
 *            The type of the Bean
 *
 */
public interface HierarchicalBeanBuilder<IDTYPE, BEANTYPE>
{

	/**
	 * Get a {@link Collection} of IDTYPE for the given bean
	 *
	 * @param bean
	 *            The bean
	 * @return The children of type IDTYPE
	 */
	Collection<IDTYPE> getChildren(BEANTYPE bean);
}