package com.jinpeng.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinpeng.dao.IFileDownloadDao;
import com.jinpeng.service.IFileDownloadService;

@Service("fileDownloadService")
public class FileDownloadServiceImpl implements IFileDownloadService {

	@Autowired
	private IFileDownloadDao fileDownloadDao;
	
	/**
	 * 根据文件的id下载文件
	 */
	@Override
	public void fileDownload(String fileId, OutputStream outputStream) {
		
		InputStream inputStream = fileDownloadDao.fileDownload(fileId);
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while ((len=inputStream.read(data))!=-1) {
				outputStream.write(data, 0, len);
			// TODO 下载进度
				
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			System.out.println("下载失败");
			e.printStackTrace();
		}
	}

}
