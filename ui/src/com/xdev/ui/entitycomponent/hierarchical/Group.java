
package com.xdev.ui.entitycomponent.hierarchical;


import java.lang.reflect.Field;
import java.util.Collection;


public interface Group
{
	public Class<?> getGroupClass();


	public Field getReference();


	// Field Value from Annotation or XML, or simply toString
	/*
	 * TODO if caption is not set at item creation time then use #toString
	 * result if possible.
	 */
	public Field getCaption();


	public Collection<?> getGroupData();


	public void setGroupData(Collection<?> groupData);



	// public void setData(Collection<?> data);

	public class Implementation implements Group
	{
		private final Class<?>	clazz;
		private final Field		identifier;
		private final Field		caption;
		private Collection<?>	groupData;


		public Implementation(final Class<?> clazz, final Field reference, final Field caption)
		{
			this.clazz = clazz;
			this.identifier = reference;
			this.caption = caption;
		}


		@Override
		public Field getReference()
		{
			return this.identifier;
		}


		@Override
		public Field getCaption()
		{
			return this.caption;

		}


		@Override
		public Class<?> getGroupClass()
		{
			return this.clazz;
		}


		@Override
		public Collection<?> getGroupData()
		{
			return this.groupData;
		}


		@Override
		public void setGroupData(final Collection<?> groupData)
		{
			this.groupData = groupData;
		}


		@Override
		public String toString()
		{
			if(this.clazz != null)
			{
				return this.clazz.getName();
			}
			return super.toString();
		}
	}
}
