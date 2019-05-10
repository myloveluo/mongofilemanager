package com.jinpeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jinpeng.service.IFileOperateService;
import com.jinpeng.service.IFileUploadService;
import com.jinpeng.service.impl.FileOperateServiceImpl;


public class FileOperateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private IFileOperateService fileOperateService;

    public FileOperateServlet() {
        super();  
    }
    
    @Override
	public void init() throws ServletException {
		
		super.init();
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		fileOperateService = (IFileOperateService) context.getBean("fileOperateService");
		
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		//单个文件的删除
		if ("singleDelete".equals(request.getParameter("operate"))) {
			String fileId = request.getParameter("fileId");
			if (fileId != null && !"".equals(fileId)) {
				fileOperateService.deleteFile(fileId);
				response.getWriter().write("删除成功！");
				return;
			}
		}
		
	//	String filename = request.getParameter("filename");
	//	response.sendRedirect("ListFilesServlet?filename="+filename);
	//	request.getRequestDispatcher("/ListFilesServlet").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		//批量删除
		if ("multipleDelete".equals(request.getParameter("operate"))) {
			String[] fileIds = request.getParameterValues("deletecheck");
			
			if (fileIds == null) {
				response.getWriter().write("未选择要删除的文件！");
				return;
			}
			fileOperateService.deleteFiles(fileIds);
			response.getWriter().write("成功删除" + fileIds.length + "个文件。");
			System.out.println("成功删除" + fileIds.length + "个文件。");
		}

	}

}
