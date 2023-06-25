package org.campusmolndal;

import java.util.Scanner;

public class ScannerClass {
    private Scanner scanner;

    public ScannerClass() {
        scanner = new Scanner(System.in);
    }
    // Läser av Integer fråna användaren
    public int readInteger() {
        return Integer.parseInt(scanner.nextLine());
    }

    // Läser av String från användaren
    public String readString() {
        return scanner.nextLine();
    }

    // Läser av Boolean från användare genom y/n
    public boolean readBoolean() {
        String input = scanner.next();
        scanner.nextLine();
        while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
            System.out.println("Invalid input. Please enter 'y' or 'n': ");
            input = scanner.next();
        }
        return input.equalsIgnoreCase("y");
    }
    // Stänger scanner
    public void close() {
        scanner.close();
    }
}

