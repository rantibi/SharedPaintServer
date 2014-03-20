package com.sharedpaint.crud.facades;

import javax.ejb.Local;

import com.sharedpaint.crud.SharedPaintEntityFacade;
import com.sharedpaint.entity.BoardDrawable;

@Local
public interface BoardDrawableFacadeLocal extends SharedPaintEntityFacade<BoardDrawable>{

}
