/**
 * Copyright 2010 Tommi S.E. Laukkanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.addons.lazyquerycontainer;


import java.io.Serializable;
import java.util.Map;


/**
 * QueryFactory implementation for BeanQuery. BeanQuery can be used to simplify
 * implementation of queries returning JavaBeans.
 *
 * @param <Q>
 *            The BeanQuery implementation class
 * @author Tommi Laukkanen
 */
@SuppressWarnings("rawtypes")
public final class BeanQueryFactory<Q extends AbstractBeanQuery>
		implements QueryFactory, Serializable
{
	/**
	 * Java serialization version UID.
	 */
	private static final long	serialVersionUID	= 1L;
	/**
	 * Query configuration contains implementation specific configuration.
	 */
	private final Class<Q>		queryClass;
	/**
	 * The query implementation class.
	 */
	private Map<String, Object>	queryConfiguration;
	
	
	/**
	 * Constructs BeanQuery and sets the user defined parameters.
	 *
	 * @param queryClass
	 *            The BeanQuery class;
	 */
	public BeanQueryFactory(final Class<Q> queryClass)
	{
		super();
		this.queryClass = queryClass;
	}
	
	
	/**
	 * Sets the query configuration for the custom query implementation.
	 *
	 * @param queryConfiguration
	 *            The query configuration to be used by the custom query
	 *            implementation.
	 */
	public void setQueryConfiguration(final Map<String, Object> queryConfiguration)
	{
		this.queryConfiguration = queryConfiguration;
	}
	
	
	/**
	 * Constructs a new query according to the given QueryDefinition.
	 *
	 * @param queryDefinition
	 *            Properties participating in the sorting.
	 * @return A new query constructed according to the given sort state.
	 */
	@Override
	public Query constructQuery(final QueryDefinition queryDefinition)
	{
		Q query;
		
		try
		{
			query = this.queryClass.getConstructor(
					new Class[]{QueryDefinition.class,Map.class,Object[].class,boolean[].class})
					.newInstance(queryDefinition,this.queryConfiguration,
							queryDefinition.getSortPropertyIds(),
							queryDefinition.getSortPropertyAscendingStates());
		}
		catch(final Exception e)
		{
			throw new RuntimeException("Error instantiating query.",e);
		}
		
		return query;
	}
	
}
