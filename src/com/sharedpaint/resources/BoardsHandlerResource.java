package com.sharedpaint.resources;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sharedpaint.beans.BoardsHandlerInterface;
import com.sharedpaint.beans.SharedPaintException;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.transfer.DrawableHolder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@ManagedBean
@Path("/sharedpaint")
public class BoardsHandlerResource {

	@Context
	private UriInfo context;

	@Context
	private SecurityContext securityContext;

	@EJB
	private BoardsHandlerInterface boardsHandler;

	@GET
	@Path("/login")
	@Interceptors(Login.class)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
	public Response login(@Context HttpServletRequest request) {
		return Response.ok().entity("Logged In").build();
	}

	@GET
	@Path("/register")
	public Response register(@Context HttpServletRequest request,
			@QueryParam("user_email") String email,
			@QueryParam("password") String password) {
		try {
			boardsHandler.createUser(email, password);
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
		return Response.ok().entity("User created").build();
	}

	@GET
	@Path("/boards")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors(Login.class)
	public Response getBoards(@Context HttpServletRequest request) {
		String email = getEmailFromHeader(request);

		try {
			List<BoardDetails> allBoardsForUser = boardsHandler
					.getAllBoardsForUser(email);
			Gson gson = new Gson();
			return Response.ok(gson.toJson(allBoardsForUser)).build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/new_board")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors(Login.class)
	public Response createNewBoard(@Context HttpServletRequest request,
			@QueryParam("board_name") String boardName) {
		String email = getEmailFromHeader(request);

		try {
			BoardDetails board = boardsHandler.createNewBoard(boardName, email);
			Gson gson = new Gson();
			return Response.ok(gson.toJson(board)).build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/new_drawble_ids")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response getNewDrawbleIds(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@QueryParam("count") int count) {
		List<Long> newIds = boardsHandler.getSafeFreeNewDrawbleIds(count);
		Gson gson = new Gson();
		return Response.ok(gson.toJson(newIds)).build();
	}

	@GET
	@Path("/drawables_in_board")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response getDrawablesInBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId) {
		try {
			BoardUpdate boardUpdate = boardsHandler
					.getAllDrawablesBoradUpdate(boardId);
			Gson gson = new Gson();
			return Response.ok(gson.toJson(boardUpdate)).build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}

		/*
		 * try { List<DrawableHolder> drawables = boardsHandler
		 * .getDrawablesInBoard(boardId); Gson gson = new Gson(); return
		 * Response.ok(gson.toJson(drawables)).build(); } catch
		 * (SharedPaintException e) { return
		 * Response.status(Status.BAD_REQUEST).entity(e.getMessage()) .build();
		 * }
		 */
	}

	@GET
	@Path("/board_update")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response getBoardUpdate(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@QueryParam("from") long from) {
		try {
			BoardUpdate boardUpdate = boardsHandler.getBoradUpdate(boardId,
					new Date(from));
			Gson gson = new Gson();
			return Response.ok(gson.toJson(boardUpdate)).build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@POST
	@Path("/add_drawable_to_board")
	@Produces(MediaType.APPLICATION_FORM_URLENCODED)
	@Interceptors(Login.class)
	// TODO fix it
	// @Interceptors({Login.class, UserInBoard.class})
	public Response addDrawableToBoard(@Context HttpServletRequest request) {
		try {
			String email = getEmailFromHeader(request);
			Gson gson = new Gson();
			Type collectionType = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> params = gson.fromJson(
					request.getParameter("params"), collectionType);

			long boardId = Long.parseLong(params.get("board_id"));
			String drawableHolderJson = params.get("drawable");

			DrawableHolder drawableHolder = gson.fromJson(drawableHolderJson,
					DrawableHolder.class);
			boardsHandler.addDrawableToBoard(drawableHolder.getId(),
					drawableHolder.getDrawable(), boardId, email);
			return Response.ok().build();

		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/board_undo")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response boardUndo(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId) {
		try {
			boardsHandler.undoInBoard(boardId);
			return Response.ok("Success").build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}

	}

	@GET
	@Path("/board_redo")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response boardRedo(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId) {
		try {
			boardsHandler.redoInBoard(boardId);
			return Response.ok("Success").build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/remove_board_member")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response removeBoardMember(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@QueryParam("user_email") String userEmail) {
		try {
			boardsHandler.removeUserFromBoard(userEmail, boardId);
			return Response.ok("Success").build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/delete_board")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, AdminInBoard.class })
	public Response deleteBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId) {
		try {
			boardsHandler.deleteBoard(boardId);
			return Response.ok("Success").build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/leave_board")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response leaveBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId) {
		try {
			String email = getEmailFromHeader(request);
			boardsHandler.removeUserFromBoard(email, boardId);
			return Response.ok("Success").build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/add_board_member")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, AdminInBoard.class })
	public Response addMemberToBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@QueryParam("user_email") String userEmail) {
		try {
			boardsHandler.addUserToBoard(userEmail, boardId);
			return Response.ok("Success").build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}

	}

	@GET
	@Path("/board_members")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class })
	public Response getUsersEmailInBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId) {

		try {
			List<String> usersInBoard = boardsHandler
					.getUsersEmailInBoard(boardId);
			Gson gson = new Gson();
			return Response.ok(gson.toJson(usersInBoard)).build();
		} catch (SharedPaintException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.build();
		}
	}

	private String getEmailFromHeader(HttpServletRequest request) {
		String authorization = request.getHeader(Login.AUTHORIZATION);
		StringTokenizer st = new StringTokenizer(new String(
				Base64.decode(authorization)), ":");
		return st.nextToken();
	}

}
