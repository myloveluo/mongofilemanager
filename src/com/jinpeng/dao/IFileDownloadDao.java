package com.jinpeng.dao;

import java.io.InputStream;

public interface IFileDownloadDao {

	/**
	 * 文件下载
	 * @param fileId 文件ID
	 * @return
	 */
	public InputStream fileDownload(String fileId);
}
