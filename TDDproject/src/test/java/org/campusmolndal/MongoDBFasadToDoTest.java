package org.campusmolndal;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import static org.junit.jupiter.api.Assertions.*;

class MongoDBFasadToDoTest {
    private MongoDBToDoFasad mongoDBToDoFasad;
    private MongoCollection<Document> mockCollection;
    private ToDO toDo1;
    private ToDO toDo2;
    private ToDO toDo3;
    private ToDO toDo4;

    @BeforeEach
    void setUp() {
        mockCollection = mock(MongoCollection.class);
        mongoDBToDoFasad = new MongoDBToDoFasad();
        mongoDBToDoFasad.collection = mockCollection;
        toDo1 = new ToDO(1, "Task 1", false, 1);
        toDo2 = new ToDO(2, "Task 2", false, 2);
        toDo3 = new ToDO(3, "Task 3", true, 2);
        toDo4 = new ToDO(4, "Task 4", false, 2);

    }

    @AfterEach
    void tearDown() {
        mongoDBToDoFasad = null;
        mockCollection = null;
        toDo1 = null;
        toDo2 = null;
        toDo3 = null;
        toDo4 = null;
    }


    // Testar lagra
    @Test
    void insertOne() {
        ToDO toDo = new ToDO(1, "Task 1", false, 1);
        Document expectedDoc = toDo.toDoc();
        mongoDBToDoFasad.insertOne(toDo);
        Mockito.verify(mockCollection).insertOne(expectedDoc);
    }
    // Testar hämta med ID
    @Test
    void findByID() {
        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        when(mockCollection.find(Mockito.any(Document.class))).thenReturn(mockFindIterable);

        when(mockFindIterable.first()).thenReturn(toDo1.toDoc());

        ToDO actualToDo = mongoDBToDoFasad.FindByID(toDo1.getId());

        assertNotNull(actualToDo);
        assertEquals(toDo1.getId(), actualToDo.getId());
        assertEquals(toDo1.getText(), actualToDo.getText());
        assertEquals(toDo1.getDone(), actualToDo.getDone());
        assertEquals(toDo1.getAssignedTo(), actualToDo.getAssignedTo());
    }


    // Testar hämta senaste ID
    @Test
    void getLatestTaskID() {
        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);

        FindIterable<Document> mockSortedIterable = Mockito.mock(FindIterable.class);
        when(mockFindIterable.sort(Mockito.any(Document.class))).thenReturn(mockSortedIterable);

        FindIterable<Document> mockLimitedIterable = Mockito.mock(FindIterable.class);
        when(mockSortedIterable.limit(1)).thenReturn(mockLimitedIterable);

        when(mockLimitedIterable.first()).thenReturn(toDo4.toDoc());

        int latestTaskID = mongoDBToDoFasad.getLatestTaskID();

        assertEquals(4, latestTaskID);
    }

    // Testar uppdatera
    @Test
    void updateOne() {
        ToDO toDo = new ToDO(1, "Task 1", false, 1);
        Document filter = new Document("id", 1);
        Document update = new Document("$set", toDo.toDoc());
        UpdateResult mockResult = mock(UpdateResult.class);
        when(mockCollection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(mockResult);
        when(mockResult.getMatchedCount()).thenReturn(1L);

        mongoDBToDoFasad.updateOne(1, toDo);

        Mockito.verify(mockCollection).updateOne(filter, update);
    }
    // Testar radera
    @Test
    void delete() {
        Document doc = new Document("id", 1);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockCollection.deleteOne(Mockito.any(Document.class))).thenReturn(mockResult);
        when(mockResult.getDeletedCount()).thenReturn(1L);

        mongoDBToDoFasad.Delete(1);

        Mockito.verify(mockCollection).deleteOne(doc);
    }




    // Testar hämtar alla med task med assignedto 2
    @Test
    void findByAssignedTo() {
        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        when(mockCollection.find(Mockito.any(Document.class))).thenReturn(mockFindIterable);

        List<Document> mockDocuments = new ArrayList<>();
        mockDocuments.add(toDo2.toDoc());
        mockDocuments.add(toDo3.toDoc());
        mockDocuments.add(toDo4.toDoc());

        MongoCursor<Document> mockCursor = Mockito.mock(MongoCursor.class);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(true, true, true, false);
        when(mockCursor.next()).thenReturn(mockDocuments.get(0), mockDocuments.get(1), mockDocuments.get(2));

        ArrayList<ToDO> actualTodos = mongoDBToDoFasad.findByAssignedTo(2);

        assertEquals(3, actualTodos.size());
        assertEquals(toDo2.getId(), actualTodos.get(0).getId());
        assertEquals(toDo2.getText(), actualTodos.get(0).getText());
        assertEquals(toDo2.getDone(), actualTodos.get(0).getDone());
        assertEquals(toDo2.getAssignedTo(), actualTodos.get(0).getAssignedTo());

        assertEquals(toDo3.getId(), actualTodos.get(1).getId());
        assertEquals(toDo3.getText(), actualTodos.get(1).getText());
        assertEquals(toDo3.getDone(), actualTodos.get(1).getDone());
        assertEquals(toDo3.getAssignedTo(), actualTodos.get(1).getAssignedTo());

        assertEquals(toDo4.getId(), actualTodos.get(2).getId());
        assertEquals(toDo4.getText(), actualTodos.get(2).getText());
        assertEquals(toDo4.getDone(), actualTodos.get(2).getDone());
        assertEquals(toDo4.getAssignedTo(), actualTodos.get(2).getAssignedTo());
    }



    // Testar hämta alla task, jämför mellan två array
    @Test
    void findAll() {
        List<ToDO> toDoList = new ArrayList<>();
        toDoList.add(toDo1);
        toDoList.add(toDo2);
        toDoList.add(toDo3);
        toDoList.add(toDo4);

        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        MongoCursor<Document> mockCursor = Mockito.mock(MongoCursor.class);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(true, true, true, true, false);

        when(mockCursor.next())
                .thenReturn(toDoList.get(0).toDoc())
                .thenReturn(toDoList.get(1).toDoc())
                .thenReturn(toDoList.get(2).toDoc())
                .thenReturn(toDoList.get(3).toDoc());

        MongoCollection<Document> mockCollection = mock(MongoCollection.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);

        MongoDBToDoFasad mongoDBFasad = new MongoDBToDoFasad();
        mongoDBFasad.collection = mockCollection;

        ArrayList<ToDO> todos = new ArrayList<>();
        FindIterable<Document> findResult = mongoDBFasad.collection.find();
        for (Document doc : findResult) {
            ToDO todo = ToDO.fromDoc(doc);
            todos.add(todo);
            System.out.println(todo);
        }


        ArrayList<ToDO> actualArray = new ArrayList<>(todos);

        assertEquals(todos.size(), actualArray.size());
        assertArrayEquals(todos.toArray(), actualArray.toArray());
    }

    // Testar när användare försöker uppdatera med ID som inte existerar i databasen
    @Test
    void updateOne_NonExistingTask() {
        ToDO toDo = new ToDO(10, "Task 1", false, 1);
        Document filter = new Document("id", 10);
        Document update = new Document("$set", toDo.toDoc());
        UpdateResult mockResult = mock(UpdateResult.class);
        when(mockCollection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(mockResult);
        when(mockResult.getMatchedCount()).thenReturn(0L);

        mongoDBToDoFasad.updateOne(10, toDo);

        Mockito.verify(mockCollection).updateOne(filter, update);
    }

    // Testar när användare försöker radera med ID som inte existerar i databasen
    @Test
    void delete_NonExistingUser() {
        Document doc = new Document("id", 10);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockCollection.deleteOne(Mockito.any(Document.class))).thenReturn(mockResult);
        when(mockResult.getDeletedCount()).thenReturn(0L);

        mongoDBToDoFasad.Delete(10);

        Mockito.verify(mockCollection).deleteOne(doc);
    }

    // Testar när användare försöker hitta en task som inte existerar i databasen
    @Test
    void findByID_NonExistingUser() {
        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        when(mockCollection.find(Mockito.any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null);

        ToDO actualToDo = mongoDBToDoFasad.FindByID(10);

        assertNull(actualToDo);
    }

}



