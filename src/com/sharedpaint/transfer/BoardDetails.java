package com.sharedpaint.transfer;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BoardDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private Date lastUpdate;	
	private String managerEmail;

	public BoardDetails(){
	}
	
	public BoardDetails(long id, String name, Date lastUpdate,
			String managerEmail) {
		super();
		this.id = id;
		this.name = name;
		this.lastUpdate = lastUpdate;
		this.managerEmail = managerEmail;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

}
