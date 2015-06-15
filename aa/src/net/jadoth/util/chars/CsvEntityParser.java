package net.jadoth.util.chars;

import net.jadoth.Jadoth;
import net.jadoth.collections.BulkList;

public abstract class CsvEntityParser<T>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private final int columnCount;
	private final int collectorInitialCapacity;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public CsvEntityParser(final int columnCount)
	{
		this(columnCount, 1024);
	}

	public CsvEntityParser(final int columnCount, final int collectorInitialCapacity)
	{
		super();
		this.columnCount = columnCount;
		this.collectorInitialCapacity = collectorInitialCapacity;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public BulkList<T> parse(final _charArrayRange input)
	{
		return this.parseInto(input, this.collector());
	}

	public abstract BulkList<T> parseInto(final _charArrayRange input, final BulkList<T> collector);

	protected BulkList<T> collector()
	{
		return new BulkList<>(this.collectorInitialCapacity);
	}

	protected void validateRow(final BulkList<String> row, final int rowIndex)
	{
		if(Jadoth.to_int(row.size()) == this.columnCount){
			return;
		}
		throw new RuntimeException("Column count mismatch ("+Jadoth.to_int(row.size())+" != "+this.columnCount+") at row "+rowIndex);
	}



	protected abstract T apply(BulkList<String> row);
}
