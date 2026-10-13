-- ==========================================================
--  Base de datos del proyecto TechnoStore
-- ==========================================================
--  Este archivo crea la base de datos completa:
--  las 6 tablas y los 25 procedimientos.
--
--  Como usarlo:
--    Opcion 1: abrir este archivo en MySQL Workbench y ejecutarlo.
--    Opcion 2: desde la consola ->  mysql -u root -p < technostore.sql
--
--  OJO: este archivo crea la base desde cero. Si ya la tienes
--  creada, dara error porque las tablas ya existen. Para volver
--  a empezar de cero tendrias que borrarla antes con:
--      DROP DATABASE technostore;
-- ==========================================================

-- Le dice a MySQL que este archivo esta escrito en UTF-8,
-- que es el formato de texto que usa las tildes y la enie.
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS technostore;
USE technostore;


-- ==========================================================
--  TABLAS
-- ==========================================================

CREATE TABLE marcas (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE usuarios (
  id INT NOT NULL AUTO_INCREMENT,
  correo VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  rol ENUM('ADMIN','CLIENTE') NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE celulares (
  id INT NOT NULL AUTO_INCREMENT,
  marca_fk INT NOT NULL,
  modelo VARCHAR(100) NOT NULL,
  sistema_operativo VARCHAR(50) NOT NULL,
  gama ENUM('ALTA','MEDIA','BAJA') NOT NULL,
  precio DECIMAL(12,2) NOT NULL,
  stock INT NOT NULL,
  PRIMARY KEY (id),
  KEY fk_celulares_marca (marca_fk),
  CONSTRAINT fk_celulares_marca FOREIGN KEY (marca_fk) REFERENCES marcas (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT chk_celular_precio CHECK (precio > 0),
  CONSTRAINT chk_celular_stock CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE clientes (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_fk INT NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  identificacion VARCHAR(30) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY identificacion (identificacion),
  KEY fk_clientes_usuario (usuario_fk),
  CONSTRAINT fk_clientes_usuario FOREIGN KEY (usuario_fk) REFERENCES usuarios (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE ventas (
  id INT NOT NULL AUTO_INCREMENT,
  cliente_fk INT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_ventas_cliente (cliente_fk),
  CONSTRAINT fk_ventas_cliente FOREIGN KEY (cliente_fk) REFERENCES clientes (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT chk_venta_total CHECK (total >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE detalle_ventas (
  id INT NOT NULL AUTO_INCREMENT,
  venta_fk INT NOT NULL,
  celular_fk INT NOT NULL,
  cantidad INT NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_detalle_ventas_venta (venta_fk),
  KEY fk_detalle_ventas_celular (celular_fk),
  CONSTRAINT fk_detalle_ventas_celular FOREIGN KEY (celular_fk) REFERENCES celulares (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_detalle_ventas_venta FOREIGN KEY (venta_fk) REFERENCES ventas (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT chk_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT chk_detalle_subtotal CHECK (subtotal >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ==========================================================
--  USUARIO ADMINISTRADOR
-- ==========================================================
--  Es el unico usuario con rol ADMIN del sistema. Sin el no
--  se puede entrar al menu de administrador.
-- ==========================================================

INSERT INTO usuarios (id, correo, password, rol) VALUES (1, 'admin@gmail.com', 'admin123', 'ADMIN');


-- ==========================================================
--  PROCEDIMIENTOS
-- ==========================================================
--  DELIMITER cambia el simbolo que marca el final de una
--  instruccion. Hace falta porque dentro de los procedimientos
--  hay instrucciones que terminan en punto y coma (;), y MySQL
--  se confundiria. Por eso aqui el final de instruccion es //
-- ==========================================================

DELIMITER //
CREATE PROCEDURE actualizar_celular(IN p_id INT, IN p_marca_fk INT, IN p_modelo VARCHAR(100), IN p_sistema_operativo VARCHAR(50), IN p_gama VARCHAR(10), IN p_precio DECIMAL(12,2), IN p_stock INT)
BEGIN IF p_precio <= 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El precio debe ser positivo'; END IF; IF p_stock < 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El stock no puede ser negativo'; END IF; UPDATE celulares SET marca_fk = p_marca_fk, modelo = p_modelo, sistema_operativo = p_sistema_operativo, gama = p_gama, precio = p_precio, stock = p_stock WHERE id = p_id; IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El celular no existe'; END IF; END
//

CREATE PROCEDURE actualizar_cliente(IN p_id INT, IN p_nombre VARCHAR(100), IN p_identificacion VARCHAR(30), IN p_telefono VARCHAR(20))
BEGIN UPDATE clientes SET nombre = p_nombre, identificacion = p_identificacion, telefono = p_telefono WHERE id = p_id; IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El cliente no existe'; END IF; END
//

CREATE PROCEDURE actualizar_marca(in p_id int, in p_nombre varchar(50))
begin
update marcas set nombre = p_nombre where id = p_id;
if row_count() = 0 then
signal sqlstate '45000' set message_text = 'La marca no existe';
end if;
end
//

CREATE PROCEDURE agregar_item_venta(IN p_venta_fk INT, IN p_celular_fk INT, IN p_cantidad INT)
BEGIN DECLARE v_precio DECIMAL(12,2); DECLARE v_stock INT; DECLARE v_subtotal DECIMAL(12,2); DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END; START TRANSACTION; IF p_cantidad <= 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cantidad debe ser positiva'; END IF; SELECT precio, stock INTO v_precio, v_stock FROM celulares WHERE id = p_celular_fk FOR UPDATE; IF v_stock < p_cantidad THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuficiente'; END IF; SET v_subtotal = (v_precio * p_cantidad) * 1.19; INSERT INTO detalle_ventas(venta_fk, celular_fk, cantidad, subtotal) VALUES (p_venta_fk, p_celular_fk, p_cantidad, v_subtotal); UPDATE celulares SET stock = stock - p_cantidad WHERE id = p_celular_fk; UPDATE ventas SET total = total + v_subtotal WHERE id = p_venta_fk; COMMIT; END
//

CREATE PROCEDURE buscar_celular(IN p_id INT, OUT p_marca_fk INT, OUT p_modelo VARCHAR(100), OUT p_sistema_operativo VARCHAR(50), OUT p_gama VARCHAR(10), OUT p_precio DECIMAL(12,2), OUT p_stock INT)
BEGIN IF NOT EXISTS (SELECT 1 FROM celulares WHERE id = p_id) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El celular no existe'; END IF; SELECT marca_fk, modelo, sistema_operativo, gama, precio, stock INTO p_marca_fk, p_modelo, p_sistema_operativo, p_gama, p_precio, p_stock FROM celulares WHERE id = p_id; END
//

CREATE PROCEDURE buscar_cliente(IN p_id INT, OUT p_usuario_fk INT, OUT p_nombre VARCHAR(100), OUT p_identificacion VARCHAR(30), OUT p_telefono VARCHAR(20))
BEGIN IF NOT EXISTS (SELECT 1 FROM clientes WHERE id = p_id) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El cliente no existe'; END IF; SELECT usuario_fk, nombre, identificacion, telefono INTO p_usuario_fk, p_nombre, p_identificacion, p_telefono FROM clientes WHERE id = p_id; END
//

CREATE PROCEDURE buscar_usuario_por_correo(IN p_correo VARCHAR(100))
BEGIN SELECT id, correo, password, rol FROM usuarios WHERE correo = p_correo; END
//

CREATE PROCEDURE buscar_usuario_por_id(IN p_id INT)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM usuarios WHERE id = p_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario no existe';
    END IF;
    SELECT id, correo, password, rol FROM usuarios WHERE id = p_id;
END
//

CREATE PROCEDURE crear_venta(IN p_cliente_fk INT, OUT p_id_venta INT)
BEGIN IF NOT EXISTS (SELECT 1 FROM clientes WHERE id = p_cliente_fk) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El cliente no existe'; END IF; INSERT INTO ventas(cliente_fk, fecha, total) VALUES (p_cliente_fk, NOW(), 0); SET p_id_venta = LAST_INSERT_ID(); END
//

CREATE PROCEDURE eliminar_celular(IN p_id INT)
BEGIN DELETE FROM celulares WHERE id = p_id; IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El celular no existe'; END IF; END
//

CREATE PROCEDURE eliminar_cliente(IN p_id INT)
BEGIN DELETE FROM clientes WHERE id = p_id; IF ROW_COUNT() = 0 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El cliente no existe'; END IF; END
//

CREATE PROCEDURE eliminar_marca(in p_id int)
begin
delete from marcas where id = p_id;
if row_count() = 0 then
signal sqlstate '45000' set message_text = 'La marca no existe';
end if;
end
//

CREATE PROCEDURE listar_celulares()
BEGIN
  SELECT c.id, c.modelo, c.sistema_operativo, c.gama, c.precio, c.stock,
         m.id AS marca_id, m.nombre AS marca
  FROM celulares c
  JOIN marcas m ON c.marca_fk = m.id
  ORDER BY m.nombre, c.modelo;
END
//

CREATE PROCEDURE listar_clientes()
BEGIN SELECT c.id, c.nombre, c.identificacion, c.telefono, u.correo FROM clientes c JOIN usuarios u ON c.usuario_fk = u.id ORDER BY c.nombre; END
//

CREATE PROCEDURE listar_detalle_venta(IN p_venta_fk INT)
BEGIN SELECT d.id, d.cantidad, d.subtotal, c.modelo AS modelo, m.nombre AS marca FROM detalle_ventas d JOIN celulares c ON d.celular_fk = c.id JOIN marcas m ON c.marca_fk = m.id WHERE d.venta_fk = p_venta_fk; END
//

CREATE PROCEDURE listar_marcas()
begin
select id, nombre from marcas order by nombre;
end
//

CREATE PROCEDURE listar_ventas()
BEGIN SELECT v.id, v.fecha, v.total, c.nombre AS cliente FROM ventas v JOIN clientes c ON v.cliente_fk = c.id ORDER BY v.fecha DESC; END
//

CREATE PROCEDURE listar_ventas_por_cliente(IN p_cliente_fk INT)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM clientes WHERE id = p_cliente_fk) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El cliente no existe';
    END IF;
    SELECT v.id, v.fecha, v.total, c.nombre AS cliente
    FROM ventas v
    JOIN clientes c ON v.cliente_fk = c.id
    WHERE v.cliente_fk = p_cliente_fk
    ORDER BY v.fecha DESC;
END
//

CREATE PROCEDURE registrar_celular(
in p_marca_fk int,
in p_modelo varchar(100),
in p_sistema_operativo varchar(50),
in p_gama varchar(10),
in p_precio decimal(12,2),
in p_stock int,
out p_id int)
begin
if p_precio <= 0 then
signal sqlstate '45000' set message_text = 'El precio debe ser positivo';
end if;
if p_stock < 0 then
signal sqlstate '45000' set message_text = 'El stock no puede ser negativo';
end if;
if not exists (select 1 from marcas where id = p_marca_fk) then
signal sqlstate '45000' set message_text = 'La marca no existe';
end if;

insert into celulares(marca_fk, modelo, sistema_operativo, gama, precio, stock) 
values (p_marca_fk, p_modelo, p_sistema_operativo, p_gama, p_precio, p_stock);
set p_id = last_insert_id();
end
//

CREATE PROCEDURE registrar_cliente(IN p_usuario_fk INT, IN p_nombre VARCHAR(100), IN p_identificacion VARCHAR(30), IN p_telefono VARCHAR(20), OUT p_id INT)
BEGIN IF NOT EXISTS (SELECT 1 FROM usuarios WHERE id = p_usuario_fk) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario no existe'; END IF; IF EXISTS (SELECT 1 FROM clientes WHERE identificacion = p_identificacion) THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe un cliente con esa identificación'; END IF; INSERT INTO clientes(usuario_fk, nombre, identificacion, telefono) VALUES (p_usuario_fk, p_nombre, p_identificacion, p_telefono); SET p_id = LAST_INSERT_ID(); END
//

CREATE PROCEDURE registrar_marcas(in p_nombre varchar(50), out p_id int)
BEGIN
insert into marcas(nombre) values (p_nombre);
set p_id = last_insert_id();
end
//

CREATE PROCEDURE registrar_usuario(IN p_correo VARCHAR(100), IN p_password VARCHAR(255), IN p_rol VARCHAR(10), OUT p_id INT)
BEGIN IF p_correo NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,}$' THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Formato de correo inválido'; END IF; INSERT INTO usuarios(correo, password, rol) VALUES (p_correo, p_password, p_rol); SET p_id = LAST_INSERT_ID(); END
//

CREATE PROCEDURE reporte_stock_bajo()
BEGIN SELECT c.id, c.stock, c.gama, c.modelo, m.nombre AS marca FROM celulares c JOIN marcas m ON c.marca_fk = m.id WHERE c.stock < 5 ORDER BY c.stock ASC; END
//

CREATE PROCEDURE reporte_top3_vendidos()
BEGIN SELECT c.id, c.modelo, m.nombre AS marca, SUM(d.cantidad) AS unidades_vendidas FROM detalle_ventas d JOIN celulares c ON d.celular_fk = c.id JOIN marcas m ON c.marca_fk = m.id GROUP BY c.id, c.modelo, m.nombre ORDER BY unidades_vendidas DESC LIMIT 3; END
//

CREATE PROCEDURE reporte_ventas_por_mes()
BEGIN
      SELECT YEAR(fecha) AS año, MONTH(fecha) AS mes,
             COUNT(*) AS numero_ventas, SUM(total) AS total
      FROM ventas
      GROUP BY YEAR(fecha), MONTH(fecha)
      ORDER BY año, mes;
  END
//
DELIMITER ;
