package com.example.todolistspring.Controller;

import com.example.todolistspring.models.Tarea;
import com.example.todolistspring.models.Usuario;
import com.example.todolistspring.repository.UsuarioRepository;
import com.example.todolistspring.security.JwtUtil;
import com.example.todolistspring.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// hola este es el bueno funcional
@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository; // Añade esta dependencia

    @Autowired
    public TareaController(TareaService tareaService,
                           JwtUtil jwtUtil,
                           UsuarioRepository usuarioRepository) {
        this.tareaService = tareaService;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/hola")  // Ruta final: GET /api/tareas/hola
    public String holaMundo() {
        return "Mire profe si jalo a la primera jeje";
    }
/*
    @GetMapping // Ruta final: GET /api/tareas
    public List<Tarea> obtenerTodas() {
        return tareaService.findAll();
    }

 */

    @GetMapping("/{id}") // Ruta final: GET /api/tareas/1
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable long id) {
        Optional<Tarea> tarea = tareaService.ObtenerPorId(id);
        return tarea.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarea> crear(@RequestBody Tarea tarea,
                                       @RequestHeader("Authorization") String token) {
        // Extraer username del token
        String username = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));

        // Buscar usuario en la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignar usuario a la tarea
        tarea.setUsuario(usuario);

        // Guardar la tarea
        Tarea nuevaTarea = tareaService.Guardar(tarea);
        return ResponseEntity.ok(nuevaTarea);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @RequestBody Tarea tareaActualizada) {
        try {
            Tarea tarea = tareaService.Actualizar(id, tareaActualizada);
            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tarea> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        try {
            Tarea tarea = tareaService.actualizarParcial(id, campos);
            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        if (tareaService.ObtenerPorId(id).isPresent()) {
            tareaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTodas(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));

        // Obtener tareas del usuario específico
        List<Tarea> tareas = tareaService.findByUsuarioUsername(username);
        return ResponseEntity.ok(tareas);
    }
}




