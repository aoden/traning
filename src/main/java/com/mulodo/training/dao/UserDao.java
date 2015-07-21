package com.mulodo.training.dao;

import java.util.List;

import com.mulodo.training.domain.User;

public interface UserDao extends Dao<User, Integer>{
	
	List<User> findAllUser();

}
