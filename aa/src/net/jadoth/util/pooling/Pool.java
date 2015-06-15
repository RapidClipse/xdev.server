package net.jadoth.util.pooling;

import java.util.function.Supplier;

import net.jadoth.exceptions.ExceptionCreator;
import net.jadoth.functional.Procedure;

public interface Pool<E> extends Supplier<E>
{
	public interface Configuration<E>
	{
		public int getMaximumCount();

		public long getPoolCheckInterval();

		public Supplier<E> getSupplier();

		public Procedure<? super E> getDispatcher();

		public Procedure<? super E> getReturner();

		public Procedure<? super E> getCloser();

		public Pool.WaitTimeoutProvider getWaitTimeoutProvider();

		public Pool.WaitIntervalProvider getWaitIntervalProvider();

		public Controller getController();

		public ExceptionCreator<? extends RuntimeException> getExceptionCreator();



		public class Implementation<E> implements Configuration<E>
		{
			///////////////////////////////////////////////////////////////////////////
			// instance fields  //
			/////////////////////

			final int                                          maximumCount        ;
			final long                                         poolCheckInterval   ;
			final Supplier<E>                                  supplier            ;
			final Procedure<? super E>                         dispatcher          ;
			final Procedure<? super E>                         returner            ;
			final Procedure<? super E>                         closer              ;
			final Pool.WaitTimeoutProvider                     waitTimeoutProvider ;
			final Pool.WaitIntervalProvider                    waitIntervalProvider;
			final Controller                                   controller          ;
			final ExceptionCreator<? extends RuntimeException> exceptionCreator    ;



			///////////////////////////////////////////////////////////////////////////
			// constructors     //
			/////////////////////

			public Implementation(
				final int                                          maximumCount        ,
				final long                                         poolCheckInterval   ,
				final Supplier<E>                                  supplier            ,
				final Procedure<? super E>                         dispatcher          ,
				final Procedure<? super E>                         returner            ,
				final Procedure<? super E>                         closer              ,
				final Pool.WaitTimeoutProvider                     waitTimeoutProvider ,
				final Pool.WaitIntervalProvider                    waitIntervalProvider,
				final Controller                                   controller          ,
				final ExceptionCreator<? extends RuntimeException> exceptionCreator
			)
			{
				super();
				this.maximumCount         = maximumCount        ;
				this.poolCheckInterval    = poolCheckInterval   ;
				this.supplier             = supplier            ;
				this.dispatcher           = dispatcher          ;
				this.returner             = returner            ;
				this.closer               = closer              ;
				this.waitTimeoutProvider  = waitTimeoutProvider ;
				this.waitIntervalProvider = waitIntervalProvider;
				this.controller           = controller          ;
				this.exceptionCreator     = exceptionCreator    ;
			}



			///////////////////////////////////////////////////////////////////////////
			// override methods //
			/////////////////////

			@Override
			public int getMaximumCount()
			{
				return this.maximumCount;
			}

			@Override
			public long getPoolCheckInterval()
			{
				return this.poolCheckInterval;
			}

			@Override
			public Supplier<E> getSupplier()
			{
				return this.supplier;
			}

			@Override
			public Procedure<? super E> getDispatcher()
			{
				return this.dispatcher;
			}

			@Override
			public Procedure<? super E> getReturner()
			{
				return this.returner;
			}

			@Override
			public Procedure<? super E> getCloser()
			{
				return this.closer;
			}

			@Override
			public WaitTimeoutProvider getWaitTimeoutProvider()
			{
				return this.waitTimeoutProvider;
			}

			@Override
			public WaitIntervalProvider getWaitIntervalProvider()
			{
				return this.waitIntervalProvider;
			}

			@Override
			public Controller getController()
			{
				return this.controller;
			}

			@Override
			public ExceptionCreator<? extends RuntimeException> getExceptionCreator()
			{
				return this.exceptionCreator;
			}

		}

	}

	@Override
	public E get();

	public void takeBack(E element);

	public int getMaximumCount();

	public int getTotalCount();

	public int getFreeCount();

	public int getUsedCount();


	@FunctionalInterface
	public interface Controller
	{
		public int calculateCloseCount(
			int  maxConnectionCount  ,
			int  totalConnectionCount,
			int  freeConnectionCount ,
			long lastGetTime         ,
			long lastReturnTime      ,
			long lastCloseTime
		);
	}

	@FunctionalInterface
	public interface WaitTimeoutProvider
	{
		public long provideWaitTimeout();
	}

	@FunctionalInterface
	public interface WaitIntervalProvider
	{
		public long provideWaitInterval();
	}

}
