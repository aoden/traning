package com.mulodo.training.service;

import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public interface PhotoService {

	void saveImage(HttpServletRequest request);
}
