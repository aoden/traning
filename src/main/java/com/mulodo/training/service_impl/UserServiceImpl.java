package com.mulodo.training.service_impl;


import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import util.HashUtil;

import com.mulodo.training.dao.PhotoDao;
import com.mulodo.training.dao.TokenDao;
import com.mulodo.training.dao.UserDao;
import com.mulodo.training.domain.User;
import com.mulodo.training.dto.ErrorDTO;
import com.mulodo.training.dto.LoginResponseDTO;
import com.mulodo.training.service.UserService;

@Path("/login")
@Component
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private TokenDao tokenDao;

	@Autowired
	private ServletContext servletContext;
	
	@Transactional
	@GET
	@Path("/create")
	public Response created(){
		
		
		/*
		User user = new User();
		user.setEmail("hhh@gmail.com");
		user.setUserName("hhh");
		user.setPassword("81dc9bdb52d04dc20036dbd8313ed055");
		
		userDao.save(user);
		return Response.accepted().build();
		
		
		
		Photo photo = new Photo();
		photo.setComment("dsdsdsddffdgf");
		photo.setUser(userDao.get(1));
		photo.setPath("/");
		
		Photo photo2 = new Photo();
		photo2.setComment("dsdsdsddffdgf");
		photo2.setUser(userDao.get(1));
		photo2.setPath("/");
		
		photoDao.save(photo);
		photoDao.save(photo2);
		*/
		return Response.accepted().build();
	}
	
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_XML)
	public Response login(@Context HttpServletRequest request){
				
		try{
			
			String email = null;
			String password = null; 
			
			//get the request payload
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			FileItemIterator iterator = servletFileUpload.getItemIterator(request);
			
			while (iterator.hasNext()) {
				FileItemStream fileItemStream = iterator.next();
				if("email".equals(fileItemStream.getFieldName())){
					
					StringWriter stringWriter = new StringWriter();
					IOUtils.copy(fileItemStream.openStream(), stringWriter, "utf-8");
					email = stringWriter.toString();
				} else if("password".equals(fileItemStream.getFieldName())){
					
					StringWriter stringWriter = new StringWriter();
					IOUtils.copy(fileItemStream.openStream(), stringWriter, "utf-8");
					password = stringWriter.toString();
				}
			}

			Map<String,Object> params = new HashMap<String, Object>();
			params.put("email", email);
			
			//find user form DB
			User user = (User) userDao.find("from User u where u.email = :email", params, 0, 1);
			
			//hash the input with MD5 Algorithm     
	        String hashString = HashUtil.hash("MD5", password);
	        
	        //check the password
	        if(hashString.equals(user.getPassword())){
	        	
	        	//generate token
	        	String token = buildToken(String.valueOf(user.getId()),new Date().toString());
	        	LoginResponseDTO loginResponseDTO = buildDTO(200, token);
	        	
	        	//save token in database
	        	tokenDao.saveToken(token, user);	        	
	        	
	        	return Response.status(200).entity(loginResponseDTO).build();
	        }else throw new NullPointerException();//error 401,password incorrect
			
		}catch(HibernateException ex1){//database error
			
			ex1.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		}
		catch (NoSuchAlgorithmException e) {//encrypt error
			
			e.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
			
		}catch(NullPointerException ex){//not exist user or wrong password
			
			ex.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(401,null);
			
			return Response.status(401).entity(loginResponseDTO).build();
		} catch (InvalidKeyException e) {//encrypt error
			
			e.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		} catch (FileUploadException e2) {
			
			e2.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		} catch (IOException e3) {
			
			e3.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		} 
		
	}
	
	private String buildToken(String id, String date) throws NoSuchAlgorithmException, InvalidKeyException {
		
		SecretKey secretKey = null;

	    byte[] keyBytes = date.getBytes();
	    secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	    Mac mac = null;
	    mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
	   

	    byte[] text = id.getBytes();

	    return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
		
	}

	private LoginResponseDTO buildDTO(Integer b, String token) {
		
		ErrorDTO error=null;
		switch (b) {
		case 200:
			
			error = new ErrorDTO(b, "OK");
			break;
		case 401:
			
			error = new ErrorDTO(b, "User name or password incorrect!");break;
		case 500:
			
			error = new ErrorDTO(b, "Internal Error");
			
		default:
			break;
		}
		return new LoginResponseDTO(error,token);
	}

	
	
}
