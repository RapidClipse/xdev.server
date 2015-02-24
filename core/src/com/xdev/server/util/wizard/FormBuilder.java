
package com.xdev.server.util.wizard;


import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbstractSelect;
import com.xdev.server.util.MasterDetail;


//master detail for forms
public interface FormBuilder<T> extends XdevExecutableCommandObject
{
	public void setMasterComponent(AbstractSelect masterComponent);
	
	
	public void setForm(BeanFieldGroup<T> form);
	
	
	
	public class XdevFormBuilder<T> implements FormBuilder<T>
	{
		private AbstractSelect		masterComponent;
		private BeanFieldGroup<T>	form;
		private MasterDetail		masterDetail;
		
		
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
		public void setMasterComponent(AbstractSelect masterComponent)
		{
			this.masterComponent = masterComponent;
		}
		
		
		@Override
		public void setForm(BeanFieldGroup<T> form)
		{
			this.form = form;
		}
	}
	
}
