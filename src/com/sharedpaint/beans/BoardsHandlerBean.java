package com.sharedpaint.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.sharedpaint.crud.DBQuerisLocal;
import com.sharedpaint.crud.facades.BoardDrawableFacadeLocal;
import com.sharedpaint.crud.facades.BoardFacadeLocal;
import com.sharedpaint.crud.facades.RedoDrawableFacadeLocal;
import com.sharedpaint.crud.facades.RemovedDrawableFacadeLocal;
import com.sharedpaint.crud.facades.UserFacadeLocal;
import com.sharedpaint.entity.Board;
import com.sharedpaint.entity.BoardDrawable;
import com.sharedpaint.entity.RedoDrawable;
import com.sharedpaint.entity.RemovedDrawable;
import com.sharedpaint.entity.RemovedDrawablePK;
import com.sharedpaint.entity.User;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.transfer.DrawableHolder;
import com.sharedpaint.utils.EntitiesConvertorUtility;
import com.sharedpaint.utils.LocksHandler;

/**
 * Session Bean implementation class BoradsHandlerBean
 */
@Stateless
@Remote(BoardsHandlerInterface.class)
public class BoardsHandlerBean implements BoardsHandlerInterface {
 
	@EJB
	DBQuerisLocal dbQuerisLocal;

	@EJB
	private BoardFacadeLocal boardFacade;

	@EJB
	private UserFacadeLocal userFacade;

	@EJB
	private BoardDrawableFacadeLocal boardDrawableFacade;

	@EJB
	private RedoDrawableFacadeLocal redoDrawableFacade;

	@EJB
	private RemovedDrawableFacadeLocal removedDrawableFacade;

	LocksHandler<Long> locks;
	Lock usersLock;

	public BoardsHandlerBean() {
		locks = new LocksHandler<>();
		usersLock = new ReentrantLock();
	}

	@Override
	public void createUser(String email, String password)
			throws SharedPaintException {
		try {
			usersLock.lock();
			User user = userFacade.read(email);

			if (user != null) {
				throw new SharedPaintException("User " + email
						+ " already not exist");
			}

			user = new User(email, password);
			userFacade.create(user);
		} finally {
			usersLock.unlock();
		}

	}

	@Override
	public User login(String email, String password) throws SharedPaintException {
		//TODO: add lock here
		User user = userFacade.read(email);

		if ((user == null) || (!password.equals(user.getPassword()))) {
			throw new SharedPaintException("email or password are incorrect");
		}

		return user;
	}

	@Override
	public BoardDetails createNewBoard(String name, String managerEmail)
			throws SharedPaintException {
		Board board = new Board(name);
		User manager = readUser(managerEmail);

		board.setManager(manager);
		board.setLastUpdate(new Date(System.currentTimeMillis()));
		manager.getBoards().add(board);
		boardFacade.create(board);
		userFacade.update(manager);
		return EntitiesConvertorUtility.boardToBoardDetails(board);
	}

	@Override
	public void deleteBoard(long boardId) throws SharedPaintException {
		locks.getLock(boardId).writeLock().lock();
		try {
			Board board = readBoard(boardId);
			boardFacade.delete(board);
		} finally {
			locks.getLock(boardId).writeLock().unlock();
		}
	}

	@Override
	public void addUserToBoard(String userEmail, long boardId)
			throws SharedPaintException {
		locks.getLock(boardId).writeLock().lock();
		try {

			User user = readUser(userEmail);
			Board board = readBoard(boardId);

			if (board.getMembers().contains(user)) {
				throw new SharedPaintException("user " + user.getEmail()
						+ " already a member in board " + boardId);
			}

			board.getMembers().add(user);
			user.getBoards().add(board);
			boardFacade.update(board);
			userFacade.update(user);
		} finally {
			locks.getLock(boardId).writeLock().unlock();
		}
	}

	@Override
	public void removeUserFromBoard(String userEmail, long boardId)
			throws SharedPaintException {
		locks.getLock(boardId).writeLock().lock();
		try {
			User user = readUser(userEmail);
			Board board = readBoard(boardId);

			if (board.getManager().equals(user)) {
				throw new SharedPaintException("User " + user.getEmail()
						+ " is the manager of board " + boardId
						+ " so it can't removed");
			}

			if (!board.getMembers().remove(user)) {
				throw new SharedPaintException("user " + user.getEmail()
						+ " is not a member in board " + boardId);
			}

			boardFacade.update(board);
		} finally {
			locks.getLock(boardId).writeLock().unlock();
		}
	}

	@Override
	public List<BoardDetails> getAllBoardsForUser(String userEmail)
			throws SharedPaintException {
		User user = readUser(userEmail);
		List<BoardDetails> boards = new ArrayList<>();
		
		for (Board board : user.getBoards()) {
			boards.add(EntitiesConvertorUtility.boardToBoardDetails(board));
		}
		
		return boards;
	}
	
	@Override
	public List<String> getUsersEmailInBoard(long boardId)
			throws SharedPaintException {
		locks.getLock(boardId).readLock().lock();
		try {
			readBoard(boardId);
			return dbQuerisLocal.getUsersEmailInBoard(boardId);
		} finally {
			locks.getLock(boardId).readLock().unlock();
		}
	}
	
	@Override
	public void addDrawableToBoard(long drawableId, Serializable drawable,
			long boardId, String userEmail) throws SharedPaintException {
		locks.getLock(boardId).writeLock().lock();
		try {
			Board board = readBoard(boardId);
			User user = readUser(userEmail);
			
			if(!board.getMembers().contains(user)){
				throw new SharedPaintException("Users is not member in board");
			}
			
			BoardDrawable boardDrawble = new BoardDrawable(drawableId,
					drawable, board);
			board.getDrawables().add(boardDrawble);
			board.getRedoDrawables().clear();
			board.setLastUpdate(new Date(System.currentTimeMillis()));

			boardDrawableFacade.create(boardDrawble);
			boardFacade.update(board);
		} finally {
			locks.getLock(boardId).writeLock().unlock();
		}
	}

	@Override
	public List<DrawableHolder> getDrawablesInBoard(long boardId)
			throws SharedPaintException {
		locks.getLock(boardId).readLock().lock();
		try {
			Board board = readBoard(boardId);
			List<DrawableHolder> allDrawables = new ArrayList<>();

			for (BoardDrawable boardDrawable : board.getDrawables()) {
				allDrawables.add(EntitiesConvertorUtility
						.boardDrawableToDrawableHolder(boardDrawable));
			}

			return allDrawables;
		} finally {
			locks.getLock(boardId).readLock().unlock();
		}

	}

	@Override
	public BoardUpdate getAllDrawablesBoradUpdate(long boardId)
			throws SharedPaintException {
		locks.getLock(boardId).readLock().lock();
		Date to = new Date(System.currentTimeMillis());
		try {
			readBoard(boardId);
			BoardUpdate boardUpdate = new BoardUpdate();
			boardUpdate.setFrom(new Date(0));
			boardUpdate.setTo(to);
			boardUpdate.setNewDrawables(getDrawablesInBoardForUser(boardId,  new Date(0), to));
			boardUpdate.setRemovedDrawables(new HashMap<Long,Date>());
			return boardUpdate;
		} catch(RuntimeException t){
			t.printStackTrace();return null;
		}finally {
			locks.getLock(boardId).readLock().unlock();
		}
	}
	
	@Override
	public BoardUpdate getBoradUpdate(long boardId, Date from)
			throws SharedPaintException {
		locks.getLock(boardId).readLock().lock();
		Date to = new Date(System.currentTimeMillis());
		try {
			readBoard(boardId);
			BoardUpdate boardUpdate = new BoardUpdate();
			boardUpdate.setFrom(from);
			boardUpdate.setTo(to);
			boardUpdate.setNewDrawables(getDrawablesInBoardForUser(boardId,from, to));
			boardUpdate.setRemovedDrawables(getRemovedDrawablesInBoard(boardId,
					from, to));
			return boardUpdate;
		} catch(RuntimeException t){
			t.printStackTrace();return null;
		}finally {
			locks.getLock(boardId).readLock().unlock();
		}
	}

	private List<DrawableHolder> getDrawablesInBoardForUser(long boardId, Date from,
			Date to) throws SharedPaintException {
		return EntitiesConvertorUtility
				.boardDrawableListToDrawableHolderList(dbQuerisLocal
						.getDrawablesInBoard(boardId, from, to));
	}

	private Map<Long, Date> getRemovedDrawablesInBoard(long boardId, Date from,
			Date to) throws SharedPaintException {
		List<RemovedDrawable> removed = dbQuerisLocal
				.getRemovedDrawableInBoard(boardId, from, to);
		Map<Long, Date> result = new HashMap<>();

		for (RemovedDrawable removedDrawable : removed) {
			result.put(removedDrawable.getId(), removedDrawable.getRemoveDate());
		}

		return result;
	}


	@Override
	public boolean undoInBoard(long boardId) {
		locks.getLock(boardId).writeLock().lock();
		try {
			BoardDrawable lastDrawable = dbQuerisLocal
					.getLastDrawableInBoard(boardId);

			if (lastDrawable == null) {
				return false;
			}

			Board board = lastDrawable.getBoard();
			RedoDrawable redoDrawable = new RedoDrawable(lastDrawable);
			board.getRedoDrawables().add(redoDrawable);
			Date lastUpdate = new Date(System.currentTimeMillis());
			board.setLastUpdate(lastUpdate);

			RemovedDrawable removedDrawable = removedDrawableFacade
					.read(new RemovedDrawablePK(lastDrawable.getId(), boardId));

			// If this drawable deleted before just update the date
			if (removedDrawable == null) {
				removedDrawable = new RemovedDrawable(lastDrawable);
				removedDrawable.setRemoveDate(lastUpdate);
				board.getRemovedDrawables().add(removedDrawable);
				removedDrawableFacade.create(removedDrawable);
			} else {
				removedDrawable.setRemoveDate(lastUpdate);
				removedDrawableFacade.update(removedDrawable);
			}

			redoDrawableFacade.create(redoDrawable);
			boardDrawableFacade.delete(lastDrawable);
			boardFacade.update(board);
			return true;
		} finally {
			locks.getLock(boardId).writeLock().unlock();
		}
	}

	@Override
	public boolean redoInBoard(long boardId) {
		locks.getLock(boardId).writeLock().lock();
		try {
			RedoDrawable redoDrawable = dbQuerisLocal
					.getRedoDrawableInBoard(boardId);

			if (redoDrawable == null) {
				return false;
			}

			Board board = redoDrawable.getBoard();
			board.getRedoDrawables().remove(redoDrawable);
			BoardDrawable boardDrawable = new BoardDrawable(redoDrawable);
			board.getDrawables().add(boardDrawable);
			board.setLastUpdate(new Date(System.currentTimeMillis()));

			boardDrawableFacade.create(boardDrawable);
			redoDrawableFacade.delete(redoDrawable);
			boardFacade.update(board);
			return true;
		} finally {
			locks.getLock(boardId).writeLock().unlock();
		}
	}

	@Override
	public List<Long> getSafeFreeNewDrawbleIds(int count) {
		List<Long> list = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			list.add(dbQuerisLocal.getNextBoardSequenceId());
		}

		return list;
	}
	
	@Override
	public boolean isUserMemberInBoard(String userEmail, long boardId) throws SharedPaintException{
		Board board = readBoard(boardId);
		User user = readUser(userEmail);
		return board.getMembers().contains(user);
	}

	@Override
	public boolean isUserManagerInBoard(String userEmail, long boardId) throws SharedPaintException{
		Board board = readBoard(boardId);
		User user = readUser(userEmail);
		return board.getManager().equals(user);
	}
	
	private Board readBoard(long boardId) throws SharedPaintException {
		Board board = boardFacade.read(boardId);

		if (board == null) {
			throw new SharedPaintException("Board " + boardId
					+ " does not exist");
		}
		return board;
	}

	private User readUser(String userEmail) throws SharedPaintException {
		User user = userFacade.read(userEmail);

		if (user == null) {
			throw new SharedPaintException("User " + userEmail
					+ " does not exist");
		}
		return user;
	}

}
