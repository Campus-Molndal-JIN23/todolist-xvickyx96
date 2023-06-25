package org.campusmolndal;
import java.util.ArrayList;
public class Menu {
    private MongoDBToDoFasad dbTask;
    private MongoDBUserFasad dbUser;
    ScannerClass scannerClass = new ScannerClass();

    public Menu() {
        dbTask = new MongoDBToDoFasad();
        dbUser = new MongoDBUserFasad();
    }


    public void MenuStart() {

        System.out.println("Welcome to your ToDo list");
        boolean keepRunning = true;

            while (keepRunning) {
                System.out.println("----------------------------------");
                System.out.println("Main Menu:");
                System.out.println("----------------------------------");
                System.out.println("1. See all tasks.");
                System.out.println("2. Find a task with task ID.");
                System.out.println("3. Create task.");
                System.out.println("4. Update task.");
                System.out.println("5. Delete task.");
                System.out.println("----------------------------------");
                System.out.println("6. See all users.");
                System.out.println("7. Find user with user ID.");
                System.out.println("8. Create user.");
                System.out.println("9. Update user information");
                System.out.println("10. Delete user.");
                System.out.println("11. See all task that a user have.");
                System.out.println("----------------------------------");
                System.out.println("12. Exit.");
                System.out.print("Enter your choice: ");
                int choice = scannerClass.readInteger();

                switch (choice) {
                    case 1:
                        showAllTask();
                        break;
                    case 2:
                        findTask();
                        break;
                    case 3:
                        createTask();
                        break;
                    case 4:
                        updateTask();
                        break;
                    case 5:
                        deleteTask();
                        break;
                    case 6:
                    showAllUser();
                        break;
                    case 7:
                        findUser();
                        break;
                    case 8:
                        createUser();
                        break;
                    case 9:
                        updateUser();
                        break;

                    case 10:
                        deleteUser();
                        break;
                    case 11:
                        findTasksByAssignedTo();
                        break;
                    case 12:
                    keepRunning = false;
                }
            }

            scannerClass.close();
           System.out.println("Thanks for using the program!");
       }

    // Skapa användare
    private void createUser() {
        System.out.println("Write name of the user: ");
        String userNamn = scannerClass.readString();
        System.out.println("Write age of the user: ");
        Integer ålder = scannerClass.readInteger();
        int latestUserID = dbUser.getLatestUserID();
        int newUserID = latestUserID + 1;
        User user = new User(newUserID, userNamn, ålder);
        dbUser.insertOne(user);
        System.out.println("User created with ID: " + newUserID);
    }

    // Söka på användare genom att skriva nummer till ID user
    private void findUser() {
        System.out.println("Write id of the user that you want to find by ID: ");
        Integer userID = scannerClass.readInteger();
        System.out.println(dbUser.FindByID(userID));
    }


    // Radera användare genom att skriva nummer till ID user
    private void deleteUser() {
        System.out.println("Write id of the user that you want to delete: ");
        Integer taskID = scannerClass.readInteger();
        dbUser.Delete(taskID);
    }


    // Uppdatera användare genom att skriva nummer till ID user samt den nya infon
    private void updateUser() {
        System.out.println("Write id of the user you want to update: ");
        Integer userID = scannerClass.readInteger();
        System.out.println(dbUser.FindByID(userID));
        System.out.println("Write name of the user: ");
        String namn = scannerClass.readString();
        System.out.println("Write age of the user: ");
        Integer age = scannerClass.readInteger();
        User user = new User(userID, namn, age);
        dbUser.updateOne(userID, user);
    }

    // Visar alla användare
    private void showAllUser() {
        ArrayList<User> userList = dbUser.findAll();
        if (userList.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("This is all users:");
            for (User user : userList) {
                System.out.println(user);
            }
        }
    }

    // Skapar task
    private void createTask() {
        System.out.println("Write name of the task: ");
        String taskNamn = scannerClass.readString();
        System.out.println("Write ID of the user that will work with this task: ");
        int userID = scannerClass.readInteger();
        System.out.println("Is it done: y/n");
        boolean done = scannerClass.readBoolean();

        int latestTaskID = dbTask.getLatestTaskID();
        int newTaskID = latestTaskID + 1;

        ToDO toDo = new ToDO(newTaskID, taskNamn, done, userID);
        dbTask.insertOne(toDo);
        System.out.println("Task created with ID: " + newTaskID);
    }

    // Hittar alla task som en User skall göra genom assignedto nummer
    private void findTasksByAssignedTo() {
        System.out.println("Write ID of the user to find which task the person is doing: ");
        int userID = scannerClass.readInteger();

        ArrayList<ToDO> tasks = dbTask.findByAssignedTo(userID);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found for the user with ID: " + userID);
        } else {
            System.out.println("Tasks assigned to the user with ID " + userID + ":");
            for (ToDO task : tasks) {
                System.out.println(task);
            }
        }
    }

    // Uppdatera task med ID samt ny info
    private void updateTask () {
            System.out.println("Write id of the task that you want to update: ");
            Integer taskID = scannerClass.readInteger();
            System.out.println(dbTask.FindByID(taskID));
            System.out.println("Write new name of the task: ");
            String taskNamn = scannerClass.readString();
            System.out.println("Write ID of the user that works with this task: ");
            int userID = scannerClass.readInteger();
            System.out.println("Is it done: y/n");
            Boolean done = scannerClass.readBoolean();
            ToDO toDo = new ToDO(taskID, taskNamn, done, userID);
            dbTask.updateOne(taskID, toDo);
            System.out.println();
        }
    // Hitta task med ID
        private void findTask () {
            System.out.println("Write id of the task that you want to find by ID: ");
            Integer taskID = scannerClass.readInteger();
            System.out.println(dbTask.FindByID(taskID));
        }

    // Radera task med ID
        private void deleteTask () {
            System.out.println("Write id of the task that you want to delete: ");
            Integer taskID = scannerClass.readInteger();
            dbTask.Delete(taskID);
        }

    // Visar alla tasks
        private void showAllTask () {
            ArrayList<ToDO> taskList = dbTask.findAll();
            if (taskList.isEmpty()) {
                System.out.println("No tasks found.");
            } else {
                System.out.println("This is all tasks:");
                for (ToDO task : taskList) {
                    System.out.println(task);
                }
            }
        }
    }
