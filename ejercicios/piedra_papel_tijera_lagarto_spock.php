<!-- PARA EJECUTAR EL PROGRAMA USAR =   php piedra_papel_tijera_lagarto_spock.php  -->

<?php

function jugador1() {
    $opciones = [
        1 => "Piedra",
        2 => "Papel",
        3 => "Tijera",
        4 => "Lagarto",
        5 => "Spock"
    ];

    echo "Elige una opción:\n";
    foreach ($opciones as $key => $value) {
        echo "$key: $value\n";
    }

    while (true) {
        echo "Jugador 1, ingresa un número entre 1 y 5: ";
        $input = trim(fgets(STDIN));

       
        if (ctype_digit($input) && array_key_exists(intval($input), $opciones)) {
            $jugador1 = intval($input);
            echo "Jugador 1 eligió " . $opciones[$jugador1] . "\n\n";
            return $jugador1;
        }

        echo "Opción no válida. Inténtalo de nuevo.\n";
    }



}

function jugador2() {
    $opciones = [
        1 => "Piedra",
        2 => "Papel",
        3 => "Tijera",
        4 => "Lagarto",
        5 => "Spock"
    ];

    echo "Elige una opción:\n";
    foreach ($opciones as $key => $value) {
        echo "$key: $value\n";
    }

    while (true) {
        echo "Jugador2, ingresa un número entre 1 y 5: ";
        $input = trim(fgets(STDIN));

       
        if (ctype_digit($input) && array_key_exists(intval($input), $opciones)) {
            $jugador2 = intval($input);
            echo "Jugador 2 eligió " . $opciones[$jugador2] . "\n\n";
            return $jugador2;
        }

        echo "Opción no válida. Inténtalo de nuevo.\n";
    }



}


  // php piedra_papel_tijera_lagarto_spock.php 0 3
/*
 * La matriz $resultados define las reglas del juego:
 * - 1 (Piedra) vence a 3 (Tijera) y 4 (Lagarto), pero pierde contra 2 (Papel) y 5 (Spock).
 * - 2 (Papel) vence a 1 (Piedra) y 5 (Spock), pero pierde contra 3 (Tijera) y 4 (Lagarto).
 * - 3 (Tijera) vence a 2 (Papel) y 4 (Lagarto), pero pierde contra 1 (Piedra) y 5 (Spock).
 * - 4 (Lagarto) vence a 2 (Papel) y 5 (Spock), pero pierde contra 1 (Piedra) y 3 (Tijera).
 * - 5 (Spock) vence a 1 (Piedra) y 3 (Tijera), pero pierde contra 2 (Papel) y 4 (Lagarto).
 */
function piedra_papel_tijera_lagarto_spock($jugador1, $jugador2) {
    $resultados = [
        1 => [2 => "Gana Jugador 2 piedra pierde contra papel", 3 => "Gana Jugador 1 piedra le gana a tijera", 4 => "Gana Jugador 1 piedra le gana a lagarto", 5 => "Gana Jugador 2 piedra pierde contra spock"],
        2 => [1 => "Gana Jugador 1 papel le gana a piedra", 3 => "Gana Jugador 2 papel pierde contra tijera", 4 => "Gana Jugador 2 papel pierde contra lagarto", 5 => "Gana Jugador 1 papel le gana a spock"],
        3 => [1 => "Gana Jugador 2 tijera pierde contra piedra", 2 => "Gana Jugador 1 tijera le gana a papel", 4 => "Gana Jugador 1 tijera le gana a lagarto", 5 => "Gana Jugador 2 tijera pierde contra spock"],
        4 => [1 => "Gana Jugador 2 lagarto pierde contra piedra", 2 => "Gana Jugador 1 lagarto le gana a papel", 3 => "Gana Jugador 2 lagarto pierde contra tijera", 5 => "Gana Jugador 1 lagarto le gana a spock"],
        5 => [1 => "Gana Jugador 1 spock le gana a piedra", 2 => "Gana Jugador 2 spock pierde contra papel", 3 => "Gana Jugador 1 spock le gana a tijera", 4 => "Gana Jugador 2 spock pierde contra lagarto"]
    ];


    if ($jugador1 == $jugador2) {
        echo "ELIGIERON EL MISMO 'EMPATE' \n";
    } else {
        echo $resultados[$jugador1][$jugador2] . "\n";
    }
}

// Llamar a las funciones y jugar
if ($argc > 2) {
    $jugador1 = intval($argv[1]);
    $jugador2 = intval($argv[2]);
} else {
    $jugador1 = jugador1();
    $jugador2 = jugador2();
}

piedra_papel_tijera_lagarto_spock($jugador1, $jugador2);



?>

<!-- PARA EJECUTAR EL PROGRAMA USAR  =   php  piedra_papel_tijera_lagarto_spock.php   -->


<!-- DONDE LE SALDRA UN MENU EL CUAL PODRA ELEGIR LAS OPCIONES DEL JUEGO  -->
