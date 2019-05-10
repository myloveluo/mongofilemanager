package com.jinpeng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinpeng.dao.IListFilesDao;
import com.jinpeng.entity.FileInfo;
import com.jinpeng.service.IListFilesService;
import com.jinpeng.utils.MongoUtil;

@Service("listFilesService")
public class ListFilesServiceImpl implements IListFilesService {
	
	@Autowired
	private IListFilesDao listFilesDao;
	
	/**
	 * 根据文件名列出文件
	 */
	@Override
	public List<FileInfo> listFiles(String filename) {
		List<FileInfo> fileInfoList = listFilesDao.listFiles(filename);
		if (fileInfoList == null) {
			return null;
		}
		//如果文件有缩略图，在文件信息里增加缩略图URL
		for (FileInfo fileInfo : fileInfoList) {
			String thumbnailId = fileInfo.getThumbnailId();
			if (thumbnailId != null) {
				String thumbnailURL = getThumbnailURL(thumbnailId);
				fileInfo.setThumbnailURL(thumbnailURL);	
			}
		}
		return fileInfoList;
	}
	
	/**
	 * 拼接访问缩略图的URL
	 * @param fileId 缩略图ID
	 * @return 缩略图URL字符串
	 */
	private String getThumbnailURL(String thumbnailId) {
		String host = MongoUtil.getHost();
		String dbName = MongoUtil.getDBName();
		String thumbnailURL = "http://" + host + "/" + dbName + "/" + thumbnailId;
		return thumbnailURL;
	}
	
	/**
	 * 列出模糊匹配的文件
	 */
	@Override
	public List<FileInfo> listMatch(String filename) {
		// 
		List<FileInfo> fileInfoList = listFilesDao.listMatch(filename);
		if (fileInfoList == null) {
			return null;
		}
		//Map<String, String> idURLMap = new LinkedHashMap<String, String>();
//		for (Map.Entry<String, String> entry : idsMap.entrySet()) {
//			String thumbnailRUL = getThumbnailURL(entry.getValue());
//			idURLMap.put(entry.getKey(), thumbnailRUL);
//		}
		//如果文件有缩略图，在文件信息里增加缩略图URL
		for (FileInfo fileInfo : fileInfoList) {
			String thumbnailId = fileInfo.getThumbnailId();
			if (thumbnailId != null) {
				String thumbnailURL = getThumbnailURL(thumbnailId);
				fileInfo.setThumbnailURL(thumbnailURL);	
			}
		}
		return fileInfoList;
	}
	
	/**
	 * 列出所有文件
	 */
	@Override
	public List<FileInfo> listAll() {
		List<FileInfo> fileInfoList = listFilesDao.listAll();
		if (fileInfoList == null) {
			return null;
		}
		for (FileInfo fileInfo : fileInfoList) {
			String thumbnailId = fileInfo.getThumbnailId();
			if (thumbnailId != null) {
				String thumbnailURL = getThumbnailURL(thumbnailId);
				fileInfo.setThumbnailURL(thumbnailURL);	
			}
		}
		
		return fileInfoList;
	}
	
	/**
	 * 列出所有图片文件
	 */
	@Override
	public List<FileInfo> listImage() {
		List<String> suffixList = new ArrayList<String>();
		suffixList.add(".png");
		suffixList.add(".jpg");
		suffixList.add(".jpeg");
    	suffixList.add(".gif");
		suffixList.add(".bmp");
		List<FileInfo> fileInfoList = listFilesDao.listBySuffix(suffixList);
		if (fileInfoList == null) {
			return null;
		}
		for (FileInfo fileInfo : fileInfoList) {
			String thumbnailId = fileInfo.getThumbnailId();
			if (thumbnailId != null) {
				String thumbnailURL = getThumbnailURL(thumbnailId);
				fileInfo.setThumbnailURL(thumbnailURL);	
			}
		}
		return fileInfoList;
	}
	
	/**
	 * 列出所有视频文件
	 */
	@Override
	public List<FileInfo> listVideo() {
		List<String> suffixList = new ArrayList<String>();
		suffixList.add(".mp4");
		suffixList.add(".flv");
		suffixList.add(".rmvb");
    	suffixList.add(".avi");
		suffixList.add(".mkv");
		suffixList.add(".wmv");
		List<FileInfo> fileInfoList = listFilesDao.listBySuffix(suffixList);
		if (fileInfoList == null) {
			return null;
		}
		for (FileInfo fileInfo : fileInfoList) {
			String thumbnailId = fileInfo.getThumbnailId();
			if (thumbnailId != null) {
				String thumbnailURL = getThumbnailURL(thumbnailId);
				fileInfo.setThumbnailURL(thumbnailURL);	
			}
		}
		return fileInfoList;
	}
	
	/**
	 * 列出所有文档文件
	 */
	@Override
	public List<FileInfo> listDocument() {
		List<String> suffixList = new ArrayList<String>();
		suffixList.add(".doc");
		suffixList.add(".docx");
		suffixList.add(".pdf");
		suffixList.add(".txt");
		List<FileInfo> fileInfoList = listFilesDao.listBySuffix(suffixList);
		if (fileInfoList == null) {
			return null;
		}
		for (FileInfo fileInfo : fileInfoList) {
			String thumbnailId = fileInfo.getThumbnailId();
			if (thumbnailId != null) {
				String thumbnailURL = getThumbnailURL(thumbnailId);
				fileInfo.setThumbnailURL(thumbnailURL);	
			}
		}
		return fileInfoList;
	}

}
