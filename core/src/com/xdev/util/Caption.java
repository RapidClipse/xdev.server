/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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

package com.xdev.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xdev.res.StringResourceProvider;


/**
 * The caption annotation defines the {@link String} representation of an
 * element.
 * <p>
 * The element can either be a type or an attribute (field respectively method).
 * <p>
 * If a type is annotated with @Caption the {@link #value()} can contain member
 * variables which will be replaced by their actual values of the object
 * instance. The variable syntax is <code>{%memberName}</code>.
 * <p>
 * String resource tags - <code>{$resourceVariable}</code> - are supported too,
 * see
 * {@link StringResourceProvider#lookupResourceString(String, java.util.Locale, Object)
 * String resources}.
 * <p>
 * Example:
 *
 * <pre>
 * &#64;Caption("Person: {%lastName}, {%firstName}")
 * &#64;Entity
 * public class Person
 * {
 * 	private String firstName;
 * 	private String lastName;
 * 	private Date dob;
 *  ...
 * }
 *
 * Person p = new Person();
 * p.setFirstName("Peter");
 * p.setLastName("Smith");
 * String caption = CaptionUtils.resolveCaption(p); // -> Person: Smith, Peter
 * </pre>
 *
 * The caption annotation is used e.g. by all UI components which contain
 * entites, such as combobox, list, table or filter. The table uses the
 * attributes' captions as column titles.
 *
 * @see CaptionUtils
 * @see CaptionResolver
 * 		
 * 		
 * @author XDEV Software
 * 		
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Caption
{
	/**
	 * The caption's format string. Details are shown in the {@link Caption
	 * class doc}.
	 */
	public String value() default "";
}
