<?php
class DataSource {
    private $cadenaParaConexion;
    private $conexion;

    public function __construct() {
        try {
            $this->cadenaParaConexion = "mysql:host=localhost;dbname=usuarios";
            $this->conexion = new PDO($this->cadenaParaConexion, "root", "");
            $this->conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); // Habilitar excepciones
        } catch (PDOException $e) {
            echo "Error en la conexión: " . $e->getMessage();
        }
    }

    public function ejecutarConsulta($sql = "", $values = []) {
        if ($sql != "") {
            $consulta = $this->conexion->prepare($sql);
            $consulta->execute($values);
            return $consulta->fetchAll(PDO::FETCH_ASSOC);
        } else {
            return 0;
        }
    }

    public function ejecutarActualizacion($sql = "", $values = []) {
        if ($sql != "") {
            $consulta = $this->conexion->prepare($sql);
            $consulta->execute($values);
            return $consulta->rowCount();
        } else {
            return 0;
        }
    }

    public function getLastInsertId()
    {
        return $this->conexion->lastInsertId(); // Devuelve el último ID insertado
    }
}
?>