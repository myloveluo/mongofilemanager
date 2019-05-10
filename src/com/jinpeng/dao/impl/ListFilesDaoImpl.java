package com.jinpeng.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.jinpeng.dao.IListFilesDao;
import com.jinpeng.entity.FileInfo;
import com.jinpeng.utils.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

@Repository("listFilesDao")
public class ListFilesDaoImpl implements IListFilesDao {

	/**
	 * 列出文件
	 */
	@Override
	public List<FileInfo> listFiles(String filename) {
		
		//连接到数据库
		MongoDatabase mongoDatabase = MongoUtil.getDatabase();//dbName数据库名称
		GridFSBucket gfsbk = GridFSBuckets.create(mongoDatabase);
		
		
		//根据文件名查询所有文件
		GridFSFindIterable iterable = gfsbk.find(Filters.eq("filename", filename));
		MongoCursor<GridFSFile> cursor = iterable.iterator();
		if (!cursor.hasNext()) {
			return null; //结果中没有数据，则返回null
		}
		
		//遍历查询结果
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		while (cursor.hasNext()) {
			GridFSFile gridFSFile = cursor.next();
			String file_name = gridFSFile.getFilename();//文件名
			String fileId = gridFSFile.getObjectId().toString();//文件ID
			long size = gridFSFile.getLength();//文件大小
			Date uploadDate = gridFSFile.getUploadDate();//文件上传日期
			String thumbnailId = getThumbnailId(fileId);//文件缩略图ID
			FileInfo fileInfo = new FileInfo(fileId, thumbnailId, file_name, size, uploadDate);
			fileInfoList.add(fileInfo);
		}

		return fileInfoList;
	}
	
	
	/**
	 * 根据文件id获取对应的缩略图id
	 * @param fileId
	 * @return 缩略图ID,没有缩略图则为null
	 */
	public String getThumbnailId(String fileId) {
		
		DB db = MongoUtil.getDB();
		GridFS gridFS = new GridFS(db);
		GridFSDBFile gridFSDBFile = gridFS.find(new ObjectId(fileId));
		DBObject metaData = gridFSDBFile.getMetaData();
	
		//该文件是否有metadata字段
		if (metaData != null) {
			Object thumbnailId = metaData.get("thumbnailId");
			//该文件是否有缩略图
			if (thumbnailId != null)
				return thumbnailId.toString();
		}
		return null;
	}


	/**
	 * 列出模糊匹配的文件
	 */
	@Override
	public List<FileInfo> listMatch(String filename) {
/*		//获取连接
		DB db = MongoUtil.getDB();
		DBCollection fsCollection = db.getCollection("fs.files");
		
		//构造查询条件，模糊匹配文件名
		Pattern pattern = Pattern.compile("^.*"+filename+".*$", Pattern.CASE_INSENSITIVE);
		BasicDBObject query = new BasicDBObject();
        query.put("filename",pattern);
        BasicDBObject sort = new BasicDBObject();
        sort.put("filename",1);// 1,表示正序； －1,表示倒序
        
        //查询
        DBCursor cursor = fsCollection.find(query);
        if (cursor == null) {
        	return null;
        }
        cursor = cursor.sort(sort);
        
        //遍历查询结果
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
        while (cursor.hasNext()) {
			DBObject fileObject = cursor.next();
			String fileId = fileObject.get("_id").toString();//文件ID
			String file_name = fileObject.get("filename").toString();//文件名
			long size = Long.parseLong(fileObject.get("length").toString());//文件大小
			Object date = fileObject.get("uploadDate");//文件上传日期， 这样获取的日期与本地相差8个小时
			System.out.println(date);
			String thumbnailId = getThumbnailId(fileId);//文件缩略图ID
			
			FileInfo fileInfo = new FileInfo(fileId, thumbnailId, file_name);
			fileInfoList.add(fileInfo);
		}
      */  
		
		//获取连接
		MongoDatabase database = MongoUtil.getDatabase();
		GridFSBucket gridFSBucket = GridFSBuckets.create(database);
		
		//构造查询条件，模糊匹配文件名
		Pattern pattern = Pattern.compile("^.*"+filename+".*$", Pattern.CASE_INSENSITIVE);
		BasicDBObject queryObject = new BasicDBObject();
		queryObject.put("filename",pattern);
        
        //查询
        GridFSFindIterable  iterable = gridFSBucket.find(queryObject);
        MongoCursor<GridFSFile> cursor = iterable.iterator();
        if (!cursor.hasNext()) {
        	return null;  //如果没有对应的结果，则返回空
        }
        
        //遍历查询结果
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();//文件信息集合
        while (cursor.hasNext()) {
        	GridFSFile gridFSFile = cursor.next();
			String file_name = gridFSFile.getFilename();//文件名
			String fileId = gridFSFile.getObjectId().toString();//文件ID
			long size = gridFSFile.getLength();//文件大小
			Date uploadDate = gridFSFile.getUploadDate();//文件上传日期
			String thumbnailId = getThumbnailId(fileId);//文件缩略图ID
			FileInfo fileInfo = new FileInfo(fileId, thumbnailId, file_name, size, uploadDate);
			fileInfoList.add(fileInfo);
			
		}

		return fileInfoList;
	}



	/**
	 * 列出所有文件
	 */
	@Override
	public List<FileInfo> listAll() {
		//获取数据库连接
		MongoDatabase mongoDatabase = MongoUtil.getDatabase();
		GridFSBucket gfsbk = GridFSBuckets.create(mongoDatabase);
		
		//查询所有文件（除了缩略图）
		GridFSFindIterable iterable = gfsbk.find();
		MongoCursor<GridFSFile> cursor = iterable.iterator();
		if (!cursor.hasNext() ) {
			return null;//文件不存在，则返回null
		}
		
		//遍历查询结果
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
        while (cursor.hasNext()) {
			GridFSFile gridFSFile = cursor.next();
			String filename = gridFSFile.getFilename();//文件名
			String fileId = gridFSFile.getObjectId().toString();//文件ID
			long size = gridFSFile.getLength();//文件大小
			Date uploadDate = gridFSFile.getUploadDate();//文件上传日期
	//		System.out.println(uploadDate);
	//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//		String str = sdf.format(uploadDate);
	//		System.out.println(str);
			String thumbnailId = getThumbnailId(fileId);//文件缩略图ID
			
			FileInfo fileInfo = new FileInfo(fileId, thumbnailId, filename, size, uploadDate);
			fileInfoList.add(fileInfo);	
		}
		return fileInfoList;
	}


	/**
	 * 根据文件后缀名查询
	 */
	@Override
	public List<FileInfo> listBySuffix(List<String> suffixList) {
		/*//获取连接
		DB db = MongoUtil.getDB();
		DBCollection fsCollection = db.getCollection("fs.files");
		MongoDatabase database = MongoUtil.getDatabase();
		GridFSBucket gridFSBucket = GridFSBuckets.create(database);
		
		//构造查询条件
		List<BasicDBObject> list = new ArrayList<BasicDBObject>();//查询对象集合
        for (String suffix : suffixList) {
        	//正则表达式
        	Pattern pattern = Pattern.compile("^.*" + suffix + "$", Pattern.CASE_INSENSITIVE);
        	list.add(new BasicDBObject("filename",pattern));
        }
        BasicDBObject queryObject = new BasicDBObject();
        queryObject.put(QueryOperators.OR,list);//多个后缀名之间是“或”的关系
        
        //查询
        gridFSBucket.find(queryObject);
        DBCursor cursor = fsCollection.find(queryObject);
        if (cursor == null) {
        	return null;  //如果没有对应的结果，则返回空
        }
        
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();//文件信息集合
        while (cursor.hasNext()) {
			
			DBObject fileObject = cursor.next();
			String fileId = fileObject.get("_id").toString();//文件ID
			String file_name = fileObject.get("filename").toString();//文件名
			long size = Long.parseLong(fileObject.get("length").toString());//文件大小
			Object date = fileObject.get("uploadDate");//文件上传日期
			System.out.println(date);
			String thumbnailId = getThumbnailId(fileId);//文件缩略图ID
			
			FileInfo fileInfo = new FileInfo(fileId, thumbnailId, file_name, size, date);
			fileInfoList.add(fileInfo);
		}
		return fileInfoList;*/
		
		//获取连接
		MongoDatabase database = MongoUtil.getDatabase();
		GridFSBucket gridFSBucket = GridFSBuckets.create(database);
		
		//构造查询条件
		List<BasicDBObject> list = new ArrayList<BasicDBObject>();//查询对象集合
        for (String suffix : suffixList) {
        	//正则表达式
        	Pattern pattern = Pattern.compile("^.*" + suffix + "$", Pattern.CASE_INSENSITIVE);
        	list.add(new BasicDBObject("filename",pattern));
        }
        BasicDBObject queryObject = new BasicDBObject();
        queryObject.put(QueryOperators.OR,list);//多个后缀名之间是“或”的关系
        
        //查询
        GridFSFindIterable  iterable = gridFSBucket.find(queryObject);
        MongoCursor<GridFSFile> cursor = iterable.iterator();
        if (!cursor.hasNext()) {
        	return null;  //如果没有对应的结果，则返回空
        }
        
        //遍历查询结果
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();//文件信息集合
        while (cursor.hasNext()) {
        	GridFSFile gridFSFile = cursor.next();
			String filename = gridFSFile.getFilename();//文件名
			String fileId = gridFSFile.getObjectId().toString();//文件ID
			long size = gridFSFile.getLength();//文件大小
			Date uploadDate = gridFSFile.getUploadDate();//文件上传日期
			String thumbnailId = getThumbnailId(fileId);//文件缩略图ID
			FileInfo fileInfo = new FileInfo(fileId, thumbnailId, filename, size, uploadDate);
			fileInfoList.add(fileInfo);
			
		}
		return fileInfoList;
		
	}
	
	

}
