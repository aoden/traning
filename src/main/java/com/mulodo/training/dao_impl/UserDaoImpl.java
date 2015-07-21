package com.mulodo.training.dao_impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mulodo.training.dao.AbstractDao;
import com.mulodo.training.dao.UserDao;
import com.mulodo.training.domain.User;

@Repository
@Qualifier("userDao")
public class UserDaoImpl extends AbstractDao<User, Integer> implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllUser() {
		
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(getEntityClass()).list();
	}

	@Override
	public Class<User> getEntityClass() {
		
		return User.class;
	}

}
