package net.jadoth.util.chars;

import static net.jadoth.Jadoth.notNull;
import net.jadoth.Jadoth;
import net.jadoth.collections.BulkList;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.csv.CsvRowCollector;

public final class CsvEntityCollector<T> implements CsvRowCollector
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	private final CsvEntityParser<T> parser;
	private final BulkList<T>        rows  ;
	private final BulkList<String>   row   ;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public CsvEntityCollector(final CsvEntityParser<T> parser, final BulkList<T> rows)
	{
		super();
		this.parser = notNull(parser) ;
		this.rows   = notNull(rows)   ;
		this.row    = new BulkList<>();
	}

	@Override
	public final void beginTable(
		final String                   tableName  ,
		final XGettingSequence<String> columnNames,
		final XGettingList<String>     columnTypes
	)
	{
		/* table meta data ignored in entity-based implementation.
		 * Could however be used for validation in more complex implementations.
		 */
	}

	@Override
	public final void accept(final char[] data, final int offset, final int length)
	{
		this.row.add(data == null ?null :new String(data, offset, length));
	}

	@Override
	public final void completeRow()
	{
		this.parser.validateRow(this.row, Jadoth.to_int(this.rows.size()));
		final T entity;
		try
		{
			entity = this.parser.apply(this.row);
		}
		catch(final RuntimeException e)
		{
			throw new RuntimeException("Exception while parsing row "+Jadoth.to_int(this.rows.size()), e);
		}
		this.rows.add(entity);
		this.row.clear();
	}

	@Override
	public final void completeTable()
	{
		this.row.clear();
	}

	public final BulkList<T> rows()
	{
		return this.rows;
	}

}
