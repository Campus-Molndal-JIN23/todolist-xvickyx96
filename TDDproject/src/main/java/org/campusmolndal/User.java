package org.campusmolndal;

import org.bson.Document;

public class User {
    private int userID;
    private String name;
    private int age;

    public User(int userID, String name, int age) {
        this.userID = userID;
        this.name = name;
        this.age = age;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String toString() {
        return "User ID " + getUserID() + ": " + getName() + " is " + getAge() + " years old.";
    }

    public Document toDoc() {
        return new Document("userID", getUserID())
                .append("name", getName())
                .append("age", getAge());
    }

    public static User fromDoc(Document doc) {
        if (doc == null) {
            return null;
        }

        int userID = doc.getInteger("userID", 0);
        String name = doc.getString("name");
        int age = doc.getInteger("age", 0);
        return new User(userID, name, age);
    }
}
