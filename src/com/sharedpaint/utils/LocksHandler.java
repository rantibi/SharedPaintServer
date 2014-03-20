package com.sharedpaint.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocksHandler<T> {
	private Map<T, ReadWriteLock> locks;
	private Lock lock;

	public LocksHandler() {
		locks = new HashMap<T, ReadWriteLock>();
		lock = new ReentrantLock();
	}

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

	public void startReadOperation(T key) {
		getLock(key).readLock().lock();
		getLock(key).writeLock().lock();
		getLock(key).readLock().unlock();
	}

	public void finishReadOpertion(T key) {
		getLock(key).writeLock().unlock();
	}

	public void startWriteOperation(T key) {
		getLock(key).writeLock().lock();
		getLock(key).readLock().lock();
	}

	public void finishWriteOperation(T key) {
		getLock(key).readLock().unlock();
		getLock(key).writeLock().unlock();
	}

}
