
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


/**
 * Returns a Vaadin Item property chain.
 * 
 */
// TODO create VaadinItemPathConcatenator to avoid manual "." appending
public class XdevEntityReferenceResolver implements EntityReferenceResolver
{
	private final Configuration		config;
	private final EntityIDResolver	idResolver;
	
	
	public XdevEntityReferenceResolver()
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
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public String getReferenceEntityPropertyName(Class<?> referenceEntity, Class<?> entity)
			throws RuntimeException
	{
		PersistentClass clazz = this.config.getClassMapping(entity.getName());
		Property ref = null;
		
		for(@SuppressWarnings("unchecked")
		Iterator<Property> i = clazz.getReferenceablePropertyIterator(); i.hasNext();)
		{
			Property it = i.next();
			/*
			 * not only referenceable properties are returned, hence a manual
			 * check is required
			 */
			if(this.getReferencedPropertyName(it.getValue()) != null)
			{
				ref = it;
				String propertyName = this.getReferencedPropertyName(ref.getValue());
				
				if(propertyName != null)
				{
					if(propertyName.equals(referenceEntity.getName()))
					{
						return ref.getName();
					}
				}
			}
		}
		return getReferenceEntityPropertyname(referenceEntity,entity,ref);
	}
	
	
	// look deeper into entity
	protected String getReferenceEntityPropertyname(Class<?> referenceEntity,
			Class<?> previousClass, Property previousProperty) throws RuntimeException
	{
		if(previousProperty != null)
		{
			String name = previousProperty.getName() + ".";
			previousClass = previousProperty.getType().getReturnedClass();
			PersistentClass clazz = this.config.getClassMapping(previousClass.getName());
			
			for(@SuppressWarnings("unchecked")
			Iterator<Property> i = clazz.getReferenceablePropertyIterator(); i.hasNext();)
			{
				Property it = i.next();
				/*
				 * not only referenceable properties are returned, hence a
				 * manual check is required
				 */
				if(this.getReferencedPropertyName(it.getValue()) != null)
				{
					previousProperty = it;
					String propertyName = this.getReferencedPropertyName(previousProperty
							.getValue());
					
					if(propertyName != null)
					{
						if(propertyName.equals(referenceEntity.getName()))
						{
							return this.attachId(name,previousProperty);
						}
					}
				}
			}
		}
		return getReferenceEntityPropertyname(referenceEntity,previousClass,previousProperty);
	}
	
	
	private String attachId(String itemPropertyPath, Property property)
	{
		Class<?> javaClass = property.getType().getReturnedClass();
		return itemPropertyPath + property.getName() + "."
				+ this.idResolver.getEntityIDProperty(javaClass).getName();
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
