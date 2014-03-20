package com.sharedpaint.crud;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import com.sharedpaint.entity.AbstractSharedPaintEntity;

@Stateless
public class CRUDEntityFacade<T extends AbstractSharedPaintEntity> implements
		SharedPaintEntityFacade<T> {

	private transient final Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public CRUDEntityFacade() {
		entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	} 

	@PersistenceContext(unitName = "GlassFishEJBPaintServer")
	protected transient EntityManager manager;

	@Override
	public T create(final T entity) throws EntityExistsException,
			IllegalStateException, IllegalArgumentException,
			TransactionRequiredException {
		manager.persist(entity);
		manager.flush();
		return entity;
	}

	@Override
	public T read(final Serializable primaryKey) throws IllegalStateException,
			IllegalArgumentException {
		return manager.find(entityClass, primaryKey);
	}

	@Override
	public T update(final T entity) throws IllegalStateException,
			IllegalArgumentException, TransactionRequiredException {
		manager.merge(entity);
		manager.flush();
		return entity;
	}

	@Override
	public void delete(final T entity) throws IllegalStateException,
			IllegalArgumentException, TransactionRequiredException,
			PersistenceException {
		manager.remove(entity);
		manager.flush();
	}

}