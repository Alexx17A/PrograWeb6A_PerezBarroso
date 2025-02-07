<?php
// Verifica si la solicitud es de tipo POST.
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Inicia la sesión para acceder y almacenar variables de sesión.
    session_start();
    
    // Obtiene los datos enviados en formato JSON y los decodifica a un objeto PHP.
    $data = json_decode(file_get_contents('php://input'));
    
    // Verifica si existe un tablero almacenado en la sesión.
    if (!isset($_SESSION['tablero'])) {
        // Si no hay un tablero activo, devuelve un mensaje de error en formato JSON.
        echo json_encode(['error' => 'No hay tablero activo']);
        exit; // Finaliza la ejecución del script.
    }
    
    // Recupera el tablero almacenado en la sesión.
    $tablero = $_SESSION['tablero'];
    
    // Convierte los valores de fila y columna en enteros para evitar errores de tipo de dato.
    $fila = (int)$data->fila;
    $columna = (int)$data->columna;
    
    // Verifica si la posición seleccionada existe en el tablero.
    if (isset($tablero[$fila][$columna])) {
        // Devuelve el valor de la celda seleccionada en formato JSON.
        echo json_encode([
            'valor' => $tablero[$fila][$columna]
        ]);
    } else {
        // Si la posición es inválida, devuelve un mensaje de error en formato JSON.
        echo json_encode(['error' => 'Posición inválida']);
    }
}
?> 