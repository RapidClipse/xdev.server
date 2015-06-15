package net.jadoth.config;

public abstract class ConfigEntry<T>
{
	final String key;

	public ConfigEntry(final String key)
	{
		super();
		this.key = key;
	}

	public final String key()
	{
		return this.key;
	}

	public abstract T parse(String value);
}


final class ConfigEntryString extends ConfigEntry<String>
{
	ConfigEntryString(final String key)
	{
		super(key);
	}

	@Override
	public final String parse(final String value)
	{
		return value;
	}

}

final class ConfigEntryBoolean extends ConfigEntry<Boolean>
{
	ConfigEntryBoolean(final String key)
	{
		super(key);
	}

	@Override
	public final Boolean parse(final String value)
	{
		if("1".equals(value)){
			return Boolean.TRUE;
		}
		if("0".equals(value)){
			return Boolean.FALSE;
		}
		return Boolean.valueOf(value);
	}

}

final class ConfigEntryInteger extends ConfigEntry<Integer>
{
	ConfigEntryInteger(final String key)
	{
		super(key);
	}

	@Override
	public final Integer parse(final String value)
	{
		return Integer.valueOf(value);
	}

}

final class ConfigEntryLong extends ConfigEntry<Long>
{
	ConfigEntryLong(final String key)
	{
		super(key);
	}

	@Override
	public final Long parse(final String value)
	{
		return Long.valueOf(value);
	}

}

final class ConfigEntryDouble extends ConfigEntry<Double>
{
	ConfigEntryDouble(final String key)
	{
		super(key);
	}

	@Override
	public final Double parse(final String value)
	{
		return Double.valueOf(value);
	}

}
