
package com.xdev.server.util;


public interface EntityReferenceResolver
{
	public String getReferenceEntityPropertyName(Class<?> referenceEntity, Class<?> entity);
}
