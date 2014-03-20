package com.sharedpaint.crud.facades;

import javax.ejb.Local;

import com.sharedpaint.crud.SharedPaintEntityFacade;
import com.sharedpaint.entity.Board;

@Local
public interface BoardFacadeLocal extends SharedPaintEntityFacade<Board> {

}
