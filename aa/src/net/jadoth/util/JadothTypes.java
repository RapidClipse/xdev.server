/*
 * Copyright (c) 2008-2010, Thomas Muenz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.jadoth.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.jadoth.exceptions.NumberRangeException;

/**
 * Collection of generic util logic missing or too complicated in JDK API.
 *
 * @author Thomas Muenz
 */
public abstract class JadothTypes
{
	///////////////////////////////////////////////////////////////////////////
	// Class Type Classifiers //
	///////////////////////////

	public static boolean isBoolean(final Class<?> c)
	{
		return c == boolean.class || c == Boolean.class;
	}

	public static boolean isByte(final Class<?> c)
	{
		return c == byte.class || c == Byte.class;
	}

	public static boolean isShort(final Class<?> c)
	{
		return c == short.class || c == Short.class;
	}

	public static boolean isInteger(final Class<?> c)
	{
		return c == int.class || c == Integer.class;
	}

	public static boolean isLong(final Class<?> c)
	{
		return c == long.class || c == Long.class;
	}

	public static boolean isFloat(final Class<?> c)
	{
		return c == float.class || c == Float.class;
	}

	public static boolean isDouble(final Class<?> c)
	{
		return c == double.class || c == Double.class;
	}

	public static boolean isCharacter(final Class<?> c)
	{
		return c == char.class || c == Character.class;
	}
	
	//just for conformity in use along with the other ones
	public static boolean isString(final Class<?> c)
	{
		return c == String.class;
	}



	///////////////////////////////////////////////////////////////////////////
	// Class Category Classifiers //
	///////////////////////////////

	public static boolean isNaturalNumber(final Class<?> c)
	{
		return c == byte.class 	|| c == Byte.class
			|| c == short.class || c == Short.class
			|| c == int.class 	|| c == Integer.class
			|| c == long.class 	|| c == Long.class
			|| c == BigInteger.class
			|| c == AtomicInteger.class
			|| c == AtomicLong.class
		;
	}

	public static boolean isDecimal(final Class<?> c)
	{
		return c == float.class || c == Float.class
			|| c == double.class || c == Double.class
			|| c == BigDecimal.class
		;
	}

	public static boolean isNumber(final Class<?> c)
	{
		return c == byte.class 	|| c == Byte.class
			|| c == short.class || c == Short.class
			|| c == int.class 	|| c == Integer.class
			|| c == long.class 	|| c == Long.class
			|| c == float.class || c == Float.class
			|| c == double.class || c == Double.class
			|| c == BigInteger.class
			|| c == AtomicInteger.class
			|| c == AtomicLong.class
			|| c == BigDecimal.class
		;
	}

	public static boolean isLiteral(final Class<?> c)
	{
		return c == String.class || c == char.class || c == Character.class;
	}

	public static boolean isValueType(final Class<?> c)
	{
		// all value types, ordered in common use probability
		return c == String.class
			|| c == Integer.class
			|| c == Long.class
			|| c == Character.class
			|| c == Double.class
			|| c == Byte.class
			|| c == Boolean.class
			|| c == Float.class
			|| c == Short.class
			|| c == BigInteger.class
			|| c == BigDecimal.class
			|| c == Field.class // Field instances are no cached singletons but get copied over and over as value types
			|| ValueType.class.isAssignableFrom(c)
		;
	}



	///////////////////////////////////////////////////////////////////////////
	// Object Category Classifiers //
	////////////////////////////////

	public static boolean isNaturalNumber(final Object o)
	{
		// NOT Float and Double
		return o instanceof Byte
			|| o instanceof Short
			|| o instanceof Integer
			|| o instanceof Long
			|| o instanceof BigInteger
			|| o instanceof AtomicInteger
			|| o instanceof AtomicLong
		;
	}
	
	//just for conformity in use along with the other ones
	public static boolean isNumber(final Object o)
	{
		return o instanceof Number;
	}

	public static boolean isDecimal(final Object o)
	{
		return o instanceof Float || o instanceof Double || o instanceof BigDecimal;
	}

	public static boolean isLiteral(final Object o)
	{
		return o instanceof String || o instanceof Character;
	}
	
	//just for conformity in use along with the other ones
	public static boolean isBoolean(final Object o)
	{
		return o instanceof Boolean;
	}

	/**
	 * Checks if the type of the passed instance is an immutable special value type of the java language.
	 * This includes all primitive wrappers and {@link String}.
	 *
	 * @param o the instance to be checked
	 * @return {@code true} if the type of the passed instance is an immutable special value type.
	 */
	public static boolean isValueType(final Object o)
	{
		// all value types, ordered in common use probability
		return o instanceof String
			|| isPrimitiveWrapperType(o)
			|| o instanceof BigInteger
			|| o instanceof BigDecimal
			|| o instanceof ValueType
		;
	}

	public static boolean isPrimitiveWrapperType(final Object o)
	{
		// all primitive wrapper types, ordered in common use probability
		return o instanceof Integer
			|| o instanceof Long
			|| o instanceof Double
			|| o instanceof Character
			|| o instanceof Boolean
			|| o instanceof Float
			|| o instanceof Byte
			|| o instanceof Short
		;
	}

	public static <A> Class<A> validateInterfaceType(final Class<A> type)
	{
		if(!type.isInterface()){
			throw JadothExceptions.cutStacktraceByOne(new IllegalArgumentException("Not an interface type:"+type));
		}
		return type;
	}

	public static <A> Class<A> validateNonInterfaceType(final Class<A> type)
	{
		if(type.isInterface()){
			throw JadothExceptions.cutStacktraceByOne(new IllegalArgumentException("Interface type:"+type));
		}
		return type;
	}

	public static <A> Class<A> validateNonArrayType(final Class<A> type)
	{
		if(type.isArray()){
			throw JadothExceptions.cutStacktraceByOne(new IllegalArgumentException("Array type:"+type));
		}
		return type;
	}

	public static <A> Class<A> validateArrayType(final Class<A> arrayType)
	{
		if(!arrayType.isArray()){
			throw JadothExceptions.cutStacktraceByOne(new IllegalArgumentException("Not an array type:"+arrayType));
		}
		return arrayType;
	}

	public static <A> Class<A> validatePrimitiveType(final Class<A> primitiveType)
	{
		if(!primitiveType.isPrimitive()){
			throw JadothExceptions.cutStacktraceByOne(new IllegalArgumentException("Not a primitive type:"+primitiveType));
		}
		return primitiveType;
	}

	public static <A> Class<A> validateNonPrimitiveType(final Class<A> primitiveType)
	{
		if(primitiveType.isPrimitive()){
			throw JadothExceptions.cutStacktraceByOne(new IllegalArgumentException("Primitive type:"+primitiveType));
		}
		return primitiveType;
	}

	public static final int parseIntFrom1Bytes(final byte[] b, final int offset)
	{
		return b[offset]&0xff;
	}
	
	public static final int parseIntFrom2Bytes(final byte[] b, int offset)
	{
		return (b[offset++]&0xff)<<8 | b[offset]&0xff;
	}
	
	public static final int parseIntFrom3Bytes(final byte[] b, int offset)
	{
		return (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | b[offset]&0xff;
	}
	
	public static final int parseIntFrom4Bytes(final byte[] b, int offset)
	{
		return b[offset++]<<24 | (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | b[offset]&0xff;
	}

	public static final int parseIntFromBytes(final byte[] b, int offset, final int byteCount)
	{
		switch(byteCount){
			case 1:
				return b[offset]&0xff;
			case 2:
				return (b[offset++]&0xff)<<8 | b[offset]&0xff;
			case 3:
				return (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | b[offset]&0xff;
			default:
				return b[offset++]<<24 | (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | b[offset]&0xff;
		}
	}

	public static final int parseIntFrom1Bytes(final byte[] b)
	{
		return b[0]&0xff;
	}
	
	public static final int parseIntFrom2Bytes(final byte[] b)
	{
		return (b[0]&0xff)<<8 | b[1]&0xff;
	}
	
	public static final int parseIntFrom3Bytes(final byte[] b)
	{
		return (b[0]&0xff)<<16 | (b[1]&0xff)<<8 | b[2]&0xff;
	}
	
	public static final int parseIntFrom4Bytes(final byte[] b)
	{
		return b[0]<<24 | (b[1]&0xff)<<16 | (b[2]&0xff)<<8 | b[3]&0xff;
	}

	public static final byte[] convertIntTo1Bytes(final int i)
	{
		return new byte[]{(byte)i};
	}
	
	public static final byte[] convertIntTo2Bytes(final int i)
	{
		return new byte[]{(byte)(i>>8), (byte)i};
	}
	
	public static final byte[] convertIntTo3Bytes(final int i)
	{
		return new byte[]{(byte)(i>>16), (byte)(i>>8), (byte)i};
	}
	
	public static final byte[] convertIntTo4Bytes(final int i)
	{
		return new byte[]{(byte)(i>>24), (byte)(i>>16), (byte)(i>>8), (byte)i};
	}

	public static final byte[] convertIntTo4Bytes(final int i, final int byteCount)
	{
		switch(byteCount){
			case 1:  return new byte[]{(byte)i};
			case 2:  return new byte[]{(byte)(i>>8), (byte)i};
			case 3:  return new byte[]{(byte)(i>>16), (byte)(i>>8), (byte)i};
			default: return new byte[]{(byte)(i>>24), (byte)(i>>16), (byte)(i>>8), (byte)i};
		}
	}

	public static final byte[] writeIntTo1Bytes(final int i, final byte[] bytes, final int offset)
	{
		bytes[offset] = (byte)i;
		return bytes;
	}
	
	public static final byte[] writeIntTo2Bytes(final int i, final byte[] bytes, int offset)
	{
		if(offset + 2 > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+3);
		}

		bytes[offset++] = (byte)(i>>8);
		bytes[offset] = (byte)i;
		return bytes;
	}
	
	public static final byte[] writeIntTo3Bytes(final int i, final byte[] bytes, int offset)
	{
		if(offset + 3 > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+3);
		}

		bytes[offset++] = (byte)(i>>16);
		bytes[offset++] = (byte)(i>>8);
		bytes[offset] = (byte)i;
		return bytes;
	}
	
	public static final byte[] writeIntTo4Bytes(final int i, final byte[] bytes, int offset)
	{
		if(offset + 4 > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+3);
		}

		bytes[offset++] = (byte)(i>>24);
		bytes[offset++] = (byte)(i>>16);
		bytes[offset++] = (byte)(i>>8);
		bytes[offset] = (byte)i;
		return bytes;
	}

	public static final byte[] writeIntTo4Bytes(final int i, final byte[] bytes, int offset, final int byteCount)
	{
		if(offset + byteCount > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+byteCount-1);
		}

		switch(byteCount){
			case 4:	bytes[offset++] = (byte)(i>>24);
			case 3: bytes[offset++] = (byte)(i>>16);
			case 2: bytes[offset++] = (byte)(i>>8);
			case 1: bytes[offset] = (byte)i;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException(offset);
		}
		return bytes;
	}

	public static final int to_int(final long value) throws NumberRangeException
	{
		if(value > Integer.MAX_VALUE){
			throw new IllegalArgumentException(value+" > "+Integer.MAX_VALUE);
		}
		else if(value < Integer.MIN_VALUE){
			throw new IllegalArgumentException(value+" < "+Integer.MIN_VALUE);
		}
		return (int)value;
	}

	public static final int to_int(final float value) throws NumberRangeException
	{
		if(value > Integer.MAX_VALUE){
			throw new IllegalArgumentException(value+" > "+Integer.MAX_VALUE);
		}
		else if(value < Integer.MIN_VALUE){
			throw new IllegalArgumentException(value+" < "+Integer.MIN_VALUE);
		}
		return (int)value;
	}

	public static final int to_int(final double value) throws NumberRangeException
	{
		if(value > Integer.MAX_VALUE){
			throw new IllegalArgumentException(value+" > "+Integer.MAX_VALUE);
		}
		else if(value < Integer.MIN_VALUE){
			throw new IllegalArgumentException(value+" < "+Integer.MIN_VALUE);
		}
		return (int)value;
	}

	public static final int to_int(final Number value) throws NumberRangeException, NullPointerException
	{
		return to_int(value.longValue());
	}


	/**
	 * This method makes a best effort to interpret or convert the passed value as/to an {@link Integer} instance
	 * by applying the following rules:
	 * <ul>
	 * <li>If <code>value</code> is {@code null}, {@code null} is returned.</li>
	 * <li>If <code>value</code> is of type {@link Integer}, it is returned directly.</li>
	 * <li>If <code>value</code> is of any other {@link Number} type, an {@link Integer} value is derived from it.</li>
	 * <li>If <code>value</code> is of type {@link String}, an attempt is made to parse the string value to an
	 * Integer, potentially throwing a {@link NumberFormatException}.</li>
	 * <li>Otherwise, an {@link IllegalArgumentException} is thrown.</li>
	 * </ul>
	 * <p>
	 * <b><u>/!\ Caution:</u></b><br>
	 * The purpose of this method is only to provide a workaround solution to adapt factual code
	 * (i.e. unchangeable library code) to an otherwise necessary architecture.<br>
	 * Do NOT rely on this method as a means to support bad program design in the first place!.
	 *
	 * @param value the value to be intepreted as {@link Integer}.
	 * @return a successfully interpreted or converted {@link Integer} value.
	 * @throws NumberFormatException if the passed value is a string that failed to be parsed as {@link Integer}.
	 * @throws IllegalArgumentException if the passed value's type cannot be interpreted as {@link Integer} at all.
	 */
	public static final Integer interpretInteger(final Object value) throws NumberFormatException, IllegalArgumentException
	{
		if(value == null){
			return null;
		}
		if(value instanceof Integer){
			return (Integer)value;
		}
		if(value instanceof Number){
			return Integer.valueOf(((Number)value).intValue());
		}
		if(value instanceof String){
			return Integer.valueOf((String)value); // possible NumberFormatException
		}
		throw new IllegalArgumentException("value of type "+value.getClass()+" cannot be interpreted as "+Integer.class);
	}

}
