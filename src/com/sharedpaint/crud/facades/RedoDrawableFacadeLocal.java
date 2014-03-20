package com.sharedpaint.crud.facades;

import javax.ejb.Local;

import com.sharedpaint.crud.SharedPaintEntityFacade;
import com.sharedpaint.entity.RedoDrawable;

@Local
public interface RedoDrawableFacadeLocal extends SharedPaintEntityFacade<RedoDrawable>{

}
