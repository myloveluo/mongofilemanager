package com.jinpeng.entity;

import java.util.Date;
/**
 * 文件信息
 * @author lqm
 *
 */
public class FileInfo {

	private String fileId = null;//文件ID
	private String thumbnailId = null;//文件缩略图ID
	private String filename = null;//文件名
	private long size = 0;//文件大小
	private Date uploadDate = null;//文件上传时间
	private String thumbnailURL = null;//缩略图URL
	
	public FileInfo() {
		
	}
	
	public FileInfo(String fileId,String thumbnailId,String filename) {
		this.fileId = fileId;
		this.thumbnailId = thumbnailId;
		this.filename = filename;
	}
	
	public FileInfo(String fileId,String thumbnailId,String filename,long size,Date uploadDate) {
		this.fileId = fileId;
		this.thumbnailId = thumbnailId;
		this.filename = filename;
		this.size = size;
		this.uploadDate = uploadDate;
	}
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getThumbnailId() {
		return thumbnailId;
	}
	public void setThumbnailId(String thumbnailId) {
		this.thumbnailId = thumbnailId;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getThumbnailURL() {
			return thumbnailURL;
	}
	public void setThumbnailURL(String thumbnailURL) {
			this.thumbnailURL = thumbnailURL;
	}
	
}
