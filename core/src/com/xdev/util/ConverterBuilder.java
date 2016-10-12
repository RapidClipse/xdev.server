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


import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.util.converter.StringToBigIntegerConverter;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.data.util.converter.StringToByteConverter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.data.util.converter.StringToShortConverter;


/**
 * @author XDEV Software
 *
 */
public abstract class ConverterBuilder
{
	private static enum ConverterType
	{
		STRING_TO_BYTE,
		STRING_TO_SHORT,
		STRING_TO_INTEGER,
		STRING_TO_LONG,
		STRING_TO_BIG_INTEGER,
		STRING_TO_FLOAT,
		STRING_TO_DOUBLE,
		STRING_TO_BIG_DECIMAL,
		STRING_TO_BOOLEAN,
		STRING_TO_DATE
	}
	
	protected final ConverterType type;
	
	
	ConverterBuilder(final ConverterType type)
	{
		this.type = type;
	}
	
	
	public static StringToNumberConverterBuilder stringToByte()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_BYTE);
	}
	
	
	public static StringToNumberConverterBuilder stringToShort()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_SHORT);
	}
	
	
	public static StringToNumberConverterBuilder stringToInteger()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_INTEGER);
	}
	
	
	public static StringToNumberConverterBuilder stringToLong()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_LONG);
	}
	
	
	public static StringToNumberConverterBuilder stringToBigInteger()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_BIG_INTEGER);
	}
	
	
	public static StringToNumberConverterBuilder stringToFloat()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_FLOAT);
	}
	
	
	public static StringToNumberConverterBuilder stringToDouble()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_DOUBLE);
	}
	
	
	public static StringToNumberConverterBuilder stringToBigDecimal()
	{
		return new StringToNumberConverterBuilder(ConverterType.STRING_TO_BIG_DECIMAL);
	}
	
	
	public static StringToBooleanConverterBuilder stringToBoolean()
	{
		return new StringToBooleanConverterBuilder(ConverterType.STRING_TO_BOOLEAN);
	}
	
	
	public static StringToDateConverterBuilder stringToDate()
	{
		return new StringToDateConverterBuilder(ConverterType.STRING_TO_DATE);
	}
	
	
	public abstract Converter<?, ?> build();
	
	
	private static Locale getLocale(final Locale thisLocale, final Locale localeParam)
	{
		// user set locale has priority
		if(thisLocale != null)
		{
			return thisLocale;
		}
		// context's locale
		else if(localeParam != null)
		{
			return localeParam;
		}
		// use default
		return Locale.getDefault(Locale.Category.FORMAT);
	}
	
	
	
	public static class StringToNumberConverterBuilder extends ConverterBuilder
	{
		private static enum FormatType
		{
			INTEGER, NUMBER, CURRENCY, PERCENT
		}
		
		private Locale					locale;
		private FormatType				formatType;
		
		// NumberFormat
		private Boolean					groupingUsed;
		private Integer					maximumIntegerDigits;
		private Integer					minimumIntegerDigits;
		private Integer					maximumFractionDigits;
		private Integer					minimumFractionDigits;
		private Currency				currency;
		private String					currencySymbol;
		private RoundingMode			roundingMode;
		
		// DecimalFormat
		private DecimalFormatSymbols	decimalFormatSymbols;
		private Boolean					decimalSeparatorAlwaysShown;
		private Integer					groupingSize;
		private Integer					multiplier;
		private String					negativePrefix;
		private String					negativeSuffix;
		private String					positivePrefix;
		private String					positiveSuffix;
		
		
		StringToNumberConverterBuilder(final ConverterType type)
		{
			super(type);
			
			switch(type)
			{
				case STRING_TO_BYTE:
				case STRING_TO_SHORT:
				case STRING_TO_INTEGER:
				case STRING_TO_LONG:
				{
					this.formatType = FormatType.INTEGER;
				}
				break;
				
				default:
				{
					this.formatType = FormatType.NUMBER;
				}
			}
		}
		
		
		public StringToNumberConverterBuilder locale(final Locale locale)
		{
			this.locale = locale;
			return this;
		}
		
		
		public StringToNumberConverterBuilder currency()
		{
			this.formatType = FormatType.CURRENCY;
			return this;
		}
		
		
		public StringToNumberConverterBuilder percent()
		{
			this.formatType = FormatType.PERCENT;
			return this;
		}
		
		
		public StringToNumberConverterBuilder groupingUsed(final boolean groupingUsed)
		{
			this.groupingUsed = groupingUsed;
			return this;
		}
		
		
		public StringToNumberConverterBuilder maximumIntegerDigits(final int maximumIntegerDigits)
		{
			this.maximumIntegerDigits = maximumIntegerDigits;
			return this;
		}
		
		
		public StringToNumberConverterBuilder minimumIntegerDigits(final int minimumIntegerDigits)
		{
			this.minimumIntegerDigits = minimumIntegerDigits;
			return this;
		}
		
		
		public StringToNumberConverterBuilder maximumFractionDigits(final int maximumFractionDigits)
		{
			this.maximumFractionDigits = maximumFractionDigits;
			return this;
		}
		
		
		public StringToNumberConverterBuilder minimumFractionDigits(final int minimumFractionDigits)
		{
			this.minimumFractionDigits = minimumFractionDigits;
			return this;
		}
		
		
		public StringToNumberConverterBuilder currency(final Currency currency)
		{
			currency(); // set to currency format
			this.currency = currency;
			return this;
		}
		
		
		public StringToNumberConverterBuilder currencySymbol(final String currencySymbol)
		{
			currency(); // set to currency format
			this.currencySymbol = currencySymbol;
			return this;
		}
		
		
		public StringToNumberConverterBuilder decimalFormatSymbols(
				final DecimalFormatSymbols decimalFormatSymbols)
		{
			this.decimalFormatSymbols = decimalFormatSymbols;
			return this;
		}
		
		
		public StringToNumberConverterBuilder roundingMode(final RoundingMode roundingMode)
		{
			this.roundingMode = roundingMode;
			return this;
		}
		
		
		public StringToNumberConverterBuilder decimalSeparatorAlwaysShown(
				final boolean decimalSeparatorAlwaysShown)
		{
			this.decimalSeparatorAlwaysShown = decimalSeparatorAlwaysShown;
			return this;
		}
		
		
		public StringToNumberConverterBuilder groupingSize(final int groupingSize)
		{
			this.groupingSize = groupingSize;
			return this;
		}
		
		
		public StringToNumberConverterBuilder multiplier(final int multiplier)
		{
			this.multiplier = multiplier;
			return this;
		}
		
		
		public StringToNumberConverterBuilder negativePrefix(final String negativePrefix)
		{
			this.negativePrefix = negativePrefix;
			return this;
		}
		
		
		public StringToNumberConverterBuilder negativeSuffix(final String negativeSuffix)
		{
			this.negativeSuffix = negativeSuffix;
			return this;
		}
		
		
		public StringToNumberConverterBuilder positivePrefix(final String positivePrefix)
		{
			this.positivePrefix = positivePrefix;
			return this;
		}
		
		
		public StringToNumberConverterBuilder positiveSuffix(final String positiveSuffix)
		{
			this.positiveSuffix = positiveSuffix;
			return this;
		}
		
		
		@Override
		public Converter<String, ? extends Number> build()
		{
			switch(this.type)
			{
				case STRING_TO_BYTE:
				{
					return new StringToByteConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							return StringToNumberConverterBuilder.this.getFormat(locale);
						}
					};
				}
				
				case STRING_TO_SHORT:
				{
					return new StringToShortConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							return StringToNumberConverterBuilder.this.getFormat(locale);
						}
					};
				}
				
				case STRING_TO_INTEGER:
				{
					return new StringToIntegerConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							return StringToNumberConverterBuilder.this.getFormat(locale);
						}
					};
				}
				
				case STRING_TO_LONG:
				{
					return new StringToLongConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							return StringToNumberConverterBuilder.this.getFormat(locale);
						}
					};
				}
				
				case STRING_TO_BIG_INTEGER:
				{
					return new StringToBigIntegerConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							final NumberFormat format = StringToNumberConverterBuilder.this
									.getFormat(locale);
							if(format instanceof DecimalFormat)
							{
								((DecimalFormat)format).setParseBigDecimal(true);
							}
							return format;
						}
					};
				}
				
				case STRING_TO_FLOAT:
				{
					return new StringToFloatConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							return StringToNumberConverterBuilder.this.getFormat(locale);
						}
					};
				}
				
				case STRING_TO_DOUBLE:
				{
					return new StringToDoubleConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							return StringToNumberConverterBuilder.this.getFormat(locale);
						}
					};
				}
				
				case STRING_TO_BIG_DECIMAL:
				{
					return new StringToBigDecimalConverter()
					{
						@Override
						protected NumberFormat getFormat(final Locale locale)
						{
							final NumberFormat format = StringToNumberConverterBuilder.this
									.getFormat(locale);
							if(format instanceof DecimalFormat)
							{
								((DecimalFormat)format).setParseBigDecimal(true);
							}
							return format;
						}
					};
				}
				
				default:
					return null;
			}
		}
		
		
		private NumberFormat getFormat(final Locale localeParam)
		{
			final Locale locale = getLocale(this.locale,localeParam);
			
			NumberFormat format = null;
			
			switch(this.formatType)
			{
				case INTEGER:
					format = NumberFormat.getIntegerInstance(locale);
				break;
				
				case NUMBER:
					format = NumberFormat.getNumberInstance(locale);
				break;
				
				case CURRENCY:
					format = NumberFormat.getCurrencyInstance(locale);
				break;
				
				case PERCENT:
					format = NumberFormat.getPercentInstance(locale);
				break;
			}
			
			if(this.groupingUsed != null)
			{
				format.setGroupingUsed(this.groupingUsed);
			}
			if(this.maximumIntegerDigits != null)
			{
				format.setMaximumIntegerDigits(this.maximumIntegerDigits);
			}
			if(this.minimumIntegerDigits != null)
			{
				format.setMinimumIntegerDigits(this.minimumIntegerDigits);
			}
			if(this.maximumFractionDigits != null)
			{
				format.setMaximumFractionDigits(this.maximumFractionDigits);
			}
			if(this.minimumFractionDigits != null)
			{
				format.setMinimumFractionDigits(this.minimumFractionDigits);
			}
			if(this.currency != null)
			{
				format.setCurrency(this.currency);
			}
			if(this.roundingMode != null)
			{
				format.setRoundingMode(this.roundingMode);
			}
			
			if(format instanceof DecimalFormat)
			{
				final DecimalFormat decimalFormat = (DecimalFormat)format;
				
				if(this.decimalFormatSymbols != null)
				{
					decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols);
				}
				if(this.currencySymbol != null)
				{
					decimalFormat.getDecimalFormatSymbols().setCurrencySymbol(this.currencySymbol);
				}
				if(this.decimalSeparatorAlwaysShown != null)
				{
					decimalFormat.setDecimalSeparatorAlwaysShown(this.decimalSeparatorAlwaysShown);
				}
				if(this.groupingSize != null)
				{
					decimalFormat.setGroupingSize(this.groupingSize);
				}
				if(this.multiplier != null)
				{
					decimalFormat.setMultiplier(this.multiplier);
				}
				if(this.negativePrefix != null)
				{
					decimalFormat.setNegativePrefix(this.negativePrefix);
				}
				if(this.negativeSuffix != null)
				{
					decimalFormat.setNegativeSuffix(this.negativeSuffix);
				}
				if(this.positivePrefix != null)
				{
					decimalFormat.setPositivePrefix(this.positivePrefix);
				}
				if(this.positiveSuffix != null)
				{
					decimalFormat.setPositiveSuffix(this.positiveSuffix);
				}
			}
			
			return format;
		}
	}
	
	
	
	public static class StringToBooleanConverterBuilder extends ConverterBuilder
	{
		private String	trueString;
		private String	falseString;
		
		
		StringToBooleanConverterBuilder(final ConverterType type)
		{
			super(type);
		}
		
		
		public StringToBooleanConverterBuilder trueString(final String trueString)
		{
			this.trueString = trueString;
			return this;
		}
		
		
		public StringToBooleanConverterBuilder falseString(final String falseString)
		{
			this.falseString = falseString;
			return this;
		}
		
		
		@Override
		public Converter<String, Boolean> build()
		{
			return new StringToBooleanConverter()
			{
				@Override
				protected String getTrueString()
				{
					if(StringToBooleanConverterBuilder.this.trueString != null)
					{
						return StringToBooleanConverterBuilder.this.trueString;
					}
					
					return super.getTrueString();
				}
				
				
				@Override
				protected String getFalseString()
				{
					if(StringToBooleanConverterBuilder.this.falseString != null)
					{
						return StringToBooleanConverterBuilder.this.falseString;
					}
					
					return super.getTrueString();
				}
			};
		}
	}
	
	
	
	public static class StringToDateConverterBuilder extends ConverterBuilder
	{
		private Locale				locale;
		private boolean				date		= true;
		private boolean				time		= true;
		private int					dateStyle	= DateFormat.DEFAULT;
		private int					timeStyle	= DateFormat.DEFAULT;
		private String				pattern;
		private DateFormatSymbols	dateFormatSymbols;
		
		
		StringToDateConverterBuilder(final ConverterType type)
		{
			super(type);
		}
		
		
		public StringToDateConverterBuilder locale(final Locale locale)
		{
			this.locale = locale;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateOnly()
		{
			this.date = true;
			this.time = false;
			return this;
		}
		
		
		public StringToDateConverterBuilder timeOnly()
		{
			this.date = false;
			this.time = true;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateAndTime()
		{
			this.date = true;
			this.time = true;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateStyleShort()
		{
			this.dateStyle = DateFormat.SHORT;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateStyleMedium()
		{
			this.dateStyle = DateFormat.MEDIUM;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateStyleLong()
		{
			this.dateStyle = DateFormat.LONG;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateStyleFull()
		{
			this.dateStyle = DateFormat.FULL;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateStyleDefault()
		{
			this.dateStyle = DateFormat.DEFAULT;
			return this;
		}
		
		
		public StringToDateConverterBuilder timeStyleShort()
		{
			this.timeStyle = DateFormat.SHORT;
			return this;
		}
		
		
		public StringToDateConverterBuilder timeStyleMedium()
		{
			this.timeStyle = DateFormat.MEDIUM;
			return this;
		}
		
		
		public StringToDateConverterBuilder timeStyleLong()
		{
			this.timeStyle = DateFormat.LONG;
			return this;
		}
		
		
		public StringToDateConverterBuilder timeStyleFull()
		{
			this.timeStyle = DateFormat.FULL;
			return this;
		}
		
		
		public StringToDateConverterBuilder timeStyleDefault()
		{
			this.timeStyle = DateFormat.DEFAULT;
			return this;
		}
		
		
		public StringToDateConverterBuilder pattern(final String pattern)
		{
			this.pattern = pattern;
			return this;
		}
		
		
		public StringToDateConverterBuilder dateFormatSymbols(
				final DateFormatSymbols dateFormatSymbols)
		{
			this.dateFormatSymbols = dateFormatSymbols;
			return this;
		}
		
		
		private DateFormat getFormat(final Locale localeParam)
		{
			final Locale locale = getLocale(this.locale,localeParam);
			
			if(this.pattern != null)
			{
				if(this.dateFormatSymbols != null)
				{
					return new SimpleDateFormat(this.pattern,this.dateFormatSymbols);
				}
				return new SimpleDateFormat(this.pattern,locale);
			}
			
			if(this.date && this.time)
			{
				return DateFormat.getDateTimeInstance(this.dateStyle,this.timeStyle,locale);
			}
			if(this.date)
			{
				return DateFormat.getDateInstance(this.dateStyle,locale);
			}
			return DateFormat.getTimeInstance(this.timeStyle,locale);
		}
		
		
		@Override
		public Converter<String, Date> build()
		{
			return new StringToDateConverter()
			{
				@Override
				protected DateFormat getFormat(final Locale locale)
				{
					return StringToDateConverterBuilder.this.getFormat(locale);
				}
			};
		}
	}
}
