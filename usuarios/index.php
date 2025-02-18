<?php

require_once 'Usuario.php';
require_once 'UsuarioDAO.php';

$usuarioDAO = new UsuarioDAO();

// Crear el primer usuario (Bugs Bunny)
$bugs = new Usuario();
$bugs->setNombres("Bugs");
$bugs->setApellidos("Bunny");
$bugs->setUsuario("bugs123"); // Proporciona un valor para 'usuario'
$bugs->setCorreo("bugsBunny@wb.com");
$bugs->setPassword("password123"); // Proporciona un valor para 'password'

// Crear el segundo usuario (Lola Bunny)
$lola = new Usuario();
$lola->setNombres("Lola");
$lola->setApellidos("Bunny");
$lola->setUsuario("lola123"); // Proporciona un valor para 'usuario'
$lola->setCorreo("LolaBunny@wb.com");
$lola->setPassword("password456"); // Proporciona un valor para 'password'

// Crear el tercer usuario (Porky Pig)
$porky = new Usuario();
$porky->setNombres("Porky");
$porky->setApellidos("Pig");
$porky->setUsuario("porky123"); // Proporciona un valor para 'usuario'
$porky->setCorreo("porkypig@wb.com");
$porky->setPassword("password789"); // Proporciona un valor para 'password'

// Insertar los usuarios en la base de datos
$usuarioDAO->insertar($bugs);
$usuarioDAO->insertar($lola);
$usuarioDAO->insertar($porky);

// Actualizar el correo de Porky Pig
$porky->setCorreo('porkypig@wb.com'); // Correo corregido
$usuarioDAO->actualizar($porky);

// Eliminar a Bugs Bunny
$usuarioDAO->eliminar($bugs->getId());


?>