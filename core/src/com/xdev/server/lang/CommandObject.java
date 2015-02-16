
package com.xdev.server.lang;


public interface CommandObject
{
	//enable lambdability
	public abstract <T> void execute(T info);
	
	
	public abstract void init();
	
}
