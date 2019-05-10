package com.jinpeng.dao;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;

public interface IFileUploadDao {
	
	/**
	 * 准备文件上传
	 * @param filename 文件名
	 * @return 文件上传流
	 */
	public GridFSUploadStream prepareUpload(String filename);
	
	/**
	 * 获取文件下载流
	 * @param fileId 文件ID
	 * @return 文件下载流
	 */
	public GridFSDownloadStream getDownloadStream(String fileId);

	
	/**
	 * 在文件的元数据中增加对应的缩略图ID
	 * @param fileId 文件ID
	 * @param thumbnailId 缩略图ID
	 */
	public void addFileMetaData(String fileId, String thumbnailId);
	
}
