package com.sharedpaint.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sun.istack.NotNull;

/**
 * Entity implementation class for Entity: Board
 * 
 */
@Entity
@Table(name = "Boards")
public class Board extends AbstractSharedPaintEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@NotNull
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	@JoinTable(name = "board_members", joinColumns = { @JoinColumn(name = "board_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "user_email", referencedColumnName = "email") })
	@ManyToMany
	Set<User> members;

	@ManyToOne
	User manager;

	@OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
	List<BoardDrawable> drawables;

	@OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
	List<RedoDrawable> redoDrawables;

	@OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
	List<RemovedDrawable> removedDrawables;

	public Board() {
		super();
		members = new HashSet<User>();
		drawables = new LinkedList<>();
		redoDrawables = new LinkedList<>();
		removedDrawables = new LinkedList<>();
	}

	public Board(String name) {
		this();
		this.name = name;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
		members.add(manager);
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

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	public List<BoardDrawable> getDrawables() {
		return drawables;
	}

	public void setDrawables(List<BoardDrawable> drawables) {
		this.drawables = drawables;
	}

	public List<RedoDrawable> getRedoDrawables() {
		return redoDrawables;
	}

	public void setRedoDrawables(List<RedoDrawable> redoDrawables) {
		this.redoDrawables = redoDrawables;
	}

	public List<RemovedDrawable> getRemovedDrawables() {
		return removedDrawables;
	}

	public void setRemovedDrawables(List<RemovedDrawable> removedDrawables) {
		this.removedDrawables = removedDrawables;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Board other = (Board) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/*
	 * public void undo() {
	 * 
	 * if (drawables.size() > 0){ Collections.sort(drawables); DrawableEntity
	 * drawable = drawables.remove(drawables.size() - 1); em.remove(drawable);
	 * removedDrawables.put(drawable.getId(), new
	 * Date(System.currentTimeMillis())); RedoDrawable redoDrawable = new
	 * RedoDrawable(drawable.getId(), drawable.getObject(), this);
	 * redoDrawables.add(redoDrawable); } }
	 * 
	 * public void redo(){ if (redoDrawables.size() > 0){
	 * Collections.sort(redoDrawables); DrawableEntity drawable =
	 * redoDrawables.remove(redoDrawables.size() - 1); em.remove(drawable);
	 * addDrawable(drawable.getId(), drawable.getObject()); } }
	 */

}
