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

public class MongoDBToDoFasad {
    MongoClient client;
    MongoDatabase dbTask;
    MongoCollection<Document> collection;

    String connString = "mongodb://localhost:27017";
    String collectionName = "ToDo";
    String databaseName = "Task";


    public MongoDBToDoFasad(String connString, String databaseName, String collectionName) {
        this.connString = connString;
        this.collectionName = collectionName;
        this.databaseName = databaseName;
        Connect();
    }

    public MongoDBToDoFasad() {
        Connect();
    }
    // skapar index
    private void createIndex() {
        try {
            collection.createIndex(new Document("namn", 1),
                    new IndexOptions().unique(false));
            System.out.println("Index created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating index: " + e.getMessage());
        }
    }
    // Lagrar task i databasen
    public void insertOne(ToDO toDO) {
        Document doc = toDO.toDoc();
        collection.insertOne(doc);

    }
    // Hämtar task från databasen med hjälp av ID
    public ToDO FindByID(Integer taskID) {
        Document doc = new Document("id", taskID);
        Document search = collection.find(doc).first();
        return ToDO.fromDoc(search);
    }

    // Kollar den senaste ID numret i databasen
    public int getLatestTaskID() {
        FindIterable<Document> result = collection.find().sort(new Document("id", -1)).limit(1);
        Document latestTask = result.first();
        if (latestTask != null) {
            return latestTask.getInteger("id");
        } else {
            return 0;
        }
    }

    // Hämtar och uppdaterar task med hjälp av ID
    public void updateOne(Integer taskID, ToDO toDo) {
        Document filter = new Document("id", taskID);
        Document update = new Document("$set", toDo.toDoc());
        UpdateResult result = collection.updateOne(filter, update);

        if (result == null) {
            System.out.println("Task not found.");
        } else {
            System.out.println("Task updated successfully.");
        }
    }

    // Raderar task med hjälp av ID
    public void Delete(Integer taskID) {
        Document doc = new Document("id", taskID);
        DeleteResult result = collection.deleteOne(doc);

        if (result.getDeletedCount() == 0) {
            System.out.println("Task not found.");
        } else {
            System.out.println("Task deleted successfully.");
        }
    }
    // Hämtar alla task som har samma assignedTo nummer när användaren skriver in numret.
    public ArrayList<ToDO> findByAssignedTo(int assignedTo) {
        ArrayList<ToDO> tasks = new ArrayList<>();

        Document query = new Document("assignedTo", assignedTo);
        FindIterable<Document> result = collection.find(query);

        for (Document doc : result) {
            ToDO task = ToDO.fromDoc(doc);
            tasks.add(task);
        }

        return tasks;
    }


    // Hämtar alla task från databasen
    public ArrayList<ToDO> findAll() {
        FindIterable<Document> result = collection.find();
        ArrayList<ToDO> toDoes = new ArrayList<>();

        result.forEach((Consumer<? super Document>) toDo -> toDoes.add(ToDO.fromDoc(toDo)));

        return toDoes;
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
            dbTask = client.getDatabase(databaseName);
            collection = dbTask.getCollection(collectionName);
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