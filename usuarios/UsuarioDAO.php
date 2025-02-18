<?php

require_once 'DataSource.php';
require_once 'Usuario.php';
require_once 'IDao.php';

class UsuarioDAO implements IDao
{
    private $dataSource;

    public function __construct()
    {
        $this->dataSource = new DataSource();
    }

    public function buscarTodos()
    {
        $sql = "SELECT * FROM usuarios";
        $data = $this->dataSource->ejecutarConsulta($sql);
    
        $usuarios = [];
    
        foreach ($data as $usuarioData) {
            $usuario = new Usuario(
                $usuarioData['id'],
                $usuarioData['nombres'],
                $usuarioData['apellidos'],
                $usuarioData['usuario'], // Incluye el valor de 'usuario'
                $usuarioData['correo'],
                $usuarioData['password'] // Incluye el valor de 'password'
            );
            array_push($usuarios, $usuario);
        }
    
        return $usuarios;
    }


    public function buscar($id)
    {
        $sql = "SELECT * FROM usuarios WHERE id = :id";
        $values = [':id' => $id];

        return $this->dataSource->ejecutarConsulta($sql, $values);
    }

    public function insertar(Usuario $usuario)
{
    $sql = "INSERT INTO usuarios (nombres, apellidos, usuario, correo, password) 
            VALUES (:nombres, :apellidos, :usuario, :correo, :password)";
    $values = [
        ':nombres' => $usuario->getNombres(),
        ':apellidos' => $usuario->getApellidos(),
        ':usuario' => $usuario->getUsuario(), // Incluye el valor de 'usuario'
        ':correo' => $usuario->getCorreo(),
        ':password' => $usuario->getPassword() // Incluye el valor de 'password'
    ];

    return $this->dataSource->ejecutarActualizacion($sql, $values);
}

    public function actualizar(Usuario $usuario)
    {
        $sql = "UPDATE usuarios SET nombres = :nombres, apellidos = :apellidos, correo = :correo WHERE id = :id";
        $values = [
            ':id' => $usuario->getId(),
            ':nombres' => $usuario->getNombres(),
            ':apellidos' => $usuario->getApellidos(),
            ':correo' => $usuario->getCorreo()
        ];
        return $this->dataSource->ejecutarActualizacion($sql, $values);
    }

    public function eliminar($id)
{
    $sql = "DELETE FROM usuarios WHERE id = :id";
    $values = [':id' => $id];
    return $this->dataSource->ejecutarActualizacion($sql, $values);
}




}