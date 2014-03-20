package com.sharedpaint.transfer;

import java.io.Serializable;
import java.util.Date;

public class DrawableHolder implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private Date dateTime;
	private byte[] drawable;
	
	public DrawableHolder() {
	}
	
	public DrawableHolder(long id, Date dateTime, byte[] drawable) {
		super();
		this.id = id;
		this.dateTime = dateTime;
		this.drawable = drawable; 
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public byte[] getDrawable() {
		return drawable;
	}

	public void setDrawable(byte[] drawable) {
		this.drawable = drawable;
	}
	
}
