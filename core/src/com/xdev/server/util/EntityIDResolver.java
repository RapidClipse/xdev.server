
package com.xdev.server.util;

import org.hibernate.mapping.Property;


public interface EntityIDResolver
{
	 public Property getEntityIDProperty(Class<?> entity);
}
