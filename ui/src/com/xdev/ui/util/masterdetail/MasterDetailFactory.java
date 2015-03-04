
package com.xdev.ui.util.masterdetail;


import com.xdev.server.util.GenericFactory;


public class MasterDetailFactory implements GenericFactory<MasterDetail>
{
	private MasterDetail	instance;


	@Override
	public MasterDetail getImplementation()
	{
		if(this.instance != null)
		{
			return this.instance;
		}
		this.instance = new MasterDetail.Implementation();
		return this.instance;
	}

}
