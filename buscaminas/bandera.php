<?php
// Inicia la sesión para almacenar datos en variables de sesión.
session_start();

// Obtiene los datos enviados desde el cliente en formato JSON y los decodifica a un array asociativo.
$data = json_decode(file_get_contents('php://input'), true);
$fila = $data['fila']; // Extrae la fila de los datos recibidos.
$columna = $data['columna']; // Extrae la columna de los datos recibidos.

// Si no existe la variable de sesión 'banderas', se inicializa como un array vacío.
if (!isset($_SESSION['banderas'])) {
    $_SESSION['banderas'] = [];
}

// Genera una clave única para la celda basada en su posición (fila-columna).
$key = "$fila-$columna";

// Verifica si la celda ya tiene una bandera colocada.
if (isset($_SESSION['banderas'][$key])) {
    unset($_SESSION['banderas'][$key]); // Si ya tiene bandera, la elimina.
    $marcada = false; // Indica que la bandera ha sido removida.
} else {
    $_SESSION['banderas'][$key] = true; // Si no tenía bandera, la agrega.
    $marcada = true; // Indica que la bandera ha sido colocada.
}

// Devuelve la respuesta en formato JSON con el estado actualizado de la celda.
echo json_encode(['marcada' => $marcada]);
?>
