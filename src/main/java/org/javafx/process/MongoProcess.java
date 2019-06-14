package org.javafx.process;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoProcess {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoProcess(String host, int port, String database) {
        this.mongoClient = new MongoClient(host, port);
        //new MongoClientURI( "mongodb://xplorerAdmin:admin@localhost:27017/xplorer" )
        this.mongoDatabase = this.mongoClient.getDatabase(database);
    }

    public MongoProcess(String mongoUri, String database) {
        System.out.println("mongoUri : " + mongoUri);
        this.mongoClient = new MongoClient(new MongoClientURI( mongoUri ));
        this.mongoDatabase = this.mongoClient.getDatabase(database);
    }

    public List<String> getAllCollectionNames() {
        List<String> collectionsNames = new ArrayList<>();
        for(String collectionName : this.mongoDatabase.listCollectionNames()) {
            collectionsNames.add(collectionName);
        }

        return collectionsNames;
    }

    public List<String> getAllEntriesOfCollection(String collection) {
        MongoCollection mongoCollection = this.mongoDatabase.getCollection(collection);
        FindIterable<Document> findIterable = mongoCollection.find();
        List<String> documents = new ArrayList<>();
        for(Document doc: findIterable) {
            documents.add(doc.toJson());
        }

        return documents;
    }

    public void disconnect() {
        this.mongoClient.close();
    }
}
