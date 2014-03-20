package com.sharedpaint.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name="users")
public class User extends AbstractSharedPaintEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	             message="{Invalid email}")
	private String email;
	
	@NotNull
	@Size(min=5, message="{Password must be at least 5 chars")
	private String password;

	@JoinTable(name = "board_members", joinColumns = { @JoinColumn(name = "user_email", referencedColumnName = "email") }, inverseJoinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "id")})
	@ManyToMany(mappedBy="members")
	Set<Board> boards;

	
	public User() {
		super();
	}

	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}



	public User(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
   
	public Set<Board> getBoards() {
		return boards;
	}

	public void setBoards(Set<Board> boards) {
		this.boards = boards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	
}
