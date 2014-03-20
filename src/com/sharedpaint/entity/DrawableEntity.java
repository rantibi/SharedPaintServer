package com.sharedpaint.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@IdClass(DrawableEntityPK.class)
@MappedSuperclass
public abstract class DrawableEntity extends AbstractSharedPaintEntity implements Comparable<DrawableEntity>{
	
	private static final long serialVersionUID = 1L;
	@ManyToOne(optional=false)
	@Id
	private Board board;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	@Lob
	private Serializable object;
		
	public DrawableEntity() {
	}
	
	public DrawableEntity(long id, Serializable object, Board board) {
		super();
		this.object = object;
		this.id = id;
		this.board = board;
		dateTime = new Date(System.currentTimeMillis());
	}
	
	public long getId() {
		return id;
	}
	
	
	public Serializable getObject() {
		return object;
	}
	
	public Date getDateTime() {
		return dateTime;
	}		
	
	public void setBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public int compareTo(DrawableEntity o) {
		
		int timesDifference = (int) (this.dateTime.getTime() - o.getDateTime().getTime());

		return (timesDifference != 0) ? timesDifference : (int)(this.id - o.id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((object == null) ? 0 : object.hashCode());
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
		DrawableEntity other = (DrawableEntity) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (id != other.id)
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
}
