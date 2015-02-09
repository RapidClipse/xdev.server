
package com.xdev.server.util;

import org.hibernate.mapping.Property;


public interface EntityIDResolver
{
	 public <T> Property getEntityIDProperty(Class<T> entity);
}
