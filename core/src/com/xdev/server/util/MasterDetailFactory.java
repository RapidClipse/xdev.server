
package com.xdev.server.util;




public class MasterDetailFactory implements GenericFactory<MasterDetail>
{
	private MasterDetail	instance;
	
	
	@Override
	public MasterDetail getImplementation()
	{
		if(instance != null)
		{
			return instance;
		}
		instance = new MasterDetail.Implementation();
		return instance;
	}
	
}
