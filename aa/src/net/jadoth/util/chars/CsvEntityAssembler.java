package net.jadoth.util.chars;

import net.jadoth.Jadoth;
import net.jadoth.collections.ConstList;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XImmutableSequence;
import net.jadoth.csv.CsvRowAssembler;

public abstract class CsvEntityAssembler<T> implements CsvRowAssembler<T>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private final XImmutableSequence<String> columnHeader        ;
	private final int                        rowCharCountEstimate;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	protected CsvEntityAssembler(final XGettingSequence<String> columnHeader)
	{
		this(columnHeader, 100);
	}

	protected CsvEntityAssembler(final XGettingSequence<String> columnHeader, final int rowCharCountEstimate)
	{
		super();
		this.columnHeader         = columnHeader.immure();
		this.rowCharCountEstimate = rowCharCountEstimate ;
	}

	protected CsvEntityAssembler(final String... columnHeader)
	{
		this(new ConstList<>(columnHeader));
	}

	protected CsvEntityAssembler(final int rowCharCountEstimate, final String... columnHeader)
	{
		this(new ConstList<>(columnHeader), rowCharCountEstimate);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final XImmutableSequence<String> columnHeader()
	{
		return this.columnHeader;
	}

	protected VarString collector(final int entityCount)
	{
		return VarString.New(entityCount * this.rowCharCountEstimate);
	}

	public VarString assemble(final XGettingCollection<T> entities)
	{
		return this.assembleInto(this.collector(Jadoth.to_int(entities.size())), this.columnHeader(), entities);
	}

	public abstract VarString assembleInto(
		VarString                vs          ,
		XGettingSequence<String> columnHeader,
		XGettingCollection<T>    entities
	);

	public VarString assembleInto(
		final VarString             vs      ,
		final XGettingCollection<T> entities
	)
	{
		this.assembleInto(vs, this.columnHeader(), entities);
		return vs;
	}

}