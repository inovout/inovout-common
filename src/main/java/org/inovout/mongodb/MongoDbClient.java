package org.inovout.mongodb;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class MongoDbClient {
	private MongoClient mongoClient;

	public MongoDbClient(List<ServerAddress> serverAddresses) {

		this.mongoClient = new MongoClient(serverAddresses);
	}

	private static final Map<String, DB> databases = new Hashtable<String, DB>();
	private static final Map<String, DBCollection> collections = new Hashtable<String, DBCollection>();

	public DB getDatabase(String database) {
		DB db = databases.get(database);
		if (db == null) {
			db = mongoClient.getDB(database);
			databases.put(database, db);
		}
		return db;
	}

	public DBCollection getCollection(String database, String collection) {
		String key = database + "." + collection;
		DBCollection dbCollection = collections.get(key);
		if (dbCollection == null) {
			dbCollection = getDatabase(database).getCollection(collection);
			collections.put(key, dbCollection);
		}
		return dbCollection;
	}
}
