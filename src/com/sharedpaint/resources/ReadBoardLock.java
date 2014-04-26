package com.sharedpaint.resources;

import java.util.concurrent.locks.ReadWriteLock;

import javax.annotation.ManagedBean;

/**
 * Synchronize read board changes 
 */
@ManagedBean
public class ReadBoardLock extends BoardLock {

	@Override
	protected void lock(ReadWriteLock lock) {
		lock.readLock().lock();
		
	}

	@Override
	protected void unlock(ReadWriteLock lock) {
		lock.readLock().lock();
	}

}
