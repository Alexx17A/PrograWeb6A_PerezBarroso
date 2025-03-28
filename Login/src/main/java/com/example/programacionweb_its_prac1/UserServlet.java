package com.example.programacionweb_its_prac1;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Base64;

import static com.example.programacionweb_its_prac1.AutenticacionServlet.generalKey;

@WebServlet("/user-servlet/*")
public class UserServlet extends HttpServlet {
    private final JsonResponse jResp = new JsonResponse();

    private final HashMap<String, User> users = AutenticacionServlet.users;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String authTokenHeader = req.getHeader("Authorization");
        if (authTokenHeader == null || !authTokenHeader.startsWith("Bearer ")) {
            jResp.failed(req, resp, "Token no proporcionado", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authTokenHeader.split(" ")[1];
        validateAuthToken(req, resp, token);
    }


    /**
     * Método que se utiliza para validar el token de autenticación. Si el token es válido, se envía una respuesta exitosa.
     * Si el token no es válido, se envía una respuesta fallida.
     * @param req
     * @param resp
     * @param token Token de autenticación
     * @throws IOException
     */
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

