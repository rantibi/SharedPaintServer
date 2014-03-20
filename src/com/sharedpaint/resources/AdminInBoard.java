package com.sharedpaint.resources;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sharedpaint.beans.BoardsHandlerInterface;
import com.sharedpaint.beans.SharedPaintException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@ManagedBean
public class AdminInBoard {

	@EJB
	private BoardsHandlerInterface boardsHandler;

	@AroundInvoke
	private Object isUserManagerInBoard(InvocationContext ic) {
		String email = null;
		
		HttpServletRequest request = (HttpServletRequest) ic.getParameters()[0];

		String authorization = request.getHeader(Login.AUTHORIZATION);

		if (authorization == null) {
			return Response.status(400).entity("Access Denied!")
					.type(MediaType.TEXT_PLAIN).build();
		}

		StringTokenizer st = new StringTokenizer(new String(
				Base64.decode(authorization)), ":");
		email = st.hasMoreTokens() ? st.nextToken() : null;

		if (email == null) {
			return Response.status(400).entity("Access Denied!")
					.type(MediaType.TEXT_PLAIN).build();
		}

		long boardId = getBoardId(ic);

		if (boardId == -1) {
			return Response.status(400).entity("Access Denied!")
					.type(MediaType.TEXT_PLAIN).build();

		}

		try {
			if (!boardsHandler.isUserManagerInBoard(email, boardId)) {
				throw new SharedPaintException("User " + email
						+ " not manager in board: " + boardId);
			}
			return ic.proceed();
		} catch (Exception e) {
			return Response.status(400).entity(e.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
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
