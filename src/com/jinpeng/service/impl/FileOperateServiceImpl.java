package com.jinpeng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinpeng.dao.IFileOperateDao;
import com.jinpeng.service.IFileOperateService;

@Service("fileOperateService")
public class FileOperateServiceImpl implements IFileOperateService {
	
	@Autowired
	private IFileOperateDao fileOperateDao;

	/**
	 * 删除单个文件
	 */
	@Override
	public void deleteFile(String fileId) {
		fileOperateDao.deleteFile(fileId);
		
	}

	/**
	 * 删除多个文件
	 */
	@Override
	public void deleteFiles(String[] fileIds) {
		//循环删除每个文件
		for (String fileId : fileIds) {
			deleteFile(fileId);
		}
	}

	/**
	 * 文件重命名
	 */
	@Override
	public void rename(String fileId, String newFilename) {
		// 
		fileOperateDao.rename(fileId, newFilename);
	}

}
