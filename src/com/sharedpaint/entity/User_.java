package com.sharedpaint.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-02-28T22:45:09.593+0200")
@StaticMetamodel(User.class)
public class User_ extends AbstractSharedPaintEntity_ {
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SetAttribute<User, Board> boards;
}
