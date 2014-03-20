package com.sharedpaint.transfer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BoardUpdate implements Serializable{
	private static final long serialVersionUID = 1L;
	List<DrawableHolder> newDrawables;
	Map<Long,Date> removedDrawables;
	Date from;
	Date to;
	
	public BoardUpdate() {
	}
	
	public BoardUpdate(List<DrawableHolder> newDrawables,
			Map<Long, Date> removedDrawables, Date from, Date to) {
		super();
		this.newDrawables = newDrawables;
		this.removedDrawables = removedDrawables;
		this.from = from;
		this.to = to;
	}

	public List<DrawableHolder> getNewDrawables() {
		return newDrawables;
	}

	public void setNewDrawables(List<DrawableHolder> newDrawables) {
		this.newDrawables = newDrawables;
	}

	public Map<Long, Date> getRemovedDrawables() {
		return removedDrawables;
	}

	public void setRemovedDrawables(Map<Long, Date> removedDrawables) {
		this.removedDrawables = removedDrawables;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}
	
	
	
}
