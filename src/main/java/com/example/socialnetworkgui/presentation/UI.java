package com.example.socialnetworkgui.presentation;

import com.example.socialnetworkgui.business.Service;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UI {

    private final Service service;

    private final Scanner scanner;

    public UI(Service service) {
        this.service = service;
        scanner = new Scanner(System.in);
    }

    private void addUser() throws ValidationException {
        try {
            System.out.println("Enter name:");
            String name = scanner.nextLine();
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();
            service.addUser( name,email,password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeUser() throws RepoException {
        try {
            System.out.println("Enter id:");
            int id = scanner.nextInt();
            scanner.nextLine();
            service.removeUser(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printUsers() throws RepoException {
        Iterable<User> users = service.getUsersList();
        for (User user : users) {
            System.out.println(user);
        }
    }

    private void addFriendship() throws ValidationException {
        try {
            System.out.println("Enter the id of the user:");
            int idUser = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter the id of his friend:");
            int idFriend = scanner.nextInt();
            scanner.nextLine();
            service.addFriendship( idUser, idFriend);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeFriendship() throws RepoException {
        try {
            System.out.println("Enter id:");
            int id = scanner.nextInt();
            scanner.nextLine();
            service.removeFriendship(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printFriendships() throws RepoException {
        Iterable<Friendship> friendships = service.getFriendshipsList();
        for (Friendship friendship : friendships) {
            System.out.println(friendship);
        }
    }

    private void printNumberOfCommunities() throws RepoException {
        int number = service.getNumberOfCommunities();
        System.out.println(number);
    }

    private void printMostSociableCommunity() throws RepoException {
        List<User> users = service.getMostSociableCommunity();
        for (User user : users) {
            System.out.print(user + " -> ");
        }
        System.out.println();
    }

    private void printMenu() {
        String[] menu = {
                "Menu:",
                "1.Add a new user",
                "2.Remove a user",
                "3.Print all users",
                "4.Add a new friendship",
                "5.Remove a friendship",
                "6.Print all friendships",
                "7.Get the number of communities",
                "8.Get the most sociable community"
        };
        for (String s : menu) {
            System.out.println(s);
        }
    }

    /**
     * Function that starts the loop of the application
     *
     * @throws ValidationException
     * @throws RepoException
     */
    public void run() throws ValidationException, RepoException {
        while (true) {
            printMenu();
            String cmd = scanner.nextLine();
            if (Objects.equals(cmd, "1")) {
                addUser();
            } else if (Objects.equals(cmd, "2")) {
                removeUser();
            } else if (Objects.equals(cmd, "3")) {
                printUsers();
            } else if (Objects.equals(cmd, "4")) {
                addFriendship();
            } else if (Objects.equals(cmd, "5")) {
                removeFriendship();
            } else if (Objects.equals(cmd, "6")) {
                printFriendships();
            } else if (Objects.equals(cmd, "7")) {
                printNumberOfCommunities();
            } else if (Objects.equals(cmd, "8")) {
                printMostSociableCommunity();
            } else {
                return;
            }
        }
    }
}
