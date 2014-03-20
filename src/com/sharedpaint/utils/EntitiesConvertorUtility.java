package com.sharedpaint.utils;

import java.util.ArrayList;
import java.util.List;

import com.sharedpaint.entity.Board;
import com.sharedpaint.entity.BoardDrawable;
import com.sharedpaint.transfer.BoardDetails;
import com.sharedpaint.transfer.DrawableHolder;

public class EntitiesConvertorUtility {

	public static BoardDetails boardToBoardDetails(Board board){
		BoardDetails details = new BoardDetails();
		details.setId(board.getId());
		details.setLastUpdate(board.getLastUpdate());
		details.setManagerEmail(board.getManager().getEmail());
		details.setName(board.getName());
		return details;
	}
	
	public static DrawableHolder boardDrawableToDrawableHolder(BoardDrawable boardDrawable){
		DrawableHolder drawableHolder = new DrawableHolder();
		drawableHolder.setId(boardDrawable.getId());
		drawableHolder.setDrawable((byte[]) boardDrawable.getObject());
		drawableHolder.setDateTime(boardDrawable.getDateTime());
		return drawableHolder;
	}
	
	public static List<DrawableHolder> boardDrawableListToDrawableHolderList(
			List<BoardDrawable> boardDrawables) {
		List<DrawableHolder> drawables = new ArrayList<>();
		
		for (BoardDrawable boardDrawable : boardDrawables) {
			drawables.add(boardDrawableToDrawableHolder(boardDrawable));
		}
		
		return drawables;
	}
}
