package com.sharedpaint.crud.facades;

import javax.ejb.Stateless;

import com.sharedpaint.crud.CRUDEntityFacade;
import com.sharedpaint.entity.RedoDrawable;

@Stateless
public class RedoDrawableFacade extends CRUDEntityFacade<RedoDrawable>
		implements RedoDrawableFacadeLocal {

}
