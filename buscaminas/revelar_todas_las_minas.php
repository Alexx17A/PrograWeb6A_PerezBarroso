<?php
// Especifica que la respuesta será en formato JSON.
header('Content-Type: application/json');

// Simulación de una matriz con minas.
// En una implementación real, esta información debería generarse dinámicamente.
$minas = [
    ['fila' => 1, 'columna' => 2], // Mina en la posición (1,2)
    ['fila' => 3, 'columna' => 5], // Mina en la posición (3,5)
    ['fila' => 6, 'columna' => 7]  // Mina en la posición (6,7)
];

// Convierte la matriz de minas a formato JSON y la imprime como respuesta.
echo json_encode(['minas' => $minas]);
?>
