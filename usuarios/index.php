<?php

require_once 'Usuario.php';
require_once 'UsuarioDAO.php';

$usuarioDAO = new UsuarioDAO();

// Crear usuario bugs
$bugs = new Usuario();
$bugs->setNombres("Bugs");
$bugs->setApellidos("Bunny");
$bugs->setUsuario("bugs123");
$bugs->setCorreo("bugsBunny@wb.com");
$bugs->setPassword("password123");


// Crear usuario lola
$lola = new Usuario();
$lola->setNombres("Lola");
$lola->setApellidos("Bunny");
$lola->setUsuario("lola123");
$lola->setCorreo("LolaBunny@wb.com");
$lola->setPassword("password456");


// Crear usuario daffy
$daffy = new Usuario();
$daffy->setNombres("Daffy");
$daffy->setApellidos("Duck");
$daffy->setUsuario("daffy123");
$daffy->setCorreo("daffyDuck@wb.com");
$daffy->setPassword("password555");


// Crear usuario porky
$porky = new Usuario();
$porky->setNombres("Porky");
$porky->setApellidos("Pig");
$porky->setUsuario("porky123");
$porky->setCorreo("porkypig@wb.com");
$porky->setPassword("password789");

// Insertar usuarios
$usuarioDAO->insertar($bugs);
$usuarioDAO->insertar($lola);
$usuarioDAO->insertar($daffy);
$usuarioDAO->insertar($porky);

// Actualizar el correo de Porky Pig
$porky->setCorreo('porkypigito@wb.com');
$usuarioDAO->actualizar($porky);

// Eliminar a Bugs Bunny
$usuarioDAO->eliminar($bugs->getId()); //osea este no va estar en la tabla cuando se actualice (envien los datos)

// Mostrar la lista actualizada de usuarios
$usuarios = $usuarioDAO->buscarTodos();
foreach ($usuarios as $usuario) {
    echo $usuario->getNombres() . " " . $usuario->getApellidos() ." " . $usuario->getCorreo() . "<br>";
}

?>