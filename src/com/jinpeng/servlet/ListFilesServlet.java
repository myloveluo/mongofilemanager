package com.jinpeng.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jinpeng.entity.FileInfo;
import com.jinpeng.service.IListFilesService;
import com.jinpeng.service.impl.ListFilesServiceImpl;

/**
 * Servlet implementation class ListFilesServlet
 */

public class ListFilesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private IListFilesService listFilesService;
	
	@Override
	public void init() throws ServletException {
		
		super.init();
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		listFilesService = (IListFilesService) context.getBean("listFilesService");
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//System.out.println(listFilesService);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		
		String listType = request.getParameter("listType");//查询类型
		String name = request.getParameter("filename");
		
		String filename = null;
		if (name != null) {
			filename = new String(name.getBytes("iso8859-1"),"UTF-8" );//解决中文编码问题
		}
		
		//精确查询
		if (listType.equals("accurate")) {
			if ("".equals(name) || name==null){
				response.getWriter().write("输入不能为空!");
				return;
			}
			List<FileInfo> fileInfoList = listFilesService.listFiles(filename);
			if (fileInfoList == null) {
				response.getWriter().write("查询的文件不存在!");
				//System.out.println("查询的文件不存在!");
				return;
			}
		//	request.setAttribute("filename", filename);
			request.setAttribute("fileInfoList", fileInfoList);
			RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/matchResult.jsp");
			rd.forward(request, response);
			
		}
		
		//模糊查询
		if (listType.equals("match")) {
			if ("".equals(name) || name==null){
				response.getWriter().write("输入不能为空!");
				return;
			}
			List<FileInfo> fileInfoList = listFilesService.listMatch(filename);
			//查询结果为空，提示用户
			if (fileInfoList == null) {
				response.getWriter().write("查询的文件不存在!");
				//System.out.println("查询的文件不存在");
				return;
			}
			
			request.setAttribute("fileInfoList", fileInfoList);
			RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/matchResult.jsp");
			rd.forward(request, response);
		}
		
		//查询所有文件
		if (listType.equals("all")) {
			List<FileInfo> fileInfoList = listFilesService.listAll();
			//查询结果为空，提示用户
			if (fileInfoList == null) {
				response.getWriter().write("查询的文件不存在!");
				//System.out.println("查询的文件不存在");
			}
			
			request.setAttribute("fileInfoList", fileInfoList);
			RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/matchResult.jsp");
			rd.forward(request, response);
		}
		
		//查询所有图片文件
		if (listType.equals("image")) {
			List<FileInfo> fileInfoList = listFilesService.listImage();
			//查询结果为空，提示用户
			if (fileInfoList == null) {
				response.getWriter().write("查询的文件不存在!");
				//System.out.println("查询的文件不存在");
			}
			
			request.setAttribute("fileInfoList", fileInfoList);
			RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/matchResult.jsp");
			rd.forward(request, response);
		}
		
		//查询所有视频文件
		if (listType.equals("video")) {
			List<FileInfo> fileInfoList = listFilesService.listVideo();
			//查询结果为空，提示用户
			if (fileInfoList == null) {
				response.getWriter().write("查询的文件不存在!");
				//System.out.println("查询的文件不存在");
			}
			
			request.setAttribute("fileInfoList", fileInfoList);
			RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/matchResult.jsp");
			rd.forward(request, response);
		}
		
		//查询所有文档文件
		if (listType.equals("doc")) {
			List<FileInfo> fileInfoList = listFilesService.listDocument();
			//查询结果为空，提示用户
			if (fileInfoList == null) {
				response.getWriter().write("查询的文件不存在!");
				//System.out.println("查询的文件不存在");
			}
			
			request.setAttribute("fileInfoList", fileInfoList);
			RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/matchResult.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
