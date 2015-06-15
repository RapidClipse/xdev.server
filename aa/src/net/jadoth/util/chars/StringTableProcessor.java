package net.jadoth.util.chars;

import net.jadoth.functional.Procedure;

public interface StringTableProcessor<T>
{
	public <C extends Procedure<? super T>> C processStringTable(StringTable sourceData, C collector);



	public abstract class Implementation<T> implements StringTableProcessor<T>
	{
		protected abstract void validateColumnNames(StringTable sourceData);

		protected abstract T parseRow(String[] dataRow);

		@Override
		public final <C extends Procedure<? super T>> C processStringTable(
			final StringTable sourceData,
			final C           collector
		)
		{
			this.validateColumnNames(sourceData);
			sourceData.rows().iterate(new Procedure<String[]>() {
				@Override
				public void accept(final String[] dataRow)
				{
					collector.accept(Implementation.this.parseRow(dataRow));
				}
			});
			return collector;
		}
	}
}
