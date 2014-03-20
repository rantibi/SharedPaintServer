package com.sharedpaint.crud.facades;

import javax.ejb.Stateless;

import com.sharedpaint.crud.CRUDEntityFacade;
import com.sharedpaint.entity.Board;

@Stateless
public class BoardFacade extends CRUDEntityFacade<Board> implements BoardFacadeLocal{

}
