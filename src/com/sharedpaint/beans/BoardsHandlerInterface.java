package com.sharedpaint.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.sharedpaint.entity.User;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.BoardUpdate;
import com.sharedpaint.transfer.DrawableHolder;

@Remote
public interface BoardsHandlerInterface {
	
	/**
	 * create new user
	 * @param email
	 * @param password
	 * @throws SharedPaintException
	 */
	public void createUser(String email, String password) throws SharedPaintException;
	
	/**
	 * Check if the password is user password. If user not exist return also false.
	 * @param email
	 * @param password
	 * @return is password correct
	 * @throws SharedPaintException 
	 */
	public User login(String email, String password) throws SharedPaintException;

	/**
	 * Create new board
	 * 
	 * @param name
	 *            - The name of the board
	 * @param managerEmail
	 *            - The board manager email
	 * @return new board id
	 * @throws SharedPaintException
	 *             - if manager not exist
	 */
	public BoardDetails createNewBoard(String name, String managerEmail)
			throws SharedPaintException;

	/**
	 * Delete a board according to it id
	 * 
	 * @param boardId
	 * @return
	 * @throws SharedPaintException
	 *             - then board not exist
	 */
	public void deleteBoard(long boardId) throws SharedPaintException;

	/**
	 * Set the user as member in the group
	 * 
	 * @param userEmail
	 * @param boardId
	 * @throws SharedPaintException
	 */
	public void addUserToBoard(String userEmail, long boardId)
			throws SharedPaintException;

	/**
	 * Remove user from board
	 * 
	 * @param userEmail
	 * @param boardId
	 * @throws SharedPaintException
	 */
	public void removeUserFromBoard(String userEmail, long boardId)
			throws SharedPaintException;

	/**
	 * Add new drawable to board according to it id.
	 * 
	 * @param drawableHolder
	 *            - The new drawable
	 * @param boardId
	 * @param userEmail 
	 * @throws SharedPaintException
	 */
	public void addDrawableToBoard(long drawableId, Serializable drawable,
			long boardId, String userEmail) throws SharedPaintException;

	/**
	 * return all the drawables that related to the board
	 * 
	 * @param boardId
	 * @return list of all the drawables
	 * @throws SharedPaintException
	 */
	public List<DrawableHolder> getDrawablesInBoard(long boardId)
			throws SharedPaintException;

	/**
	 * return all the drawables that related to the board as board update
	 * 
	 * @param boardId
	 * @param userEmail
	 * @return all the drawables that related to the board as board update
	 * @throws SharedPaintException
	 */
	BoardUpdate getAllDrawablesBoradUpdate(long boardId)
			throws SharedPaintException;

	
	/**
	 * return drawables that related to the board that added in time range and
	 * drawables id that removed form the board in time range
	 * 
	 * @param boardId
	 * @param from
	 * @param to
	 * @return
	 * @throws SharedPaintException
	 */
	public BoardUpdate getBoradUpdate(long boardId, Date from)
			throws SharedPaintException;

	/**
	 * Return all the boards that the user participate in.
	 * 
	 * @param userEmail
	 * @return
	 * @throws SharedPaintException
	 */
	public List<BoardDetails> getAllBoardsForUser(String userEmail)
			throws SharedPaintException;

	/**
	 * Return all the users that participate in board
	 * 
	 * @param boardId
	 * @return
	 * @throws SharedPaintException
	 */
	public List<String> getUsersEmailInBoard(long boardId)
			throws SharedPaintException;

	/**
	 * Remove the last drawable that added to board
	 * 
	 * @param boardId
	 * @return
	 * @throws SharedPaintException 
	 */
	public void undoInBoard(long boardId) throws SharedPaintException;

	/**
	 * Retrieve the last drawable that undo in board
	 * 
	 * @param boardId
	 * @return
	 * @throws SharedPaintException 
	 */
	public void redoInBoard(long boardId) throws SharedPaintException;

	/**
	 * This method return new drawables id's, so that it cannot repeat. The
	 * client will call this method and he can be sure that this id's are unique
	 * 
	 * @param count
	 *            - how many id's
	 * @return
	 */
	public List<Long> getSafeFreeNewDrawbleIds(int count);

	/**
	 * Check if user is a member in board
	 * @param userEmail
	 * @param boardId
	 * @return Is user a member in board
	 * @throws SharedPaintException 
	 */
	public boolean isUserMemberInBoard(String userEmail, long boardId) throws SharedPaintException;

	/**
	 * Check if user is the manager of board
	 * @param userEmail
	 * @param boardId
	 * @return Is user  manager of board
	 * @throws SharedPaintException 
	 */
	public boolean isUserManagerInBoard(String userEmail, long boardId)
			throws SharedPaintException;



}