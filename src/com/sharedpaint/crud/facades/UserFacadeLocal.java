package com.sharedpaint.crud.facades;

import javax.ejb.Local;

import com.sharedpaint.crud.SharedPaintEntityFacade;
import com.sharedpaint.entity.User;

@Local
public interface UserFacadeLocal extends SharedPaintEntityFacade<User>{

}
