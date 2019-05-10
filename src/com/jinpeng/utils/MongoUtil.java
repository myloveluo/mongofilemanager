package com.jinpeng.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {
	
	private static MongoClient mongoClient = null;//
	private static String host = null;//mongodb，nginx安装的主机地址
	private static int port = 0;//mongodb的端口号
	private static String dbName = null;//数据库名
	
	static {
		Properties pros = new Properties();
		//InputStream inputStream = MongoUtil.class.getClassLoader().getResourceAsStream("mongodb.properties");
		String path = MongoUtil.class.getClassLoader().getResource("mongodb.properties").getPath();
		//System.out.println(path);
		try {
			FileInputStream inputStream = new FileInputStream(path);
			pros.load(inputStream);
		} catch (IOException e) {
			System.out.println("读取mongodb.properties文件失败！");
			throw new ExceptionInInitializerError(e);//初始化错误
		}
		host = pros.getProperty("host");
		port = Integer.parseInt(pros.getProperty("port"));
		dbName = pros.getProperty("dbName");
		mongoClient = new MongoClient(host, port);
	}
	
	/**
	 * 获取MongoDB，nginx的主机地址
	 * @return 主机地址
	 */
	public static String getHost() {
		return host;
	}
	
	/**
	 * 获取MongoDB的端口号
	 * @return 端口号
	 */
	public static int getPort() {
		return port;
	}
	
	/**
	 * 获取数据库名
	 * @return 数据库名
	 */
	public static String getDBName() {
		return dbName;
	}
	
	/**
	 * 获取MongoClient实例，该实例也就是一个连接池
	 * @return MongoClient实例
	 */
	public static MongoClient getMongoClient() {
		return mongoClient;
	}
	
	/**
	 * 根据给定的数据库名，获取MongoDatabase实例
	 * @param dbName
	 * @return MongoDatabase实例
	 */
	public static MongoDatabase getDatabase(String dbName) {
		if (dbName == null || "".equals(dbName)) {
			return null;
		}
		
		return mongoClient.getDatabase(dbName);
	}
	
	/**
	 * 根据配置的数据库名，获取MongoDatabase实例
	 * @return MongoDatabase实例
	 */
	public static MongoDatabase getDatabase() {
		return mongoClient.getDatabase(MongoUtil.dbName);
	}
	
	/**
	 * 根据给定的数据库名，获取DB实例
	 * @param dbName 数据库名
	 * @return DB实例
	 */
	public static DB getDB(String dbName) {
		if (dbName == null || "".equals(dbName)) {
			return null;
		}
		DB db = new DB(mongoClient, dbName);
		return db;
	}
	
	/**
	 * 根据配置的数据库名，获取DB实例
	 * @return DB实例
	 */
	public static DB getDB() {
		DB db = new DB(mongoClient, MongoUtil.dbName);
		return db;
	}
	
}
