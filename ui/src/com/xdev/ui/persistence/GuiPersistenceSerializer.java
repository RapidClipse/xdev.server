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

package com.xdev.ui.persistence;


import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


/**
 * @author XDEV Software
 *		
 */
public interface GuiPersistenceSerializer
{
	public final static GuiPersistenceSerializer DEFAULT = new Json();


	public String serialize(final GuiPersistentStates states);


	public GuiPersistentStates deserialize(final String data) throws JsonParseException;



	public static class Json implements GuiPersistenceSerializer
	{
		@Override
		public String serialize(final GuiPersistentStates states)
		{
			return gson().toJson(states,GuiPersistentStates.class);
		}


		@Override
		public GuiPersistentStates deserialize(final String data) throws JsonParseException
		{
			return gson().fromJson(data,GuiPersistentStates.class);
		}


		protected Gson gson()
		{
			return new GsonBuilder().registerTypeAdapter(Class.class,new ClassHandler())
					.registerTypeAdapter(Object[].class,new ObjectArrayHandler())
					.registerTypeAdapter(GuiPersistenceEntry.class,new GuiPersistenceEntryHandler())
					.enableComplexMapKeySerialization().create();
		}



		protected static abstract class Handler
		{
			public JsonElement serializeObject(final Object value,
					final JsonSerializationContext context)
			{
				if(value == null)
				{
					return JsonNull.INSTANCE;
				}

				final Class<?> clazz = value.getClass();
				final JsonObject jsonObj = new JsonObject();
				jsonObj.addProperty("class",clazz.getName());
				jsonObj.add("value",context.serialize(value,clazz));
				return jsonObj;
			}


			public Object deserializeObject(final JsonElement element,
					final JsonDeserializationContext context) throws JsonParseException
			{
				if(element.isJsonNull())
				{
					return null;
				}

				if(!element.isJsonObject())
				{
					throw new JsonParseException("Unexpected element: " + element);
				}

				final JsonObject jsonObj = (JsonObject)element;
				final String clazzName = jsonObj.get("class").getAsString();
				try
				{
					final Class<?> clazz = Class.forName(clazzName);
					return context.deserialize(jsonObj.get("value"),clazz);
				}
				catch(final ClassNotFoundException e)
				{
					throw new JsonParseException(e);
				}
			}
		}



		protected static class GuiPersistenceEntryHandler extends Handler implements
				JsonSerializer<GuiPersistenceEntry>, JsonDeserializer<GuiPersistenceEntry>
		{
			@Override
			public JsonElement serialize(final GuiPersistenceEntry entry, final Type type,
					final JsonSerializationContext context)
			{
				final JsonObject jsonObj = new JsonObject();

				entry.values().forEach((key, value) -> {
					jsonObj.add(key,serializeObject(value,context));
				});

				return jsonObj;
			}


			@Override
			public GuiPersistenceEntry deserialize(final JsonElement element, final Type type,
					final JsonDeserializationContext context) throws JsonParseException
			{
				final Map<String, Object> values = new LinkedHashMap<>();

				final JsonObject jsonObj = (JsonObject)element;
				jsonObj.entrySet().forEach(entry -> {
					final String key = entry.getKey();
					final JsonElement jsonElement = entry.getValue();
					values.put(key,deserializeObject(jsonElement,context));
				});

				return GuiPersistenceEntry.New(values);
			}
		}



		protected static class ClassHandler extends Handler
				implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>>
		{
			@Override
			public JsonElement serialize(final Class<?> clazz, final Type type,
					final JsonSerializationContext context)
			{
				if(clazz == null)
				{
					return JsonNull.INSTANCE;
				}

				final JsonObject jsonObj = new JsonObject();
				jsonObj.addProperty("class",clazz.getName());
				return jsonObj;
			}


			@Override
			public Class<?> deserialize(final JsonElement element, final Type type,
					final JsonDeserializationContext context) throws JsonParseException
			{
				if(element.isJsonNull())
				{
					return null;
				}

				final JsonObject jsonObj = (JsonObject)element;
				final String clazzName = jsonObj.get("class").getAsString();
				try
				{
					return Class.forName(clazzName);
				}
				catch(final ClassNotFoundException e)
				{
					throw new JsonParseException(e);
				}
			}
		}



		protected static class ObjectArrayHandler extends Handler
				implements JsonSerializer<Object[]>, JsonDeserializer<Object[]>
		{
			@Override
			public JsonElement serialize(final Object[] array, final Type type,
					final JsonSerializationContext context)
			{
				final JsonArray jsonArray = new JsonArray();
				for(final Object value : array)
				{
					jsonArray.add(serializeObject(value,context));
				}
				return jsonArray;
			}


			@Override
			public Object[] deserialize(final JsonElement element, final Type type,
					final JsonDeserializationContext context) throws JsonParseException
			{
				final JsonArray jsonArray = (JsonArray)element;
				final int size = jsonArray.size();
				final Object[] array = new Object[size];
				for(int i = 0; i < size; i++)
				{
					array[i] = deserializeObject(jsonArray.get(i),context);
				}
				return array;
			}
		}
	}
}
