package com.sharedpaint.entity;

import java.io.Serializable;

public class DrawableEntityPK implements Serializable{

	private static final long serialVersionUID = 1L;

	private long board;
	private long id;
	
	public DrawableEntityPK() {
	}
	
	public DrawableEntityPK(long board, long id) {
		super();
		this.board = board;
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (board ^ (board >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DrawableEntityPK other = (DrawableEntityPK) obj;
		if (board != other.board)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	

}
