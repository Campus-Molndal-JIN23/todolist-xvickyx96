package org.campusmolndal;

import org.bson.Document;

public class ToDO {
    private int id;
    private String text;
    private boolean done;
    private int assignedTo;

    public ToDO(int id, String text, boolean done, int assignedTo) {
        this.id = id;
        this.text = text;
        this.done = done;
        this.assignedTo = assignedTo;
    }

    public int getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }


    public boolean getDone() {
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String status = getDone() ? "done" : "not done";
        return "Task ID " + getId() + ": " + getText() + " is " + status + " and assigned to user with ID number " + getAssignedTo() + ".";
    }

    public Document toDoc() {
        return new Document("id", getId())
                .append("name", getText())
                .append("done", getDone())
                .append("assignedTo", getAssignedTo());
    }

    public static ToDO fromDoc(Document doc) {
        if (doc == null) {
            return null;
        }

        int id = doc.getInteger("id", 0);
        String text = doc.getString("name");
        boolean done = doc.getBoolean("done", false);
        int assignedTo = doc.getInteger("assignedTo", 0);
        return new ToDO(id, text, done, assignedTo);
    }
}




