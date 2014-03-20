package com.sharedpaint.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: RedoDrawable
 *
 */
@Entity
@Table(name = "board_redo_drawables")
public class RedoDrawable extends DrawableEntity {

	private static final long serialVersionUID = 1L;
	
	public RedoDrawable(long id, Serializable object, Board board) {
		super(id, object, board);
	}


	public RedoDrawable() {
		super();
	}


	public RedoDrawable(BoardDrawable boardDrawable) {
		this(boardDrawable.getId(), boardDrawable.getObject(), boardDrawable.getBoard());
	}
   
}
