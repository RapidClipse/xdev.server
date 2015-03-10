
package com.xdev.server.util;


import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.Value;


public class HibernateMetaDataUtils
{
	public static String getReferencablePropertyName(final org.hibernate.mapping.Value value)
	{
		/*
		 * there is no super type for each relation type e.g.
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

		return null;
	}


	private static OneToMany getOneToMany(Value value)
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
