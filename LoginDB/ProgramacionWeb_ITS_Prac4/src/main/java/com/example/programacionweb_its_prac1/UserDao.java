    package com.example.programacionweb_its_prac1;

    import conexion.Conexion;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;

    public class UserDAO {
        private final Conexion c;

        public UserDAO() {
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
            String query = "SELECT fullName, email, username, password FROM users WHERE username = ?";
            ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{username});

            if (res.isEmpty()) return null;

            ArrayList<String> r = res.get(0);
            User user = new User();
            user.setFullName(r.get(0));
            user.setEmail(r.get(1));
            user.setUsername(r.get(2));
            user.setPassword(r.get(3));
            return user;
        }

        public User consultarPorEmail(String email) {
            String query = "SELECT fullName, email, username, password FROM users WHERE email = ?";
            ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{email});

            if (res.isEmpty()) return null;

            ArrayList<String> r = res.get(0);
            User user = new User();
            user.setFullName(r.get(0));
            user.setEmail(r.get(1));
            user.setUsername(r.get(2));
            user.setPassword(r.get(3));
            return user;
        }

        public boolean existeEmail(String email) {
            String query = "SELECT COUNT(*) FROM users WHERE email = ?";
            ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{email});
            return Integer.parseInt(res.get(0).get(0)) > 0;
        }
    }