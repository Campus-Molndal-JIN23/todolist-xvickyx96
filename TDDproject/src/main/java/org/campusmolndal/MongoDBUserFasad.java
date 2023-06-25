package org.campusmolndal;


import com.mongodb.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.IndexOptions;
import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("ALL")

public class MongoDBUserFasad {
    MongoClient client;
    MongoDatabase dbUser;
    MongoCollection<Document> collection;

    String connString = "mongodb://localhost:27017";
    String collectionName = "Users";
    String databaseName = "User";


    public MongoDBUserFasad(String connString, String databaseName, String collectionName) {
        this.connString = connString;
        this.collectionName = collectionName;
        this.databaseName = databaseName;
        Connect();
    }

    public MongoDBUserFasad() {
        Connect();
    }
    private void createIndex() {
        try {
            collection.createIndex(new Document("namn", 1),
                    new IndexOptions().unique(false));
            System.out.println("Index created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating index: " + e.getMessage());
        }
    }
    // Lagrar user i databasen
    public void insertOne(User user){
        Document doc = user.toDoc();
        collection.insertOne(doc);

    }
    // Hämtar user från databasen med hjälp av ID
    public User FindByID(Integer userID) {
        Document doc = new Document("userID", userID);
        Document search = collection.find(doc).first();
        return User.fromDoc(search);
    }
    // Hämtar senaste user ID från databasen
    public int getLatestUserID() {
        FindIterable<Document> result = collection.find().sort(new Document("userID", -1)).limit(1);
        Document latestTask = result.first();
        if (latestTask != null) {
            return latestTask.getInteger("userID");
        } else {
            return 0;
        }
    }


    // Uppdaterar user med hjälp av user ID
    public void updateOne(Integer userID, User user) {
        Document filter = new Document("userID", userID);
        Document update = new Document("$set", user.toDoc());
        UpdateResult result = collection.updateOne(filter, update);

        if (result == null) {
            System.out.println("User not found.");
        } else {
            System.out.println("User updated successfully.");
        }
    }


    // Raderar user i databasen med hjälp av user ID
    public void Delete(Integer userID) {
        Document doc = new Document("userID", userID);
        DeleteResult result = collection.deleteOne(doc);

        if (result.getDeletedCount() == 0) {
            System.out.println("User not found.");
        } else {
            System.out.println("User deleted successfully.");
        }
    }

    // Hämtar alla users från databasen
    public ArrayList<User> findAll() {
        FindIterable<Document> result = collection.find();
        ArrayList<User> users = new ArrayList<>();

        result.forEach((Consumer<? super Document>) user -> users.add(User.fromDoc(user)));

        return users;
    }

    private void Connect() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connString))
                .build();

        try {
            client = MongoClients.create(settings);
            dbUser = client.getDatabase(databaseName);
            collection = dbUser.getCollection(collectionName);
        } catch (Exception ex) {
            System.out.println("Ooops!");
            System.out.println(ex.getMessage());
        }

        try {
            createIndex();
        } catch (Exception ex) {
            System.out.println(";)");
        }
    }


}
