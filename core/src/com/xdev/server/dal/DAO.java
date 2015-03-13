
package com.xdev.server.dal;


import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.googlecode.genericdao.dao.jpa.GenericDAO;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DAO
{
	public Class<? extends GenericDAO<?, ? extends Serializable>> daoClass();
}
