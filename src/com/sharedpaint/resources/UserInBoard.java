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
import com.sharedpaint.entity.Board;
import com.sharedpaint.entity.User;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@ManagedBean
public class UserInBoard {

	@EJB
	private BoardsHandlerInterface boardsHandler;

	@AroundInvoke
	private Object isUserInBoard(InvocationContext ic) throws Exception {
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
		
		if (!boardsHandler.isUserMemberInBoard(email, boardId)) {
			throw new SharedPaintException("User " + email
					+ " not member in board: " + boardId);
		}
		return ic.proceed();
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
