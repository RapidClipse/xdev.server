
package com.xdev.ui.util.wizard;


import java.util.function.Consumer;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbstractSelect;


public final class XDEV
{
	// --------------- MASTER DETAIL -----------------------
	
	public static void bindComponents(final Consumer<JPAComponentFilterBuilder> consumer)
	{
		final JPAComponentFilterBuilder builder = new XdevJPAComponentFilterBuilder();
		consumer.accept(builder);
		builder.execute();
	}
	
	
	public static <T> void bindForm(final AbstractSelect masterComponent,
			final BeanFieldGroup<T> form)
	{
		XDEV.bindForm((Consumer<FormBuilder<T>>)formBinder -> {
			formBinder.setForm(form);
			formBinder.setMasterComponent(masterComponent);
		});
	}
	
	
	protected static <T> void bindForm(final Consumer<FormBuilder<T>> consumer)
	{
		final FormBuilder<T> builder = new FormBuilder.XdevFormBuilder<T>();
		consumer.accept(builder);
		builder.execute();
	}
	
	
	// -------------------------------------------------------
	
	// TODO default createReport method?
	public static void createReport(final Consumer<ReportCreator> consumer)
	{
		final ReportCreator builder = new EntityReportCreator();
		consumer.accept(builder);
		builder.execute();
	}
	
	
	public static void buildTree(final Consumer<XdevFillTree> consumer, final AbstractSelect tree)
	{
		final XdevFillTree builder = new XdevFillTree(tree);
		consumer.accept(builder);
		builder.execute();
	}
}
