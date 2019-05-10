package com.jinpeng.service;

public interface IFileOperateService {

	/**
	 * 删除单个文件
	 * @param fileId 文件ID
	 */
	public void deleteFile(String fileId);
	
	/**
	 * 删除多个文件
	 * @param fileIds 文件ID列表
	 */
	public void deleteFiles(String[] fileIds);
	
	/**
	 * 文件重命名
	 * @param fileId 文件ID
	 * @param newFilename 新文件名
	 */
	public void rename(String fileId, String newFilename);
	
}
