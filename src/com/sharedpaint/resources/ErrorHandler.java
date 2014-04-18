package com.sharedpaint.resources;

import javax.annotation.ManagedBean;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@ManagedBean
public class ErrorHandler {

	@AroundInvoke
	private Object error(InvocationContext ic) { 
				try {
			return ic.proceed();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
		}
	}
}
