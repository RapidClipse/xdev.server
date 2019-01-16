/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.communication;


import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;


/**
 * @author XDEV Software
 * @since 4.0
 */
public class HibernateIntegrator implements Integrator
{
	static
	{
		try
		{
			XdevURLStreamHandlerFactory.installIfNeeded();
		}
		catch(final Exception e)
		{
		}
	}
	
	
	@Override
	public void integrate(final Metadata metadata, final SessionFactoryImplementor sessionFactory,
			final SessionFactoryServiceRegistry serviceRegistry)
	{
	}


	@Override
	public void disintegrate(final SessionFactoryImplementor sessionFactory,
			final SessionFactoryServiceRegistry serviceRegistry)
	{
	}
}
