package com.mulodo.training.dao;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Le Dinh Minh Khoi
 *	
 * @param <T> The entity's class
 * @param <K> The entity's Id class
 */
public interface Dao<T,K extends Serializable> {
	
	void save(T entity);
	void delete(T entity);
	void update (T entity);
	T load(Serializable id);
	T get(Serializable id);
	
	
	Object find(String queryString,Map<String,Object> params,int begin,int end);
}
