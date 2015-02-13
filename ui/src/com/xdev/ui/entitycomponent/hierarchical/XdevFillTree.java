
package com.xdev.ui.entitycomponent.hierarchical;


import java.util.Collection;

import com.vaadin.data.util.HierarchicalContainer;
import com.xdev.server.util.EntityReferenceResolver;
import com.xdev.server.util.HibernateEntityReferenceResolver;
import com.xdev.ui.util.annotation.Caption;
import com.xdev.ui.util.annotation.EntityFieldAnnotation;


//Just a Delegate to FillTree.Implementation for utility purpose
public class XdevFillTree
{
	private final FillTree					fillTreeComposite;
	private final EntityReferenceResolver	referenceResolver;
	private final EntityFieldAnnotation		captionAnnotation;


	/**
	 *
	 */
	public XdevFillTree()
	{
		super();
		this.fillTreeComposite = new FillTree.Implementation();
		// this.fillTreeComposite.setHierarchical(treeComponent);
		this.referenceResolver = new HibernateEntityReferenceResolver();
		this.captionAnnotation = new EntityFieldAnnotation();
	}


	/*
	 * --------------- UTILITY DELEGATORS -------------------
	 */

	// TODO create tailored exception type
	public <T> void addRootGroup(final Class<T> clazz) throws RuntimeException
	{
		this.fillTreeComposite.addRootGroup(clazz,
				this.captionAnnotation.getAnnotationField(clazz,Caption.class));
	}


	// TODO create tailored exception type
	public void addGroup(final Class<?> clazz, final Class<?> parentClazz) throws RuntimeException
	{
		// TODO check bidirectionals
		final String propertyName = this.referenceResolver.getReferenceEntityPropertyName(
				parentClazz,clazz);
		try
		{
			this.fillTreeComposite.addGroup(clazz,clazz.getDeclaredField(propertyName),
					this.captionAnnotation.getAnnotationField(clazz,Caption.class));
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


	public HierarchicalContainer fillTree()
	{
		return this.fillTreeComposite.fillTree(new HierarchicalContainer());
	}
}
