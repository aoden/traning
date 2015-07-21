package com.mulodo.training.dao;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.training.dao.Dao;

@Transactional
public abstract class AbstractDao<T,K extends Serializable> implements Dao<T, K>{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public abstract Class<T> getEntityClass();

	
	
	@SuppressWarnings("unchecked")
	@Override
	public T get(Serializable id) {
		
		return (T) sessionFactory.getCurrentSession().get(getEntityClass(), id);
	}



	@SuppressWarnings("unchecked")
	@Override
	public T load(Serializable id) {
		
		return (T) sessionFactory.getCurrentSession().load(getEntityClass(), id);
	}


	@Override
	public void save(T entity) {
		
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
	}

	@Override
	public void delete(T entity) {
		
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}

	@Override
	public void update(T entity) {
		
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}


	@Override
	public Object find(String queryString,Map<String, Object> params, int begin, int end) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery(queryString);
		if(params != null)
			//iterate through the map and set query parameters
			for(Map.Entry<String, Object> entry: params.entrySet()){
				
				query.setParameter(entry.getKey(), entry.getValue());
			}
		try {
				
			return query.setFirstResult(begin).setMaxResults(end).uniqueResult();
		} catch (NonUniqueResultException e) {
				
			return session.createQuery(queryString).setFirstResult(begin).setMaxResults(end).list();
		}
		
	}

	
}
