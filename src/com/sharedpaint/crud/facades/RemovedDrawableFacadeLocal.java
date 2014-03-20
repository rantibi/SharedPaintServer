package com.sharedpaint.crud.facades;

import javax.ejb.Local;

import com.sharedpaint.crud.SharedPaintEntityFacade;
import com.sharedpaint.entity.RemovedDrawable;

@Local
public interface RemovedDrawableFacadeLocal extends SharedPaintEntityFacade<RemovedDrawable>{

}
