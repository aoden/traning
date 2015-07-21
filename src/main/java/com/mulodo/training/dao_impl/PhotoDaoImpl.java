package com.mulodo.training.dao_impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mulodo.training.dao.AbstractDao;
import com.mulodo.training.dao.PhotoDao;
import com.mulodo.training.domain.Photo;

@Repository
@Qualifier("photoDao")
public class PhotoDaoImpl extends AbstractDao<Photo, Integer> implements PhotoDao {

	@Override
	public Class<Photo> getEntityClass() {
		
		return Photo.class;
	}

}
