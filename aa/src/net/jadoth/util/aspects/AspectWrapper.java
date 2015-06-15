package net.jadoth.util.aspects;

import java.lang.reflect.Proxy;

import net.jadoth.reflect.JadothReflect;


public class AspectWrapper<T> implements java.lang.reflect.InvocationHandler
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	final T subject;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public AspectWrapper(final T subject)
	{
		super();
		this.subject = subject;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	protected T getSubject()
	{
		return this.subject;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	protected Class<?>[] getProxyInterfaces()
	{
		if(this.subject instanceof AspectWrapper<?>){
			return ((AspectWrapper<?>)this.subject).getProxyInterfaces();
		}
		return JadothReflect.getClassHierarchyInterfaces(this.subject.getClass());
	}

	@SuppressWarnings("unchecked")
	public T newProxyInstance()
	{
		return (T)Proxy.newProxyInstance(this.subject.getClass().getClassLoader(), this.getProxyInterfaces(), this);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public Object invoke(final Object proxy, final java.lang.reflect.Method method, final Object[] args) throws Throwable
	{
		// keep up pretending to be the subject, no matter the called method's actual logic
		final Object returnValue;
		return (returnValue = method.invoke(this.subject, args)) == this.subject ?proxy :returnValue;
	}

}
