package com.sharedpaint.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: BoardDrawble
 *
 */
@Entity
@Table(name = "board_drawables")
public class BoardDrawable extends DrawableEntity {
	
	private static final long serialVersionUID = 1L;

	public BoardDrawable() {
		super();
	}

	public BoardDrawable(long id, Serializable object, Board board) {
		super(id, object, board);
	}

	public BoardDrawable(RedoDrawable redoDrawable) {
		this(redoDrawable.getId(), redoDrawable.getObject(), redoDrawable.getBoard());
	}   
	
}
