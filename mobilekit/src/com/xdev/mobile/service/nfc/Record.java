/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.nfc;


import java.io.UnsupportedEncodingException;


/**
 * @author XDEV Software
 *
 */
public class Record
{
	private final static String CHARSET_NAME = "UTF-8";
	
	
	public static Record textRecord(final String text)
	{
		return textRecord(text,null);
	}
	
	
	public static Record textRecord(final String text, final byte[] id)
	{
		try
		{
			return new Record(TypeNameFormat.TNF_WELL_KNOWN.getCode(),RecordType.RTD_TEXT.getData(),
					id,text.getBytes(CHARSET_NAME));
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	public static Record uriRecord(final String uri)
	{
		return uriRecord(uri,null);
	}
	
	
	public static Record uriRecord(final String uri, final byte[] id)
	{
		try
		{
			return new Record(TypeNameFormat.TNF_WELL_KNOWN.getCode(),RecordType.RTD_URI.getData(),
					id,uri.getBytes(CHARSET_NAME));
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	public static Record absoluteUriRecord(final String uri, final String payload)
	{
		return absoluteUriRecord(uri,payload,null);
	}
	
	
	public static Record absoluteUriRecord(final String uri, final String payload, final byte[] id)
	{
		try
		{
			return new Record(TypeNameFormat.TNF_ABSOLUTE_URI.getCode(),uri.getBytes(CHARSET_NAME),
					id,payload.getBytes(CHARSET_NAME));
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	public static Record mimeMediaRecord(final String mimeType, final String payload)
	{
		return mimeMediaRecord(mimeType,payload,null);
	}
	
	
	public static Record mimeMediaRecord(final String mimeType, final String payload,
			final byte[] id)
	{
		try
		{
			return new Record(TypeNameFormat.TNF_MIME_MEDIA.getCode(),
					mimeType.getBytes(CHARSET_NAME),id,payload.getBytes(CHARSET_NAME));
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	public static Record emptyRecord()
	{
		return new Record(TypeNameFormat.TNF_EMPTY.getCode(),null,null,null);
	}
	
	
	public static Record androidApplicationRecord(final String packageName)
	{
		try
		{
			return new Record(TypeNameFormat.TNF_EXTERNAL_TYPE.getCode(),
					"android.com:pkg".getBytes(CHARSET_NAME),null,
					packageName.getBytes(CHARSET_NAME));
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	private int		tnf;
	private byte[]	type;
	private byte[]	id;
	private byte[]	payload;
	
	
	/**
	 *
	 */
	public Record()
	{
		super();
	}
	
	
	/**
	 * @param tnf
	 * @param type
	 * @param id
	 * @param payload
	 */
	public Record(final int tnf, final byte[] type, final byte[] id, final byte[] payload)
	{
		super();
		this.tnf = tnf;
		this.type = type;
		this.id = id;
		this.payload = payload;
	}
	
	
	/**
	 * @return the tnf code
	 */
	public int getTnf()
	{
		return this.tnf;
	}
	
	
	public TypeNameFormat getTypeNameFormat()
	{
		return TypeNameFormat.byNumber(this.tnf);
	}
	
	
	/**
	 * @param tnf
	 *            the tnf to set
	 */
	public void setTnf(final int tnf)
	{
		this.tnf = tnf;
	}
	
	
	public void setTypeNameFormat(final TypeNameFormat tnf)
	{
		this.tnf = tnf.getCode();
	}
	
	
	/**
	 * @return the type
	 */
	public byte[] getType()
	{
		return this.type;
	}
	
	
	public String getTypeAsString()
	{
		if(this.type == null)
		{
			return "";
		}
		
		try
		{
			return new String(this.type,CHARSET_NAME);
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	public RecordType getRecordType()
	{
		return RecordType.byData(this.type);
	}
	
	
	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final byte[] type)
	{
		this.type = type;
	}
	
	
	public void setType(final String type)
	{
		try
		{
			this.type = type.getBytes(CHARSET_NAME);
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	public void setType(final RecordType type)
	{
		this.type = type.getData();
	}
	
	
	/**
	 * @return the id
	 */
	public byte[] getId()
	{
		return this.id;
	}
	
	
	public String getIdAsString()
	{
		if(this.id == null)
		{
			return "";
		}
		
		try
		{
			return new String(this.id,CHARSET_NAME);
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final byte[] id)
	{
		this.id = id;
	}
	
	
	public void setId(final String id)
	{
		try
		{
			this.id = id.getBytes(CHARSET_NAME);
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * @return the payload
	 */
	public byte[] getPayload()
	{
		return this.payload;
	}
	
	
	public String getPayloadAsString()
	{
		try
		{
			return new String(this.payload,CHARSET_NAME);
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(final byte[] payload)
	{
		this.payload = payload;
	}
	
	
	public void setPayload(final String payload)
	{
		try
		{
			this.payload = payload.getBytes(CHARSET_NAME);
		}
		catch(final UnsupportedEncodingException e)
		{
			// shouldn't happen, it's UTF-8
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Record [tnf=" + this.tnf + ", type=" + getTypeAsString() + ", id=" + getIdAsString()
				+ ", payload=" + getPayloadAsString() + "]";
	}
}
