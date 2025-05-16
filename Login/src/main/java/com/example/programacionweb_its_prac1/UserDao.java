package com.example.programacionweb_its_prac1;
import conexion.Conexion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private final Conexion c;

    public UserDao() {
        c = new Conexion<User>();
    }

    public boolean agregar(User user) {
        String query = "INSERT INTO users(fullName, email, username, password) VALUES (?, ?, ?, ?)";
        return c.ejecutarActualizacion(query, user.getAll());
    }

    public ArrayList<User> consultarTodos() {
        String query = "SELECT fullName, email, username FROM users";
        ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{});
        ArrayList<User> users = new ArrayList<>();

        for (ArrayList<String> r : res) {
            User user = new User();
            user.setFullName(r.get(0));
            user.setEmail(r.get(1));
            user.setUsername(r.get(2));
            users.add(user);
        }
        return users;
    }

    public User consultarPorUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Warning: Empty username provided");
            return null;
        }

        String query = "SELECT fullName, email, username, password FROM users WHERE username = ?";
        System.out.println("Executing query: " + query + " with username: " + username);

        ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{username});

        if (res == null) {
            System.out.println("Error: Query returned null result");
            return null;
        }

        System.out.println("Query returned " + res.size() + " results");

        if (res.isEmpty()) {
            System.out.println("No user found with username: " + username);
            return null;
        }

        try {
            ArrayList<String> r = res.get(0);
            User user = new User();
            user.setFullName(r.get(0));
            user.setEmail(r.get(1));
            user.setUsername(r.get(2));
            user.setPassword(r.get(3));
            return user;
        } catch (Exception e) {
            System.out.println("Error processing result: " + e.getMessage());
            return null;
        }
    }
    public User consultarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Warning: Empty email provided");
            return null;
        }

        String query = "SELECT fullName, email, username, password FROM users WHERE email = ?";
        System.out.println("Executing email query: " + query + " with email: " + email);

        ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{email});

        if (res == null) {
            System.out.println("Error: Email query returned null result");
            return null;
        }

        System.out.println("Email query returned " + res.size() + " results");

        if (res.isEmpty()) {
            System.out.println("No user found with email: " + email);
            return null;
        }

        try {
            ArrayList<String> r = res.get(0);
            User user = new User();
            user.setFullName(r.get(0));
            user.setEmail(r.get(1));
            user.setUsername(r.get(2));
            user.setPassword(r.get(3));
            return user;
        } catch (Exception e) {
            System.out.println("Error processing email result: " + e.getMessage());
            return null;
        }
    }


    public boolean existeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Warning: Empty email provided for existence check");
            return false;
        }

        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        System.out.println("Checking email existence: " + email);

        ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{email});

        if (res == null || res.isEmpty()) {
            System.out.println("Error: Email existence check returned null or empty result");
            return false;
        }

        try {
            return Integer.parseInt(res.get(0).get(0)) > 0;
        } catch (Exception e) {
            System.out.println("Error parsing email count: " + e.getMessage());
            return false;
        }
    }
}
