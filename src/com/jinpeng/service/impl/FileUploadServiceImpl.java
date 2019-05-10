package com.jinpeng.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jinpeng.dao.IFileUploadDao;
import com.jinpeng.service.IFileUploadService;
import com.jinpeng.utils.ImageUtil;
import com.jinpeng.utils.OpenCVUtil;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;

@Repository("fileUploadService")
public class FileUploadServiceImpl implements IFileUploadService {
	
	@Autowired
	private IFileUploadDao fileUploadDao;
	

	/**
	 * 单个文件上传
	 */
	@Override
	public void singleUpload(InputStream inputStream, String filename, String tempDir) {
		
		GridFSUploadStream uploadStream = fileUploadDao.prepareUpload(filename);
		
		String fileId = uploadStream.getObjectId().toString();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while( (len = inputStream.read(data))!=-1 ) {
				uploadStream.write(data,0,len);
			}
			uploadStream.close();
			System.out.println("文件 “" + filename + "”上传成功，ID为：" + fileId);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("上传文件失败！");
		}
		
		//上传成功后产生缩略图
		String thumbnailId = generateThumbnail(fileId, filename, tempDir);
	
		//关联文件和缩略图,在文件的元数据中存入缩略图id
		fileUploadDao.addFileMetaData(fileId, thumbnailId);
		return;
	}

	
	/**
	 * 生成缩略图
	 * @param fileId 文件ID
	 * @param filename 文件名
	 */
	public String generateThumbnail(String fileId, String filename, String tempDir) {
		String thumbnailId = null;
		//获取文件后缀
		String suffix = filename.substring(filename.lastIndexOf(".")+1, filename.length());
		
		//如果是图片类型，刚生成缩略图
		if((suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("gif"))) {
			//fileUploadDao.storeImageThumbnail(fileId, suffix);
			thumbnailId = generateImageThumbnail(fileId, suffix);
		}
		
		//如果是视频文件，抽取一帧作为缩略图
		if((suffix.equalsIgnoreCase("mp4") || suffix.equalsIgnoreCase("avi") || suffix.equalsIgnoreCase("flv"))) {
			thumbnailId = generateVideoThumbnail(fileId, tempDir);
		}
		
		return thumbnailId;
	}
	
	
	/**
	 * 生成视频缩略图
	 * @param fileId  文件ID
	 */
	public String generateVideoThumbnail(String fileId, String tempDir) {
		
		GridFSDownloadStream downloadStream = fileUploadDao.getDownloadStream(fileId);
		String thumbnailId = null;
		
		try {
//			System.out.println("临时目录-" + tempDir);
			File videoFile = new File(tempDir, fileId + ".mp4");
			FileOutputStream fos = new FileOutputStream(videoFile);
			byte[] data = new byte[1024];
			int len = 0;
			while ((len=downloadStream.read(data))!=-1) {
				fos.write(data, 0, len);
			}
			downloadStream.close();
			fos.close();
			
			//产生缩略图
			String thumbnailName = "file_" + fileId +"_thumbnail" +".png";//缩略图命名规则：file_<文件ID>_thumbnail.png
			File frameFile=new File(tempDir, thumbnailName);
			OpenCVUtil ocu=new  OpenCVUtil();
			ocu.fetchFrame(videoFile.getAbsolutePath(), frameFile.getAbsolutePath());
			
			//存储缩略图
			GridFSUploadStream uploadStream = fileUploadDao.prepareUpload(thumbnailName);
			thumbnailId = uploadStream.getObjectId().toString();//产生缩略图ID
			FileInputStream fis = new FileInputStream(frameFile);
			while ((len=fis.read(data))!=-1) {
				uploadStream.write(data, 0, len);
			}
			uploadStream.close();
			fis.close();
		
			//删除本地临时存储的文件
			if (videoFile.exists()) {
				videoFile.delete();
			}
			if (frameFile.exists()) {
				frameFile.delete();
			}	
		} catch (Exception e) {
			System.out.println("存储视频缩略图失败");
			e.printStackTrace();
		}
		return thumbnailId;
	}
	
	
	/**
	 * 生成图片缩略图
	 * @param fileId  文件ID
	 * @param suffix  文件后缀
	 */
	public String generateImageThumbnail(String fileId, String suffix) {
		
		GridFSDownloadStream downloadStream = fileUploadDao.getDownloadStream(fileId);
		String thumbnailName = "file_"+fileId+"_thumbnail"+".png";//缩略图命名规则：file_<文件ID>_thumbnail.png
		GridFSUploadStream uploadStream = fileUploadDao.prepareUpload(thumbnailName);
		String thumbnailId = uploadStream.getObjectId().toString();//产生缩略图ID
		
		//获取图片的缩略图，并上传到mongo中
		try {
			InputStream compressStream = ImageUtil.compressImage(downloadStream, suffix, 200, 320);
			byte[] copBuffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = compressStream.read(copBuffer, 0, 1024)) != -1) {
				uploadStream.write(copBuffer, 0, bytesRead);
			}
			uploadStream.close();
			downloadStream.close();
		} catch (Exception e) {
			System.out.println("获取图片缩略图异常");
			e.printStackTrace();
		}
		
		return thumbnailId;
	}

	
	/**
	 * 多个文件上传
	 */
	@Override
	public void multipleUploads(Map<String, InputStream> uploadsMap, String tempDir) {
		
		for (Map.Entry<String, InputStream> entry : uploadsMap.entrySet()) {
			//另一种方案，开多个线程进行上传
			String filename = entry.getKey();
			InputStream inputStream = entry.getValue();
			//fileUploadDao.fileUpload(inputStream, filename);
			singleUpload(inputStream, filename, tempDir);
		}
	}

	
}
