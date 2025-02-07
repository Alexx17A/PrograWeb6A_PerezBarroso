<?php
// Inicia la sesión para almacenar y recuperar variables de sesión.
session_start();

/**
 * Clase Buscaminas que maneja la generación del tablero de juego.
 */
class Buscaminas {
    private $filas; // Número de filas del tablero
    private $columnas; // Número de columnas del tablero
    private $minas; // Número de minas en el tablero

    /**
     * Constructor de la clase que define el tamaño del tablero y la cantidad de minas según el nivel de dificultad.
     *
     * @param string $nivel Nivel de dificultad ('facil', 'medio', 'dificil').
     */
    public function __construct($nivel) {
        switch ($nivel) {
            case 'facil':
                $this->filas = 8;
                $this->columnas = 8;
                $this->minas = 10;
                break;
            case 'medio':
                $this->filas = 16;
                $this->columnas = 16;
                $this->minas = 40;
                break;
            case 'dificil':
                $this->filas = 16;
                $this->columnas = 30;
                $this->minas = 99;
                break;
            default:
                $this->filas = 8;
                $this->columnas = 8;
                $this->minas = 10;
                break;
        }
    }

    /**
     * Genera el tablero de juego con minas y números que indican la cantidad de minas alrededor.
     *
     * @return array Matriz representando el tablero.
     */
    public function generarTablero() {
        // Inicializa el tablero con ceros
        $tablero = array_fill(0, $this->filas, array_fill(0, $this->columnas, 0));

        // Coloca minas en posiciones aleatorias
        $minasColocadas = 0;
        while ($minasColocadas < $this->minas) {
            $fila = rand(0, $this->filas - 1);
            $columna = rand(0, $this->columnas - 1);

            if ($tablero[$fila][$columna] !== -1) {
                $tablero[$fila][$columna] = -1; // Representa una mina con -1
                $minasColocadas++;

                // Incrementa los números alrededor de la mina
                for ($i = max(0, $fila - 1); $i <= min($this->filas - 1, $fila + 1); $i++) {
                    for ($j = max(0, $columna - 1); $j <= min($this->columnas - 1, $columna + 1); $j++) {
                        if ($tablero[$i][$j] !== -1 && !($i === $fila && $j === $columna)) {
                            $tablero[$i][$j]++;
                        }
                    }
                }
            }
        }
        return $tablero;
    }
}

// Verifica si la solicitud es de tipo POST.
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Obtiene los datos enviados en formato JSON y los decodifica en un array asociativo.
    $data = json_decode(file_get_contents('php://input'), true);

    // Si se recibe un nivel, se genera un nuevo tablero.
    if (isset($data['nivel'])) {
        $buscaminas = new Buscaminas($data['nivel']);
        $_SESSION['tablero'] = $buscaminas->generarTablero();
        echo json_encode(['success' => true]);
    }
    // Si se recibe una coordenada de fila y columna, se devuelve el valor de la celda.
    elseif (isset($data['fila']) && isset($data['columna'])) {
        // Verifica si existe un tablero activo en la sesión.
        if (!isset($_SESSION['tablero'])) {
            echo json_encode(['error' => 'No hay tablero activo']);
            exit; // Finaliza la ejecución del script.
        }

        // Recupera el tablero de la sesión.
        $tablero = $_SESSION['tablero'];
        $fila = (int)$data['fila'];
        $columna = (int)$data['columna'];

        // Verifica si la posición seleccionada es válida en el tablero.
        if (isset($tablero[$fila][$columna])) {
            echo json_encode([
                'valor' => $tablero[$fila][$columna]
            ]);
        } else {
            echo json_encode(['error' => 'Posición inválida']);
        }
    }
}
