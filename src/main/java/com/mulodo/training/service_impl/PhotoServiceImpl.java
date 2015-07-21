package com.mulodo.training.service_impl;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.training.dao.PhotoDao;
import com.mulodo.training.dao.UserDao;
import com.mulodo.training.domain.Token;
import com.mulodo.training.domain.User;
import com.mulodo.training.service.PhotoService;

@Path("/photo")
public class PhotoServiceImpl implements PhotoService  {
	
	@Autowired
	private UserDao userDao;
	private PhotoDao photoDao;

	@Override
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_XML)
	@Transactional(rollbackFor = RuntimeException.class)
	public void saveImage(HttpServletRequest request) {
		
		String comment = null;
		//Token tokenObj = (Token) request.getAttribute("token");
		byte[] image = null;
		
		try{
			
			
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);
			while(fileItemIterator.hasNext()){
				
				FileItemStream  fileItemStream = fileItemIterator.next();
				if("comment".equals(fileItemStream.getFieldName())){
					
					StringWriter stringWriter = new StringWriter();
					IOUtils.copy(fileItemStream.openStream(), stringWriter, "utf-8");
					comment = stringWriter.toString();
				}else if("content".equals(fileItemStream.getFieldName())){
					
					
				}
			}
			
			User user = userDao.load(1);
			//User user = tokenObj.getUser();
			
		}catch(RuntimeException e){
			
			
		} catch (FileUploadException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
