package com.mulodo.training.service;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;


public interface UserService {

	Response login(HttpServletRequest request);
}
