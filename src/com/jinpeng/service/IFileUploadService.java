package com.jinpeng.service;

import java.io.InputStream;
import java.util.Map;

public interface IFileUploadService {

	/**
	 * 单个文件上传
	 * @param inputStream 输入流
	 * @param filename 文件名
	 * @param tempDir 临时目录
	 */
	public void singleUpload(InputStream inputStream, String filename, String tempDir);

	/**
	 * 批量文件上传
	 * @param uploadsMap 文件名与输入流的映射
	 * @param tempDir 临时目录
	 */
	public void multipleUploads(Map<String, InputStream> uploadsMap, String tempDir);
	
	

}
