package com.sharedpaint.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: RemovedDrawable
 * 
 */
@Entity
@IdClass(RemovedDrawablePK.class)
@Table(name = "board_removed_drawables")
public class RemovedDrawable extends AbstractSharedPaintEntity implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	
	@Id
	@ManyToOne
	private Board board;

	@Temporal(TemporalType.TIMESTAMP)
	private Date removeDate;

	public long getId() {
		return id;
	}

	public RemovedDrawable() {
		super();
	}

	public RemovedDrawable(long id, Board board) {
		super();
		this.id = id;
		this.board = board;
		removeDate = new Date(System.currentTimeMillis());
	}

	public RemovedDrawable(BoardDrawable boardDrawable) {
		this(boardDrawable.getId(), boardDrawable.getBoard());
	}

	public Board getBoard() {
		return board;
	}

	public Date getRemoveDate() {
		return removeDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setRemoveDate(Date removeDate) {
		this.removeDate = removeDate;
	}
	
}
