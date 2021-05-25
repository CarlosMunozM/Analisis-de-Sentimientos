SELECT * FROM persona 
SELECT * FROM tipo_usuario 
SELECT * FROM menu  
SELECT * FROM sub_menu  
SELECT * FROM opciones   

INSERT INTO tipo_usuario VALUES (1 , 'Administrador'); 
INSERT INTO tipo_usuario VALUES (2 , 'Jugador'); 

INSERT INTO persona  VALUES (1001, 'Carlos Luis', 'Muñoz Mendoza', '0959056738', '1998-01-19', 'carlos', MD5('12345'), 'M', 1);
INSERT INTO persona  VALUES (1002, 'Ana María', 'Morán Salazar', '0986724152', '1997-06-17', 'ana', MD5('12345'), 'F', 2);

INSERT INTO menu VALUES (1 , 'Administrador', 1); 
INSERT INTO menu VALUES (2 , 'Jugador', 1); 

INSERT INTO sub_menu VALUES (1 , 'Mostrar', 1); 
INSERT INTO sub_menu VALUES (2 , 'Insertar', 1); 
INSERT INTO sub_menu VALUES (3 , 'Actualizar', 1); 
INSERT INTO sub_menu VALUES (4 , 'Eliminar', 1); 

INSERT INTO sub_menu VALUES (5 , 'Mostrar', 2); 
INSERT INTO sub_menu VALUES (6 , 'Insertar', 2); 
INSERT INTO sub_menu VALUES (7 , 'Actualizar', 2); 
INSERT INTO sub_menu VALUES (8 , 'Eliminar', 2); 


INSERT INTO imagenes (id_imagen, ruta) VALUES (1,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img0.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (2,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img1.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (3,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img2.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (4,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img3.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (5,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img4.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (6,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img5.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (7,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img6.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (8,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img7.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (9,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img8.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (10,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img9.jpg');
INSERT INTO imagenes (id_imagen, ruta) VALUES (11,'http://192.168.100.232:8080/WS_AnalisisSentimientos/img/img10.jpg');

select * from imagenes 

SELECT P.nombres, P.apellidos, TP.nombre AS tipo_persona 
FROM persona P, tipo_usuario TP
WHERE P.id_tipousuario = TP.id_tipousuario


SELECT M.id_menu, M.nombre AS menus
FROM tipo_usuario TP, menu M
WHERE TP.id_tipousuario = M.id_tipousuario AND TP.id_tipousuario = 1

SELECT SM.id_submenu, SM.nombre
FROM sub_menu SM
WHERE id_menu = 1







