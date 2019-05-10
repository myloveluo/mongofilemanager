package com.jinpeng.dao;

import java.util.List;

import com.jinpeng.entity.FileInfo;

public interface IListFilesDao {

	/**
	 * 根据文件名列出文件
	 * @param filename 文件名
	 * @return 文件ID和缩略图ID的map，文件名不存在返回null
	 */
	public List<FileInfo> listFiles(String filename);
	
	/**
	 * 列出模糊匹配
	 * @param filename 文件名
	 * @return 文件ID和缩略图ID的map，文件名不匹配返回null
	 */
	public List<FileInfo> listMatch(String filename);
	
	/**
	 * 列出所有文件
	 * @return
	 */
	public List<FileInfo> listAll();
	
	/**
	 * 根据文件后缀名列出文件
	 * @param suffixs 文件后缀名列表
	 * @return
	 */
	public List<FileInfo> listBySuffix(List<String> suffixList);
}
