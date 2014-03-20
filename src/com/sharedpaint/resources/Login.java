package com.sharedpaint.resources;

import java.util.StringTokenizer;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sharedpaint.beans.BoardsHandlerInterface;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@ManagedBean
public class Login {

	public static final String AUTHORIZATION = "Authorization";
	@EJB
	private BoardsHandlerInterface boardsHandler;

	@AroundInvoke
	private Object login(InvocationContext ic) { 
		HttpServletRequest request = (HttpServletRequest) ic.getParameters()[0];
		String authorization = request.getHeader(AUTHORIZATION);
		 
		if(authorization == null){
			return Response.status(400).entity("Access Denied!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		 
		StringTokenizer st = new StringTokenizer(new String(Base64.decode(authorization)), ":");
		String email = st.hasMoreTokens() ? st.nextToken() : null;
		String password = st.hasMoreTokens() ? st.nextToken() : null;

		if ((email == null) || (password == null)) {
			return Response.status(400).entity("Access Denied!")
					.type(MediaType.TEXT_PLAIN).build();
		}

		try {
			boardsHandler.login(email, password);
			return ic.proceed();
		} catch (Exception e) {
			return Response.status(400).entity(e.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
		}
	}
}