package com.sharedpaint.crud.facades;

import javax.ejb.Stateless;

import com.sharedpaint.crud.CRUDEntityFacade;
import com.sharedpaint.entity.BoardDrawable;

@Stateless
public class BoardDrawableFacade extends CRUDEntityFacade<BoardDrawable> implements BoardDrawableFacadeLocal{

}
