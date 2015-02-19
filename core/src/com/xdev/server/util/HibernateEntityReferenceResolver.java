
package com.xdev.server.util;


import java.util.Iterator;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Value;

import com.xdev.server.communication.EntityManagerHelper;


public class HibernateEntityReferenceResolver implements EntityReferenceResolver
{
	private final Configuration	config;
	
	
	public HibernateEntityReferenceResolver()
	{
		this.config = new Configuration();
		Set<EntityType<?>> set = EntityManagerHelper.getEntityManager().getMetamodel()
				.getEntities();
		
		for(Iterator<EntityType<?>> i = set.iterator(); i.hasNext();)
		{
			Class<?> eClazz = i.next().getJavaType();
			try
			{
				this.config.addClass(eClazz);
			}
			catch(MappingException e)
			{
				this.config.addAnnotatedClass(eClazz);
			}
		}
		this.config.buildMappings();
	}
	
	
	@Override
	public String getReferenceEntityPropertyName(Class<?> referenceEntity, Class<?> entity)
	{
		PersistentClass clazz = this.config.getClassMapping(entity.getName());
		
		for(@SuppressWarnings("unchecked")
		Iterator<Property> i = clazz.getReferenceablePropertyIterator(); i.hasNext();)
		{
			//TODO check, it seems not only referenceable properties are returned.
			Property ref = i.next();
			String propertyName = this.getReferencedPropertyName(ref.getValue());
			
			if(propertyName != null)
			{
				if(propertyName.equals(referenceEntity.getName()))
				{
					return ref.getName();
				}
			}
		}
		return null;
	}
	
	
	private String getReferencedPropertyName(Value value)
	{
		/*
		 * luckily there is no super type for each relation type e.g.
		 * one-to-many many-to-many, one-to-one have are directly created are
		 * not inherited from a type like relation, they are independently
		 * inherited from value...
		 */
		
		final OneToMany oneToMany = getOneToMany(value);
		if(oneToMany != null)
		{
			return oneToMany.getReferencedEntityName();
		}
		else if(value instanceof ManyToOne)
		{
			final ManyToOne manyToOne = (ManyToOne)value;
			return manyToOne.getReferencedEntityName();
		}
		else if(value instanceof OneToOne)
		{
			final OneToOne oneToOne = (OneToOne)value;
			return oneToOne.getReferencedEntityName();
		}
		// else if(value instanceof SimpleValue)
		// {
		// final SimpleValue primitive = (OneToOne)value;
		// return primitive.getR
		// }
		
		return null;
	}
	
	
	private OneToMany getOneToMany(Value value)
	{
		// in case of wrapping because bidirectional
		if(value instanceof Collection)
		{
			value = ((Collection)value).getElement();
		}
		if(value instanceof OneToMany)
		{
			return (OneToMany)value;
		}
		return null;
	}
}
