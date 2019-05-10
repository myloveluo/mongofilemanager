package com.jinpeng.dao.impl;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.jinpeng.dao.IFileOperateDao;
import com.jinpeng.utils.MongoUtil;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

@Repository("fileOperateDao")
public class FileOperateDaoImpl implements IFileOperateDao {

	/**
	 * 删除文件
	 */
	@Override
	public void deleteFile(String fileId) {
		//连接MongoDB数据库
		//MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase mongoDatabase = MongoUtil.getDatabase();
		GridFSBucket bucket = GridFSBuckets.create(mongoDatabase);
		
		//查询要上传的文件是否存在
		GridFSFindIterable iterable = bucket.find(Filters.eq("_id",new ObjectId(fileId)));
		MongoCursor<GridFSFile> cursor = iterable.iterator();
		if (!cursor.hasNext()) {
			System.out.println("要删除的文件不存在！");
			return; //如果文件不存在，直接返回
		}
		
		//根据fileId删除文件
		bucket.delete(new ObjectId(fileId));
		String thumbnailId = getThumbnailId(fileId);
		//存在缩略图，则删除缩略图
		if (thumbnailId != null) {
			bucket.delete(new ObjectId(thumbnailId));
		}
	}
	
	/**
	 * 根据文件id获得缩略图id
	 */
	@Override
	public String getThumbnailId(String fileId) {
		
		//MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = MongoUtil.getDB();
		GridFS gridFS = new GridFS(db);
		GridFSDBFile gridFSDBFile = gridFS.find(new ObjectId(fileId));
		if (gridFSDBFile == null) { //该文件没有缩略图
			return null;
		}
		DBObject metaData = gridFSDBFile.getMetaData();
		//该GridFSDBFile是否增加了metadata字段
		if (metaData == null) {
			return null;
		}
		Object thumbnailId = metaData.get("thumbnailId");
		//该文件是否有缩略图
		if (thumbnailId == null) {
			return null;
		}
		return thumbnailId.toString();
	}

	/**
	 * 文件重命名
	 */
	@Override
	public void rename(String fileId, String newFilename) {
		
		//MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase mongoDatabase = MongoUtil.getDatabase();
		GridFSBucket bucket = GridFSBuckets.create(mongoDatabase);
		
		bucket.rename(new ObjectId(fileId), newFilename);
	}

}
