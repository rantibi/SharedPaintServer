package com.sharedpaint.crud;

import java.io.Serializable;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import com.sharedpaint.entity.AbstractSharedPaintEntity;

public interface SharedPaintEntityFacade<T extends AbstractSharedPaintEntity> {
	T create(T entity) throws EntityExistsException, IllegalStateException,
			IllegalArgumentException, TransactionRequiredException;

	T read(Serializable primaryKey) throws IllegalStateException,
			IllegalArgumentException;

	T update(T entity) throws IllegalStateException, IllegalArgumentException,
			TransactionRequiredException;

	void delete(T entity) throws IllegalStateException,
			IllegalArgumentException, TransactionRequiredException,
			PersistenceException;
}