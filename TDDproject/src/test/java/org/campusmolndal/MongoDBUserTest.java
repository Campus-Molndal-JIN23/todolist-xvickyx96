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

class MongoDBUserFasadTest {
    private MongoDBUserFasad mongoDBUser;
    private MongoCollection<Document> mockCollection;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    @BeforeEach
    void setUp() {
        mockCollection = mock(MongoCollection.class);
        mongoDBUser = new MongoDBUserFasad();
        mongoDBUser.collection = mockCollection;
        user1 = new User (1, "Vikram", 27);
        user2 = new User(2, "Arjun", 35);
        user3 = new User(3, "Alisha", 20);
        user4 = new User(4, "Yusuf", 15);
    }

    @AfterEach
    void tearDown() {
        mongoDBUser = null;
        mockCollection = null;
        user1 = null;
        user2 = null;
        user3 = null;
        user4 = null;
    }

    // Testar om den kan lagra
    @Test
    void insertOne() {
        User user = new User(5, "Erik", 33);
        Document expectedDoc = user.toDoc();
        mongoDBUser.insertOne(user);
        Mockito.verify(mockCollection).insertOne(expectedDoc);
    }
    // Testar hämta med user ID och ser så userID, namn och age kommer med
    @Test
    void findByID() {
        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        when(mockCollection.find(Mockito.any(Document.class))).thenReturn(mockFindIterable);

        when(mockFindIterable.first()).thenReturn(user1.toDoc());

        User actualUser = mongoDBUser.FindByID(user1.getUserID());

        assertNotNull(actualUser);
        assertEquals(user1.getUserID(), actualUser.getUserID());
        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getAge(), actualUser.getAge());
    }
    // Testar för se om den hittar senaste user ID
    @Test
    void getLatestTaskID() {
        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);

        FindIterable<Document> mockSortedIterable = Mockito.mock(FindIterable.class);
        when(mockFindIterable.sort(Mockito.any(Document.class))).thenReturn(mockSortedIterable);

        FindIterable<Document> mockLimitedIterable = Mockito.mock(FindIterable.class);
        when(mockSortedIterable.limit(1)).thenReturn(mockLimitedIterable);

        when(mockLimitedIterable.first()).thenReturn(user4.toDoc());

        int latestUserID = mongoDBUser.getLatestUserID();

        assertEquals(4, latestUserID);
    }
    // Testar att uppdatera
    @Test
    void updateOne() {
        User user = new User(1, "Erica", 31);
        Document filter = new Document("userID", 1);
        Document update = new Document("$set", user.toDoc());
        UpdateResult mockResult = mock(UpdateResult.class);
        when(mockCollection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(mockResult);
        when(mockResult.getMatchedCount()).thenReturn(1L);

        mongoDBUser.updateOne(1, user);

        Mockito.verify(mockCollection).updateOne(filter, update);
    }

    // Testar ta bort
    @Test
    void delete() {
        Document doc = new Document("userID", 1);
        DeleteResult mockResult = mock(DeleteResult.class);
        when(mockCollection.deleteOne(Mockito.any(Document.class))).thenReturn(mockResult);
        when(mockResult.getDeletedCount()).thenReturn(1L);

        mongoDBUser.Delete(1);

        Mockito.verify(mockCollection).deleteOne(doc);
    }

    // Testar hämta alla user, jämför med två arrays
    @Test
    void findAll() {
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);

        MongoCursor<Document> mockCursor = Mockito.mock(MongoCursor.class);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(true, true, true, true, false);

        when(mockCursor.next())
                .thenReturn(userList.get(0).toDoc())
                .thenReturn(userList.get(1).toDoc())
                .thenReturn(userList.get(2).toDoc())
                .thenReturn(userList.get(3).toDoc());

        MongoCollection<Document> mockCollection = mock(MongoCollection.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);

        MongoDBUserFasad mongoDBUser = new MongoDBUserFasad();
        mongoDBUser.collection = mockCollection;

        ArrayList<User> users = new ArrayList<>();
        FindIterable<Document> findResult = mongoDBUser.collection.find();
        for (Document doc : findResult) {
            User user = User.fromDoc(doc);
            users.add(user);
            System.out.println(user);
        }


        ArrayList<User> actualArray = new ArrayList<>(users);

        assertEquals(users.size(), actualArray.size());
        assertArrayEquals(users.toArray(), actualArray.toArray());
    }
}