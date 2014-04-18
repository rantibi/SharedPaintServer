package com.sharedpaint.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocksHandler<T> {
	private Map<T, Lock> locks;
	private Lock lock;

	public LocksHandler() {
		locks = new HashMap<T, Lock>();
		lock = new ReentrantLock();
	}

	private Lock getLock(T key) {
		Lock readWriteLock = locks.get(key);
		if (readWriteLock == null) {
			lock.lock();
			try {
				readWriteLock = locks.get(key);
				
				if (readWriteLock == null) {
					readWriteLock = new ReentrantLock();
					locks.put(key, readWriteLock);
				}
			} finally {
				lock.unlock();
			}
		}

		return readWriteLock;

	}

	public void lock(T key) {
		getLock(key).lock();
	}
	
	public void unlock(T key) {
		getLock(key).unlock();
	}

}
