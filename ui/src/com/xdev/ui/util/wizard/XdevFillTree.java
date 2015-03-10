
package com.xdev.ui.util.wizard;


import java.util.Collection;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.xdev.server.util.Caption;
import com.xdev.server.util.EntityReferenceResolver;
import com.xdev.server.util.HibernateEntityReferenceResolver;
import com.xdev.server.util.ReflectionUtils;
import com.xdev.ui.entitycomponent.hierarchical.FillTree;
import com.xdev.ui.entitycomponent.hierarchical.Group;


//Just a Delegate to FillTree.Implementation for utility purpose
public class XdevFillTree implements XdevExecutableCommandObject
{
	private final FillTree					fillTreeComposite;
	private final EntityReferenceResolver	referenceResolver;


	/**
	 *
	 */
	public XdevFillTree(final AbstractSelect tree)
	{
		super();
		this.fillTreeComposite = new FillTree.Implementation();
		this.fillTreeComposite.setHierarchicalReceiver(tree);
		this.referenceResolver = new HibernateEntityReferenceResolver();
	}


	/*
	 * --------------- UTILITY DELEGATORS -------------------
	 */

	// TODO create tailored exception type
	public <T> Group addRootGroup(final Class<T> clazz) throws RuntimeException
	{
		return this.fillTreeComposite.addRootGroup(clazz,
				ReflectionUtils.getAnnotatedField(clazz,Caption.class));
	}


	// TODO create tailored exception type
	public Group addGroup(final Class<?> clazz, final Class<?> parentClazz) throws RuntimeException
	{
		// TODO check bidirectionals
		final String propertyName = this.referenceResolver.getReferenceEntityPropertyName(
				parentClazz,clazz);
		try
		{
			return this.fillTreeComposite.addGroup(clazz,clazz.getDeclaredField(propertyName),
					ReflectionUtils.getAnnotatedField(clazz,Caption.class));
		}
		catch(NoSuchFieldException | SecurityException e)
		{
			throw new RuntimeException(e);
		}
	}


	public <T> void setGroupData(final Class<T> groupClass, final Collection<T> data)
	{
		this.fillTreeComposite.setGroupData(groupClass,data);
	}


	@Override
	public void execute()
	{
		this.fillTreeComposite.fillTree(new HierarchicalContainer());
	}


	public void setHierarchicalReceiver(final AbstractSelect treeComponent)
	{
		this.fillTreeComposite.setHierarchicalReceiver(treeComponent);
	}


	public AbstractSelect getHierarchicalReceiver()
	{
		return this.fillTreeComposite.getHierarchicalReceiver();
	}
}
