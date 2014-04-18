package com.sharedpaint.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-13T11:44:50.378+0300")
@StaticMetamodel(RemovedDrawable.class)
public class RemovedDrawable_ extends AbstractSharedPaintEntity_ {
	public static volatile SingularAttribute<RemovedDrawable, Long> id;
	public static volatile SingularAttribute<RemovedDrawable, Board> board;
	public static volatile SingularAttribute<RemovedDrawable, Date> removeDate;
}
