package com.jinpeng.dao.impl;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.jinpeng.dao.IFileUploadDao;
import com.jinpeng.utils.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

@Repository("fileUploadDao")
public class FileUploadDaoImpl implements IFileUploadDao {

	/**
	 * 准备文件上传
	 */
	@Override
	public GridFSUploadStream prepareUpload(String filename) {
		//连接到MongoDB服务
		//MongoClient mongoClient = new MongoClient("localhost", 27017);
		//连接到数据库
		MongoDatabase mongoDatabase = MongoUtil.getDatabase();
		//使用默认的Bucket名称"fs"创建一个gridFSBucket
		GridFSBucket gfsbk = GridFSBuckets.create(mongoDatabase);
		
		GridFSUploadStream uploadStream = gfsbk.openUploadStream(filename);
		return uploadStream;
	}

	/**
	 * 在文件的元数据中增加对应的缩略图ID
	 */
	@Override
	public void addFileMetaData(String fileId, String thumbnailId) {
		//MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = MongoUtil.getDB();
		GridFS gridFS = new GridFS(db);
		GridFSDBFile gridFSDBFile = gridFS.find(new ObjectId(fileId));
		DBObject obj  = new BasicDBObject("thumbnailId", new ObjectId(thumbnailId));
		//System.out.println(gridFSDBFile.getMetaData());
		gridFSDBFile.setMetaData(obj);
		//System.out.println(gridFSDBFile.getMetaData());
		gridFSDBFile.save();
	}
	
	/**
	 * 获取文件下载流
	 */
	@Override
	public GridFSDownloadStream getDownloadStream(String fileId) {
		
		MongoDatabase mongoDatabase = MongoUtil.getDatabase();
		GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabase);
		GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(new ObjectId(fileId));
		return downloadStream;
	}

}
