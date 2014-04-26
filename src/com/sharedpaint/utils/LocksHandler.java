package com.sharedpaint.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.ejb.Singleton;

@Singleton
public class LocksHandler<T> implements LocksInterface<T> {
	private Map<T, ReadWriteLock> locks;
	private Lock lock;

	public LocksHandler() {
		locks = new HashMap<T, ReadWriteLock>();
		lock = new ReentrantLock();
	}
	
	@Override
	public ReadWriteLock getLock(T key) {
		ReadWriteLock readWriteLock = locks.get(key);
		if (readWriteLock == null) {
			lock.lock();
			try {
				readWriteLock = locks.get(key);
				
				if (readWriteLock == null) {
					readWriteLock = new ReentrantReadWriteLock();
					locks.put(key, readWriteLock);
				}
			} finally {
				lock.unlock();
			}
		}

		return readWriteLock;

	}
}
