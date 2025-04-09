package com.example.programacionweb_its_prac1;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import io.jsonwebtoken.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

import java.io.IOException;
import java.util.Base64;

import static com.example.programacionweb_its_prac1.AutenticacionServlet.generalKey;


@WebServlet("/user-servlet/*")
public class UserServlet extends HttpServlet {
    private final JsonResponse jResp = new JsonResponse();
    private final UserDAO userDao = new UserDAO();
    private static final String SECRET_KEY = "mWQKjKflpJSqyj0nDdSG9ZHE6x4tNaXGb35J6d7G5mo=";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Validar token
        String token = getTokenFromHeader(req);
        if (token == null || !validateToken(token)) {
            jResp.failed(req, resp, "Token no válido o expirado", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Procesar la solicitud según el path
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            // GET /user-servlet/ - Listar todos los usuarios
            ArrayList<User> users = userDao.consultarTodos();
            jResp.success(req, resp, "Lista de usuarios", users);
        } else {
            // GET /user-servlet/{username} - Obtener usuario por username
            String[] path = req.getPathInfo().split("/");
            if (path.length == 2) {
                String username = path[1];
                User user = userDao.consultarPorUsername(username);
                if (user != null) {
                    // No devolver la contraseña
                    user.setPassword(null);
                    jResp.success(req, resp, "Usuario encontrado", user);
                } else {
                    jResp.failed(req, resp, "Usuario no encontrado", HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                jResp.failed(req, resp, "Ruta no válida", HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Validar token
        String token = getTokenFromHeader(req);
        if (token == null || !validateToken(token)) {
            jResp.failed(req, resp, "Token no válido o expirado", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // POST /user-servlet/ - Crear nuevo usuario
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (fullName == null || email == null || username == null || password == null) {
            jResp.failed(req, resp, "Todos los campos son obligatorios", HttpServletResponse.SC_UNPROCESSABLE_ENTITY);
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
        User newUser = new User(fullName, email, username, encryptedPassword);

        if (userDao.agregar(newUser)) {
            jResp.success(req, resp, "Usuario creado con éxito", null);
        } else {
            jResp.failed(req, resp, "Error al crear el usuario", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String getTokenFromHeader(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


    /**
     * Método que se utiliza para validar el token de autenticación. Si el token es válido, se envía una respuesta exitosa.
     * Si el token no es válido, se envía una respuesta fallida.
     * @param req
     * @param resp
     * @param token Token de autenticación
     * @throws IOException

    private void validateAuthToken (HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
        // String[] chunks = token.split("\\.");

        // Base64.Decoder decoder = Base64.getUrlDecoder();

        // String header = new String(decoder.decode(chunks[0]));
        // String payload = new String(decoder.decode(chunks[1]));

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(generalKey())
                .build();
        try {
            // Extraer el username del token
            String username = jwtParser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            // Buscar el usuario en el HashMap
            User user = users.get(username);
            if (user == null) {
                jResp.failed(req, resp, "Usuario no encontrado", HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Crear mapa con datos del usuario (excluyendo password)
            Map<String, Object> userData = new HashMap<>();
            userData.put("fullName", user.getFullName());
            userData.put("email", user.getEmail());
            userData.put("username", user.getUsername());

            jResp.success(req, resp, "Datos de usuario", userData);

        } catch (Exception e) {
            jResp.failed(req, resp, "Unauthorized: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
     */
