package com.jinpeng.service;

import java.util.List;

import com.jinpeng.entity.FileInfo;

public interface IListFilesService {
	
	/**
	 * 精确查询
	 * @param filename 文件名
	 * @return 文件信息对象的list，文件不存在返回null
	 */
	public List<FileInfo> listFiles(String filename);
	
	/**
	 * 模糊查询
	 * @param filename 文件名
	 * @return 文件信息对象的list，文件不存在返回null
	 */
	public List<FileInfo> listMatch(String filename);
	
	/**
	 * 列出所有文件
	 * @return 文件信息对象的list，文件不存在返回null
	 */
	public List<FileInfo> listAll();
	
	/**
	 * 列出所有图片文件
	 * @return 文件信息对象的list，文件不存在返回null
	 */
	public List<FileInfo> listImage();
	
	/**
	 * 列出所有视频文件
	 * @return 文件信息对象的list，文件不存在返回null
	 */
	public List<FileInfo> listVideo();
	
	/**
	 * 列出所有文档文件
	 * @return 文件信息对象的list，文件不存在返回null
	 */
	public List<FileInfo> listDocument();
}
