package net.jadoth.config;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.collections.EqConstHashTable;
import net.jadoth.collections.EqHashEnum;
import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingTable;
import net.jadoth.functional.Procedure;
import net.jadoth.meta.JadothConsole;
import net.jadoth.util.KeyValue;
import net.jadoth.util.chars.VarString;


public abstract class AbstractConfig implements Config
{
	///////////////////////////////////////////////////////////////////////////
	// class methods  //
	///////////////////

	static final EqHashTable<String, String> toTable(final EqHashEnum<ConfigFile> configFiles)
	{
		final EqHashTable<String, String> newConfig = EqHashTable.New();
		configFiles.iterate(new Procedure<ConfigFile>() {
			@Override
			public void accept(final ConfigFile e)
			{
				newConfig.addAll(e.table());
			}
		});
		return newConfig;
	}

	static final EqHashTable<String, String> toTable(final XGettingCollection<KeyValue<String, String>> entries)
	{
		// eintr�ge in neuer hashtable sammeln
		final EqHashTable<String, String> newConfig = EqHashTable.New();
		newConfig.addAll(entries);
		return newConfig;
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	final String                            identifier     ;
	final EqHashTable<String, ConfigFile>   configFiles     = EqHashTable.New();
	final XGettingTable<String, ConfigFile> viewConfigFiles = this.configFiles.view();



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	AbstractConfig(final String identifier)
	{
		super();
		this.identifier = notNull(identifier);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final XGettingTable<String, ConfigFile> files()
	{
		return this.viewConfigFiles;
	}

	@Override
	public abstract XGettingTable<String, String> table();

	@Override
	public abstract String get(String key);

	@Override
	public final String identifier()
	{
		return this.identifier;
	}

	@Override
	public final <T> T get(final ConfigEntry<T> entry)
	{
		try{
			return entry.parse(this.get(entry.key));
		}
		catch(final Exception e) {
			throw new RuntimeException("Exception for entry "+entry.key, e); // (18.11.2013 TM)EXCP: proper exception
		}
	}


	void updateFile(final ConfigFile newConfigFile)
	{
		ConfigFile file = this.configFiles.get(newConfigFile.name());
		if(file == null){
			this.configFiles.add(newConfigFile.name(), file = newConfigFile);
		}
		else {
			file.table().putAll(newConfigFile.table()); // put to override old entries
		}
	}

	void updateFiles(final EqHashTable<String, ConfigFile> newConfigFiles)
	{
		newConfigFiles.values().iterate(new Procedure<ConfigFile>() {
			@Override
			public void accept(final ConfigFile e)
			{
				AbstractConfig.this.updateFile(e);
			}
		});
	}

	EqConstHashTable<String, String> compileEntries()
	{
		final EqHashTable<String, String> table = EqHashTable.New();
		this.configFiles.values().iterate(new Procedure<ConfigFile>() {
			@Override
			public void accept(final ConfigFile e)
			{
				table.addAll(e.table());
			}
		});
		return table.immure();
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public String toString()
	{
		final VarString vs = VarString.New();

		this.configFiles.values().iterate(new Procedure<ConfigFile>() {
			@Override
			public void accept(final ConfigFile e)
			{
				vs.add(e.name()).blank().add('(').add(e.name).add(')').lf();
				JadothConsole.assembleTable(vs, e.table(), "---", "---\n", "\n---", null, null);
				vs.lf().add("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||").lf();
			}
		});
		return vs.toString();
	}

}
