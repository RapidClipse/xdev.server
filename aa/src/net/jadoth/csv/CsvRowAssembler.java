package net.jadoth.csv;

import net.jadoth.functional.BiProcedure;

public interface CsvRowAssembler<R> extends BiProcedure<R, CsvAssembler>
{
	public static final CsvRowAssembler<CharSequence> delimitedString =
		new CsvRowAssembler<CharSequence>()
		{
			@Override
			public void accept(final CharSequence s, final CsvAssembler assembler)
			{
				if(s == null){
					return;
				}
				assembler.addRowValueDelimited(s);
			}
		}
	;

	public static final CsvRowAssembler<CharSequence> simpleString =
		new CsvRowAssembler<CharSequence>()
		{
			@Override
			public void accept(final CharSequence s, final CsvAssembler assembler)
			{
				if(s == null){
					return;
				}
				assembler.addRowValueSimple(s);
			}
		}
	;



	@Override
	public void accept(R row, CsvAssembler rowAssembler);

}
