/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.security.authentication;

import static com.xdev.security.Utils.notNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xdev.security.configuration.xml.XmlConfiguration;
import com.xdev.security.configuration.xml.XmlSubject;


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
	 * @return the username/password entries in the form of a {@link Map} instance.
	 */
	public static Map<String, String> buildEntries(final List<XmlSubject> subjects)
	{
		final HashMap<String, String> entries = new HashMap<>(subjects.size());

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
