/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.communication;


import javax.persistence.LockModeType;


/**
 * @author XDEV Software
 *
 */
public interface Conversation
{
	// public void setEntityManager(EntityManager em);
	//
	//
	// public EntityManager getEntityManager();

	public boolean isActive();


	public boolean isPessimisticUnit();


	public void setPessimisticUnit(boolean lockState, LockModeType type);


	public LockModeType getLockModeType();


	public void setLockModeType(final LockModeType lockModeType);


	public void start();


	public void end();



	public class Implementation implements Conversation
	{

		private boolean			isActive		= false;
		private boolean			pessimisticUnit	= false;
		private LockModeType	lockModeType;


		@Override
		public LockModeType getLockModeType()
		{
			return this.lockModeType;
		}


		@Override
		public void setLockModeType(final LockModeType lockModeType)
		{
			this.lockModeType = lockModeType;
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.Conversation#isActive()
		 */
		@Override
		public boolean isActive()
		{
			return this.isActive;
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.Conversation#start()
		 */
		@Override
		public void start()
		{
			this.isActive = true;
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.Conversation#end()
		 */
		@Override
		public void end()
		{
			this.isActive = false;
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.Conversation#isPessimisticUnit()
		 */
		@Override
		public boolean isPessimisticUnit()
		{
			return this.pessimisticUnit;
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.Conversation#setPessimisticUnit(boolean)
		 */
		@Override
		public void setPessimisticUnit(final boolean mode, final LockModeType type)
		{
			this.pessimisticUnit = mode;
			this.lockModeType = type;
		}

	}
}
