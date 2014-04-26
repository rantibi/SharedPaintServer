package com.sharedpaint.utils;

import java.util.concurrent.locks.ReadWriteLock;

import javax.ejb.Local;

@Local
public interface LocksInterface<T> {
	public abstract ReadWriteLock getLock(T key);

}