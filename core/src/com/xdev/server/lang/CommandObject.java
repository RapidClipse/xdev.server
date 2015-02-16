
package com.xdev.server.lang;


public interface CommandObject
{
	//enable lambdability
	public abstract <T> void execute(T delegator);
	
	
	public abstract void init();
	
}
