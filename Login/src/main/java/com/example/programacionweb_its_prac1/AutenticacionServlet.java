package com.example.programacionweb_its_prac1;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import io.jsonwebtoken.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;

@WebServlet("/autenticacion-servlet/*")

/**
 * Clase que contiene los siguientes endpoints:
 * - register
 * - login
 * - logout
 */
public class AutenticacionServlet extends HttpServlet {
    private static final String SECRET_KEY = "mWQKjKflpJSqyj0nDdSG9ZHE6x4tNaXGb35J6d7G5mo=";
    //se cambio el hash por DAO
    private final JsonResponse jResp = new JsonResponse();
    private final UserDao userDao = new UserDao();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        if (req.getPathInfo() == null) {
            jResp.failed(req, resp, "404 - Recurso no encontrado", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] path = req.getPathInfo().split("/");

        if (path.length < 2) {
            jResp.failed(req, resp, "404 - Recurso no encontrado", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String action = path[1];

        switch (action) {
            case "users":
                getAllUsers(req, resp);
                break;
            default:
                jResp.failed(req, resp, "404 - Recurso no encontrado", HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ArrayList<User> users = userDao.consultarTodos();

        if (users == null || users.isEmpty()) {
            jResp.failed(req, resp, "No se encontraron usuarios", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Convertimos cada usuario a su representación Map
        ArrayList<Map<String, Object>> usersData = new ArrayList<>();
        for (User user : users) {
            usersData.add(user.toMap());
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("users", usersData);

        jResp.success(req, resp, "Usuarios obtenidos con éxito", responseData);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        if (req.getPathInfo() == null) {
            jResp.failed(req, resp, "404 - Recurso no encontrado", HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String[] path = req.getPathInfo().split("/");

        if (req.getPathInfo().equals("/")) {
            jResp.failed(req, resp, "404 - Recurso no encontrado", HttpServletResponse.SC_NOT_FOUND);
        }

        String action = path[1];

        switch (action) {
            case "register":
                register(req, resp);
                break;
            case "login":
                login(req, resp);
                break;
            case "logout":
                logout(req, resp);
                break;
            default:
                jResp.failed(req, resp, "404 - Recurso no encontrado", HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Metodo que se utiliza para el endpoint /autenticacion-servlet/register de tipo POST
     * Se encarga de registrar un usuario en el sistema, recibe los siguientes parametros:
     * - username
     * - password
     * - fullName
     * - email
     *
     * Si alguno de los parametros es nulo, se responde con un mensaje de error, en caso contrario
     * se encripta la contraseña y se crea un nuevo usuario con los datos proporcionados.
     * @param req
     * @param resp
     * @throws IOException
     */
    //SE CAMBIO EL REGISTER PARA USAR DAO
    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");

        if (username == null || password == null || fullName == null || email == null) {
            jResp.failed(req, resp, "Todos los campos son obligatorios", HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return;
        }

        if (userDao.consultarPorUsername(username) != null) {
            jResp.failed(req, resp, "El nombre de usuario ya está en uso", HttpServletResponse.SC_CONFLICT);
            return;
        }

        if (userDao.existeEmail(email)) {
            jResp.failed(req, resp, "El correo electrónico ya está registrado", HttpServletResponse.SC_CONFLICT);
            return;
        }

        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(fullName, email, username, encryptedPassword);

        if (userDao.agregar(user)) {
            jResp.success(req, resp, "Usuario creado con éxito", null);
        } else {
            jResp.failed(req, resp, "Error al crear el usuario", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Metodo que se utiliza para el endpoint /autenticacion-servlet/login de tipo POST
     * Se encarga de autenticar un usuario en el sistema, recibe los siguientes parametros:
     * - username
     * - password
     *
     * Si el usuario no existe o la contraseña es incorrecta, se responde con un mensaje de error,
     * en caso contrario se genera un token JWT y se responde con un mensaje de éxito.
     * @param req
     * @param resp
     * @throws IOException
     */

    // SE ACTUALIZO LA MANERA DE LOGIN
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String usernameOrEmail = req.getParameter("username");
        String password = req.getParameter("password");

        // Buscar usuario por username
        User user = userDao.consultarPorUsername(usernameOrEmail);

        // Si no se encuentra por username, buscar por email
        if (user == null) {
            user = userDao.consultarPorEmail(usernameOrEmail);
        }

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            long expirationTime = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minutos
            String token = Jwts.builder()
                    .subject(user.getUsername())
                    .setExpiration(new Date(expirationTime))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();

            // Asignar el token al usuario
            user.setJwt(token);

            // Crear respuesta
            Map<String, String> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("username", user.getUsername());
            responseData.put("fullName", user.getFullName());
            responseData.put("email", user.getEmail());

            jResp.success(req, resp, "Autenticación exitosa", responseData);
        } else {
            jResp.failed(req, resp, "Credenciales inválidas", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Metodo que se utiliza para el endpoint /autenticacion-servlet/logout de tipo POST
     * Se encarga de cerrar la sesión de un usuario en el sistema.
     * @param req
     * @param resp
     * @throws IOException
     */
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Logged out successfully");
    }

    /**
     * Metodo que se encarga de encriptar una contraseña
     * @param password Contraseña a encriptar
     * @return String con la contraseña encriptada
     */
    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Metodo que se encarga de verificar si una contraseña es correcta
     * @param inputPassword Contraseña ingresada por el usuario
     * @param storedPassword Contraseña almacenada en la base de datos (HasMap)
     * @return true si la contraseña es correcta, false en caso contrario
     */
    private boolean verifyPassword(String inputPassword, String storedPassword) {
        return BCrypt.checkpw(inputPassword, storedPassword);
    }

    /**
     * Metodo que se encarga de generar una clave secreta
     * @return SecretKey con la clave secreta generada
     */
    public static SecretKey generalKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}