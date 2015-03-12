
package com.xdev.ui.util.wizard;


import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.xdev.ui.entitycomponent.EntityComponent;
import com.xdev.ui.util.masterdetail.MasterDetail;


//master detail for forms
public interface FormBuilder<T> extends XdevExecutableCommandObject
{
	public void setMasterComponent(EntityComponent<T> masterComponent);


	public void setForm(BeanFieldGroup<T> form);



	public class XdevFormBuilder<T> implements FormBuilder<T>
	{
		private EntityComponent<T>	masterComponent;
		private BeanFieldGroup<T>	form;
		private final MasterDetail	masterDetail;


		public XdevFormBuilder()
		{
			this.masterDetail = new MasterDetail.Implementation();
		}


		@Override
		public void execute()
		{
			this.masterDetail.connectForm(this.masterComponent,this.form);
		}


		@Override
		public void setMasterComponent(final EntityComponent<T> masterComponent)
		{
			this.masterComponent = masterComponent;
		}


		@Override
		public void setForm(final BeanFieldGroup<T> form)
		{
			this.form = form;
		}
	}

}
