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

package com.xdev.ui;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.declarative.DesignContext;


/**
 * @author XDEV Software
 * @since 3.1
 */
public class XdevBootstrapCol extends CustomComponent
{
	private final static String	STYLE_COL						= "col";
	private final static String	STYLE_COL_HIDDEN_PREFIX			= "hidden-";
	private final static String	STYLE_COL_HIDDEN_SUFFIX_UP		= "-up";
	private final static String	STYLE_COL_HIDDEN_SUFFIX_DOWN	= "-down";
	private final static String	STYLE_ALIGNMENT					= "d-flex";
	private final static String	STYLE_ALIGNMENT_TOP				= "align-items-start";
	private final static String	STYLE_ALIGNMENT_MIDDLE			= "align-items-center";
	private final static String	STYLE_ALIGNMENT_BOTTOM			= "align-items-end";
	private final static String	STYLE_ALIGNMENT_LEFT			= "justify-content-start";
	private final static String	STYLE_ALIGNMENT_CENTER			= "justify-content-center";
	private final static String	STYLE_ALIGNMENT_RIGHT			= "justify-content-end";
	
	
	
	public static enum Breakpoint
	{
		XS("col-"), SM("col-sm-"), MD("col-md-"), LG("col-lg-"), XL("col-xl-");
		
		final String prefix;
		
		
		private Breakpoint(final String prefix)
		{
			this.prefix = prefix;
		}
	}
	
	private final static int							MAX_COLS			= 12;
	private final static Map<Alignment, List<String>>	ALIGNMENT_STYLES	= new HashMap<>();
	private final static Set<String>					COL_STYLES			= new HashSet<>();
	static
	{
		COL_STYLES.add(STYLE_COL);
		for(final Breakpoint breakpoint : Breakpoint.values())
		{
			for(int i = 1; i <= MAX_COLS; i++)
			{
				COL_STYLES.add(breakpoint.prefix + i);
			}
			COL_STYLES.add(STYLE_COL_HIDDEN_PREFIX + breakpoint.name().toLowerCase()
					+ STYLE_COL_HIDDEN_SUFFIX_UP);
			COL_STYLES.add(STYLE_COL_HIDDEN_PREFIX + breakpoint.name().toLowerCase()
					+ STYLE_COL_HIDDEN_SUFFIX_DOWN);
		}
		
		COL_STYLES.add(STYLE_ALIGNMENT);
		ALIGNMENT_STYLES.put(Alignment.TOP_LEFT,
				Arrays.asList(STYLE_ALIGNMENT_TOP,STYLE_ALIGNMENT_LEFT));
		ALIGNMENT_STYLES.put(Alignment.TOP_CENTER,
				Arrays.asList(STYLE_ALIGNMENT_TOP,STYLE_ALIGNMENT_CENTER));
		ALIGNMENT_STYLES.put(Alignment.TOP_RIGHT,
				Arrays.asList(STYLE_ALIGNMENT_TOP,STYLE_ALIGNMENT_RIGHT));
		ALIGNMENT_STYLES.put(Alignment.MIDDLE_LEFT,
				Arrays.asList(STYLE_ALIGNMENT_MIDDLE,STYLE_ALIGNMENT_LEFT));
		ALIGNMENT_STYLES.put(Alignment.MIDDLE_CENTER,
				Arrays.asList(STYLE_ALIGNMENT_MIDDLE,STYLE_ALIGNMENT_CENTER));
		ALIGNMENT_STYLES.put(Alignment.MIDDLE_RIGHT,
				Arrays.asList(STYLE_ALIGNMENT_MIDDLE,STYLE_ALIGNMENT_RIGHT));
		ALIGNMENT_STYLES.put(Alignment.BOTTOM_LEFT,
				Arrays.asList(STYLE_ALIGNMENT_BOTTOM,STYLE_ALIGNMENT_LEFT));
		ALIGNMENT_STYLES.put(Alignment.BOTTOM_CENTER,
				Arrays.asList(STYLE_ALIGNMENT_BOTTOM,STYLE_ALIGNMENT_CENTER));
		ALIGNMENT_STYLES.put(Alignment.BOTTOM_RIGHT,
				Arrays.asList(STYLE_ALIGNMENT_BOTTOM,STYLE_ALIGNMENT_RIGHT));
		ALIGNMENT_STYLES.values().forEach(styles -> styles.forEach(COL_STYLES::add));
	}
	
	private final Map<Breakpoint, Integer>	breakpoints	= new TreeMap<>();
	private Breakpoint						hiddenUp;
	private Breakpoint						hiddenDown;
	private Alignment						alignment;
	
	
	public XdevBootstrapCol()
	{
		super();
		
		updateColumnStyles();
	}
	
	
	public void setContent(final Component c)
	{
		setCompositionRoot(c);
	}
	
	
	public void setBreakpoint(final Breakpoint breakpoint, final int columns)
	{
		setBreakpoint(breakpoint,columns,true);
	}
	
	
	protected void setBreakpoint(final Breakpoint breakpoint, final int columns,
			final boolean updateStyles)
	{
		if(columns > MAX_COLS)
		{
			throw new IllegalArgumentException("Column value cannot be higher than " + MAX_COLS);
		}
		if(columns <= 0)
		{
			this.breakpoints.remove(breakpoint);
		}
		else
		{
			this.breakpoints.put(breakpoint,columns);
		}
		if(updateStyles)
		{
			updateColumnStyles();
		}
	}
	
	
	public void removeBreakpoint(final Breakpoint breakpoint)
	{
		this.breakpoints.remove(breakpoint);
		updateColumnStyles();
	}
	
	
	public void setBreakpoints(final int xs, final int sm, final int md, final int lg, final int xl)
	{
		setBreakpoint(Breakpoint.XS,xs,false);
		setBreakpoint(Breakpoint.SM,sm,false);
		setBreakpoint(Breakpoint.MD,md,false);
		setBreakpoint(Breakpoint.LG,lg,false);
		setBreakpoint(Breakpoint.XL,xl,false);
		updateColumnStyles();
	}
	
	
	public int getBreakpoint(final Breakpoint breakpoint)
	{
		final Integer columns = this.breakpoints.get(breakpoint);
		return columns == null ? 0 : columns.intValue();
	}
	
	
	public Breakpoint getHiddenUp()
	{
		return this.hiddenUp;
	}
	
	
	public void setHiddenUp(final Breakpoint hiddenUp)
	{
		this.hiddenUp = hiddenUp;
		updateColumnStyles();
	}
	
	
	public Breakpoint getHiddenDown()
	{
		return this.hiddenDown;
	}
	
	
	public void setHiddenDown(final Breakpoint hiddenDown)
	{
		this.hiddenDown = hiddenDown;
		updateColumnStyles();
	}
	
	
	public Alignment getAlignment()
	{
		return this.alignment;
	}
	
	
	public void setAlignment(final Alignment alignment)
	{
		this.alignment = alignment;
		updateColumnStyles();
	}
	
	
	protected void updateColumnStyles()
	{
		final List<String> styles = this.breakpoints.entrySet().stream()
				.map(e -> e.getKey().prefix + e.getValue()).collect(Collectors.toList());
		if(styles.isEmpty())
		{
			styles.add(STYLE_COL);
		}
		
		if(this.hiddenUp != null)
		{
			styles.add(STYLE_COL_HIDDEN_PREFIX + this.hiddenUp.name().toLowerCase()
					+ STYLE_COL_HIDDEN_SUFFIX_UP);
		}
		if(this.hiddenDown != null)
		{
			styles.add(STYLE_COL_HIDDEN_PREFIX + this.hiddenDown.name().toLowerCase()
					+ STYLE_COL_HIDDEN_SUFFIX_DOWN);
		}
		
		if(this.alignment != null)
		{
			styles.add(STYLE_ALIGNMENT);
			ALIGNMENT_STYLES.get(this.alignment).forEach(styles::add);
		}
		
		// add existing user defined styles
		Arrays.stream(getStyleName().split("\\s")).map(String::trim)
				.filter(name -> !(name.isEmpty() || COL_STYLES.contains(name)))
				.forEach(styles::add);
		
		setStyleName(styles.stream().collect(Collectors.joining(" ")));
	}
	
	
	@Override
	public void readDesign(final Element design, final DesignContext designContext)
	{
		super.readDesign(design,designContext);

		final List<String> styles = Arrays.stream(getStyleName().split("\\s")).map(String::trim)
				.filter(name -> !name.isEmpty()).collect(Collectors.toList());
		
		styles.forEach(style -> {
			
			final Breakpoint[] breakpoints = Breakpoint.values();
			for(int i = breakpoints.length; --i >= 0;)
			{
				final Breakpoint breakpoint = breakpoints[i];
				if(style.startsWith(breakpoint.prefix))
				{
					try
					{
						final int value = Integer
								.parseInt(style.substring(breakpoint.prefix.length()));
						this.breakpoints.put(breakpoint,value);
						break;
					}
					catch(final NumberFormatException e)
					{
					}
				}
			}
		});
		
		ALIGNMENT_STYLES.entrySet().stream().filter(entry -> styles.containsAll(entry.getValue()))
				.findAny().ifPresent(entry -> this.alignment = entry.getKey());
	}
}
