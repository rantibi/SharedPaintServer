package com.sharedpaint.resources;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.sharedpaint.beans.BoardsHandlerInterface;
import com.sharedpaint.beans.SharedPaintException;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.transfer.DrawableHolder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@ManagedBean
@Path("/sharedpaint")
@Interceptors(ErrorHandler.class)
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
	@Interceptors(ErrorHandler.class)
	public Response register(@Context HttpServletRequest request,
			@QueryParam("user_email") String email,
			@QueryParam("password") String password)
			throws SharedPaintException {
		boardsHandler.createUser(email, password);
		return Response.ok().entity("User created").build();
	}

	@GET
	@Path("/boards")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors(Login.class)
	public Response getBoards(@Context HttpServletRequest request)
			throws SharedPaintException {
		String email = getEmailFromHeader(request);
		List<BoardDetails> allBoardsForUser = boardsHandler
				.getAllBoardsForUser(email);
		Gson gson = new Gson();
		return Response.ok(gson.toJson(allBoardsForUser)).build();
	}

	@GET
	@Path("/new_board")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors(Login.class)
	public Response createNewBoard(@Context HttpServletRequest request,
			@QueryParam("board_name") String boardName)
			throws SharedPaintException {
		String email = getEmailFromHeader(request);
		BoardDetails board = boardsHandler.createNewBoard(boardName, email);
		Gson gson = new Gson();
		return Response.ok(gson.toJson(board)).build();
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
	@Interceptors({ Login.class, UserInBoard.class, ReadBoardLock.class })
	public Response getDrawablesInBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId)
			throws SharedPaintException {
		BoardUpdate boardUpdate = boardsHandler
				.getAllDrawablesBoradUpdate(boardId);
		Gson gson = new Gson();
		return Response.ok(gson.toJson(boardUpdate)).build();
	}

	@GET
	@Path("/board_update")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class, ReadBoardLock.class })
	public Response getBoardUpdate(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@QueryParam("from") long from) throws SharedPaintException {
		BoardUpdate boardUpdate = boardsHandler.getBoradUpdate(boardId,
				new Date(from));
		Gson gson = new Gson();
		return Response.ok(gson.toJson(boardUpdate)).build();
	}

	@POST
	@Path("/add_drawable_to_board")
	@Produces(MediaType.APPLICATION_FORM_URLENCODED)
	@Interceptors({ Login.class, UserInBoard.class, WriteBoardLock.class })
	public Response addDrawableToBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@FormParam("drawable") String drawable) throws SharedPaintException {
		String email = getEmailFromHeader(request);
		Gson gson = new Gson();

		DrawableHolder drawableHolder = gson.fromJson(drawable,
				DrawableHolder.class);
		boardsHandler.addDrawableToBoard(drawableHolder.getId(),
				drawableHolder.getDrawable(), boardId, email);
		return Response.ok().build();
	}

	@GET
	@Path("/board_undo")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class, WriteBoardLock.class })
	public Response boardUndo(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId)
			throws SharedPaintException {
		boardsHandler.undoInBoard(boardId);
		return Response.ok("Success").build();
	}

	@GET
	@Path("/board_redo")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class, WriteBoardLock.class })
	public Response boardRedo(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId)
			throws SharedPaintException {
		boardsHandler.redoInBoard(boardId);
		return Response.ok("Success").build();
	}

	@GET
	@Path("/remove_board_member")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class, WriteBoardLock.class })
	public Response removeBoardMember(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@QueryParam("user_email") String userEmail)
			throws SharedPaintException {
		boardsHandler.removeUserFromBoard(userEmail, boardId);
		return Response.ok("Success").build();
	}

	@GET
	@Path("/delete_board")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, AdminInBoard.class, WriteBoardLock.class })
	public Response deleteBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId)
			throws SharedPaintException {
		boardsHandler.deleteBoard(boardId);
		return Response.ok("Success").build();
	}

	@GET
	@Path("/leave_board")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class, WriteBoardLock.class })
	public Response leaveBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId)
			throws SharedPaintException {
		String email = getEmailFromHeader(request);
		boardsHandler.removeUserFromBoard(email, boardId);
		return Response.ok("Success").build();
	}

	@GET
	@Path("/add_board_member")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, AdminInBoard.class, WriteBoardLock.class })
	public Response addMemberToBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId,
			@QueryParam("user_email") String userEmail)
			throws SharedPaintException {
		boardsHandler.addUserToBoard(userEmail, boardId);
		return Response.ok("Success").build();

	}

	@GET
	@Path("/board_members")
	@Produces(MediaType.APPLICATION_JSON)
	@Interceptors({ Login.class, UserInBoard.class, ReadBoardLock.class })
	public Response getUsersEmailInBoard(@Context HttpServletRequest request,
			@QueryParam("board_id") @BoardId long boardId)
			throws SharedPaintException {
		List<String> usersInBoard = boardsHandler.getUsersEmailInBoard(boardId);
		Gson gson = new Gson();
		return Response.ok(gson.toJson(usersInBoard)).build();
	}

	private String getEmailFromHeader(HttpServletRequest request) {
		String authorization = request.getHeader(Login.AUTHORIZATION);
		StringTokenizer st = new StringTokenizer(new String(
				Base64.decode(authorization)), ":");
		return st.nextToken();
	}

}
