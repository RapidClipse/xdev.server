/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authentication;

import static net.jadoth.Jadoth.notNull;

import java.io.File;
import java.util.List;

import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.types.XGettingMap;
import xdev.security.configuration.xml.XmlConfiguration;
import xdev.security.configuration.xml.XmlSubject;


/**
 * {@link InMemoryAuthenticator.Provider} implementation that loads the required username/password data from
 * a specified XML file.
 *
 * @author XDEV Software (TM)
 */
public final class XmlInMemoryAuthenticatorProvider implements InMemoryAuthenticator.Provider
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Creates a new {@link XmlInMemoryAuthenticatorProvider} instance that uses the passed {@link File} instance
	 * to read the configuration.
	 *
	 * @param xmlFile the xml file to read the configuration from.
	 * @return a new {@link XmlInMemoryAuthenticatorProvider} instance
	 */
	public static final XmlInMemoryAuthenticatorProvider New(final File xmlFile)
	{
		return new XmlInMemoryAuthenticatorProvider(
			notNull(xmlFile)
		);
	}

	/**
	 * Read the authentication data from the passed file and returns an new {@link InMemoryAuthenticator} instance
	 * based on that data.
	 *
	 * @param xmlFile the xml file to read the configuration from.
	 * @return a new {@link InMemoryAuthenticator} instance.
	 * @see #buildAuthenticator(XmlConfiguration)
	 */
	public static InMemoryAuthenticator provideAuthenticatorFromFile(final File xmlFile)
	{
		final XmlConfiguration xmlConfig = XmlConfiguration.readFromFile(xmlFile);
		return buildAuthenticator(xmlConfig);
	}

	/**
	 * Returns a new {@link InMemoryAuthenticator} instance based on the passed {@link XmlConfiguration} instance.
	 *
	 * @param xmlConfig the {@link XmlConfiguration} instance containing the configuration data.
	 * @return a new {@link InMemoryAuthenticator} instance based on the passed data.
	 */
	public static InMemoryAuthenticator buildAuthenticator(final XmlConfiguration xmlConfig)
	{
		return InMemoryAuthenticator.New(buildEntries(xmlConfig.subjects()));
	}

	/**
	 * Transforms the passed {@link List} of {@link XmlSubject} instances into a string-string map of username/password
	 * entries.
	 *
	 * @param subjects the {@link List} of {@link XmlSubject} instances from which the entries shall be built.
	 * @return the username/password entries in the form of a {@link XGettingMap} instance.
	 */
	public static XGettingMap<String, String> buildEntries(final List<XmlSubject> subjects)
	{
		final EqHashTable<String, String> entries = EqHashTable.NewCustom(subjects.size());

		for(final XmlSubject subject : subjects)
		{
			entries.put(subject.name(), subject.password());
		}

		return entries;
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/**
	 * The xml file to read the configuration from.
	 */
	private final File xmlFile;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Implementation-detail constructor.
	 *
	 * @param xmlFile The xml file to read the configuration from.
	 */
	XmlInMemoryAuthenticatorProvider(final File xmlFile)
	{
		super();
		this.xmlFile = xmlFile;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * Creates a new {@link InMemoryAuthenticator} instance from the configuration defined in the xml file.
	 *
	 * @return a new {@link InMemoryAuthenticator} instance.
	 */
	@Override
	public final InMemoryAuthenticator provideAuthenticator()
	{
		return provideAuthenticatorFromFile(this.xmlFile);
	}

}
