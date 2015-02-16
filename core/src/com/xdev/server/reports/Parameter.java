/*
 * XDEV BI Suite
 * 
 * Copyright (c) 2011 - 2013, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package com.xdev.server.reports;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marker annotation for report parameter setters
 * 
 * @author XDEV Software
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter
{
}
