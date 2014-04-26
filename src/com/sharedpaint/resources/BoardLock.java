package com.sharedpaint.resources;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReadWriteLock;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.sharedpaint.utils.LocksInterface;


/**
 * Synchronize board changes 
 */
@ManagedBean
public abstract class BoardLock {
	
	@EJB
	protected LocksInterface<Long> locks;

	protected abstract void lock(ReadWriteLock lock);
	protected abstract void unlock(ReadWriteLock lock);
	
	@AroundInvoke
	private Object lockBoard(InvocationContext ic) throws Exception {
		long boardId = getBoardId(ic);
		ReadWriteLock lock = locks.getLock(boardId);
		try {
			lock(lock);
			return ic.proceed();
		} finally {
			unlock(lock);
		}
	}

	private long getBoardId(InvocationContext ic) {
		Method method = ic.getMethod();
		Annotation[][] annotations = method.getParameterAnnotations();
		// iterate through annotations and check
		Object[] parameterValues = ic.getParameters();

		for (int i = 0; i < annotations.length; i++) {
			for (int j = 0; j < annotations[i].length; j++) {
				if (annotations[i][j] instanceof BoardId) {
					if (!(parameterValues[i] instanceof Long)) {
						throw new RuntimeException(
								"BoardId must be on long type");
					}
					return (long) parameterValues[i];
				}
			}
		}

		return -1;
	}
}
