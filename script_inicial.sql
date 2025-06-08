-- Limpiar tablas (con modo seguro desactivado)
SET SQL_SAFE_UPDATES = 0;
DELETE FROM turno;
DELETE FROM servicio_lugar;
DELETE FROM servicio_dias_semana;
DELETE FROM servicio;
DELETE FROM lugar;
DELETE FROM profesional;
DELETE FROM especialidad;
DELETE FROM profesional_especialidad;
DELETE FROM solicitante;  -- Importante borrar antes que usuario
DELETE FROM usuario;
DELETE FROM contacto;
DELETE FROM direccion;
DELETE FROM localidad;
DELETE FROM provincia;
SET SQL_SAFE_UPDATES = 1;

-- Insertar datos con relaciones correctas
-- 1. Primero las tablas independientes
INSERT INTO provincia (id, nombre) VALUES
(1, 'Buenos Aires');

INSERT INTO localidad (id, nombre, provincia_id) VALUES
(1, 'Lanús', 1);

INSERT INTO direccion (id, calleYaltura, localidad_id) VALUES
(1, 'Av. 9 de Julio 123', 1);

INSERT INTO contacto (id, apellido, dni, nombre, direccion_id) VALUES
(1, 'Pérez', 12345678, 'Juan', 1);

-- 2. Usuario (debe existir antes que solicitante)
INSERT INTO usuario (id, created_at, email, password, updated_at, contacto_id) VALUES
(1, NOW(), 'juan@example.com', '1234', NOW(), 1);

-- 3. Solicitante (referencia a usuario.id)
INSERT INTO solicitante (id,pago) VALUES  -- Asumo que hay una columna usuario_id
(1,0);  -- El ID 1 referencia al usuario creado arriba

-- 4. Profesional
INSERT INTO profesional (id, matricula) VALUES
(1, 'MAT123');

-- 5. Servicio
INSERT INTO servicio (id, duracion_minutos, estado, horario_fin, horario_inicio, nombre) VALUES
(1, 30, 1, '18:00:00', '08:00:00', 'Consulta General');

INSERT INTO servicio_dias_semana(servicio_id, dias_semana) values(1,'MONDAY');

-- 6. Lugar
INSERT INTO lugar (id, nombre, direccion_id) VALUES
(1, 'Centro medico', 1);

-- 7. Servicio_Lugar
INSERT INTO servicio_lugar (id, activo, lugar_id, profesional_id, servicio_id) VALUES
(1, 1, 1, 1, 1);

-- 8. Finalmente los turnos (con referencias válidas)
INSERT INTO turno (created_at, estado, fecha_hora, updated_at, servicio_lugar_id, solicitante_id) VALUES
(NOW(), 1, '2025-06-10 09:00:00', NOW(), 1, 1),
(NOW(), 0, '2025-06-11 10:30:00', NOW(), 1, 1);
