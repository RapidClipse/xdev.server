
package com.xdev.server.util.wizard;


import java.util.function.Consumer;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbstractSelect;


public final class XDEV
{
	// --------------- MASTER DETAIL -----------------------
	
	public static void bindComponents(Consumer<ComponentFilterBuilder> consumer)
	{
		ComponentFilterBuilder builder = new ComponentFilterBuilder.XdevComponentFilterBuilder();
		consumer.accept(builder);
		builder.execute();
	}
	
	
	public static <T> void bindForm(AbstractSelect masterComponent, BeanFieldGroup<T> form)
	{
		XDEV.bindForm((Consumer<FormBuilder<T>>)formBinder -> {
			formBinder.setForm(form);
			formBinder.setMasterComponent(masterComponent);
		});
	}
	
	
	protected static <T> void bindForm(Consumer<FormBuilder<T>> consumer)
	{
		FormBuilder<T> builder = new FormBuilder.XdevFormBuilder<T>();
		consumer.accept(builder);
		builder.execute();
	}
	
	
	// -------------------------------------------------------
	
	public static void createReport(Consumer<ReportCreator> consumer)
	{
		ReportCreator builder = new EntityReportCreator();
		consumer.accept(builder);
		builder.execute();
	}
	
	//TODO default createReport method?
}
