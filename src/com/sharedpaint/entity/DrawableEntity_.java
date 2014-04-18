package com.sharedpaint.entity;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-13T11:44:50.359+0300")
@StaticMetamodel(DrawableEntity.class)
public class DrawableEntity_ extends AbstractSharedPaintEntity_ {
	public static volatile SingularAttribute<DrawableEntity, Board> board;
	public static volatile SingularAttribute<DrawableEntity, Long> id;
	public static volatile SingularAttribute<DrawableEntity, Date> dateTime;
	public static volatile SingularAttribute<DrawableEntity, Serializable> object;
}
