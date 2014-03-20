package com.sharedpaint.crud;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.sharedpaint.entity.BoardDrawable;
import com.sharedpaint.entity.RedoDrawable;
import com.sharedpaint.entity.RemovedDrawable;

@Local
public interface DBQuerisLocal {

	public List<BoardDrawable> getDrawablesInBoard(long boardId, Date from,
			Date to);

	public List<RemovedDrawable> getRemovedDrawableInBoard(long boardId,
			 Date from, Date to);

	public List<String> getUsersEmailInBoard(long boardId);

	public BoardDrawable getLastDrawableInBoard(long boardId);

	public RedoDrawable getRedoDrawableInBoard(long boardId);

	long getNextBoardSequenceId();
}