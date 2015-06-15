package net.jadoth.util.chars;

import static net.jadoth.math.JadothMath.notNegative;

import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.ConstList;
import net.jadoth.collections.EqConstHashEnum;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XImmutableEnum;
import net.jadoth.collections.types.XImmutableList;
import net.jadoth.functional.Procedure;
import net.jadoth.util.branching.ThrowBreak;

public interface StringTable
{

	/**
	 * An arbitrary name identifying this table instance, potentially <code>null</code>.
	 *
	 * @return this table's name.
	 */
	public String name();

	public XGettingEnum<String> columnNames();

	public XGettingList<String> columnTypes();

	public XGettingList<String[]> rows();



	public interface Creator
	{
		public StringTable createStringTable(
			String                   name       ,
			XGettingSequence<String> columnNames,
			XGettingList<String>     columnTypes,
			XGettingList<String[]>   rows
		);
	}



	public final class Static
	{
		public static final String assembleString(final StringTable st)
		{
			return assembleString(VarString.New((int)(st.rows().size() * 100.0f)), st).toString();
		}

		public static final VarString assembleString(final VarString vs, final StringTable st)
		{
			if(st.columnNames().isEmpty()){
				return vs.add("[no columns]");
			}

			st.columnNames().iterate(new Procedure<String>() {
				@Override public void accept(final String column)
				{
					vs.add(column).tab();
				}
			});
			vs.deleteLast();

			if(!st.columnTypes().isEmpty()){
				vs.lf().add('(');
				st.columnTypes().iterate(new Procedure<String>() {
					@Override public void accept(final String column)
					{
						vs.add(column).tab();
					}
				});
				vs.setLast(')');
			}
			if(!st.rows().isEmpty()){
				st.rows().iterate(new Procedure<String[]>() {
					@Override public void accept(final String[] row)
					{
						vs.lf();
						for(int i = 0; i < row.length; i++){
							vs.add(row[i]).tab();
						}
						if(row.length != 0){
							vs.deleteLast();
						}
					}
				});
			}

			return vs;
		}


		private Static() { throw new UnsupportedOperationException(); } // enforce static only
	}



	public final class Implementation implements StringTable
	{
		public static final class Creator implements StringTable.Creator
		{

			@Override
			public StringTable createStringTable(
				final String                   name       ,
				final XGettingSequence<String> columnNames,
				final XGettingList<String>     columnTypes,
				final XGettingList<String[]>   rows
			)
			{
				return new StringTable.Implementation(name, columnNames, columnTypes, rows);
			}

		}


		///////////////////////////////////////////////////////////////////////////
		// constants        //
		/////////////////////

		private static void validateColumnCount(final int columnCount, final XGettingList<String[]> rows)
		{
			final int columnCountMismatchIndex = rows.scan(new ColumnCountValidator(columnCount));
			if(columnCountMismatchIndex >= 0)
			{
				// (01.07.2013)EXCP: proper exception
				throw new IllegalArgumentException("Invalid column count in row "+columnCountMismatchIndex+" ("+columnCount+" required, "+rows.at(columnCountMismatchIndex).length+" available)");
			}
		}



		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private final String                  name   ;
		private final EqConstHashEnum<String> columns;
		private final ConstList<String>       types  ;
		private final ConstList<String[]>     rows   ;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		public Implementation(
			final XGettingSequence<String> columns    ,
			final XGettingList<String>     columnTypes,
			final XGettingList<String[]>   rows
		)
		{
			this(null, columns, columnTypes, rows);
		}

		public Implementation(
			final String                   name       ,
			final XGettingSequence<String> columns    ,
			final XGettingList<String>     columnTypes,
			final XGettingList<String[]>   rows
		)
		{
			super();
			this.name    = name                 ; // may be null
			this.columns = EqConstHashEnum.New(columns);
			validateColumnCount(Jadoth.to_int(this.columns.size()), rows);
			this.types   = new ConstList<>(columnTypes);
			this.rows    = new ConstList<>(rows);
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public final String name()
		{
			return this.name;
		}

		@Override
		public final XImmutableEnum<String> columnNames()
		{
			return this.columns;
		}

		@Override
		public final XGettingList<String> columnTypes()
		{
			return this.types;
		}

		@Override
		public final XImmutableList<String[]> rows()
		{
			return this.rows;
		}


		static final class ColumnCountValidator implements Predicate<String[]>
		{
			///////////////////////////////////////////////////////////////////////////
			// instance fields  //
			/////////////////////

			private final int columnCount;


			///////////////////////////////////////////////////////////////////////////
			// constructors     //
			/////////////////////

			ColumnCountValidator(final int columnCount)
			{
				super();
				this.columnCount = notNegative(columnCount);
			}



			///////////////////////////////////////////////////////////////////////////
			// override methods //
			/////////////////////

			@Override
			public final boolean test(final String[] row) throws ThrowBreak
			{
				return row.length != this.columnCount;
			}

		}

	}

}
