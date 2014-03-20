package com.sharedpaint.crud.facades;

import javax.ejb.Stateless;

import com.sharedpaint.crud.CRUDEntityFacade;
import com.sharedpaint.entity.RemovedDrawable;

@Stateless
public class RemovedDrawableFacade extends CRUDEntityFacade<RemovedDrawable> implements RemovedDrawableFacadeLocal{

}
