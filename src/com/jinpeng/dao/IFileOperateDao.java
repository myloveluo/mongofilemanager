package com.jinpeng.dao;

public interface IFileOperateDao {

	/**
	 * 删除文件
	 * @param fileId 文件ID
	 */
	public void deleteFile(String fileId);
	
	/**
	 * 根据文件ID获取缩略图ID
	 * @param fileId 文件ID
	 * @return 缩略图ID
	 */
	public String getThumbnailId(String fileId);

	/**
	 * 文件重命名
	 * @param fileId 文件ID
	 * @param newFilename 新文件名
	 */
	public void rename(String fileId, String newFilename);

}
