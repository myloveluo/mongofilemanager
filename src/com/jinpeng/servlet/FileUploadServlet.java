package com.jinpeng.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jinpeng.service.IFileUploadService;
import com.jinpeng.service.IListFilesService;
import com.jinpeng.service.impl.FileUploadServiceImpl;
import com.jinpeng.utils.MongoUtil;

@MultipartConfig
public class FileUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5919435374158062525L;
	
	private IFileUploadService fileUploadService;

	@Override
	public void init() throws ServletException {
		
		super.init();
		//注入属性
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		fileUploadService = (IFileUploadService) context.getBean("fileUploadService");
		
	}
	
	
	/**
	 * 获取上传的文件名称
	 * @param part
	 * @return
	 */
	private String getFilename(Part part) {
		String contentDispositionHeader = part.getHeader("content-disposition");
		String[] elements = contentDispositionHeader.split(";");
		for (String element : elements){
			if (element.trim().startsWith("filename")) {
				return element.substring(element.indexOf("=")+1).trim().replace("\"", "");
			}
		}
		return null;
	}

	/**
	 * 单个文件上传
	 * @param request
	 * @param response
	 * @param tempDir
	 */
	private void singleUpload(HttpServletRequest request, HttpServletResponse response ,String tempDir) {
		
		
		
		try {
			Part part = request.getPart("filename");
			String filename = getFilename(part);
			if (filename != null && !filename.isEmpty()) {
				InputStream inputStream = part.getInputStream();
				fileUploadService.singleUpload(inputStream,filename,tempDir);
			}
		} catch (Exception e) {
			//TODO 提示上传失败
			System.out.println("单个文件上传失败");
			e.printStackTrace();
		} 
	}
	
	/**
	 * 批量文件上传
	 * @param request
	 * @param response
	 * @param tempDir
	 */
	private void multipleUploads(HttpServletRequest request, HttpServletResponse response ,String tempDir) {
		
		
		Map<String, InputStream> uploadsMap = new LinkedHashMap<String, InputStream>();
		try {
			Collection<Part> parts = request.getParts();
			for (Part part : parts) {
				//
				if (part.getContentType() != null) {
					//long size = part.getSize();
					String filename = getFilename(part);
					
					if (filename != null && !filename.isEmpty()) {
						InputStream inputStream = part.getInputStream();
						uploadsMap.put(filename, inputStream);
						//采用循环单个文件上传的方法实现
						//fileUploadService.fileUpload(inputStream,filename);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("批量文件上传失败");
			e.printStackTrace();
		} 
		fileUploadService.multipleUploads(uploadsMap,tempDir);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		
		//临时文件存放目录
		String tempDir = request.getSession().getServletContext().getRealPath("/tempDir");
		System.out.println(tempDir);
		
		String uploadType = request.getParameter("uploadType");//上传类型：singleUpload，multipleUploads
		//判断为单个文件上传还是批量文件上传
		if (uploadType != null && !"".equals(uploadType)) {
			if (uploadType.equals("singleUpload"))
				singleUpload(request, response, tempDir);	
			if (uploadType.equals("multipleUploads"))
				multipleUploads(request, response, tempDir);
		}
		
		response.getWriter().write("上传成功！");	
		
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
}
