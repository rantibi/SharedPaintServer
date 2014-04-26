package com.sharedpaint.resources;

import java.util.concurrent.locks.ReadWriteLock;

import javax.annotation.ManagedBean;

/**
 * Synchronize write board changes 
 */
@ManagedBean
public class WriteBoardLock extends BoardLock {

	@Override
	protected void lock(ReadWriteLock lock) {
		lock.writeLock().lock();
		
	}

	@Override
	protected void unlock(ReadWriteLock lock) {
		lock.writeLock().lock();
	}

}