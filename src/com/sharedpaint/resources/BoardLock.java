package com.sharedpaint.resources;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.ManagedBean;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.sharedpaint.utils.LocksHandler;


@ManagedBean
public class BoardLock {
	private static LocksHandler<Long> locks = new LocksHandler<>();

	@AroundInvoke
	private Object lockBoard(InvocationContext ic) throws Exception {
		long boardId = getBoardId(ic);
		try {
			locks.lock(boardId);			
			return ic.proceed();
		} finally {
			locks.unlock(boardId);
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
