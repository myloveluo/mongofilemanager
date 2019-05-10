package com.jinpeng.dao.impl;

import java.io.InputStream;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.jinpeng.dao.IFileDownloadDao;
import com.jinpeng.utils.MongoUtil;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

@Repository("fileDownloadDao")
public class FileDownloadDaoImpl implements IFileDownloadDao {

	/**
	 * 文件下载
	 */
	@Override
	public InputStream fileDownload(String fileId) {
		
		//MongoClient mongoClient = new MongoClient("localhost",27017);
		MongoDatabase mongodatabase = MongoUtil.getDatabase();
		GridFSBucket bucket = GridFSBuckets.create(mongodatabase);
		GridFSDownloadStream downloadStream = bucket.openDownloadStream(new ObjectId(fileId));
		
		return downloadStream;
	}

}
