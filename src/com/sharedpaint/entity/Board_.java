package com.sharedpaint.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-13T11:44:50.321+0300")
@StaticMetamodel(Board.class)
public class Board_ extends AbstractSharedPaintEntity_ {
	public static volatile SingularAttribute<Board, Long> id;
	public static volatile SingularAttribute<Board, String> name;
	public static volatile SingularAttribute<Board, Date> lastUpdate;
	public static volatile SetAttribute<Board, User> members;
	public static volatile SingularAttribute<Board, User> manager;
	public static volatile ListAttribute<Board, BoardDrawable> drawables;
	public static volatile ListAttribute<Board, RedoDrawable> redoDrawables;
	public static volatile ListAttribute<Board, RemovedDrawable> removedDrawables;
}
