package com.jinpeng.service;

import java.io.OutputStream;

public interface IFileDownloadService {

	//public int showProgress();
	
	/**
	 * 文件下载
	 * @param fileId 文件ID
	 * @param outputStream 输出流
	 */
	public void fileDownload(String fileId, OutputStream outputStream);
}
