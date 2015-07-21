package com.mulodo.training.filter;

import com.mulodo.training.dao.TokenDao;
import com.mulodo.training.domain.Token;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;

public class TokenFilter implements Filter {

    private ApplicationContext ctx;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        //get the application context to access spring beans
        ctx = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = null;

        try {

            //check multipart request
            if (httpRequest.getHeader("Content-Type") != null && httpRequest.getHeader("Content-Type").contains("multipart")) {

                //get the token and verify
                ServletFileUpload servletFileUpload = new ServletFileUpload();
                FileItemIterator iterator = servletFileUpload.getItemIterator(httpRequest);

                while (iterator.hasNext()) {
                    FileItemStream fileItemStream = iterator.next();
                    if ("token".equals(fileItemStream.getFieldName())) {

                        StringWriter stringWriter = new StringWriter();
                        IOUtils.copy(fileItemStream.openStream(), stringWriter, "utf-8");
                        token = stringWriter.toString();

                    }
                }
            } else {

                token = request.getParameter("token");
            }

            TokenDao tokenDao = (TokenDao) ctx.getBean("tokenDao");
            //check token
            Token tokenObj = tokenDao.findToken(token);
            if (tokenObj != null) {

                httpRequest.setAttribute("token", tokenObj);
                chain.doFilter(request, response);

            } else {

                System.out.println("invalid");
                httpResponse.setContentType("application/xml");
                PrintWriter out = httpResponse.getWriter();
                out.print(buildResponse(httpResponse, 401, "Token invalid!"));
                response.flushBuffer();
            }


        } catch (NullPointerException ex) {


            httpResponse.setContentType("application/xml");
            PrintWriter out = httpResponse.getWriter();
            out.print(buildResponse(httpResponse, 401, "Token invalid!"));

            httpResponse.flushBuffer();
            ex.printStackTrace();

        } catch (FileUploadException e) {

            httpResponse.setContentType("application/xml");
            PrintWriter out = httpResponse.getWriter();
            out.print(buildResponse(httpResponse, 500, "Internal Error"));
            httpResponse.flushBuffer();
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {


            e.printStackTrace();
        } catch (HibernateException e) {

            httpResponse.setContentType("application/xml");
            PrintWriter out = httpResponse.getWriter();
            out.print(buildResponse(httpResponse, 500, "Internal Error"));
            httpResponse.flushBuffer();
            e.printStackTrace();
        } finally {


        }
    }

    private String buildResponse(HttpServletResponse httpResponse, int code, String messege) throws IOException {


        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>").append("<response>").append("<error><status>").
                append(code).append("</status>").append("<messege>").
                append(messege).append("</messege></error></response>");

        return sb.toString();
    }

    @Override
    public void destroy() {


    }

}
