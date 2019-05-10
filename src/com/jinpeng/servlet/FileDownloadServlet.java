package com.jinpeng.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jinpeng.service.IFileDownloadService;
import com.jinpeng.service.IFileOperateService;
import com.jinpeng.service.impl.FileDownloadServiceImpl;


public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private IFileDownloadService fileDownloadService;
       
    public FileDownloadServlet() {
        super();
    }

    @Override
   	public void init() throws ServletException {
   		
   		super.init();
   		
   		ServletContext servletContext = this.getServletContext();
   		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
   		fileDownloadService = (IFileDownloadService) context.getBean("fileDownloadService");
   		
   	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("filename");
		String fileId = request.getParameter("fileId");
		response.addHeader("Content-Disposition", "attachment; filename="+filename);
		OutputStream outputStream= response.getOutputStream();
		fileDownloadService.fileDownload(fileId, outputStream);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
