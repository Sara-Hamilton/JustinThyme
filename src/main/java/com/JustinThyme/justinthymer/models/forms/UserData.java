package com.JustinThyme.justinthymer.models.forms;

import java.util.ArrayList;

public class UserData {

    static ArrayList<User> users = new ArrayList<>();

    //add
    public static void add(User newUser) { users.add(newUser); }

    //getAll
    public static ArrayList<User> getAll() { return users; }

    //getById
    public static User getById(int id) {
        User aUser = null;

        for (User someUser : users) {
            if (someUser.getId() == id) {
                aUser = someUser;
            }
        }

            return aUser;

        }

    //getByUsername
    public static User getByUsername(String username) {
        User aUser = null;

        for (User someUser : users) {
            if (someUser.getUsername() == username) {
                aUser = someUser;
            }
        }

        return aUser;

    }


}
