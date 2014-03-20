package com.sharedpaint.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractSharedPaintEntity implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;

}
