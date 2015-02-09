
package com.xdev.ui.entitycomponent;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.ScrollableResults;

import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import com.xdev.server.util.KeyValueType;
import com.xdev.ui.paging.PageChangedListener;
import com.xdev.ui.paging.PageableComponent;
import com.xdev.ui.paging.PageableContainer;
import com.xdev.ui.paging.PagedTableEvent;
import com.xdev.ui.paging.PagingUIModelProvider;


public class XdevPagedTable<ET> extends AbstractEntityTable<ET, PageableContainer<ET>> implements
		PageableComponent<ET>
{
	/**
	 *
	 */
	private static final long		serialVersionUID	= 1L;
	private PageableContainer<ET>	container			= null;
	private final ScrollableResults	results;


	public XdevPagedTable()
	{
		super("");
		this.results = null;
		this.addStyleName("pagedtable");
	}


	public XdevPagedTable(final ScrollableResults results)
	{
		super("");
		this.results = results;
		this.addStyleName("pagedtable");
	}


	public XdevPagedTable(final PageableContainer<ET> container)
	{
		super("",container);
		this.container = container;
		this.results = container.getResults();
		this.addStyleName("pagedtable");
	}


	public XdevPagedTable(final PageableContainer<ET> container, final String caption)
	{
		super(caption,container);
		this.container = container;
		this.results = container.getResults();
		this.addStyleName("pagedtable");
		this.setCaption(caption);
	}

	private List<PageChangedListener<ET>>	listeners	= null;


	public HorizontalLayout createControls()
	{
		final Label itemsPerPageLabel = new Label("Items per page:");
		final ComboBox itemsPerPageSelect = new ComboBox();

		itemsPerPageSelect.addItem("5");
		itemsPerPageSelect.addItem("10");
		itemsPerPageSelect.addItem("25");
		itemsPerPageSelect.addItem("50");
		itemsPerPageSelect.addItem("100");
		itemsPerPageSelect.addItem("600");
		itemsPerPageSelect.setImmediate(true);
		itemsPerPageSelect.setNullSelectionAllowed(false);
		itemsPerPageSelect.setWidth("80px");
		itemsPerPageSelect.addValueChangeListener(new ValueChangeListener()
		{
			private static final long	serialVersionUID	= -2255853716069800092L;


			@Override
			public void valueChange(final com.vaadin.data.Property.ValueChangeEvent event)
			{
				setPageLength(Integer.valueOf(String.valueOf(event.getProperty().getValue())));
			}
		});
		itemsPerPageSelect.select("5");
		final Label pageLabel = new Label("Page:&nbsp;",ContentMode.HTML);
		final TextField currentPageTextField = new TextField();
		currentPageTextField.setValue(String.valueOf(this.container.getCurrentPage()));
		currentPageTextField.setConverter(Integer.class);
		currentPageTextField.addValidator(new IntegerRangeValidator("Wrong page number",0,
				this.container.getTotalAmountOfPages() - 1));
		final Label separatorLabel = new Label("&nbsp;/&nbsp;",ContentMode.HTML);
		final Label totalPagesLabel = new Label(String.valueOf(this.container
				.getTotalAmountOfPages() - 1),ContentMode.HTML);
		currentPageTextField.setStyleName(ValoTheme.TEXTFIELD_SMALL);
		currentPageTextField.setImmediate(true);
		currentPageTextField.addValueChangeListener(new ValueChangeListener()
		{
			private static final long	serialVersionUID	= -2255853716069800092L;


			@Override
			public void valueChange(final com.vaadin.data.Property.ValueChangeEvent event)
			{
				if(currentPageTextField.isValid() && currentPageTextField.getValue() != null)
				{
					final int page = Integer.valueOf(String.valueOf(currentPageTextField.getValue()));
					setCurrentPage(page);
				}
			}
		});

		// pageLabel.setWidth(null);
		// currentPageTextField.setWidth("20px");
		// separatorLabel.setWidth(null);
		// totalPagesLabel.setWidth(null);

		final HorizontalLayout controlBar = new HorizontalLayout();
		final HorizontalLayout pageSize = new HorizontalLayout();
		final HorizontalLayout pageManagement = new HorizontalLayout();
		final Button first = new Button("<<",new ClickListener()
		{
			private static final long	serialVersionUID	= -355520120491283992L;


			@Override
			public void buttonClick(final ClickEvent event)
			{
				XdevPagedTable.this.firstPage();
				currentPageTextField.setValue(String.valueOf(XdevPagedTable.this.container
						.getCurrentPage()));
			}
		});
		final Button previous = new Button("<",new ClickListener()
		{
			private static final long	serialVersionUID	= -355520120491283992L;


			@Override
			public void buttonClick(final ClickEvent event)
			{
				XdevPagedTable.this.previousPage();
				currentPageTextField.setValue(String.valueOf(XdevPagedTable.this.container
						.getCurrentPage()));
			}
		});
		final Button next = new Button(">",new ClickListener()
		{
			private static final long	serialVersionUID	= -1927138212640638452L;


			@Override
			public void buttonClick(final ClickEvent event)
			{
				XdevPagedTable.this.nextPage();
				currentPageTextField.setValue(String.valueOf(XdevPagedTable.this.container
						.getCurrentPage()));
			}
		});
		final Button last = new Button(">>",new ClickListener()
		{
			private static final long	serialVersionUID	= -355520120491283992L;


			@Override
			public void buttonClick(final ClickEvent event)
			{
				XdevPagedTable.this.lastPage();
				currentPageTextField.setValue(String.valueOf(XdevPagedTable.this.container
						.getCurrentPage() - 1));
			}
		});
		first.setStyleName(ValoTheme.BUTTON_LINK);
		previous.setStyleName(ValoTheme.BUTTON_LINK);
		next.setStyleName(ValoTheme.BUTTON_LINK);
		last.setStyleName(ValoTheme.BUTTON_LINK);

		first.setEnabled(this.container.getCurrentPage() > 0);
		previous.setEnabled(this.container.getCurrentPage() > 0);
		next.setEnabled(this.container.getCurrentPage() < this.container.getTotalAmountOfPages() - 1);
		last.setEnabled(!(this.container.getCurrentPage() == this.container.getTotalAmountOfPages() - 1));

		itemsPerPageLabel.addStyleName("pagedtable-itemsperpagecaption");
		itemsPerPageSelect.addStyleName("pagedtable-itemsperpagecombobox");
		pageLabel.addStyleName("pagedtable-pagecaption");
		currentPageTextField.addStyleName("pagedtable-pagefield");
		separatorLabel.addStyleName("pagedtable-separator");
		totalPagesLabel.addStyleName("pagedtable-total");
		first.addStyleName("pagedtable-first");
		previous.addStyleName("pagedtable-previous");
		next.addStyleName("pagedtable-next");
		last.addStyleName("pagedtable-last");

		itemsPerPageLabel.addStyleName("pagedtable-label");
		itemsPerPageSelect.addStyleName("pagedtable-combobox");
		pageLabel.addStyleName("pagedtable-label");
		currentPageTextField.addStyleName("pagedtable-label");
		separatorLabel.addStyleName("pagedtable-label");
		totalPagesLabel.addStyleName("pagedtable-label");
		first.addStyleName("pagedtable-button");
		previous.addStyleName("pagedtable-button");
		next.addStyleName("pagedtable-button");
		last.addStyleName("pagedtable-button");

		pageSize.addComponent(itemsPerPageLabel);
		pageSize.addComponent(itemsPerPageSelect);
		pageSize.setComponentAlignment(itemsPerPageLabel,Alignment.MIDDLE_LEFT);
		pageSize.setComponentAlignment(itemsPerPageSelect,Alignment.MIDDLE_LEFT);
		pageSize.setSpacing(true);
		pageManagement.addComponent(first);
		pageManagement.addComponent(previous);
		pageManagement.addComponent(pageLabel);
		pageManagement.addComponent(currentPageTextField);
		pageManagement.addComponent(separatorLabel);
		pageManagement.addComponent(totalPagesLabel);
		pageManagement.addComponent(next);
		pageManagement.addComponent(last);
		pageManagement.setComponentAlignment(first,Alignment.MIDDLE_LEFT);
		pageManagement.setComponentAlignment(previous,Alignment.MIDDLE_LEFT);
		pageManagement.setComponentAlignment(pageLabel,Alignment.MIDDLE_LEFT);
		pageManagement.setComponentAlignment(currentPageTextField,Alignment.MIDDLE_LEFT);
		pageManagement.setComponentAlignment(separatorLabel,Alignment.MIDDLE_LEFT);
		pageManagement.setComponentAlignment(totalPagesLabel,Alignment.MIDDLE_LEFT);
		pageManagement.setComponentAlignment(next,Alignment.MIDDLE_LEFT);
		pageManagement.setComponentAlignment(last,Alignment.MIDDLE_LEFT);
		pageManagement.setWidth(null);
		pageManagement.setSpacing(true);
		controlBar.addComponent(pageSize);
		controlBar.addComponent(pageManagement);
		controlBar.setComponentAlignment(pageManagement,Alignment.MIDDLE_CENTER);
		controlBar.setWidth("100%");
		controlBar.setExpandRatio(pageSize,1);

		this.addListener(new PageChangedListener<ET>()
		{
			@Override
			public void pageChanged(final PagedTableEvent<ET> event)
			{
				first.setEnabled(XdevPagedTable.this.container.getCurrentPage() > 0);
				previous.setEnabled(XdevPagedTable.this.container.getCurrentPage() > 0);
				next.setEnabled(XdevPagedTable.this.container.getCurrentPage() < XdevPagedTable.this.container
						.getTotalAmountOfPages() - 1);
				last.setEnabled(!(XdevPagedTable.this.container.getCurrentPage() == XdevPagedTable.this.container
						.getTotalAmountOfPages() - 1));

				currentPageTextField.setValue(String.valueOf(XdevPagedTable.this.container
						.getCurrentPage()));
				totalPagesLabel.setValue(String.valueOf(XdevPagedTable.this.container
						.getTotalAmountOfPages() - 1));
				itemsPerPageSelect.setValue(String.valueOf(XdevPagedTable.this.container
						.getPageLength()));
			}
		});
		return controlBar;
	}


	public void addListener(final PageChangedListener<ET> listener)
	{
		if(this.listeners == null)
		{
			this.listeners = new ArrayList<PageChangedListener<ET>>();
		}
		this.listeners.add(listener);
	}


	public void removeListener(final PageChangedListener<ET> listener)
	{
		if(this.listeners == null)
		{
			this.listeners = new ArrayList<PageChangedListener<ET>>();
		}
		this.listeners.remove(listener);
	}


	private void firePageChangedEvent()
	{
		if(this.listeners != null)
		{
			final PagedTableEvent<ET> event = new PagedTableEvent<ET>(this);
			for(final PageChangedListener<ET> listener : this.listeners)
			{
				listener.pageChanged(event);
			}
		}
	}


	public void setAlwaysRecalculateColumnWidths(final boolean alwaysRecalculateColumnWidths)
	{
		this.alwaysRecalculateColumnWidths = alwaysRecalculateColumnWidths;
	}


	// Delegates //

	@Override
	public void nextPage()
	{
		this.setCurrentPageFirstItemIndex(this.container.nextPage());
		firePageChangedEvent();
	}


	@Override
	public void previousPage()
	{
		this.setCurrentPageFirstItemIndex(this.container.previousPage());
		firePageChangedEvent();
	}


	@Override
	public void lastPage()
	{
		this.setCurrentPageFirstItemIndex(this.container.lastPage());
		firePageChangedEvent();
	}


	@Override
	public void firstPage()
	{
		this.setCurrentPageFirstItemIndex(this.container.firstPage());
		firePageChangedEvent();
	}


	@Override
	public int getTotalAmountOfPages()
	{
		return this.container.getTotalAmountOfPages();
	}


	@Override
	public int getCurrentPage()
	{
		return this.container.getCurrentPage();
	}


	@Override
	public void setCurrentPage(final int currentPage)
	{
		this.container.setCurrentPage(currentPage);
		firePageChangedEvent();
	}


	@Override
	public void setPageLength(final int pageLength)
	{
		super.setPageLength(pageLength);
		if(this.container != null)
		{
			this.container.setPageLength(pageLength);
			// TODO event required?
			firePageChangedEvent();
		}
	}


	// @SuppressWarnings("unchecked")
	// // due to Vaadin container API casting here is inevitable. Container
	// clearly
	// // should be implemented generic.
	// @Override
	// public void setContainerDataSource(Container container)
	// {
	// try
	// {
	// this.container = (PageableContainer<ET>)container;
	// this.setContainerDataSource(container);
	// }
	// catch(ClassCastException e)
	// {
	// throw new IllegalArgumentException(
	// "PagedComponents can only handle instances of ScrollableContainer");
	// }
	//
	// }
	//
	//
	// @Override
	// public PageableContainer<ET> getContainerDataSource()
	// {
	// return this.container;
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PagingUIModelProvider<ET> getModelProvider()
	{
		return new PagingUIModelProvider<ET>(this.results);
	}


	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final <K, V> void setModel(final Class<ET> entityClass,
			final KeyValueType<K, V>... nestedProperties)
	{
		this.setGenericDataSource(this.getModelProvider().getModel(this,entityClass,
				nestedProperties));
	}

}
