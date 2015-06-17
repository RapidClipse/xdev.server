
package com.xdev.security.authentication.ldap;


import java.util.Hashtable;

import javax.naming.Context;


public class LDAPConfiguration
{
	private final String					providerUrl;
	private String							principal, credential;
	
	// Optional
	private final String					searchbase, securityAuthentication, securityProtocol, /*
																								 * default
																								 * is
																								 * jndi
																								 */
											contextFactory;
	
	private final Hashtable<String, String>	ldapEnviromentConfiguration;
	
	
	/**
	 *
	 */
	private LDAPConfiguration(final LDAPConfigurationBuilder builder)
	{
		this.providerUrl = builder.providerUrl;
		
		// optionals
		this.searchbase = builder.searchbase;
		this.securityAuthentication = builder.securityAuthentication;
		this.securityProtocol = builder.securityProtocol;
		this.contextFactory = builder.contextFactory;
		
		this.ldapEnviromentConfiguration = new Hashtable<String, String>();
		this.ldapEnviromentConfiguration.put(Context.INITIAL_CONTEXT_FACTORY,
				this.getContextFactory());
		this.ldapEnviromentConfiguration.put(Context.PROVIDER_URL,this.getProviderUrl());
		this.ldapEnviromentConfiguration.put(Context.SECURITY_AUTHENTICATION,
				this.getSecurityAuthentication());
		
		if(getSecurityProtocol() != null && !getSecurityProtocol().isEmpty())
		{
			this.ldapEnviromentConfiguration.put(Context.SECURITY_PROTOCOL,
					this.getSecurityAuthentication());
		}
	}
	
	
	
	public static class LDAPConfigurationBuilder implements
			com.xdev.security.authentication.ldap.Builder<LDAPConfiguration>
	{
		private final String	providerUrl;
		
		// Optional
		private String			searchbase, securityAuthentication = "none", securityProtocol,
				contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
		
		
		/**
 *
 */
		public LDAPConfigurationBuilder(final String providerUrl)
		{
			this.providerUrl = providerUrl;
		}
		
		
		public LDAPConfigurationBuilder searchBase(final String searchBase)
		{
			this.searchbase = searchBase;
			return this;
		}
		
		
		public LDAPConfigurationBuilder securityAuthentication(final String securityAuthentication)
		{
			this.securityAuthentication = securityAuthentication;
			return this;
		}
		
		
		public LDAPConfigurationBuilder securityProtocol(final String securityProtocol)
		{
			this.securityProtocol = securityProtocol;
			return this;
		}
		
		
		public LDAPConfigurationBuilder contextFactory(final String contextFactory)
		{
			this.contextFactory = contextFactory;
			return this;
		}
		
		
		@Override
		public LDAPConfiguration build()
		{
			return new LDAPConfiguration(this);
		}
	}
	
	
	public void setPrincipal(final String principal)
	{
		this.principal = principal;
		this.ldapEnviromentConfiguration.put(Context.SECURITY_PRINCIPAL,this.getPrincipal());
	}
	
	
	public void setCredential(final String credential)
	{
		this.credential = credential;
		this.ldapEnviromentConfiguration.put(Context.SECURITY_CREDENTIALS,this.getCredential());
	}
	
	
	public String getProviderUrl()
	{
		return this.providerUrl;
	}
	
	
	public String getPrincipal()
	{
		return this.principal;
	}
	
	
	public String getCredential()
	{
		return this.credential;
	}
	
	
	public String getSearchbase()
	{
		return this.searchbase;
	}
	
	
	public String getSecurityAuthentication()
	{
		return this.securityAuthentication;
	}
	
	
	public String getSecurityProtocol()
	{
		return this.securityProtocol;
	}
	
	
	public String getContextFactory()
	{
		return this.contextFactory;
	}
	
	
	public Hashtable<String, String> getLdapEnviromentConfiguration()
	{
		return this.ldapEnviromentConfiguration;
	}
}
