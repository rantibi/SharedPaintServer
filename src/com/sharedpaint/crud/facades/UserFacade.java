package com.sharedpaint.crud.facades;

import javax.ejb.Stateless;

import com.sharedpaint.crud.CRUDEntityFacade;
import com.sharedpaint.entity.User;

@Stateless
public class UserFacade extends CRUDEntityFacade<User> implements UserFacadeLocal{

}
